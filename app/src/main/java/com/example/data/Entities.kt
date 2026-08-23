package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gratitude_entries")
data class GratitudeEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val prompt: String = "Wofür bist du in diesem Moment dankbar?",
    val moodEmoji: String = "✨",
    val moodLabel: String = "Gelassen",
    val category: String = "Gelassenheit", // Gelassenheit, Verständnis, Ausgeglichenheit, Alltag
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "Verständnis", "Gelassenheit", "Ausgeglichenheit"
    val estimatedMinutes: Int = 5,
    val iconEmoji: String = "🌱",
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val dayOffset: Int = 0 // for daily rotation
)

@Entity(tableName = "meditation_records")
data class MeditationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
