package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeDao {
    @Query("SELECT * FROM gratitude_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<GratitudeEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: GratitudeEntry): Long

    @Delete
    suspend fun deleteEntry(entry: GratitudeEntry)

    @Query("SELECT COUNT(*) FROM gratitude_entries")
    fun getEntryCount(): Flow<Int>
}

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY isCompleted ASC, id ASC")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE category = :category ORDER BY isCompleted ASC, id ASC")
    fun getChallengesByCategory(category: String): Flow<List<Challenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<Challenge>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge): Long

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Query("UPDATE challenges SET isCompleted = :completed, completedAt = :completedAt WHERE id = :id")
    suspend fun setCompleted(id: Long, completed: Boolean, completedAt: Long?)

    @Query("SELECT COUNT(*) FROM challenges WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM challenges")
    fun getTotalCount(): Flow<Int>
}

@Dao
interface MeditationDao {
    @Query("SELECT * FROM meditation_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<MeditationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: MeditationRecord): Long

    @Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM meditation_records")
    fun getTotalMeditationSeconds(): Flow<Int>
}
