package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.Challenge
import com.example.data.GratitudeEntry
import com.example.meditation.Meditation
import com.example.meditation.MeditationAudioEngine
import com.example.meditation.MeditationCatalog
import com.example.mindfulness.MindfulnessCatalog
import com.example.mindfulness.SenseExercise
import com.example.podcast.PodcastCatalog
import com.example.podcast.PodcastChapter
import com.example.podcast.PodcastEpisode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(
        database.gratitudeDao(),
        database.challengeDao(),
        database.meditationDao()
    )

    val audioEngine = MeditationAudioEngine()

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

    // ─── Podcast State ────────────────────────────────────────────────────────
    val podcastEpisode: StateFlow<PodcastEpisode> = MutableStateFlow(PodcastCatalog.currentEpisode).asStateFlow()

    private val _isPodcastPlaying = MutableStateFlow(false)
    val isPodcastPlaying: StateFlow<Boolean> = _isPodcastPlaying.asStateFlow()

    private val _podcastPositionSeconds = MutableStateFlow(0)
    val podcastPositionSeconds: StateFlow<Int> = _podcastPositionSeconds.asStateFlow()

    private val _podcastPlaybackSpeed = MutableStateFlow(1.0f)
    val podcastPlaybackSpeed: StateFlow<Float> = _podcastPlaybackSpeed.asStateFlow()

    private val _currentPodcastChapterIndex = MutableStateFlow(0)
    val currentPodcastChapterIndex: StateFlow<Int> = _currentPodcastChapterIndex.asStateFlow()

    private var podcastJob: Job? = null

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

    fun startPodcast() {
        if (_isPodcastPlaying.value) return
        _isPodcastPlaying.value = true
        // Play smooth ambient resonance in background while podcast is active
        audioEngine.startAmbient(frequencyHz = 432.0, volume = 0.25f)

        podcastJob?.cancel()
        podcastJob = viewModelScope.launch {
            val total = podcastEpisode.value.durationSeconds
            while (_isPodcastPlaying.value && _podcastPositionSeconds.value < total) {
                val delayMs = (1000L / _podcastPlaybackSpeed.value).toLong()
                delay(delayMs)
                val newPos = _podcastPositionSeconds.value + 1
                _podcastPositionSeconds.value = newPos

                // Update active chapter
                val chapters = podcastEpisode.value.chapters
                val activeChap = chapters.indexOfLast { it.timeSeconds <= newPos }.coerceAtLeast(0)
                if (activeChap != _currentPodcastChapterIndex.value) {
                    _currentPodcastChapterIndex.value = activeChap
                }
            }
            if (_podcastPositionSeconds.value >= total) {
                _isPodcastPlaying.value = false
                audioEngine.stop()
            }
        }
    }

    fun pausePodcast() {
        _isPodcastPlaying.value = false
        podcastJob?.cancel()
        audioEngine.stop()
    }

    fun seekPodcastTo(seconds: Int) {
        val bounded = seconds.coerceIn(0, podcastEpisode.value.durationSeconds)
        _podcastPositionSeconds.value = bounded
        val chapters = podcastEpisode.value.chapters
        _currentPodcastChapterIndex.value = chapters.indexOfLast { it.timeSeconds <= bounded }.coerceAtLeast(0)
    }

    fun skipPodcast(secondsDelta: Int) {
        seekPodcastTo(_podcastPositionSeconds.value + secondsDelta)
    }

    fun selectPodcastChapter(chapter: PodcastChapter) {
        seekPodcastTo(chapter.timeSeconds)
        if (!_isPodcastPlaying.value) {
            startPodcast()
        }
    }

    fun cyclePodcastSpeed() {
        _podcastPlaybackSpeed.value = when (_podcastPlaybackSpeed.value) {
            1.0f -> 1.25f
            1.25f -> 1.5f
            else -> 1.0f
        }
    }

    // ─── Mindfulness Sense Exercises Actions ──────────────────────────────────
    fun openSenseExercise(exercise: SenseExercise) {
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
            repository.toggleChallenge(challenge)
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
        }
    }

    fun deleteGratitudeEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.deleteGratitudeEntry(entry)
        }
    }

    // ─── Meditation Actions ───────────────────────────────────────────────────
    fun openMeditation(meditation: Meditation, customMinutes: Int = meditation.defaultMinutes) {
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
                repository.recordMeditationSession(
                    title = med.title,
                    category = med.category,
                    durationSeconds = _meditationDurationSeconds.value
                )
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
        podcastJob?.cancel()
        senseJob?.cancel()
        timerJob?.cancel()
    }
}

