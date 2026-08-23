package com.example.ui.gratitude

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GratitudeEntry
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GratitudeJournalScreen(
    entries: List<GratitudeEntry>,
    onAddEntry: (text: String, prompt: String, moodEmoji: String, moodLabel: String, category: String) -> Unit,
    onDeleteEntry: (GratitudeEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAddingNew by remember { mutableStateOf(false) }
    var entryText by remember { mutableStateOf("") }
    val prompts = remember {
        listOf(
            "Wofür bist du in diesem Moment besonders dankbar?",
            "Welcher kleine Moment der Gelassenheit hat dir heute gutgetan?",
            "Wo hast du heute deinem inneren Schweinehund mit Verständnis begegnet?",
            "Welche Person oder Situation hat dir heute Ruhe geschenkt?"
        )
    }
    var selectedPromptIndex by remember { mutableIntStateOf(0) }
    
    val moods = remember {
        listOf(
            Triple("✨", "Gelassen", "Gelassenheit"),
            Triple("🤝", "Verständnisvoll", "Verständnis"),
            Triple("🌱", "Ausgeglichen", "Ausgeglichenheit"),
            Triple("⚡", "Motiviert", "Tatkraft"),
            Triple("❤️", "Dankbar", "Herzlichkeit")
        )
    }
    var selectedMoodIndex by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header Banner
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ElectricIndigo.copy(alpha = 0.25f))
                                .border(1.dp, SoftIceBlue.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Dankbarkeit",
                                tint = SoftIceBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Dankbarkeitstagebuch",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${entries.size} reflektierte Momente",
                                color = LightCerulean,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Fokus auf das Gute stärkt Gelassenheit und nimmt dem inneren Schweinehund die Schwere.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Add Entry Action Card / Form
        item {
            AnimatedVisibility(visible = !isAddingNew) {
                Button(
                    onClick = { isAddingNew = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftIceBlue,
                        contentColor = DeepIndigoText
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Eintrag", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Neuen Moment festhalten",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedVisibility(visible = isAddingNew) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ImmersiveSurface)
                        .border(1.dp, ElectricIndigo.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NEUER EINTRAG",
                                color = LightCerulean,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.5.sp
                            )
                            IconButton(
                                onClick = { isAddingNew = false },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Abbrechen", tint = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Rotating Prompt selector
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(ImmersiveSurfaceAlt)
                                .clickable {
                                    selectedPromptIndex = (selectedPromptIndex + 1) % prompts.size
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "💡 Impulsfrage (tippen zum Wechseln):",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = prompts[selectedPromptIndex],
                                    color = SoftIceBlue,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Mood selection
                        Text(
                            text = "Stimmung:",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            moods.forEachIndexed { idx, item ->
                                val isSelected = idx == selectedMoodIndex
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) ElectricIndigo.copy(alpha = 0.4f) else ImmersiveSurfaceAlt)
                                        .border(
                                            1.dp,
                                            if (isSelected) ElectricIndigo else ImmersiveSurfaceBorder,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { selectedMoodIndex = idx }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${item.first} ${item.second}",
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Input field
                        OutlinedTextField(
                            value = entryText,
                            onValueChange = { entryText = it },
                            placeholder = {
                                Text("Schreibe auf, was dir Ruhe, Freude oder Verständnis geschenkt hat...", color = TextDarkMuted, fontSize = 13.sp)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricIndigo,
                                unfocusedBorderColor = ImmersiveSurfaceBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = SoftIceBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (entryText.isNotBlank()) {
                                    val mood = moods[selectedMoodIndex]
                                    onAddEntry(
                                        entryText.trim(),
                                        prompts[selectedPromptIndex],
                                        mood.first,
                                        mood.second,
                                        mood.third
                                    )
                                    entryText = ""
                                    isAddingNew = false
                                }
                            },
                            enabled = entryText.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftIceBlue,
                                contentColor = DeepIndigoText,
                                disabledContainerColor = ImmersiveSurfaceAlt,
                                disabledContentColor = TextDarkMuted
                            )
                        ) {
                            Text("Im Tagebuch speichern", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "VERGANGENE EINTRÄGE",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (entries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(ImmersiveSurface.copy(alpha = 0.5f))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🌱", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Noch keine Einträge",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Halte heute deinen ersten dankbaren Moment fest.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(entries, key = { it.id }) { entry ->
                GratitudeEntryCard(entry = entry, onDelete = { onDeleteEntry(entry) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GratitudeEntryCard(
    entry: GratitudeEntry,
    onDelete: () -> Unit
) {
    val dateStr = remember(entry.timestamp) {
        val sdf = SimpleDateFormat("dd. MMM yyyy • HH:mm", Locale.GERMAN)
        sdf.format(Date(entry.timestamp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ImmersiveSurfaceAlt)
            .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
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
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ElectricIndigo.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(entry.moodEmoji, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = entry.moodLabel,
                            color = SoftIceBlue,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = dateStr,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Löschen",
                        tint = TextDarkMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = entry.text,
                color = TextPrimary,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            if (entry.prompt.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ImmersiveSurface.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Impuls: ${entry.prompt}",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}
