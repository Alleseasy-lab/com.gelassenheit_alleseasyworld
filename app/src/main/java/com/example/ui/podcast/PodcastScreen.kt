package com.example.ui.podcast

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.podcast.PodcastChapter
import com.example.podcast.PodcastEpisode
import com.example.ui.theme.*

@Composable
fun PodcastScreen(
    episode: PodcastEpisode,
    isPlaying: Boolean,
    positionSeconds: Int,
    playbackSpeed: Float,
    activeChapterIndex: Int,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSkip: (Int) -> Unit,
    onCycleSpeed: () -> Unit,
    onSelectChapter: (PodcastChapter) -> Unit,
    onNavigateToMindfulness: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Podcast Player Card
        item {
            PodcastHeroPlayerCard(
                episode = episode,
                isPlaying = isPlaying,
                positionSeconds = positionSeconds,
                playbackSpeed = playbackSpeed,
                activeChapterIndex = activeChapterIndex,
                onTogglePlayPause = onTogglePlayPause,
                onSeekTo = onSeekTo,
                onSkip = onSkip,
                onCycleSpeed = onCycleSpeed
            )
        }

        // Bridge to Practice Callout
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(ElectricIndigo.copy(alpha = 0.35f), ImmersiveSurfaceAlt)
                        )
                    )
                    .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                    .clickable { onNavigateToMindfulness() }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SoftIceBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🖐️", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "VOM HÖREN INS SPÜREN",
                                color = LightCerulean,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "5-Sinne-Übungen aus dem Podcast starten",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Zur Praxis",
                        tint = SoftIceBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Section Title: Chapters
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KAPITEL & THEMEN",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "${episode.chapters.size} Abschnitte",
                    color = LightCerulean,
                    fontSize = 12.sp
                )
            }
        }

        // Chapter List items
        itemsIndexed(episode.chapters) { index, chapter ->
            val isActive = index == activeChapterIndex
            ChapterItemCard(
                chapter = chapter,
                isActive = isActive,
                isPlaying = isPlaying,
                onClick = { onSelectChapter(chapter) }
            )
        }

        // Section Title: Key Insights & Takeaways
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "KERNERKENNTNISSE AUS DER EPISODE",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
        }

        // Takeaways Cards
        itemsIndexed(episode.keyTakeaways) { idx, takeaway ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ImmersiveSurface.copy(alpha = 0.75f))
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(ElectricIndigo.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${idx + 1}", color = SoftIceBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = takeaway,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // Core Quotes
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ZITATE ZUM VERINNERLICHEN",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
        }

        itemsIndexed(episode.quotes) { _, quote ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ImmersiveSurfaceAlt)
                    .border(1.dp, ElectricIndigo.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = "Zitat",
                            tint = LightCerulean,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Kapitel @ ${quote.timestamp}",
                            color = LightCerulean,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "„${quote.quote}“",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PodcastHeroPlayerCard(
    episode: PodcastEpisode,
    isPlaying: Boolean,
    positionSeconds: Int,
    playbackSpeed: Float,
    activeChapterIndex: Int,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSkip: (Int) -> Unit,
    onCycleSpeed: () -> Unit
) {
    val activeChapter = episode.chapters.getOrNull(activeChapterIndex)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        ImmersiveSurface.copy(alpha = 0.95f),
                        ImmersiveSurfaceAlt.copy(alpha = 0.95f)
                    )
                )
            )
            .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(32.dp))
            .padding(22.dp)
    ) {
        Column {
            // Header Row with Episode Badge & Speed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) Color(0xFF00E676) else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DEEP DIVE EPISODE 01",
                        color = LightCerulean,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                // Speed Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElectricIndigo.copy(alpha = 0.25f))
                        .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { onCycleSpeed() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        color = SoftIceBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Subtitle
            Text(
                text = episode.title,
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = episode.subtitle,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Active Chapter Indicator
            if (activeChapter != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ImmersiveBg.copy(alpha = 0.6f))
                        .border(1.dp, ImmersiveBorderHighlight, RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Kapitel",
                            tint = SoftIceBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeChapter.title,
                            color = SoftIceBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Animated Equalizer Waveform
            AudioWaveformVisualizer(
                isPlaying = isPlaying,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Scrubber Slider
            val progress = if (episode.durationSeconds > 0) {
                (positionSeconds.toFloat() / episode.durationSeconds.toFloat()).coerceIn(0f, 1f)
            } else 0f

            Slider(
                value = progress,
                onValueChange = { newProg ->
                    onSeekTo((newProg * episode.durationSeconds).toInt())
                },
                colors = SliderDefaults.colors(
                    thumbColor = SoftIceBlue,
                    activeTrackColor = LightCerulean,
                    inactiveTrackColor = ImmersiveSurfaceBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Time Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatPodcastTime(positionSeconds),
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatPodcastTime(episode.durationSeconds),
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Control Buttons: -15s, Play/Pause, +15s
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // -15s
                IconButton(
                    onClick = { onSkip(-15) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "15 Sek zurück",
                        tint = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Play / Pause Main Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(SoftIceBlue, ElectricIndigo)
                            )
                        )
                        .clickable { onTogglePlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Abspielen",
                        tint = DeepIndigoText,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // +15s
                IconButton(
                    onClick = { onSkip(15) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "15 Sek vor",
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ChapterItemCard(
    chapter: PodcastChapter,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isActive) ElectricIndigo.copy(alpha = 0.7f) else ImmersiveSurfaceBorder
    val bgColor = if (isActive) ImmersiveSurfaceAlt else ImmersiveSurface.copy(alpha = 0.65f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isActive) ElectricIndigo.copy(alpha = 0.5f) else ImmersiveBg),
                contentAlignment = Alignment.Center
            ) {
                if (isActive && isPlaying) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Spielt",
                        tint = SoftIceBlue,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Kapitel anspringen",
                        tint = if (isActive) SoftIceBlue else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    color = if (isActive) SoftIceBlue else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chapter.summary,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun AudioWaveformVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "wave")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val barCount = 28
        val spacing = size.width / barCount
        val barWidth = spacing * 0.55f

        for (i in 0 until barCount) {
            val normalizedI = i.toFloat() / barCount
            val wave = if (isPlaying) {
                val sinVal = kotlin.math.sin((normalizedI * 4 + phase * 2 * kotlin.math.PI).toFloat())
                (sinVal * 0.5f + 0.5f) * 0.7f + 0.3f
            } else {
                0.25f + (i % 3) * 0.08f
            }

            val barHeight = size.height * wave
            val top = (size.height - barHeight) / 2
            val left = i * spacing + (spacing - barWidth) / 2

            drawRoundRect(
                color = if (isPlaying) LightCerulean.copy(alpha = 0.85f) else TextMuted.copy(alpha = 0.4f),
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
        }
    }
}

fun formatPodcastTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
