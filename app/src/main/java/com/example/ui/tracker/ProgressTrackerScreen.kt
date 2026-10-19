package com.example.ui.tracker

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.MilestoneReward
import com.example.data.MilestoneTier
import com.example.data.UserGoal
import com.example.ui.theme.*

@Composable
fun ProgressTrackerScreen(
    viewModel: MainViewModel,
    onNavigateToMeditation: () -> Unit = {},
    onNavigateToReflection: () -> Unit = {}
) {
    val allGoals by viewModel.allGoals.collectAsState()
    val completedGoalsCount by viewModel.completedGoalsCount.collectAsState()
    val totalGoalsCount by viewModel.totalGoalsCount.collectAsState()
    val totalMeditationSeconds by viewModel.totalMeditationSeconds.collectAsState()
    val gratitudeEntries by viewModel.gratitudeEntries.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val milestones by viewModel.milestones.collectAsState()

    var selectedTab by remember { mutableStateOf("DAILY") } // "DAILY", "WEEKLY", "MILESTONES"
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var selectedMilestoneForDialog by remember { mutableStateOf<MilestoneReward?>(null) }

    val dailyGoals = allGoals.filter { it.period == "DAILY" }
    val weeklyGoals = allGoals.filter { it.period == "WEEKLY" }
    val currentGoals = if (selectedTab == "DAILY") dailyGoals else weeklyGoals

    val activePeriodGoals = currentGoals.filter { !it.isCompleted }
    val completedPeriodGoals = currentGoals.filter { it.isCompleted }

    val periodCompletedCount = currentGoals.count { it.isCompleted }
    val periodTotalCount = currentGoals.size
    val periodProgressRatio = if (periodTotalCount > 0) periodCompletedCount.toFloat() / periodTotalCount else 0f

    val totalMedMinutes = totalMeditationSeconds / 60
    val unlockedMilestonesCount = milestones.count { it.isUnlocked }

    // Dialog for Milestone details
    selectedMilestoneForDialog?.let { milestone ->
        MilestoneRewardDialog(
            milestone = milestone,
            onDismiss = { selectedMilestoneForDialog = null },
            onPlayChime = { viewModel.playRewardSingingBowl() }
        )
    }

    // Dialog to Add Goal
    if (showAddGoalDialog) {
        AddGoalDialog(
            initialPeriod = if (selectedTab == "WEEKLY") "WEEKLY" else "DAILY",
            onDismiss = { showAddGoalDialog = false },
            onSaveGoal = { title, desc, targetType, targetValue, unit, period, icon, category ->
                viewModel.addGoal(
                    title = title,
                    description = desc,
                    targetType = targetType,
                    targetValue = targetValue,
                    unit = unit,
                    period = period,
                    iconEmoji = icon,
                    category = category
                )
            }
        )
    }

    Scaffold(
        containerColor = ImmersiveBg,
        floatingActionButton = {
            if (selectedTab != "MILESTONES") {
                ExtendedFloatingActionButton(
                    onClick = { showAddGoalDialog = true },
                    containerColor = ElectricIndigo,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Ziel setzen", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("add_goal_fab")
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            // ─── Header Section ──────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GELASSENHEITS-TRACKER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightCerulean,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Dein Weg zur Balance",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    // Streak Badge
                    Surface(
                        color = ProAmberSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ProAmberBorder),
                        modifier = Modifier.testTag("streak_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🔥", fontSize = 16.sp)
                            Column {
                                Text(
                                    text = "$streakDays Tage",
                                    color = ProGoldLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Serie",
                                    color = TextMuted,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }

            // ─── Hero Overview Card ──────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("progress_hero_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                ElectricIndigo.copy(alpha = 0.5f),
                                ImmersiveSurfaceBorder,
                                ProGold.copy(alpha = 0.3f)
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (selectedTab == "DAILY") "Tagesfortschritt" else if (selectedTab == "WEEKLY") "Wochenfortschritt" else "Gesamtstatus",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (selectedTab == "MILESTONES") {
                                        "$unlockedMilestonesCount von ${milestones.size} Meilensteinen"
                                    } else {
                                        "$periodCompletedCount von $periodTotalCount Zielen erreicht"
                                    },
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (periodProgressRatio >= 1f && periodTotalCount > 0) {
                                        "Hervorragend! Alle Ziele vollbracht 🎉"
                                    } else {
                                        "Schritt für Schritt zu innerer Ruhe 🌱"
                                    },
                                    color = if (periodProgressRatio >= 1f && periodTotalCount > 0) ProGoldLight else TextMuted,
                                    fontSize = 12.sp
                                )
                            }

                            // Circular Progress Badge
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { if (selectedTab == "MILESTONES") unlockedMilestonesCount.toFloat() / milestones.size.coerceAtLeast(1) else periodProgressRatio },
                                    modifier = Modifier.fillMaxSize(),
                                    color = if (periodProgressRatio >= 1f) ProGold else ElectricIndigo,
                                    trackColor = ImmersiveSurfaceAlt,
                                    strokeWidth = 6.dp
                                )
                                Text(
                                    text = "${((if (selectedTab == "MILESTONES") unlockedMilestonesCount.toFloat() / milestones.size.coerceAtLeast(1) else periodProgressRatio) * 100).toInt()}%",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = ImmersiveSurfaceBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatPill(
                                icon = "🧘",
                                value = "$totalMedMinutes Min.",
                                label = "Meditiert"
                            )
                            StatPill(
                                icon = "✨",
                                value = "${gratitudeEntries.size}",
                                label = "Reflexionen"
                            )
                            StatPill(
                                icon = "🏆",
                                value = "$unlockedMilestonesCount / ${milestones.size}",
                                label = "Belohnungen"
                            )
                        }
                    }
                }
            }

            // ─── Segmented Filter Tabs ───────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ImmersiveSurface, RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabButton(
                        title = "📅 Täglich",
                        isSelected = selectedTab == "DAILY",
                        count = dailyGoals.size,
                        onClick = { selectedTab = "DAILY" },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        title = "📆 Wöchentlich",
                        isSelected = selectedTab == "WEEKLY",
                        count = weeklyGoals.size,
                        onClick = { selectedTab = "WEEKLY" },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        title = "🏆 Belohnungen",
                        isSelected = selectedTab == "MILESTONES",
                        count = unlockedMilestonesCount,
                        onClick = { selectedTab = "MILESTONES" },
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }

            // ─── Content depending on tab ────────────────────────────────────
            if (selectedTab == "MILESTONES") {
                item {
                    Text(
                        text = "Meilensteine & Auszeichnungen",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(milestones, key = { it.id }) { milestone ->
                    MilestoneCard(
                        milestone = milestone,
                        onClick = { selectedMilestoneForDialog = milestone },
                        onPlayChime = { viewModel.playRewardSingingBowl() }
                    )
                }
            } else {
                // Goals List
                if (currentGoals.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceAlt),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveSurfaceBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🎯", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (selectedTab == "DAILY") "Noch keine täglichen Ziele" else "Noch keine Wochenziele",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Erstelle ein Ziel, um deine Gewohnheiten mit Leichtigkeit zu verfolgen.",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { showAddGoalDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Erstes Ziel hinzufügen")
                                }
                            }
                        }
                    }
                } else {
                    // Active Goals Header
                    if (activePeriodGoals.isNotEmpty()) {
                        item {
                            Text(
                                text = "Aktive Ziele (${activePeriodGoals.size})",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(activePeriodGoals, key = { it.id }) { goal ->
                            GoalItemCard(
                                goal = goal,
                                onIncrement = { delta -> viewModel.incrementGoalProgress(goal, delta) },
                                onToggleComplete = { viewModel.toggleGoalComplete(goal) },
                                onDelete = { viewModel.deleteGoal(goal) }
                            )
                        }
                    }

                    // Completed Goals Header
                    if (completedPeriodGoals.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Erreichte Ziele (${completedPeriodGoals.size})",
                                    color = Color(0xFF10B981),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(completedPeriodGoals, key = { it.id }) { goal ->
                            GoalItemCard(
                                goal = goal,
                                onIncrement = { delta -> viewModel.incrementGoalProgress(goal, delta) },
                                onToggleComplete = { viewModel.toggleGoalComplete(goal) },
                                onDelete = { viewModel.deleteGoal(goal) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    title: String,
    isSelected: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) ElectricIndigo else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag("tab_${title}")
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatPill(
    icon: String,
    value: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ImmersiveSurfaceAlt)
                .border(1.dp, ImmersiveSurfaceBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Column {
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun GoalItemCard(
    goal: UserGoal,
    onIncrement: (Int) -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val progressRatio = (goal.currentValue.toFloat() / goal.targetValue.coerceAtLeast(1)).coerceIn(0f, 1f)
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Ziel löschen?") },
            text = { Text("Möchtest du '${goal.title}' wirklich entfernen?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Löschen", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Abbrechen", color = TextSecondary)
                }
            },
            containerColor = ImmersiveSurface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_card_${goal.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (goal.isCompleted) ImmersiveSurfaceAlt else ImmersiveSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (goal.isCompleted) Color(0xFF10B981).copy(alpha = 0.5f) else ImmersiveSurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Goal Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (goal.isCompleted) Color(0xFF10B981).copy(alpha = 0.15f) else ImmersiveSurfaceAlt)
                        .border(
                            1.dp,
                            if (goal.isCompleted) Color(0xFF10B981).copy(alpha = 0.4f) else ImmersiveSurfaceBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goal.iconEmoji, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Category
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = ElectricIndigo.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = goal.category,
                                color = LightCerulean,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (goal.isCompleted) {
                            Surface(
                                color = Color(0xFF10B981).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Erreicht 🎉",
                                    color = Color(0xFF10B981),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = goal.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (goal.description.isNotBlank()) {
                        Text(
                            text = goal.description,
                            color = TextMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Delete Icon
                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Löschen",
                        tint = TextDarkMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar and numbers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${goal.currentValue} / ${goal.targetValue} ${goal.unit}",
                    color = if (goal.isCompleted) Color(0xFF10B981) else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "${(progressRatio * 100).toInt()}%",
                    color = if (goal.isCompleted) Color(0xFF10B981) else TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progressRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (goal.isCompleted) Color(0xFF10B981) else ElectricIndigo,
                trackColor = ImmersiveSurfaceAlt
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!goal.isCompleted) {
                    // Quick +1 / +5 button
                    val incrementAmount = if (goal.unit.contains("Min")) 5 else 1
                    OutlinedButton(
                        onClick = { onIncrement(incrementAmount) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SoftIceBlue
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveSurfaceBorder),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("+$incrementAmount ${if (goal.unit.contains("Min")) "Min" else ""}")
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Checkmark toggle button
                Button(
                    onClick = onToggleComplete,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (goal.isCompleted) Color(0xFF10B981).copy(alpha = 0.2f) else ElectricIndigo,
                        contentColor = if (goal.isCompleted) Color(0xFF10B981) else Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = if (goal.isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (goal.isCompleted) "Erledigt" else "Abhaken",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MilestoneCard(
    milestone: MilestoneReward,
    onClick: () -> Unit,
    onPlayChime: () -> Unit
) {
    val tierColor = when (milestone.tier) {
        MilestoneTier.BRONZE -> Color(0xFFCD7F32)
        MilestoneTier.SILVER -> Color(0xFFE2E8F0)
        MilestoneTier.GOLD -> ProGold
        MilestoneTier.DIAMOND -> Color(0xFF38BDF8)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("milestone_card_${milestone.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (milestone.isUnlocked) ImmersiveSurface else ImmersiveSurfaceAlt
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (milestone.isUnlocked) tierColor.copy(alpha = 0.7f) else ImmersiveSurfaceBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Milestone Badge Icon
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (milestone.isUnlocked) tierColor.copy(alpha = 0.2f) else ImmersiveSurface
                    )
                    .border(
                        1.5.dp,
                        if (milestone.isUnlocked) tierColor else ImmersiveSurfaceBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = milestone.iconEmoji,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = tierColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = milestone.tier.label.uppercase(),
                            color = tierColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (milestone.isUnlocked) {
                        Text(
                            text = "Freigeschaltet 🎉",
                            color = Color(0xFF10B981),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = milestone.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = milestone.description,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { milestone.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = tierColor,
                    trackColor = ImmersiveSurfaceBorder
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = milestone.progressText,
                        color = TextMuted,
                        fontSize = 10.sp
                    )

                    Text(
                        text = "${(milestone.progressPercent * 100).toInt()}%",
                        color = tierColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = if (milestone.isUnlocked) tierColor else TextMuted
            )
        }
    }
}
