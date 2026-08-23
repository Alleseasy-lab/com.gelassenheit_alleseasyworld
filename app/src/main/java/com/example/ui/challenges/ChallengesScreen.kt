package com.example.ui.challenges

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Challenge
import com.example.ui.theme.*

@Composable
fun ChallengesScreen(
    challenges: List<Challenge>,
    completedCount: Int,
    totalCount: Int,
    onToggleChallenge: (Challenge) -> Unit,
    onAddCustomChallenge: (title: String, description: String, category: String, minutes: Int, icon: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Alle") }
    var isAddingCustom by remember { mutableStateOf(false) }

    var customTitle by remember { mutableStateOf("") }
    var customDesc by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("Gelassenheit") }
    var customMinutes by remember { mutableIntStateOf(5) }
    var customIcon by remember { mutableStateOf("⚡") }

    val categories = listOf("Alle", "Verständnis", "Gelassenheit", "Ausgeglichenheit")

    val filteredChallenges = remember(challenges, selectedCategory) {
        if (selectedCategory == "Alle") challenges
        else challenges.filter { it.category == selectedCategory }
    }

    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Progress Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(ImmersiveSurfaceAlt, ImmersiveSurface)
                        )
                    )
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(28.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ElectricIndigo.copy(alpha = 0.25f))
                                    .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Pokal",
                                    tint = SoftIceBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Tägliche Challenges",
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$completedCount von $totalCount gemeistert",
                                    color = LightCerulean,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = SoftIceBlue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SoftIceBlue,
                        trackColor = ImmersiveSurfaceBorder
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Kleine Schritte besiegen den Schweinehund nachhaltig – ohne Überforderung.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) ElectricIndigo.copy(alpha = 0.35f) else ImmersiveSurfaceAlt)
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else ImmersiveSurfaceBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) SoftIceBlue else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Add Custom Challenge Button or Form
        item {
            if (!isAddingCustom) {
                OutlinedButton(
                    onClick = { isAddingCustom = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftIceBlue),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveSurfaceBorder)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Neu", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eigene Challenge erstellen", fontSize = 13.sp)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(ImmersiveSurface)
                        .border(1.dp, ElectricIndigo.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "EIGENE CHALLENGE",
                                color = LightCerulean,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.5.sp
                            )
                            IconButton(
                                onClick = { isAddingCustom = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Abbrechen", tint = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customTitle,
                            onValueChange = { customTitle = it },
                            placeholder = { Text("Titel (z.B. 5 Min Schreibtisch lüften)", color = TextDarkMuted, fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricIndigo,
                                unfocusedBorderColor = ImmersiveSurfaceBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customDesc,
                            onValueChange = { customDesc = it },
                            placeholder = { Text("Kurze Beschreibung...", color = TextDarkMuted, fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricIndigo,
                                unfocusedBorderColor = ImmersiveSurfaceBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (customTitle.isNotBlank()) {
                                    onAddCustomChallenge(
                                        customTitle.trim(),
                                        customDesc.trim().ifEmpty { "Kleine, achtsame Tat für deinen Tag." },
                                        customCategory,
                                        customMinutes,
                                        customIcon
                                    )
                                    customTitle = ""
                                    customDesc = ""
                                    isAddingCustom = false
                                }
                            },
                            enabled = customTitle.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftIceBlue,
                                contentColor = DeepIndigoText
                            )
                        ) {
                            Text("Challenge hinzufügen", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Challenge Items
        items(filteredChallenges, key = { it.id }) { challenge ->
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
fun ChallengeItemCard(
    challenge: Challenge,
    onToggle: () -> Unit
) {
    val animatedBg by animateColorAsState(
        targetValue = if (challenge.isCompleted) ImmersiveSurfaceAlt.copy(alpha = 0.85f) else ImmersiveSurface,
        label = "cBg"
    )
    val animatedBorder by animateColorAsState(
        targetValue = if (challenge.isCompleted) ElectricIndigo.copy(alpha = 0.35f) else ImmersiveSurfaceBorder,
        label = "cBorder"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(22.dp))
            .clickable { onToggle() }
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (challenge.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = if (challenge.isCompleted) "Erledigt" else "Offen",
                tint = if (challenge.isCompleted) SoftIceBlue else TextMuted,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = challenge.title,
                        fontSize = 15.sp,
                        fontWeight = if (challenge.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                        color = if (challenge.isCompleted) TextSecondary else TextPrimary
                    )
                    Text(
                        text = challenge.iconEmoji,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = challenge.description,
                    fontSize = 13.sp,
                    color = if (challenge.isCompleted) TextDarkMuted else TextSecondary,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ImmersiveSurfaceAlt)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = challenge.category,
                            color = LightCerulean,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "ca. ${challenge.estimatedMinutes} Min",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
