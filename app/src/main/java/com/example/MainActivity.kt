package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meditation.MeditationCatalog
import com.example.ui.challenges.ChallengeItemCard
import com.example.ui.challenges.ChallengesScreen
import com.example.ui.gateway.TritEinButton
import com.example.ui.gratitude.GratitudeJournalScreen
import com.example.ui.meditation.MeditationCard
import com.example.ui.meditation.MeditationLibraryScreen
import com.example.ui.meditation.MeditationPlayerDialog
import com.example.ui.mindfulness.MindfulnessScreen
import com.example.ui.mindfulness.SenseExercisePlayerDialog
import com.example.ui.podcast.PodcastScreen
import com.example.ui.reflection.SelfReflectionScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = ImmersiveBg
                ) { innerPadding ->
                    SchweinehundApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SchweinehundApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    var isEntered by remember { mutableStateOf(false) }

    val activeMeditation by viewModel.activeMeditation.collectAsStateWithLifecycle()
    val meditationRemaining by viewModel.meditationRemainingSeconds.collectAsStateWithLifecycle()
    val meditationDuration by viewModel.meditationDurationSeconds.collectAsStateWithLifecycle()
    val isMeditationPlaying by viewModel.isMeditationPlaying.collectAsStateWithLifecycle()
    val currentStepIndex by viewModel.currentGuideStepIndex.collectAsStateWithLifecycle()

    val activeSenseExercise by viewModel.activeSenseExercise.collectAsStateWithLifecycle()
    val senseRemaining by viewModel.senseExerciseRemaining.collectAsStateWithLifecycle()
    val senseDuration by viewModel.senseExerciseDuration.collectAsStateWithLifecycle()
    val isSensePlaying by viewModel.isSenseExercisePlaying.collectAsStateWithLifecycle()
    val currentSenseStepIndex by viewModel.currentSenseStepIndex.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = isEntered,
            animationSpec = tween(700),
            label = "Gateway Transition"
        ) { entered ->
            if (!entered) {
                GatewayScreen(onEnter = { isEntered = true })
            } else {
                MainContentScreen(viewModel = viewModel)
            }
        }

        // Active Fullscreen Meditation Player Modal
        if (activeMeditation != null) {
            MeditationPlayerDialog(
                meditation = activeMeditation!!,
                remainingSeconds = meditationRemaining,
                durationSeconds = meditationDuration,
                isPlaying = isMeditationPlaying,
                stepIndex = currentStepIndex,
                onPlay = { viewModel.startMeditation() },
                onPause = { viewModel.pauseMeditation() },
                onReset = { viewModel.resetMeditation() },
                onClose = { viewModel.closeMeditation() },
                onDurationSelect = { viewModel.setMeditationDurationMinutes(it) },
                onBellChime = { viewModel.audioEngine.playSingingBowlBell() }
            )
        }

        // Active Fullscreen Sense Exercise Modal
        if (activeSenseExercise != null) {
            SenseExercisePlayerDialog(
                exercise = activeSenseExercise!!,
                remainingSeconds = senseRemaining,
                durationSeconds = senseDuration,
                isPlaying = isSensePlaying,
                currentStepIndex = currentSenseStepIndex,
                onPlay = { viewModel.startSenseExercise() },
                onPause = { viewModel.pauseSenseExercise() },
                onReset = { viewModel.resetSenseExercise() },
                onClose = { viewModel.closeSenseExercise() }
            )
        }
    }
}

@Composable
fun ImmersiveBackgroundGlow(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerTop = Offset(size.width * 0.5f, -size.height * 0.05f)
        val radiusTop = size.height * 0.7f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricIndigo.copy(alpha = 0.35f),
                    ElectricIndigo.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = centerTop,
                radius = radiusTop
            ),
            radius = radiusTop,
            center = centerTop
        )

        val centerBottom = Offset(size.width * 0.5f, size.height * 0.95f)
        val radiusBottom = size.width * 0.8f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF0284C7).copy(alpha = 0.15f),
                    Color(0xFF701A75).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = centerBottom,
                radius = radiusBottom
            ),
            radius = radiusBottom,
            center = centerBottom
        )
    }
}

