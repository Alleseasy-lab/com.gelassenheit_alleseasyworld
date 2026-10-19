package com.example.ui.podcast

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

// ─── Design Tokens (1:1 mit Flutter Theme) ───────────────────────────────────
private val DarkBg         = Color(0xFF070A12)
private val CardBg         = Color(0xFF10172A)
private val CardSubBg      = Color(0xFF1E293B)
private val CardBorder     = Color.White.copy(alpha = 0.06f)
private val BlueGreyText   = Color(0xFF94A3B8)
private val TextWhite70    = Color.White.copy(alpha = 0.70f)
private val TextWhite54    = Color.White.copy(alpha = 0.54f)
private val AccentGreen    = Color(0xFF69F0AE) // Colors.greenAccent
private val AccentBlue     = Color(0xFF3B82F6) // Colors.blueAccent
private val AccentPurple   = Color(0xFF8B5CF6)
private val AccentAmber    = Color(0xFFFFD54F) // Colors.amberAccent

@Composable
fun PodcastScreen(
    episode: PodcastEpisode,
    allEpisodes: List<PodcastEpisode> = emptyList(),
    isProUser: Boolean = false,
    onSelectEpisode: (PodcastEpisode) -> Unit = {},
    onOpenPaywall: () -> Unit = {},
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
            .background(DarkBg)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 28.dp)
    ) {
        // ── PRO Banner falls nicht abonniert ──────────────────────────────────
        if (!isProUser) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(ProAmberSurface, ImmersiveSurfaceAlt)
                            )
                        )
                        .border(1.dp, ProAmberBorder, RoundedCornerShape(20.dp))
                        .clickable { onOpenPaywall() }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Text("🎧", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Einfach nur zuhören & Klangbäder",
                                    color = ProGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Schalte 432 Hz Soundbaths & PRO-Episoden frei",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Button(
                            onClick = onOpenPaywall,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ProGold,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("PRO", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ── Audio / Episode Auswahlliste (Horizontal Chips) ──────────────────
        if (allEpisodes.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = "AUDIO-MEDITATION & PODCAST-AUSWAHL",
                        color = BlueGreyText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allEpisodes) { ep ->
                            val isSelected = ep.id == episode.id
                            val isLocked = ep.isProOnly && !isProUser

                            Box(
                                modifier = Modifier
                                    .width(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) ElectricIndigo.copy(alpha = 0.35f)
                                        else if (ep.isProOnly) ProAmberSurface.copy(alpha = 0.25f)
                                        else CardBg
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) SoftIceBlue
                                        else if (ep.isProOnly) ProAmberBorder
                                        else CardBorder,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        if (isLocked) {
                                            onOpenPaywall()
                                        } else {
                                            onSelectEpisode(ep)
                                        }
                                    }
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = ep.category.uppercase(),
                                            color = if (ep.isProOnly) ProGoldLight else LightCerulean,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )

                                        if (ep.isProOnly) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(ProGold.copy(alpha = 0.2f))
                                                    .border(0.5.dp, ProGold, RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    if (isLocked) {
                                                        Icon(
                                                            imageVector = Icons.Default.Lock,
                                                            contentDescription = "Gesperrt",
                                                            tint = ProGold,
                                                            modifier = Modifier.size(10.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(2.dp))
                                                    }
                                                    Text(
                                                        text = "PRO",
                                                        color = ProGoldLight,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = ep.title,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "${ep.durationSeconds / 60} Min",
                                        color = BlueGreyText,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Hauptkarte für den Podcast-Player ────────────────────────────────
        item {
            ContainerPodcastPlayer(
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

        // ── Sekundäre Info-Card (Brücke zur 5-Sinne-Übung) ────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                    .clickable { onNavigateToMindfulness() }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardSubBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🖐️", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VOM HÖREN INS SPÜREN",
                            color = AccentAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "5-Sinne-Übungen aus dem Podcast starten",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Starten",
                        tint = TextWhite54,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // ── Kapitel & Themen Sektion Header ──────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KAPITEL & THEMEN",
                    color = BlueGreyText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "${episode.chapters.size} Abschnitte",
                    color = TextWhite54,
                    fontSize = 11.sp
                )
            }
        }

        // ── Kapitel-Listenelemente ───────────────────────────────────────────
        itemsIndexed(episode.chapters) { index, chapter ->
            val isActive = index == activeChapterIndex
            ChapterListItem(
                chapter = chapter,
                isActive = isActive,
                isPlaying = isPlaying,
                onClick = { onSelectChapter(chapter) }
            )
        }

        // ── Kernerkenntnisse & Kernaussagen ──────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "KERNERKENNTNISSE AUS DER EPISODE",
                color = BlueGreyText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        itemsIndexed(episode.keyTakeaways) { idx, takeaway ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AccentBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${idx + 1}",
                            color = AccentBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = takeaway,
                        color = BlueGreyText,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // ── Zitate zum Verinnerlichen ────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ZITATE ZUM VERINNERLICHEN",
                color = BlueGreyText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        itemsIndexed(episode.quotes) { _, quote ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = "Zitat",
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Kapitel @ ${quote.timestamp}",
                            color = AccentBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "„${quote.quote}“",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ─── Hauptkarte Podcast-Player (1:1 Flutter Struktur) ────────────────────────
@Composable
fun ContainerPodcastPlayer(
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
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // Episoden-Info & Speed-Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AccentGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DEEP DIVE EPISODE 01",
                        color = AccentGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSubBg)
                        .clickable { onCycleSpeed() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        color = TextWhite70,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Titel & Beschreibung
            Text(
                text = episode.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = episode.subtitle,
                color = BlueGreyText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            // Aktives Kapitel Badge
            if (activeChapter != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSubBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Kapitel",
                            tint = AccentBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = activeChapter.title,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Fortschrittsleiste / Slider
            val progress = if (episode.durationSeconds > 0) {
                (positionSeconds.toFloat() / episode.durationSeconds.toFloat()).coerceIn(0f, 1f)
            } else 0f

            Slider(
                value = progress,
                onValueChange = { newProg ->
                    onSeekTo((newProg * episode.durationSeconds).toInt())
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = AccentBlue,
                    inactiveTrackColor = CardSubBg
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )

            PaddingValues(horizontal = 4.dp).let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatPodcastTime(positionSeconds),
                        color = BlueGreyText,
                        fontSize = 11.sp
                    )
                    Text(
                        text = formatPodcastTime(episode.durationSeconds),
                        color = BlueGreyText,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Player Controls (Zurückspringen, Play/Pause mit Gradient & Shadow, Vorspringen)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onSkip(-10) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "10s zurück",
                        tint = TextWhite70,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = AccentBlue.copy(alpha = 0.3f),
                            spotColor = AccentBlue.copy(alpha = 0.5f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(AccentBlue, AccentPurple),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            )
                        )
                        .clickable { onTogglePlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = { onSkip(10) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "10s vor",
                        tint = TextWhite70,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ─── Kapitel-Listenelement (1:1 Flutter Struktur) ────────────────────────────
@Composable
fun ChapterListItem(
    chapter: PodcastChapter,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .border(
                width = 1.dp,
                color = if (isActive) AccentBlue.copy(alpha = 0.4f) else CardBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AccentBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isActive && isPlaying) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Spielt",
                        tint = AccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Abspielen",
                        tint = AccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chapter.summary,
                    color = BlueGreyText,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun formatPodcastTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
