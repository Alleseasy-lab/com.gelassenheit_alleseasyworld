package com.example.ui.meditation

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meditation.Meditation
import com.example.meditation.MeditationCatalog
import com.example.ui.theme.*

@Composable
fun MeditationLibraryScreen(
    isProUser: Boolean = false,
    onOpenPaywall: () -> Unit = {},
    onSelectMeditation: (Meditation) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Alle") }

    val filteredMeditations = remember(selectedCategory) {
        if (selectedCategory == "Alle") {
            MeditationCatalog.meditations
        } else {
            MeditationCatalog.meditations.filter { it.category == selectedCategory }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
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
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = "Meditation",
                                tint = SoftIceBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Geführte Meditationen",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Klangschale & Naturfrequenzen",
                                color = LightCerulean,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Finde in wenigen Minuten zu Ausgeglichenheit, baue Stress ab und schaffe erholsamen Schlaf.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // PRO Upgrade Banner (if not PRO)
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
                            Text("👑", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Alle Meister-Meditationen freischalten",
                                    color = ProGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Inkl. 396Hz Deep Sleep & 528Hz Theta-Trance",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Button(
                            onClick = onOpenPaywall,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ProGold,
                                contentColor = androidx.compose.ui.graphics.Color.Black
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

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MeditationCatalog.categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) ElectricIndigo.copy(alpha = 0.35f) else ImmersiveSurfaceAlt)
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else ImmersiveSurfaceBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) SoftIceBlue else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "${filteredMeditations.size} SITZUNGEN VERFÜGBAR",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Meditation Cards
        items(filteredMeditations, key = { it.id }) { meditation ->
            MeditationCard(
                meditation = meditation,
                isProUser = isProUser,
                onClick = { onSelectMeditation(meditation) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MeditationCard(
    meditation: Meditation,
    isProUser: Boolean = false,
    onClick: () -> Unit
) {
    val isLocked = meditation.isProOnly && !isProUser

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(if (meditation.isProOnly) ProAmberSurface.copy(alpha = 0.35f) else ImmersiveSurface)
            .border(
                1.dp,
                if (meditation.isProOnly) ProAmberBorder else ImmersiveSurfaceBorder,
                RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (meditation.isProOnly) ProAmberSurface else ImmersiveSurfaceAlt)
                            .border(
                                1.dp,
                                if (meditation.isProOnly) ProGold.copy(alpha = 0.6f) else ImmersiveBorderHighlight,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(meditation.iconEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = meditation.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (meditation.isProOnly) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ProGold.copy(alpha = 0.25f))
                                        .border(0.5.dp, ProGold, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        color = ProGoldLight,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${meditation.category} • ${meditation.defaultMinutes} Min • ${meditation.ambientFrequencyHz.toInt()} Hz",
                            color = if (meditation.isProOnly) ProGoldLight.copy(alpha = 0.8f) else LightCerulean,
                            fontSize = 11.sp
                        )
                    }
                }

                // Play or Lock Icon Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isLocked) ImmersiveSurfaceAlt else if (meditation.isProOnly) ProGold else SoftIceBlue),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Gesperrt (PRO)",
                            tint = ProGold,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Starten",
                            tint = if (meditation.isProOnly) androidx.compose.ui.graphics.Color.Black else DeepIndigoText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = meditation.subtitle,
                color = if (meditation.isProOnly) ProGoldLight.copy(alpha = 0.9f) else SoftIceBlue.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = meditation.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}
