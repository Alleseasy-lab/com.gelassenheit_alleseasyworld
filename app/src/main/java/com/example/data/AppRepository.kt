package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val gratitudeDao: GratitudeDao,
    private val challengeDao: ChallengeDao,
    private val meditationDao: MeditationDao,
    private val goalDao: GoalDao
) {
    val allGratitudeEntries: Flow<List<GratitudeEntry>> = gratitudeDao.getAllEntries()
    val allChallenges: Flow<List<Challenge>> = challengeDao.getAllChallenges()
    val completedChallengesCount: Flow<Int> = challengeDao.getCompletedCount()
    val totalChallengesCount: Flow<Int> = challengeDao.getTotalCount()
    val totalMeditationSeconds: Flow<Int> = meditationDao.getTotalMeditationSeconds()
    val allGoals: Flow<List<UserGoal>> = goalDao.getAllGoals()
    val completedGoalsCount: Flow<Int> = goalDao.getCompletedGoalsCount()
    val totalGoalsCount: Flow<Int> = goalDao.getTotalGoalsCount()

    fun getGoalsByPeriod(period: String): Flow<List<UserGoal>> {
        return goalDao.getGoalsByPeriod(period)
    }

    suspend fun addGoal(goal: UserGoal): Long {
        return goalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: UserGoal) {
        goalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: UserGoal) {
        goalDao.deleteGoal(goal)
    }

    suspend fun incrementGoalProgress(goal: UserGoal, incrementBy: Int = 1) {
        val newProgress = (goal.currentValue + incrementBy).coerceAtLeast(0)
        val isNowCompleted = newProgress >= goal.targetValue
        val completedAt = if (isNowCompleted && !goal.isCompleted) System.currentTimeMillis() else goal.completedAt
        goalDao.updateProgress(
            id = goal.id,
            value = newProgress,
            completed = isNowCompleted,
            completedAt = completedAt
        )
    }

    suspend fun toggleGoalComplete(goal: UserGoal) {
        val newCompleted = !goal.isCompleted
        val newValue = if (newCompleted) goal.targetValue else 0
        val completedAt = if (newCompleted) System.currentTimeMillis() else null
        goalDao.updateProgress(
            id = goal.id,
            value = newValue,
            completed = newCompleted,
            completedAt = completedAt
        )
    }

    suspend fun addGratitudeEntry(entry: GratitudeEntry): Long {
        return gratitudeDao.insertEntry(entry)
    }

    suspend fun deleteGratitudeEntry(entry: GratitudeEntry) {
        gratitudeDao.deleteEntry(entry)
    }

    suspend fun toggleChallenge(challenge: Challenge) {
        val newCompleted = !challenge.isCompleted
        val timestamp = if (newCompleted) System.currentTimeMillis() else null
        challengeDao.setCompleted(challenge.id, newCompleted, timestamp)
    }

    suspend fun addCustomChallenge(challenge: Challenge): Long {
        return challengeDao.insertChallenge(challenge)
    }

    suspend fun recordMeditationSession(title: String, category: String, durationSeconds: Int): Long {
        return meditationDao.insertRecord(
            MeditationRecord(
                title = title,
                category = category,
                durationSeconds = durationSeconds
            )
        )
    }
}
