package com.example.ui.reflection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.data.Challenge
import com.example.data.GratitudeEntry
import com.example.ui.challenges.ChallengesScreen
import com.example.ui.gratitude.GratitudeJournalScreen
import com.example.ui.theme.*

@Composable
fun SelfReflectionScreen(
    challenges: List<Challenge>,
    completedCount: Int,
    totalCount: Int,
    gratitudeEntries: List<GratitudeEntry>,
    onToggleChallenge: (Challenge) -> Unit,
    onAddCustomChallenge: (String, String, String, Int, String) -> Unit,
    onAddGratitudeEntry: (String, String, String, String, String) -> Unit,
    onDeleteGratitudeEntry: (GratitudeEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    var subTab by remember { mutableIntStateOf(0) } // 0 = Tagebuch & Dankbarkeit, 1 = Schweinehund-Challenges

    Column(modifier = modifier.fillMaxSize()) {
        // Sub-Tab Switcher
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ImmersiveSurface)
                    .border(1.dp, ImmersiveSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                // Tab 0: Dankbarkeit
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (subTab == 0) ImmersiveSurfaceAlt else androidx.compose.ui.graphics.Color.Transparent)
                        .clickable { subTab = 0 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = if (subTab == 0) SoftIceBlue else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Dankbarkeit",
                            color = if (subTab == 0) SoftIceBlue else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Tab 1: Challenges
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (subTab == 1) ImmersiveSurfaceAlt else androidx.compose.ui.graphics.Color.Transparent)
                        .clickable { subTab = 1 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = if (subTab == 1) SoftIceBlue else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Challenges",
                            color = if (subTab == 1) SoftIceBlue else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // SubTab Content
        Box(modifier = Modifier.weight(1f)) {
            if (subTab == 0) {
                GratitudeJournalScreen(
                    entries = gratitudeEntries,
                    onAddEntry = onAddGratitudeEntry,
                    onDeleteEntry = onDeleteGratitudeEntry
                )
            } else {
                ChallengesScreen(
                    challenges = challenges,
                    completedCount = completedCount,
                    totalCount = totalCount,
                    onToggleChallenge = onToggleChallenge,
                    onAddCustomChallenge = onAddCustomChallenge
                )
            }
        }
    }
}
