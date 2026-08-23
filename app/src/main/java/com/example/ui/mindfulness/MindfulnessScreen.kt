package com.example.ui.mindfulness

import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mindfulness.MindfulnessCatalog
import com.example.mindfulness.SenseExercise
import com.example.ui.theme.*

@Composable
fun MindfulnessScreen(
    onSelectExercise: (SenseExercise) -> Unit,
    onOpenPodcast: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSosGroundingOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Card: The 5 Senses as biological shield
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(ImmersiveSurface.copy(alpha = 0.9f))
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(32.dp))
                    .padding(22.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SoftIceBlue.copy(alpha = 0.2f))
                                .border(1.dp, SoftIceBlue.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛡️", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "DIE 5 PHYSISCHEN SINNE",
                                color = LightCerulean,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "Biologische Befreiung",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Deine Sinne fordern keine Leistung und kennen keine Algorithmen. Sie sind deine unbestechliche Hardware im Hier und Jetzt.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // SOS Quick Grounding Button
                    Button(
                        onClick = { isSosGroundingOpen = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricIndigo.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = "SOS Grounding",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "5-4-3-2-1 SOS-Erdung starten",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Section Title: Senses Exercises
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SINNES-ÜBUNGEN (AUS DEM PODCAST)",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "5 Module",
                    color = LightCerulean,
                    fontSize = 12.sp
                )
            }
        }

        // Sense Exercise Items
        items(MindfulnessCatalog.senseExercises, key = { it.id }) { exercise ->
            SenseExerciseCard(
                exercise = exercise,
                onClick = { onSelectExercise(exercise) }
            )
        }

        // Deep Dive Podcast Reference
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ImmersiveSurfaceAlt)
                    .border(1.dp, ElectricIndigo.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .clickable { onOpenPodcast() }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Podcasts,
                            contentDescription = "Podcast",
                            tint = SoftIceBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "HINTERGRUNDWISSEN",
                                color = LightCerulean,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Erfahre im Podcast die Neurobiologie der Sinne",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Zum Podcast",
                        tint = SoftIceBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (isSosGroundingOpen) {
        SosGroundingDialog(onDismiss = { isSosGroundingOpen = false })
    }
}

@Composable
fun SenseExerciseCard(
    exercise: SenseExercise,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(ImmersiveSurface.copy(alpha = 0.85f))
            .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ElectricIndigo.copy(alpha = 0.25f))
                    .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(exercise.iconEmoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exercise.senseName.uppercase(),
                        color = LightCerulean,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "${exercise.practiceDurationSeconds / 60} Min",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = exercise.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = exercise.subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ImmersiveSurfaceAlt),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Übung starten",
                    tint = SoftIceBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SenseExercisePlayerDialog(
    exercise: SenseExercise,
    remainingSeconds: Int,
    durationSeconds: Int,
    isPlaying: Boolean,
    currentStepIndex: Int,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit
) {
    val progress = if (durationSeconds > 0) {
        1f - (remainingSeconds.toFloat() / durationSeconds.toFloat())
    } else 0f

    val currentStep = exercise.steps.getOrNull(currentStepIndex) ?: exercise.prompt

    // Breathing or Wave Animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ImmersiveBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SINNES-ÜBUNG • ${exercise.senseName.uppercase()}",
                            color = LightCerulean,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = exercise.title,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ImmersiveSurfaceAlt)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Schließen",
                            tint = TextSecondary
                        )
                    }
                }

                // Central Pulse Canvas with Progress Ring
                Box(
                    modifier = Modifier
                        .size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2

                        // Background circle
                        drawCircle(
                            color = ImmersiveSurfaceBorder,
                            radius = radius,
                            style = Stroke(width = strokeWidth)
                        )

                        // Glowing Active Arc
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(SoftIceBlue, LightCerulean, ElectricIndigo, SoftIceBlue)
                            ),
                            startAngle = -90f,
                            sweepAngle = progress * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth)
                        )
                    }

                    // Inner animated orb
                    Box(
                        modifier = Modifier
                            .size(170.dp * (if (isPlaying) pulseScale else 1f))
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        SoftIceBlue.copy(alpha = 0.3f),
                                        ElectricIndigo.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, SoftIceBlue.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(exercise.iconEmoji, fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            val mins = remainingSeconds / 60
                            val secs = remainingSeconds % 60
                            Text(
                                text = String.format("%02d:%02d", mins, secs),
                                color = SoftIceBlue,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Step & Neurobiological Fact Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ImmersiveSurface)
                        .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SCHRITT ${currentStepIndex + 1} VON ${exercise.steps.size}",
                            color = LightCerulean,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentStep,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 ${exercise.neuroBiologicalFact}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onReset,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(ImmersiveSurfaceAlt)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Zurücksetzen",
                            tint = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(SoftIceBlue, ElectricIndigo)
                                )
                            )
                            .clickable {
                                if (isPlaying) onPause() else onPlay()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Start",
                            tint = DeepIndigoText,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SosGroundingDialog(onDismiss: () -> Unit) {
    val items = listOf(
        "5 Dinge, die du jetzt SEHEN kannst (Farben, Formen, Licht)",
        "4 Dinge, die du jetzt TASTEN kannst (Stuhl, Kleidung, Füße)",
        "3 Geräusche, die du jetzt HÖREN kannst (Uhr, Lüfter, Atmung)",
        "2 Dinge, die du jetzt RIECHEN kannst (Kaffee, Frische Luft)",
        "1 tiefen, kühlen Atemzug durch die Nase spüren"
    )

    var checkedStates by remember { mutableStateOf(List(5) { false }) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(ImmersiveSurface)
                .border(1.dp, ImmersiveBorderHighlight, RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "5-4-3-2-1 SOS ERDUNG",
                        color = LightCerulean,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Hole dein Nervensystem in 60 Sekunden aus dem Alarmzustand zurück in den physischen Raum.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                items.forEachIndexed { index, itemText ->
                    val isChecked = checkedStates[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isChecked) ImmersiveSurfaceAlt else Color.Transparent)
                            .clickable {
                                checkedStates = checkedStates.toMutableList().also {
                                    it[index] = !it[index]
                                }
                            }
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isChecked) SoftIceBlue else TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = itemText,
                            color = if (isChecked) TextPrimary else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftIceBlue,
                        contentColor = DeepIndigoText
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ich bin wieder voll präsent", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