@Composable
fun GatewayScreen(onEnter: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBg)
    ) {
        ImmersiveBackgroundGlow()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "STATUS: BEREIT ZUM TRANSFORMIEREN",
                    color = LightCerulean.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Atme tief ein.",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-0.5).sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(36.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ImmersiveSurface.copy(alpha = 0.92f),
                                ImmersiveSurfaceAlt.copy(alpha = 0.88f)
                            )
                        )
                    )
                    .border(
                        width = 1.2.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ElectricIndigo.copy(alpha = 0.5f),
                                ImmersiveSurfaceBorder.copy(alpha = 0.4f)
                            )
                        ),
                        shape = RoundedCornerShape(36.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Smooth Ambient Inner Glow
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                ElectricIndigo.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.85f, size.height * 0.15f),
                            radius = size.width * 0.5f
                        ),
                        radius = size.width * 0.5f,
                        center = Offset(size.width * 0.85f, size.height * 0.15f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        SoftIceBlue.copy(alpha = 0.85f),
                                        ElectricIndigo.copy(alpha = 0.35f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, SoftIceBlue.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✨", fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Den Schweinehund zähmen",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Dein innerer Widerstand ist kein Feind, sondern ein Schutzsignal. Verstehe ihn mit Gelassenheit, um ihn kraftvoll zu führen.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                TritEinButton(onClick = onEnter)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Kein Hexenwerk. Nur Du und deine Gelassenheit.",
                    color = TextDarkMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MainContentScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    // 4 Tabs: 0 = Podcast, 1 = Meditation, 2 = Achtsamkeit, 3 = Selbstreflexion
    var selectedTab by remember { mutableIntStateOf(0) }

    val podcastEpisode by viewModel.podcastEpisode.collectAsStateWithLifecycle()
    val isPodcastPlaying by viewModel.isPodcastPlaying.collectAsStateWithLifecycle()
    val podcastPosition by viewModel.podcastPositionSeconds.collectAsStateWithLifecycle()
    val podcastSpeed by viewModel.podcastPlaybackSpeed.collectAsStateWithLifecycle()
    val activeChapterIndex by viewModel.currentPodcastChapterIndex.collectAsStateWithLifecycle()

    val challenges by viewModel.challenges.collectAsStateWithLifecycle()
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsStateWithLifecycle()
    val totalChallengesCount by viewModel.totalChallengesCount.collectAsStateWithLifecycle()
    val gratitudeEntries by viewModel.gratitudeEntries.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBg)
    ) {
        ImmersiveBackgroundGlow()

        Column(modifier = Modifier.fillMaxSize()) {
            // App Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "STATUS: IM EINKLANG",
                        color = LightCerulean.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = when (selectedTab) {
                            0 -> "Deep Dive Podcast"
                            1 -> "Meditationen"
                            2 -> "Achtsamkeit & Sinne"
                            3 -> "Selbstreflexion"
                            else -> "Gelassenheit"
                        },
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-0.5).sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ImmersiveSurfaceAlt)
                        .border(1.dp, ImmersiveBorderHighlight, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil",
                        tint = SoftIceBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Tab Content Switcher
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> PodcastScreen(
                        episode = podcastEpisode,
                        isPlaying = isPodcastPlaying,
                        positionSeconds = podcastPosition,
                        playbackSpeed = podcastSpeed,
                        activeChapterIndex = activeChapterIndex,
                        onTogglePlayPause = { viewModel.togglePodcastPlayPause() },
                        onSeekTo = { viewModel.seekPodcastTo(it) },
                        onSkip = { viewModel.skipPodcast(it) },
                        onCycleSpeed = { viewModel.cyclePodcastSpeed() },
                        onSelectChapter = { viewModel.selectPodcastChapter(it) },
                        onNavigateToMindfulness = { selectedTab = 2 }
                    )
                    1 -> MeditationLibraryScreen(
                        onSelectMeditation = { viewModel.openMeditation(it) }
                    )
                    2 -> MindfulnessScreen(
                        onSelectExercise = { viewModel.openSenseExercise(it) },
                        onOpenPodcast = { selectedTab = 0 }
                    )
                    3 -> SelfReflectionScreen(
                        challenges = challenges,
                        completedCount = completedChallengesCount,
                        totalCount = totalChallengesCount,
                        gratitudeEntries = gratitudeEntries,
                        onToggleChallenge = { viewModel.toggleChallenge(it) },
                        onAddCustomChallenge = { title, desc, cat, min, icon ->
                            viewModel.addCustomChallenge(title, desc, cat, min, icon)
                        },
                        onAddGratitudeEntry = { text, prompt, emoji, label, cat ->
                            viewModel.addGratitudeEntry(text, prompt, emoji, label, cat)
                        },
                        onDeleteGratitudeEntry = { viewModel.deleteGratitudeEntry(it) }
                    )
                }
            }

            // Bottom Navigation
            ImmersiveNavigationBar(
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it }
            )
        }
    }
}

