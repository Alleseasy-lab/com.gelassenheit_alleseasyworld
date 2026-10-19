package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GratitudeEntry::class, Challenge::class, MeditationRecord::class, UserGoal::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gratitudeDao(): GratitudeDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun meditationDao(): MeditationDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gelassenheit_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(
                            database.challengeDao(),
                            database.gratitudeDao(),
                            database.goalDao()
                        )
                    }
                }
            }
        }

        suspend fun populateInitialData(
            challengeDao: ChallengeDao,
            gratitudeDao: GratitudeDao,
            goalDao: GoalDao
        ) {
            val initialChallenges = listOf(
                Challenge(
                    title = "Den inneren Kritiker umarmen",
                    description = "Wenn heute der Impuls zum Aufschieben kommt: Sag innerlich 'Danke für den Schutzimpuls, aber ich beginne jetzt ganz entspannt mit 2 Minuten'.",
                    category = "Verständnis",
                    estimatedMinutes = 3,
                    iconEmoji = "🤝"
                ),
                Challenge(
                    title = "Gedankenstopp bei Selbstvorwürfen",
                    description = "Beobachte, wann du streng mit dir wirst. Ersetze den Vorwurf durch: 'Ich lerne jeden Tag dazu. Gelassenheit ist ein Weg, kein Zwang.'",
                    category = "Verständnis",
                    estimatedMinutes = 5,
                    iconEmoji = "🧠"
                ),
                Challenge(
                    title = "Die 3-Atemzüge-Insel",
                    description = "Halte dreimal am Tag für genau 3 tiefe Atemzüge inne (4s ein, 4s halten, 6s aus). Spüre, wie die Spannung im Nacken nachlässt.",
                    category = "Gelassenheit",
                    estimatedMinutes = 2,
                    iconEmoji = "🌊"
                ),
                Challenge(
                    title = "Reaktions-Verzögerung bei Stress",
                    description = "Wenn etwas Unvorhergesehenes passiert: Warte 10 Sekunden, bevor du reagierst oder urteilst. Schenke dir diesen Puffer der Gelassenheit.",
                    category = "Gelassenheit",
                    estimatedMinutes = 5,
                    iconEmoji = "⏳"
                ),
                Challenge(
                    title = "Die 2-Minuten-Mikro-Aktion",
                    description = "Wähle eine Sache, die du aufgeschoben hast. Arbeite genau 2 Minuten daran – danach darfst du aufhören. Meistens fließt die Energie danach von selbst.",
                    category = "Ausgeglichenheit",
                    estimatedMinutes = 2,
                    iconEmoji = "⚡"
                ),
                Challenge(
                    title = "Digitales Loslassen für 30 Minuten",
                    description = "Schalte dein Smartphone für 30 Minuten auf stumm oder lege es in ein anderes Zimmer. Nutze die Stille für einen Spaziergang oder eine Tasse Tee.",
                    category = "Ausgeglichenheit",
                    estimatedMinutes = 30,
                    iconEmoji = "🌿"
                )
            )
            challengeDao.insertAll(initialChallenges)

            val initialGratitude = listOf(
                GratitudeEntry(
                    text = "Dankbar für den Moment der Stille am Morgen und den Entschluss, liebevoll mit mir selbst umzugehen.",
                    prompt = "Wofür bist du dir selbst heute dankbar?",
                    moodEmoji = "✨",
                    moodLabel = "Gelassen",
                    category = "Verständnis",
                    timestamp = System.currentTimeMillis() - 86400000L
                ),
                GratitudeEntry(
                    text = "Ich habe heute trotz Müdigkeit eine kleine Aufgabe erledigt, ohne mich zu überfordern. Balance gefunden!",
                    prompt = "Welcher kleine Erfolg hat dir heute Ruhe geschenkt?",
                    moodEmoji = "🌱",
                    moodLabel = "Ausgeglichen",
                    category = "Ausgeglichenheit",
                    timestamp = System.currentTimeMillis() - 172800000L
                )
            )
            initialGratitude.forEach { gratitudeDao.insertEntry(it) }

            val initialGoals = listOf(
                UserGoal(
                    title = "10 Minuten meditieren",
                    description = "Geführte Klangreise oder Stille zur inneren Zentrierung",
                    targetType = "MEDITATION",
                    targetValue = 10,
                    currentValue = 5,
                    unit = "Minuten",
                    period = "DAILY",
                    iconEmoji = "🧘",
                    category = "Gelassenheit",
                    isCompleted = false
                ),
                UserGoal(
                    title = "Positiven Gedanken aufschreiben",
                    description = "Einen Moment der Dankbarkeit oder Selbstmitgefühl festhalten",
                    targetType = "GRATITUDE",
                    targetValue = 1,
                    currentValue = 1,
                    unit = "Eintrag",
                    period = "DAILY",
                    iconEmoji = "✨",
                    category = "Dankbarkeit",
                    isCompleted = true,
                    completedAt = System.currentTimeMillis() - 3600000L
                ),
                UserGoal(
                    title = "Tägliche Achtsamkeits-Challenge",
                    description = "Eine kleine Übung gegen den inneren Schweinehund",
                    targetType = "CHALLENGE",
                    targetValue = 1,
                    currentValue = 0,
                    unit = "Challenge",
                    period = "DAILY",
                    iconEmoji = "🌱",
                    category = "Ausgeglichenheit",
                    isCompleted = false
                ),
                UserGoal(
                    title = "45 Minuten Achtsamkeit & Meditation",
                    description = "Ausgeglichene Meditationspraxis über die ganze Woche",
                    targetType = "MEDITATION",
                    targetValue = 45,
                    currentValue = 25,
                    unit = "Minuten",
                    period = "WEEKLY",
                    iconEmoji = "🌊",
                    category = "Gelassenheit",
                    isCompleted = false
                ),
                UserGoal(
                    title = "2 Podcast Deep Dives hören",
                    description = "Wissenschaftliche Impulse zur Stressreduktion aufsaugen",
                    targetType = "PODCAST",
                    targetValue = 2,
                    currentValue = 1,
                    unit = "Episoden",
                    period = "WEEKLY",
                    iconEmoji = "🎧",
                    category = "Verständnis",
                    isCompleted = false
                )
            )
            goalDao.insertAll(initialGoals)
        }
    }
}
