package com.example.data

enum class MilestoneTier(val label: String, val colorHex: Long) {
    BRONZE("Bronze", 0xFFCD7F32),
    SILVER("Silber", 0xFFC0C0C0),
    GOLD("Gold", 0xFFFFD700),
    DIAMOND("Diamant", 0xFF67E8F9)
}

data class MilestoneReward(
    val id: String,
    val title: String,
    val description: String,
    val tier: MilestoneTier,
    val iconEmoji: String,
    val currentCount: Int,
    val targetCount: Int,
    val unit: String,
    val isUnlocked: Boolean,
    val rewardTitle: String,
    val rewardWisdom: String
) {
    val progressPercent: Float
        get() = if (targetCount > 0) (currentCount.toFloat() / targetCount).coerceIn(0f, 1f) else 0f

    val progressText: String
        get() = "$currentCount / $targetCount $unit"
}

object MilestoneCatalog {
    fun computeMilestones(
        totalMeditationSeconds: Int,
        gratitudeCount: Int,
        completedChallengesCount: Int,
        completedGoalsCount: Int,
        streakDays: Int
    ): List<MilestoneReward> {
        val totalMedMinutes = totalMeditationSeconds / 60

        return listOf(
            MilestoneReward(
                id = "first_step",
                title = "Erster Schritt",
                description = "Schließe dein erstes Ziel, Meditation oder Challenge ab",
                tier = MilestoneTier.BRONZE,
                iconEmoji = "🌱",
                currentCount = (completedGoalsCount + if (totalMedMinutes > 0) 1 else 0).coerceAtLeast(0),
                targetCount = 1,
                unit = "Aktion",
                isUnlocked = (completedGoalsCount > 0 || totalMedMinutes > 0 || completedChallengesCount > 0 || gratitudeCount > 0),
                rewardTitle = "Segen des Neubeginns 🔔",
                rewardWisdom = "„Auch der längste Weg beginnt mit dem ersten bewussten Atemzug.“ Du hast den Raum der Gelassenheit betreten."
            ),
            MilestoneReward(
                id = "meditation_10",
                title = "Insel der Stille",
                description = "Erreiche insgesamt 10 Minuten geführte oder freie Meditation",
                tier = MilestoneTier.BRONZE,
                iconEmoji = "🧘",
                currentCount = totalMedMinutes,
                targetCount = 10,
                unit = "Minuten",
                isUnlocked = totalMedMinutes >= 10,
                rewardTitle = "Klangschalen-Resonanz 🌊",
                rewardWisdom = "„Stille ist nicht die Abwesenheit von Geräuschen, sondern die Anwesenheit von innerem Frieden.“"
            ),
            MilestoneReward(
                id = "gratitude_3",
                title = "Achtsamer Geist",
                description = "Verfasse 3 Dankbarkeits- und Selbstreflexions-Einträge",
                tier = MilestoneTier.SILVER,
                iconEmoji = "✨",
                currentCount = gratitudeCount,
                targetCount = 3,
                unit = "Einträge",
                isUnlocked = gratitudeCount >= 3,
                rewardTitle = "Goldenes Herz-Symbol 💛",
                rewardWisdom = "„Dankbarkeit verwandelt das, was wir haben, in genug und mehr.“ Deine Achtsamkeit schärft den Blick fürs Gute."
            ),
            MilestoneReward(
                id = "challenges_3",
                title = "Schweinehund-Zähmer",
                description = "Meistere 3 tägliche Achtsamkeits-Challenges",
                tier = MilestoneTier.SILVER,
                iconEmoji = "🤝",
                currentCount = completedChallengesCount,
                targetCount = 3,
                unit = "Challenges",
                isUnlocked = completedChallengesCount >= 3,
                rewardTitle = "Selbstwirksamkeits-Trophäe 🏆",
                rewardWisdom = "„Dein innerer Widerstand ist kein Feind, sondern Energie, die gelenkt werden will.“ Du führst ihn mit Güte."
            ),
            MilestoneReward(
                id = "streak_3",
                title = "3-Tage-Achtsamkeits-Serie",
                description = "Bleibe 3 aufeinanderfolgende Tage mit deinen Zielen aktiv",
                tier = MilestoneTier.GOLD,
                iconEmoji = "🔥",
                currentCount = streakDays,
                targetCount = 3,
                unit = "Tage",
                isUnlocked = streakDays >= 3,
                rewardTitle = "Feuer der Beständigkeit 🔥",
                rewardWisdom = "„Kleine tägliche Schritte formen eine unerschütterliche Gewohnheit des inneren Gleichgewichts.“"
            ),
            MilestoneReward(
                id = "meditation_30",
                title = "Ozean der Ruhe",
                description = "Sammle 30 Minuten tiefe Meditation & 432 Hz Entspannung",
                tier = MilestoneTier.GOLD,
                iconEmoji = "🌊",
                currentCount = totalMedMinutes,
                targetCount = 30,
                unit = "Minuten",
                isUnlocked = totalMedMinutes >= 30,
                rewardTitle = "Tiefenentspannungs-Zertifikat 👑",
                rewardWisdom = "„Wie klares Wasser zur Ruhe kommt, wenn man es nicht aufwühlt, so klärt sich der Geist durch Geduld.“"
            ),
            MilestoneReward(
                id = "goals_5",
                title = "Zielstrebig im Einklang",
                description = "Erreiche 5 tägliche oder wöchentliche Wohlfühlziele",
                tier = MilestoneTier.GOLD,
                iconEmoji = "🎯",
                currentCount = completedGoalsCount,
                targetCount = 5,
                unit = "Ziele",
                isUnlocked = completedGoalsCount >= 5,
                rewardTitle = "Meister-Abzeichen der Ausgeglichenheit 🥇",
                rewardWisdom = "„Erfolg im Inneren bedeutet, sich selbst treu zu bleiben, ohne Druck aufzubauen.“"
            ),
            MilestoneReward(
                id = "zen_grandmaster",
                title = "Zen-Großmeister",
                description = "Vollende 60 Minuten Meditation & 7 Dankbarkeits-Einträge",
                tier = MilestoneTier.DIAMOND,
                iconEmoji = "💎",
                currentCount = (totalMedMinutes + (gratitudeCount * 5)).coerceAtMost(95),
                targetCount = 95,
                unit = "Punkte",
                isUnlocked = totalMedMinutes >= 60 && gratitudeCount >= 7,
                rewardTitle = "Diamant-Siegel der ewigen Gelassenheit 💎",
                rewardWisdom = "„Du bist nicht der Sturm – du bist der weite Himmel, durch den der Sturm zieht.“ Vollkommene Ruhe wohnt in dir."
            )
        )
    }
}
