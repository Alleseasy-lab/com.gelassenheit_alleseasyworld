package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val gratitudeDao: GratitudeDao,
    private val challengeDao: ChallengeDao,
    private val meditationDao: MeditationDao
) {
    val allGratitudeEntries: Flow<List<GratitudeEntry>> = gratitudeDao.getAllEntries()
    val allChallenges: Flow<List<Challenge>> = challengeDao.getAllChallenges()
    val completedChallengesCount: Flow<Int> = challengeDao.getCompletedCount()
    val totalChallengesCount: Flow<Int> = challengeDao.getTotalCount()
    val totalMeditationSeconds: Flow<Int> = meditationDao.getTotalMeditationSeconds()

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
