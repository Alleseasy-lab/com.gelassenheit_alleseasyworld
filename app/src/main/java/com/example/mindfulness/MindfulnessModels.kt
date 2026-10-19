package com.example.mindfulness

data class SenseExercise(
    val id: String,
    val title: String,
    val subtitle: String,
    val senseName: String, // "Hören", "Tasten", "Atmen", "Riechen", "Sehen", "Somatic", "Synästhesie"
    val iconEmoji: String,
    val prompt: String,
    val neuroBiologicalFact: String,
    val practiceDurationSeconds: Int,
    val isProOnly: Boolean = false,
    val steps: List<String>
)

object MindfulnessCatalog {
    val senseExercises = listOf(
        SenseExercise(
            id = "sense_hearing",
            title = "Tor zur Welt: Reines Hören",
            subtitle = "Frequenzen wahrnehmen ohne Bewertung",
            senseName = "Hören",
            iconEmoji = "👂",
            prompt = "Lausche den Geräuschen um dich herum. Versuche nicht zu benennen, was es ist, sondern nimm nur die Schwingung und Lautstärke wahr.",
            neuroBiologicalFact = "Hören ist passiv. Wenn du Geräusche nicht kognitiv als 'störend' etikettierst, unterbrichst du sofort den Alarmzustand der Amygdala.",
            practiceDurationSeconds = 120,
            isProOnly = false,
            steps = listOf(
                "Richte deine Aufmerksamkeit auf das am weitesten entfernte Geräusch.",
                "Höre nun das leiseste Geräusch in deiner unmittelbaren Nähe.",
                "Registriere die Tonhöhe und Lautstärke als reine physikalische Welle.",
                "Lass das Geräusch einfach da sein, ohne es gut oder schlecht zu finden."
            )
        ),
        SenseExercise(
            id = "sense_touch",
            title = "Der Echtzeit-Anker: Tastsinn",
            subtitle = "Physische Sicherheit im Hier und Jetzt spüren",
            senseName = "Tasten",
            iconEmoji = "🖐️",
            prompt = "Spüre den Druck deines Körpers auf dem Stuhl, den Stoff auf deiner Haut oder die Fußsohlen auf dem Boden.",
            neuroBiologicalFact = "Dein Nervensystem auf Hautebene kann weder in der Vergangenheit noch in der Zukunft sein. Es sendet exakt in dieser Millisekunde das Signal: Du bist physisch sicher.",
            practiceDurationSeconds = 180,
            isProOnly = false,
            steps = listOf(
                "Nimm bewusst den Kontakt zwischen deinen Füßen und dem festen Boden wahr.",
                "Spüre die Lehne und Sitzfläche deines Stuhls, die dich verlässlich tragen.",
                "Fühle die Temperatur der Luft an deinen Händen und Wangen.",
                "Entspanne bewusst die Muskeln, die jetzt nichts festhalten müssen."
            )
        ),
        SenseExercise(
            id = "sense_breath",
            title = "Die Brücke: Bewusster Atem",
            subtitle = "Kühle Luft ein, warme Luft aus",
            senseName = "Atmen",
            iconEmoji = "🌬️",
            prompt = "Beobachte den feinen Temperaturunterschied an der Nasenspitze beim Ein- und Ausatmen.",
            neuroBiologicalFact = "Der Atem ist die einzige lebenswichtige Funktion, die sowohl vom autonomen Nervensystem gesteuert als auch bewusst moduliert werden kann. Er verbindet Geist und Körper.",
            practiceDurationSeconds = 180,
            isProOnly = false,
            steps = listOf(
                "Spüre, wie die kühle Luft durch die Nase in deine Lungen strömt.",
                "Halte für einen winzigen, friedlichen Moment am Scheitelpunkt inne.",
                "Spüre die erwärmte Luft langsam und vollständig ausströmen.",
                "Lasse mit jedem Ausatmen alle Fremdsteuerung und Erwartungen los."
            )
        ),
        SenseExercise(
            id = "sense_sight",
            title = "Cortex-Reset: Reiz-Drosselung",
            subtitle = "Visuellen Cortex um 80% entlasten",
            senseName = "Sehen",
            iconEmoji = "👁️",
            prompt = "Schließe die Augen oder richte deinen Blick weich auf einen neutralen Punkt ohne zu fokussieren.",
            neuroBiologicalFact = "Bis zu 80% aller Daten, die dein Gehirn pro Sekunde verarbeiten muss, strömen über den visuellen Cortex ein. Das Schließen der Augen schaltet den Turbo-Gang sofort herunter.",
            practiceDurationSeconds = 120,
            isProOnly = true,
            steps = listOf(
                "Löse deinen Blick vom Display oder starren Objekten.",
                "Schließe sanft die Lider oder blicke weich in die Ferne.",
                "Lass die Augenmuskeln vollkommen locker werden.",
                "Spüre das Nachlassen der visuellen Anspannung hinter der Stirn."
            )
        ),
        SenseExercise(
            id = "sense_smell",
            title = "Reine Chemie: Geruchssinn",
            subtitle = "Den Duft des Raumes neutral registrieren",
            senseName = "Riechen",
            iconEmoji = "👃",
            prompt = "Nimm den unverfälschten Geruch der Luft in deiner aktuellen Umgebung wahr.",
            neuroBiologicalFact = "Gerüche umgehen den Thalamus und erreichen direkt das limbische System. Die neutrale Wahrnehmung erdet dich augenblicklich in der physischen Umgebung.",
            practiceDurationSeconds = 90,
            isProOnly = true,
            steps = listOf(
                "Atme ruhig ein und nimm den Geruch der Umgebung wahr.",
                "Vermeide jedes Urteil ('gut' oder 'schlecht').",
                "Spüre einfach die Aktivierung deiner Geruchsrezeptoren.",
                "Kehre mit klarem Kopf zurück in den Moment."
            )
        ),
        SenseExercise(
            id = "sense_vagus_ice",
            title = "Somatic Vagus: Neuro-Kältereiz",
            subtitle = "Tauchreflex zur sofortigen Puls-Senkung",
            senseName = "Somatic",
            iconEmoji = "❄️",
            prompt = "Bringe kühles Wasser oder eine kühle Handfläche sanft an deine Schläfen und den Nacken.",
            neuroBiologicalFact = "Der Mammalian Dive Reflex aktiviert den ventralen Vagusnerv innerhalb von 15 Sekunden und verlangsamt die Herzfrequenz messbar um bis zu 20%.",
            practiceDurationSeconds = 120,
            isProOnly = true,
            steps = listOf(
                "Spüre die kühle Berührung an den Schläfen und den Halsschlagadern.",
                "Atme langsam doppelt so lange aus wie ein.",
                "Lass die Zunge locker am Mundboden ruhen.",
                "Beobachte, wie sich der Herzschlag sofort beruhigt."
            )
        ),
        SenseExercise(
            id = "sense_synesthesia",
            title = "Binaurales 5-Sinne-Training",
            subtitle = "Körper- und Klang-Synästhesie zur Tiefenentspannung",
            senseName = "Synästhesie",
            iconEmoji = "✨",
            prompt = "Verknüpfe das Hören der Frequenz mit der Temperatur deiner Handflächen.",
            neuroBiologicalFact = "Multisensorische Verknüpfung synchronisiert die beiden Gehirnhälften und fördert Theta-Wellen (4-8 Hz) für Kreativität und tiefe mentale Stabilität.",
            practiceDurationSeconds = 240,
            isProOnly = true,
            steps = listOf(
                "Lausche dem Ton und spüre gleichzeitig deine warme rechte Hand.",
                "Wandere mit dem Gehör zur linken Handfläche.",
                "Nimm den gesamten Körper als klingenden Resonanzraum wahr.",
                "Verweile in diesem Zustand ungeteilter Präsenz."
            )
        )
    )
}
