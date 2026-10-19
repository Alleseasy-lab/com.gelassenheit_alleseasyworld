package com.example

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.Challenge
import com.example.data.GratitudeEntry
import com.example.data.MilestoneCatalog
import com.example.data.MilestoneReward
import com.example.data.UserGoal
import com.example.meditation.Meditation
import com.example.meditation.MeditationAudioEngine
import com.example.meditation.MeditationCatalog
import com.example.mindfulness.MindfulnessCatalog
import com.example.mindfulness.SenseExercise
import com.example.podcast.PodcastCatalog
import com.example.podcast.PodcastChapter
import com.example.podcast.PodcastEpisode
import com.example.ui.pro.ProPlan
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(
        database.gratitudeDao(),
        database.challengeDao(),
        database.meditationDao(),
        database.goalDao()
    )

    private val sharedPrefs = application.getSharedPreferences("gelassenheit_prefs", Context.MODE_PRIVATE)

    // ─── Monetization / PRO State ─────────────────────────────────────────────
    private val _isProUser = MutableStateFlow(sharedPrefs.getBoolean("is_pro_unlocked", false))
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    private val _showPaywall = MutableStateFlow(false)
    val showPaywall: StateFlow<Boolean> = _showPaywall.asStateFlow()

    fun openPaywall() {
        _showPaywall.value = true
    }

    fun closePaywall() {
        _showPaywall.value = false
    }

    fun unlockPro(plan: ProPlan) {
        sharedPrefs.edit().putBoolean("is_pro_unlocked", true).apply()
        _isProUser.value = true
    }

    fun resetProStatus() {
        sharedPrefs.edit().putBoolean("is_pro_unlocked", false).apply()
        _isProUser.value = false
    }

    val audioEngine = MeditationAudioEngine()

    // ─── Streak Days State ───────────────────────────────────────────────────
    private val _streakDays = MutableStateFlow(sharedPrefs.getInt("user_streak_days", 3))
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    fun recordStreakActivity() {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastActivityDate = sharedPrefs.getString("last_activity_date", "")

        if (lastActivityDate != todayStr) {
            val currentStreak = sharedPrefs.getInt("user_streak_days", 3)
            val newStreak = currentStreak + 1
            sharedPrefs.edit()
                .putString("last_activity_date", todayStr)
                .putInt("user_streak_days", newStreak)
                .apply()
            _streakDays.value = newStreak
        }
    }

    // ─── TTS Voice Narration Engine ──────────────────────────────────────────
    private var tts: TextToSpeech? = TextToSpeech(application, this)
    private var isTtsInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.GERMAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(_podcastPlaybackSpeed.value)
            tts?.setPitch(1.0f)
            isTtsInitialized = true

            // If user already pressed play before TTS finished loading
            if (_isPodcastPlaying.value) {
                speakCurrentPodcastChapter()
            }
        }
    }

    // Challenges State
    val challenges: StateFlow<List<Challenge>> = repository.allChallenges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedChallengesCount: StateFlow<Int> = repository.completedChallengesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalChallengesCount: StateFlow<Int> = repository.totalChallengesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Gratitude Journal State
    val gratitudeEntries: StateFlow<List<GratitudeEntry>> = repository.allGratitudeEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ─── Goal Tracker State ───────────────────────────────────────────────────
    val allGoals: StateFlow<List<UserGoal>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedGoalsCount: StateFlow<Int> = repository.completedGoalsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalGoalsCount: StateFlow<Int> = repository.totalGoalsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMeditationSeconds: StateFlow<Int> = repository.totalMeditationSeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Dynamic Milestone Rewards calculated reactively
    val milestones: StateFlow<List<MilestoneReward>> = combine(
        repository.totalMeditationSeconds,
        repository.allGratitudeEntries,
        repository.completedChallengesCount,
        repository.completedGoalsCount,
        _streakDays
    ) { medSecs, gratitudes, compChallenges, compGoals, streak ->
        MilestoneCatalog.computeMilestones(
            totalMeditationSeconds = medSecs,
            gratitudeCount = gratitudes.size,
            completedChallengesCount = compChallenges,
            completedGoalsCount = compGoals,
            streakDays = streak
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ─── Podcast State ────────────────────────────────────────────────────────
    private val _podcastEpisode = MutableStateFlow(PodcastCatalog.currentEpisode)
    val podcastEpisode: StateFlow<PodcastEpisode> = _podcastEpisode.asStateFlow()

    val allPodcastEpisodes: StateFlow<List<PodcastEpisode>> = MutableStateFlow(PodcastCatalog.allEpisodes).asStateFlow()

    private val _isPodcastPlaying = MutableStateFlow(false)
    val isPodcastPlaying: StateFlow<Boolean> = _isPodcastPlaying.asStateFlow()

    private val _podcastPositionSeconds = MutableStateFlow(0)
    val podcastPositionSeconds: StateFlow<Int> = _podcastPositionSeconds.asStateFlow()

    private val _podcastPlaybackSpeed = MutableStateFlow(1.0f)
    val podcastPlaybackSpeed: StateFlow<Float> = _podcastPlaybackSpeed.asStateFlow()

    private val _currentPodcastChapterIndex = MutableStateFlow(0)
    val currentPodcastChapterIndex: StateFlow<Int> = _currentPodcastChapterIndex.asStateFlow()

    private var podcastJob: Job? = null

    fun selectPodcastEpisode(episode: PodcastEpisode) {
        if (episode.isProOnly && !_isProUser.value) {
            _showPaywall.value = true
            return
        }
        pausePodcast()
        _podcastEpisode.value = episode
        _podcastPositionSeconds.value = 0
        _currentPodcastChapterIndex.value = 0
    }

    // ─── Active Mindfulness Sensory Exercise State ───────────────────────────
    private val _activeSenseExercise = MutableStateFlow<SenseExercise?>(null)
    val activeSenseExercise: StateFlow<SenseExercise?> = _activeSenseExercise.asStateFlow()

    private val _senseExerciseRemaining = MutableStateFlow(120)
    val senseExerciseRemaining: StateFlow<Int> = _senseExerciseRemaining.asStateFlow()

    private val _senseExerciseDuration = MutableStateFlow(120)
    val senseExerciseDuration: StateFlow<Int> = _senseExerciseDuration.asStateFlow()

    private val _isSenseExercisePlaying = MutableStateFlow(false)
    val isSenseExercisePlaying: StateFlow<Boolean> = _isSenseExercisePlaying.asStateFlow()

    private val _currentSenseStepIndex = MutableStateFlow(0)
    val currentSenseStepIndex: StateFlow<Int> = _currentSenseStepIndex.asStateFlow()

    private var senseJob: Job? = null

    // ─── Active Meditation Session State ──────────────────────────────────────
    private val _activeMeditation = MutableStateFlow<Meditation?>(null)
    val activeMeditation: StateFlow<Meditation?> = _activeMeditation.asStateFlow()

    private val _meditationDurationSeconds = MutableStateFlow(300) // 5 min default
    val meditationDurationSeconds: StateFlow<Int> = _meditationDurationSeconds.asStateFlow()

    private val _meditationRemainingSeconds = MutableStateFlow(300)
    val meditationRemainingSeconds: StateFlow<Int> = _meditationRemainingSeconds.asStateFlow()

    private val _isMeditationPlaying = MutableStateFlow(false)
    val isMeditationPlaying: StateFlow<Boolean> = _isMeditationPlaying.asStateFlow()

    private val _currentGuideStepIndex = MutableStateFlow(0)
    val currentGuideStepIndex: StateFlow<Int> = _currentGuideStepIndex.asStateFlow()

    private var timerJob: Job? = null

    // ─── Podcast Actions ──────────────────────────────────────────────────────
    fun togglePodcastPlayPause() {
        if (_isPodcastPlaying.value) {
            pausePodcast()
        } else {
            startPodcast()
        }
    }

    private fun speakCurrentPodcastChapter() {
        if (!isTtsInitialized) return
        val episode = podcastEpisode.value
        val chapIndex = _currentPodcastChapterIndex.value.coerceIn(0, episode.chapters.size - 1)
        val chapter = episode.chapters[chapIndex]
        val textToSpeak = if (chapter.narration.isNotBlank()) chapter.narration else chapter.summary

        tts?.setSpeechRate(_podcastPlaybackSpeed.value)
        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.9f)
        }
        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "podcast_chap_$chapIndex")
    }

    fun startPodcast() {
        if (_isPodcastPlaying.value) return
        _isPodcastPlaying.value = true

        // Smooth 432Hz ambient frequency background
        audioEngine.startAmbient(frequencyHz = 432.0, volume = 0.22f)

        // Voice narration for active chapter
        speakCurrentPodcastChapter()

        podcastJob?.cancel()
        podcastJob = viewModelScope.launch {
            val total = podcastEpisode.value.durationSeconds
            var lastSpokenChapter = _currentPodcastChapterIndex.value

            while (_isPodcastPlaying.value && _podcastPositionSeconds.value < total) {
                delay(1000L)
                _podcastPositionSeconds.value = (_podcastPositionSeconds.value + 1).coerceIn(0, total)

                // Update active chapter
                val chapters = podcastEpisode.value.chapters
                val activeChap = chapters.indexOfLast { it.timeSeconds <= _podcastPositionSeconds.value }.coerceAtLeast(0)
                if (activeChap != _currentPodcastChapterIndex.value) {
                    _currentPodcastChapterIndex.value = activeChap
                    if (activeChap != lastSpokenChapter) {
                        lastSpokenChapter = activeChap
                        speakCurrentPodcastChapter()
                    }
                }
            }
            if (_podcastPositionSeconds.value >= total) {
                _isPodcastPlaying.value = false
                audioEngine.stop()
                tts?.stop()
            }
        }
    }

    fun pausePodcast() {
        _isPodcastPlaying.value = false
        podcastJob?.cancel()
        audioEngine.stop()
        tts?.stop()
    }

    fun seekPodcastTo(seconds: Int) {
        val bounded = seconds.coerceIn(0, podcastEpisode.value.durationSeconds)
        _podcastPositionSeconds.value = bounded

        val chapters = podcastEpisode.value.chapters
        val newChapIdx = chapters.indexOfLast { it.timeSeconds <= bounded }.coerceAtLeast(0)
        val changedChapter = newChapIdx != _currentPodcastChapterIndex.value
        _currentPodcastChapterIndex.value = newChapIdx

        if (_isPodcastPlaying.value && changedChapter) {
            speakCurrentPodcastChapter()
        }
    }

    fun skipPodcast(secondsDelta: Int) {
        seekPodcastTo(_podcastPositionSeconds.value + secondsDelta)
    }

    fun selectPodcastChapter(chapter: PodcastChapter) {
        seekPodcastTo(chapter.timeSeconds)
        if (!_isPodcastPlaying.value) {
            startPodcast()
        } else {
            speakCurrentPodcastChapter()
        }
    }

    fun cyclePodcastSpeed() {
        val newSpeed = when (_podcastPlaybackSpeed.value) {
            1.0f -> 1.25f
            1.25f -> 1.5f
            else -> 1.0f
        }
        _podcastPlaybackSpeed.value = newSpeed
        tts?.setSpeechRate(newSpeed)
    }

    // ─── Goal Tracker Actions ────────────────────────────────────────────────
    fun addGoal(
        title: String,
        description: String = "",
        targetType: String = "CUSTOM",
        targetValue: Int = 1,
        unit: String = "Einheit",
        period: String = "DAILY",
        iconEmoji: String = "🎯",
        category: String = "Gelassenheit"
    ) {
        viewModelScope.launch {
            repository.addGoal(
                UserGoal(
                    title = title,
                    description = description,
                    targetType = targetType,
                    targetValue = targetValue.coerceAtLeast(1),
                    currentValue = 0,
                    unit = unit,
                    period = period,
                    iconEmoji = iconEmoji,
                    category = category,
                    isCompleted = false
                )
            )
        }
    }

    fun deleteGoal(goal: UserGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun incrementGoalProgress(goal: UserGoal, delta: Int = 1) {
        viewModelScope.launch {
            repository.incrementGoalProgress(goal, delta)
            recordStreakActivity()
            if (goal.currentValue + delta >= goal.targetValue && !goal.isCompleted) {
                audioEngine.playSingingBowlBell()
            }
        }
    }

    fun toggleGoalComplete(goal: UserGoal) {
        viewModelScope.launch {
            val willBeCompleted = !goal.isCompleted
            repository.toggleGoalComplete(goal)
            if (willBeCompleted) {
                recordStreakActivity()
                audioEngine.playSingingBowlBell()
            }
        }
    }

    fun playRewardSingingBowl() {
        audioEngine.playSingingBowlBell()
    }

    private fun syncGoalsForType(targetType: String, delta: Int = 1) {
        viewModelScope.launch {
            val currentGoals = allGoals.value
            currentGoals.filter { it.targetType == targetType && !it.isCompleted }.forEach { goal ->
                repository.incrementGoalProgress(goal, delta)
            }
        }
    }

    // ─── Mindfulness Sense Exercises Actions ──────────────────────────────────
    fun openSenseExercise(exercise: SenseExercise) {
        if (exercise.isProOnly && !_isProUser.value) {
            _showPaywall.value = true
            return
        }
        _activeSenseExercise.value = exercise
        _senseExerciseDuration.value = exercise.practiceDurationSeconds
        _senseExerciseRemaining.value = exercise.practiceDurationSeconds
        _currentSenseStepIndex.value = 0
        _isSenseExercisePlaying.value = false
        senseJob?.cancel()
        audioEngine.stop()
    }

    fun closeSenseExercise() {
        pauseSenseExercise()
        _activeSenseExercise.value = null
    }

    fun toggleSenseExercisePlay() {
        if (_isSenseExercisePlaying.value) {
            pauseSenseExercise()
        } else {
            startSenseExercise()
        }
    }

    fun startSenseExercise() {
        val exercise = _activeSenseExercise.value ?: return
        if (_isSenseExercisePlaying.value) return
        _isSenseExercisePlaying.value = true

        audioEngine.playSingingBowlBell()
        audioEngine.startAmbient(528.0, volume = 0.35f)

        senseJob?.cancel()
        senseJob = viewModelScope.launch {
            while (_senseExerciseRemaining.value > 0 && _isSenseExercisePlaying.value) {
                delay(1000)
                _senseExerciseRemaining.value -= 1

                val steps = exercise.steps
                if (steps.isNotEmpty()) {
                    val progress = 1f - (_senseExerciseRemaining.value.toFloat() / _senseExerciseDuration.value.toFloat())
                    val idx = (progress * steps.size).toInt().coerceIn(0, steps.size - 1)
                    if (idx != _currentSenseStepIndex.value) {
                        _currentSenseStepIndex.value = idx
                    }
                }
            }

            if (_senseExerciseRemaining.value <= 0) {
                _isSenseExercisePlaying.value = false
                audioEngine.stop()
                audioEngine.playSingingBowlBell()
                recordStreakActivity()
                syncGoalsForType("MINDFULNESS", 1)
            }
        }
    }

    fun pauseSenseExercise() {
        _isSenseExercisePlaying.value = false
        senseJob?.cancel()
        audioEngine.stop()
    }

    fun resetSenseExercise() {
        pauseSenseExercise()
        _senseExerciseRemaining.value = _senseExerciseDuration.value
        _currentSenseStepIndex.value = 0
    }

    // ─── Challenges Actions ───────────────────────────────────────────────────
    fun toggleChallenge(challenge: Challenge) {
        viewModelScope.launch {
            val willBeCompleted = !challenge.isCompleted
            repository.toggleChallenge(challenge)
            if (willBeCompleted) {
                recordStreakActivity()
                syncGoalsForType("CHALLENGE", 1)
                audioEngine.playSingingBowlBell()
            }
        }
    }

    fun addCustomChallenge(title: String, description: String, category: String, minutes: Int, icon: String) {
        viewModelScope.launch {
            repository.addCustomChallenge(
                Challenge(
                    title = title,
                    description = description,
                    category = category,
                    estimatedMinutes = minutes,
                    iconEmoji = icon
                )
            )
        }
    }

    // ─── Gratitude Journal Actions ────────────────────────────────────────────
    fun addGratitudeEntry(text: String, prompt: String, moodEmoji: String, moodLabel: String, category: String) {
        viewModelScope.launch {
            repository.addGratitudeEntry(
                GratitudeEntry(
                    text = text,
                    prompt = prompt,
                    moodEmoji = moodEmoji,
                    moodLabel = moodLabel,
                    category = category
                )
            )
            recordStreakActivity()
            syncGoalsForType("GRATITUDE", 1)
            audioEngine.playSingingBowlBell()
        }
    }

    fun deleteGratitudeEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.deleteGratitudeEntry(entry)
        }
    }

    // ─── Meditation Actions ───────────────────────────────────────────────────
    fun openMeditation(meditation: Meditation, customMinutes: Int = meditation.defaultMinutes) {
        if (meditation.isProOnly && !_isProUser.value) {
            _showPaywall.value = true
            return
        }
        _activeMeditation.value = meditation
        _meditationDurationSeconds.value = customMinutes * 60
        _meditationRemainingSeconds.value = customMinutes * 60
        _currentGuideStepIndex.value = 0
        _isMeditationPlaying.value = false
        timerJob?.cancel()
        audioEngine.stop()
    }

    fun closeMeditation() {
        pauseMeditation()
        _activeMeditation.value = null
    }

    fun setMeditationDurationMinutes(minutes: Int) {
        if (!_isMeditationPlaying.value) {
            _meditationDurationSeconds.value = minutes * 60
            _meditationRemainingSeconds.value = minutes * 60
        }
    }

    fun startMeditation() {
        if (_isMeditationPlaying.value) return
        _isMeditationPlaying.value = true
        val med = _activeMeditation.value ?: return

        audioEngine.playSingingBowlBell()
        audioEngine.startAmbient(med.ambientFrequencyHz, volume = 0.45f)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_meditationRemainingSeconds.value > 0 && _isMeditationPlaying.value) {
                delay(1000)
                _meditationRemainingSeconds.value -= 1

                val totalSteps = med.guidedSteps.size
                if (totalSteps > 0) {
                    val progress = 1f - (_meditationRemainingSeconds.value.toFloat() / _meditationDurationSeconds.value.toFloat())
                    val stepIdx = (progress * totalSteps).toInt().coerceIn(0, totalSteps - 1)
                    if (stepIdx != _currentGuideStepIndex.value) {
                        _currentGuideStepIndex.value = stepIdx
                    }
                }
            }

            if (_meditationRemainingSeconds.value <= 0) {
                _isMeditationPlaying.value = false
                audioEngine.stop()
                audioEngine.playSingingBowlBell()
                val medMinutes = _meditationDurationSeconds.value / 60
                repository.recordMeditationSession(
                    title = med.title,
                    category = med.category,
                    durationSeconds = _meditationDurationSeconds.value
                )
                recordStreakActivity()
                syncGoalsForType("MEDITATION", medMinutes)
            }
        }
    }

    fun pauseMeditation() {
        _isMeditationPlaying.value = false
        timerJob?.cancel()
        audioEngine.stop()
    }

    fun resetMeditation() {
        pauseMeditation()
        _meditationRemainingSeconds.value = _meditationDurationSeconds.value
        _currentGuideStepIndex.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.stop()
        tts?.stop()
        tts?.shutdown()
        tts = null
        podcastJob?.cancel()
        senseJob?.cancel()
        timerJob?.cancel()
    }
}