@Composable
fun FocusDashboard(
    challenges: List<com.example.data.Challenge>,
    completedCount: Int,
    totalCount: Int,
    onToggleChallenge: (com.example.data.Challenge) -> Unit,
    onOpenMeditation: (com.example.meditation.Meditation) -> Unit,
    onNavigateTab: (Int) -> Unit
) {
    val insightPercent = if (totalCount > 0) ((completedCount.toFloat() / totalCount) * 100).toInt() else 0
    val featuredMeditation = MeditationCatalog.meditations.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(ImmersiveSurface.copy(alpha = 0.85f))
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(32.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ElectricIndigo.copy(alpha = 0.25f))
                                .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚡", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Aktivierungs-Zustand",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Du kannst das auf jeden Fall.",
                                color = LightCerulean,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Besiege deinen Schweinehund durch Verständnis, Gelassenheit und Ausgeglichenheit.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Metrics Pills Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricPill(
                    percent = "${(insightPercent + 40).coerceAtMost(98)}%",
                    label = "Einsicht",
                    highlighted = false,
                    modifier = Modifier.weight(1f)
                )
                MetricPill(
                    percent = "${(insightPercent + 60).coerceAtMost(100)}%",
                    label = "Ruhe",
                    highlighted = true,
                    modifier = Modifier.weight(1f)
                )
                MetricPill(
                    percent = "$completedCount/$totalCount",
                    label = "Kraft",
                    highlighted = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Start Meditation Hero
        if (featuredMeditation != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(ElectricIndigo.copy(alpha = 0.3f), ImmersiveSurfaceAlt)
                            )
                        )
                        .border(1.dp, ElectricIndigo.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .clickable { onOpenMeditation(featuredMeditation) }
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "JETZT ENTSPANNEN",
                                color = LightCerulean,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = featuredMeditation.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${featuredMeditation.defaultMinutes} Min • ${featuredMeditation.ambientFrequencyHz.toInt()} Hz Audio",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SoftIceBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Starten", tint = DeepIndigoText)
                        }
                    }
                }
            }
        }

        // Section Title: Daily Challenges preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HEUTIGE CHALLENGES",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Alle ansehen",
                    color = LightCerulean,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onNavigateTab(1) }
                )
            }
        }

        // Display top 3 challenges
        items(challenges.take(3), key = { it.id }) { challenge ->
            ChallengeItemCard(
                challenge = challenge,
                onToggle = { onToggleChallenge(challenge) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MetricPill(
    percent: String,
    label: String,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (highlighted) ElectricIndigo.copy(alpha = 0.6f) else ImmersiveSurfaceBorder
    val labelColor = if (highlighted) LightCerulean else TextMuted
    val containerBg = if (highlighted) ImmersiveSurfaceAlt else ImmersiveSurface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(containerBg)
            .border(1.dp, borderColor, RoundedCornerShape(22.dp))
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = percent,
                color = SoftIceBlue,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label.uppercase(),
                color = labelColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun ImmersiveNavigationBar(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit
) {
    val navItems = listOf(
        NavItem("Podcast", Icons.Default.Podcasts),
        NavItem("Meditation", Icons.Default.SelfImprovement),
        NavItem("Achtsamkeit", Icons.Default.Sensors),
        NavItem("Selbstreflexion", Icons.Default.AutoStories)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ImmersiveBg.copy(alpha = 0.96f))
            .border(width = 1.dp, color = ImmersiveSurfaceAlt, shape = RoundedCornerShape(0.dp))
            .padding(horizontal = 6.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->
                val isSelected = selectedTab == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelect(index) }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ImmersiveSurfaceAlt)
                                .border(1.dp, ElectricIndigo.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = SoftIceBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.label,
                        color = if (isSelected) SoftIceBlue else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)
