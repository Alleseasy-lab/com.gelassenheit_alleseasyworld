package com.example.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

data class GoalTemplate(
    val title: String,
    val description: String,
    val targetType: String,
    val targetValue: Int,
    val unit: String,
    val period: String,
    val iconEmoji: String,
    val category: String
)

val PRESET_GOAL_TEMPLATES = listOf(
    GoalTemplate(
        title = "10 Min. täglich meditieren",
        description = "Geführte Klangreise oder Stille zur inneren Zentrierung",
        targetType = "MEDITATION",
        targetValue = 10,
        unit = "Minuten",
        period = "DAILY",
        iconEmoji = "🧘",
        category = "Gelassenheit"
    ),
    GoalTemplate(
        title = "Positiven Gedanken aufschreiben",
        description = "Einen Moment der Dankbarkeit oder Selbstmitgefühl festhalten",
        targetType = "GRATITUDE",
        targetValue = 1,
        unit = "Eintrag",
        period = "DAILY",
        iconEmoji = "✨",
        category = "Dankbarkeit"
    ),
    GoalTemplate(
        title = "1 Achtsamkeits-Challenge meistern",
        description = "Eine kleine Übung gegen den inneren Schweinehund",
        targetType = "CHALLENGE",
        targetValue = 1,
        unit = "Challenge",
        period = "DAILY",
        iconEmoji = "🌱",
        category = "Ausgeglichenheit"
    ),
    GoalTemplate(
        title = "45 Min. Wochen-Meditation",
        description = "Ausgeglichene Meditationspraxis über die ganze Woche",
        targetType = "MEDITATION",
        targetValue = 45,
        unit = "Minuten",
        period = "WEEKLY",
        iconEmoji = "🌊",
        category = "Gelassenheit"
    ),
    GoalTemplate(
        title = "2 Podcast Deep Dives hören",
        description = "Wissenschaftliche Impulse zur Stressreduktion aufsaugen",
        targetType = "PODCAST",
        targetValue = 2,
        unit = "Episoden",
        period = "WEEKLY",
        iconEmoji = "🎧",
        category = "Verständnis"
    ),
    GoalTemplate(
        title = "3 bewusste Atempausen",
        description = "Im Alltag für 3 tiefe Atemzüge innehalten",
        targetType = "MINDFULNESS",
        targetValue = 3,
        unit = "Pausen",
        period = "DAILY",
        iconEmoji = "🫁",
        category = "Gelassenheit"
    ),
    GoalTemplate(
        title = "20 Min. Spaziergang in Stille",
        description = "Ohne Smartphone die Natur und Umgebung wahrnehmen",
        targetType = "CUSTOM",
        targetValue = 20,
        unit = "Minuten",
        period = "DAILY",
        iconEmoji = "🚶",
        category = "Fokus"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    initialPeriod: String = "DAILY",
    onDismiss: () -> Unit,
    onSaveGoal: (
        title: String,
        description: String,
        targetType: String,
        targetValue: Int,
        unit: String,
        period: String,
        iconEmoji: String,
        category: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var period by remember { mutableStateOf(initialPeriod) } // "DAILY" or "WEEKLY"
    var targetType by remember { mutableStateOf("CUSTOM") }
    var targetValueText by remember { mutableStateOf("10") }
    var unit by remember { mutableStateOf("Minuten") }
    var selectedCategory by remember { mutableStateOf("Gelassenheit") }
    var selectedEmoji by remember { mutableStateOf("🎯") }

    val categories = listOf("Gelassenheit", "Dankbarkeit", "Ausgeglichenheit", "Verständnis", "Fokus")
    val emojis = listOf("🎯", "🧘", "✨", "🌱", "🌊", "🎧", "🫁", "🚶", "🍵", "🧠", "🔥", "🌿")
    val units = listOf("Minuten", "Einträge", "Übungen", "Episoden", "Mal")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_goal_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveSurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🎯",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Neues Ziel setzen",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_add_goal_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Schließen",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Templates
                Text(
                    text = "Schnellvorlagen",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(PRESET_GOAL_TEMPLATES) { template ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ImmersiveSurfaceAlt,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveSurfaceBorder),
                            modifier = Modifier
                                .clickable {
                                    title = template.title
                                    description = template.description
                                    period = template.period
                                    targetType = template.targetType
                                    targetValueText = template.targetValue.toString()
                                    unit = template.unit
                                    selectedCategory = template.category
                                    selectedEmoji = template.iconEmoji
                                }
                                .testTag("template_${template.title}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(template.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = template.title,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Period Selection (Daily vs Weekly)
                Text(
                    text = "Zielzeitraum",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = period == "DAILY",
                        onClick = { period = "DAILY" },
                        label = { Text("📅 Täglich") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricIndigo,
                            selectedLabelColor = Color.White,
                            containerColor = ImmersiveSurfaceAlt,
                            labelColor = TextSecondary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = period == "WEEKLY",
                        onClick = { period = "WEEKLY" },
                        label = { Text("📆 Wöchentlich") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricIndigo,
                            selectedLabelColor = Color.White,
                            containerColor = ImmersiveSurfaceAlt,
                            labelColor = TextSecondary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Goal Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Zielname (z.B. 10 Min. meditieren)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = ImmersiveSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = ElectricIndigo,
                        unfocusedLabelColor = TextMuted,
                        focusedContainerColor = ImmersiveSurfaceAlt,
                        unfocusedContainerColor = ImmersiveSurfaceAlt
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_goal_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Goal Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung (optional)") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = ImmersiveSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = ElectricIndigo,
                        unfocusedLabelColor = TextMuted,
                        focusedContainerColor = ImmersiveSurfaceAlt,
                        unfocusedContainerColor = ImmersiveSurfaceAlt
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Target Value & Unit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = targetValueText,
                        onValueChange = { targetValueText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Zielwert") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricIndigo,
                            unfocusedBorderColor = ImmersiveSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = ElectricIndigo,
                            unfocusedLabelColor = TextMuted,
                            focusedContainerColor = ImmersiveSurfaceAlt,
                            unfocusedContainerColor = ImmersiveSurfaceAlt
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("add_goal_target_input")
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Einheit") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricIndigo,
                            unfocusedBorderColor = ImmersiveSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = ElectricIndigo,
                            unfocusedLabelColor = TextMuted,
                            focusedContainerColor = ImmersiveSurfaceAlt,
                            unfocusedContainerColor = ImmersiveSurfaceAlt
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Emoji Picker
                Text(
                    text = "Symbol wählen",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(emojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (selectedEmoji == emoji) ElectricIndigo.copy(alpha = 0.25f) else ImmersiveSurfaceAlt)
                                .border(
                                    1.dp,
                                    if (selectedEmoji == emoji) ElectricIndigo else ImmersiveSurfaceBorder,
                                    CircleShape
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Selection
                Text(
                    text = "Kategorie",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricIndigo.copy(alpha = 0.25f),
                                selectedLabelColor = SoftIceBlue,
                                containerColor = ImmersiveSurfaceAlt,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save Action Button
                val isFormValid = title.isNotBlank() && (targetValueText.toIntOrNull() ?: 0) > 0

                Button(
                    onClick = {
                        val value = targetValueText.toIntOrNull() ?: 1
                        onSaveGoal(
                            title.trim(),
                            description.trim(),
                            targetType,
                            value,
                            unit.trim().ifEmpty { "Einheiten" },
                            period,
                            selectedEmoji,
                            selectedCategory
                        )
                        onDismiss()
                    },
                    enabled = isFormValid,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricIndigo,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_goal_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ziel aktivieren",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
