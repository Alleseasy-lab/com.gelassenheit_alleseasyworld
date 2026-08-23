package com.example.ui.meditation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meditation.Meditation
import com.example.ui.theme.*

@Composable
fun MeditationPlayerDialog(
    meditation: Meditation,
    remainingSeconds: Int,
    durationSeconds: Int,
    isPlaying: Boolean,
    stepIndex: Int,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit,
    onDurationSelect: (Int) -> Unit,
    onBellChime: () -> Unit
) {
    val progress = if (durationSeconds > 0) {
        1f - (remainingSeconds.toFloat() / durationSeconds.toFloat())
    } else 0f

    val currentStep = meditation.guidedSteps.getOrNull(stepIndex) ?: meditation.guidedSteps.first()

    // Breathing wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "breath")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg.copy(alpha = 0.96f))
    ) {
        // Glow Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricIndigo.copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.4f),
                    radius = size.width * 0.8f
                ),
                radius = size.width * 0.8f,
                center = Offset(size.width * 0.5f, size.height * 0.4f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                        .border(1.dp, ImmersiveSurfaceBorder, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Schließen", tint = TextPrimary)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = meditation.category.uppercase(),
                        color = LightCerulean,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "${meditation.ambientFrequencyHz.toInt()} Hz Harmonie",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                IconButton(
                    onClick = onBellChime,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                        .border(1.dp, ImmersiveSurfaceBorder, CircleShape)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = "Klangschale", tint = SoftIceBlue)
                }
            }

            // Central Breathing Pulse & Progress Ring
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Pulsing breath aura
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .scale(if (isPlaying) breathScale else 1f)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    ElectricIndigo.copy(alpha = 0.4f),
                                    ElectricIndigo.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Circular Progress indicator
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 5.dp.toPx()
                    // Track
                    drawCircle(
                        color = ImmersiveSurfaceBorder,
                        radius = (size.minDimension - strokeWidth) / 2,
                        style = Stroke(width = strokeWidth)
                    )
                    // Progress
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(SoftIceBlue, ElectricIndigo, LightCerulean, SoftIceBlue)
                        ),
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Center Timer & Phase
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = timeFormatted,
                        color = TextPrimary,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPlaying) currentStep.breathPhase else "Bereit",
                        color = LightCerulean,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Guided Step Instruction Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(ImmersiveSurface.copy(alpha = 0.85f))
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentStep.title,
                        color = SoftIceBlue,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentStep.instruction,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Duration selection chips (3, 5, 10, 15 min)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(3, 5, 10, 15).forEach { mins ->
                    val isSelected = (durationSeconds / 60) == mins
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ElectricIndigo.copy(alpha = 0.3f) else ImmersiveSurfaceAlt)
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else ImmersiveSurfaceBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable(enabled = !isPlaying) { onDurationSelect(mins) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "$mins Min",
                            color = if (isSelected) SoftIceBlue else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Bottom Player Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onReset,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                        .border(1.dp, ImmersiveSurfaceBorder, CircleShape)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Zurücksetzen", tint = TextSecondary)
                }

                // Big Play / Pause Button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(listOf(ElectricIndigo, SoftIceBlue))
                        )
                        .clickable {
                            if (isPlaying) onPause() else onPlay()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Starten",
                        tint = DeepIndigoText,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(
                    onClick = onBellChime,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(ImmersiveSurfaceAlt)
                        .border(1.dp, ImmersiveSurfaceBorder, CircleShape)
                ) {
                    Text(text = "🔔", fontSize = 20.sp)
                }
            }
        }
    }
}
