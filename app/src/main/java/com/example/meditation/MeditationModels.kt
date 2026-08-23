package com.example.meditation

data class GuidedStep(
    val title: String,
    val instruction: String,
    val breathPhase: String // "Einatmen (4s)", "Halten (4s)", "Ausatmen (6s)", "Ruhen (2s)", "Stille"
)

data class Meditation(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val category: String, // "Stressabbau", "Achtsamkeit", "Einschlafhilfe", "Ausgeglichenheit"
    val defaultMinutes: Int,
    val iconEmoji: String,
    val ambientFrequencyHz: Double = 432.0,
    val guidedSteps: List<GuidedStep>
)

object MeditationCatalog {
    val categories = listOf("Alle", "Stressabbau", "Achtsamkeit", "Einschlafhilfe", "Ausgeglichenheit")

    val meditations = listOf(
        Meditation(
            id = "stress_relief_1",
            title = "Innere Ruhe bei Anspannung",
            subtitle = "Körperliche Anspannung sanft lösen & Atem vertiefen",
            description = "Wenn der Tag dich überrollt oder der innere Druck steigt: Diese Meditation senkt deinen Puls, entspannt die Schultern und holt dich in die pure Gelassenheit zurück.",
            category = "Stressabbau",
            defaultMinutes = 5,
            iconEmoji = "🌊",
            ambientFrequencyHz = 432.0,
            guidedSteps = listOf(
                GuidedStep(
                    title = "Ankommen im Hier und Jetzt",
                    instruction = "Schließe sanft die Augen. Richte deine Wirbelsäule aufrecht und bequem aus. Lasse alle Erwartungen an den heutigen Tag los.",
                    breathPhase = "Einatmen (4s)"
                ),
                GuidedStep(
                    title = "Schultern und Kiefer entspannen",
                    instruction = "Spüre, wo dein Körper Spannung festhält. Lasse deine Schultern bewusst 2 Zentimeter nach unten sinken. Löse die Zunge vom Gaumen.",
                    breathPhase = "Ausatmen (6s)"
                ),
                GuidedStep(
                    title = "Den Atem als Anker nutzen",
                    instruction = "Atme tief durch die Nase in den Bauchraum ein... halte kurz inne... und atme langsam und vollständig durch den Mund aus.",
                    breathPhase = "Halten (4s)"
                ),
                GuidedStep(
                    title = "Vollkommene Gelassenheit",
                    instruction = "Spüre, wie mit jedem Ausatmen Ruhe deinen Körper durchströmt. Du bist sicher, getragen und im Einklang.",
                    breathPhase = "Ruhen (2s)"
                )
            )
        ),
        Meditation(
            id = "mindfulness_1",
            title = "Schweinehund liebevoll annehmen",
            subtitle = "Den inneren Schutzmechanismus verstehen und leiten",
            description = "Hör auf, gegen dich selbst zu kämpfen. Dein innerer Schweinehund möchte dich vor Überforderung schützen. Schenke ihm Verständnis und verwandle ihn in deinen treuesten Begleiter.",
            category = "Achtsamkeit",
            defaultMinutes = 8,
            iconEmoji = "🤝",
            ambientFrequencyHz = 528.0,
            guidedSteps = listOf(
                GuidedStep(
                    title = "Den Widerstand wahrnehmen",
                    instruction = "Nimm das Gefühl von Unlust oder Zögern wertfrei wahr. Es ist nicht dein Feind – es ist nur ein Teil von dir, der Sicherheit sucht.",
                    breathPhase = "Einatmen (4s)"
                ),
                GuidedStep(
                    title = "Verständnis schenken",
                    instruction = "Sage dir im Geiste: 'Ich verstehe, warum du zögerst. Aber wir gehen jetzt gemeinsam einen ganz kleinen, leichten Schritt.'",
                    breathPhase = "Halten (4s)"
                ),
                GuidedStep(
                    title = "Die Leichtigkeit spüren",
                    instruction = "Es muss nicht perfekt sein. Es darf sich mühelos und verspielt anfühlen. Du hast alle Kraft in dir.",
                    breathPhase = "Ausatmen (6s)"
                ),
                GuidedStep(
                    title = "Aktivierte Gelassenheit",
                    instruction = "Spüre die Klarheit in deinem Brustkorb. Bereit, mit Zuversicht voranzugehen.",
                    breathPhase = "Ruhen (2s)"
                )
            )
        ),
        Meditation(
            id = "sleep_1",
            title = "Gedankenstille zur Nacht",
            subtitle = "Sanftes Loslassen des Tages in tiefen Schlaf",
            description = "Schließe den Tag in Frieden ab. Entlasse alle to-dos und offenen Fragen in die Nacht. Bereite Körper und Geist auf erholsamen Tiefschlaf vor.",
            category = "Einschlafhilfe",
            defaultMinutes = 10,
            iconEmoji = "🌙",
            ambientFrequencyHz = 396.0,
            guidedSteps = listOf(
                GuidedStep(
                    title = "Den Tag verabschieden",
                    instruction = "Alles, was heute getan wurde, war genug. Alles, was offen blieb, darf bis morgen ruhen.",
                    breathPhase = "Einatmen (4s)"
                ),
                GuidedStep(
                    title = "Schwere & Wärme spüren",
                    instruction = "Spüre, wie deine Arme und Beine angenehm schwer und warm werden. Lass dich vollkommen in die Matratze sinken.",
                    breathPhase = "Ausatmen (6s)"
                ),
                GuidedStep(
                    title = "Gedanken wie Wolken ziehen lassen",
                    instruction = "Jeder Gedanke ist wie eine sanfte Wolke am Nachthimmel, die lautlos vorüberzieht, ohne dich zu berühren.",
                    breathPhase = "Ruhen (2s)"
                ),
                GuidedStep(
                    title = "Übergang in den Schlummer",
                    instruction = "Genieße die samtige Stille. Schlafe tief, friedvoll und geborgen.",
                    breathPhase = "Stille"
                )
            )
        ),
        Meditation(
            id = "balance_1",
            title = "Zentrierung & Fokus",
            subtitle = "Harmonie zwischen Herz, Verstand und Tatkraft",
            description = "Eine erfrischende Auszeit, um deine mentale Mitte wiederzufinden. Bringt Ausgeglichenheit und frische Motivation für deine nächsten Vorhaben.",
            category = "Ausgeglichenheit",
            defaultMinutes = 5,
            iconEmoji = "⚡",
            ambientFrequencyHz = 432.0,
            guidedSteps = listOf(
                GuidedStep(
                    title = "Aufrichtung & Klarheit",
                    instruction = "Spüre den Boden unter deinen Füßen. Richte deinen Scheitelpunkt nach oben. Atme Frische und Weite ein.",
                    breathPhase = "Einatmen (4s)"
                ),
                GuidedStep(
                    title = "Fokus im Zentrum",
                    instruction = "Bündle deine Aufmerksamkeit auf den Punkt zwischen deinen Augenbrauen. Still, konzentriert und gelassen.",
                    breathPhase = "Halten (4s)"
                ),
                GuidedStep(
                    title = "Energie freisetzen",
                    instruction = "Atme kraftvoll aus und spüre, wie neue, gelassene Tatkraft in deine Hände und deinen Geist strömt.",
                    breathPhase = "Ausatmen (6s)"
                )
            )
        )
    )
}
