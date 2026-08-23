package com.example.podcast

data class PodcastChapter(
    val timeSeconds: Int,
    val title: String,
    val summary: String
)

data class PodcastQuote(
    val quote: String,
    val author: String,
    val timestamp: String
)

data class PodcastEpisode(
    val id: String,
    val title: String,
    val showName: String,
    val subtitle: String,
    val durationSeconds: Int,
    val description: String,
    val chapters: List<PodcastChapter>,
    val quotes: List<PodcastQuote>,
    val keyTakeaways: List<String>
)

object PodcastCatalog {
    val currentEpisode = PodcastEpisode(
        id = "ep_1_biologie_der_befreiung",
        title = "Die Biologie der Befreiung",
        showName = "Gelassenheit Deep Dive Podcast",
        subtitle = "Das ungleiche Duell mit dem Algorithmus & Die 5 Sinne als Rettungsanker",
        durationSeconds = 1344, // 22:24 min
        description = "Dein Gehirn ist kein fehlerhaftes Betriebssystem, das ein Update braucht. Die Hardware, um der digitalen Fremdsteuerung zu entkommen, ist bereits ab Werk in dir verbaut: deine 5 physischen Sinne.",
        chapters = listOf(
            PodcastChapter(
                timeSeconds = 0,
                title = "00:00 • Das ungleiche Duell",
                summary = "Tausende Köpfe programmieren gegen dein Nervensystem. Aufmerksamkeit ist die wertvollste Währung der Welt."
            ),
            PodcastChapter(
                timeSeconds = 96,
                title = "01:36 • Das App-Paradoxon",
                summary = "Warum eine Push-Benachrichtigung für '3 Minuten Entspannung' kognitive Dissonanz und noch mehr Dopamin-Spikes erzeugt."
            ),
            PodcastChapter(
                timeSeconds = 283,
                title = "04:43 • Das Default Mode Network (DMN)",
                summary = "Das Ruhezustandsnetzwerk des Gehirns läuft auf Hochtouren und hält uns in Erwartungen und Sorgen gefangen."
            ),
            PodcastChapter(
                timeSeconds = 363,
                title = "06:03 • Die 5 Sinne als Hardware-Schutzschild",
                summary = "Deine Sinne haben keine Algorithmen und keine Erwartungen. Sie fordern keine Leistung, sondern sind reine physische Wahrheit."
            ),
            PodcastChapter(
                timeSeconds = 471,
                title = "07:51 • Reine Frequenz vs. Bewertung",
                summary = "Das Tor zur Welt: Hören ist passiv. Ein lautes Geräusch ist nur Physik – erst das Gehirn stempelt es als 'nervig' ab."
            ),
            PodcastChapter(
                timeSeconds = 596,
                title = "09:56 • Der Tastsinn als Echtzeit-Anker",
                summary = "Die Haut kann nicht gestern oder morgen fühlen. Der physische Kontakt zu Stuhl oder Boden beweist: Jetzt in diesem Moment bist du sicher."
            ),
            PodcastChapter(
                timeSeconds = 644,
                title = "10:44 • Respekt & Zwischenmenschlichkeit",
                summary = "Empathie erfordert Präsenz. Wer sich selbst spürt, verliert nicht den Anschluss und reagiert nicht mehr wie eine programmierte Maschine."
            ),
            PodcastChapter(
                timeSeconds = 814,
                title = "13:34 • Die Notbremse: Reale Praxis",
                summary = "Augen schließen (80% Input-Drosselung), kühle Luft einatmen, warme Luft ausatmen. 'Niemand verlangt jetzt etwas von dir.'"
            ),
            PodcastChapter(
                timeSeconds = 1186,
                title = "19:46 • Die Zukunft & Synthetische Sinne",
                summary = "Was passiert, wenn VR und Metaverse beginnen, unsere biologischen Sinne zu manipulieren? Der Wert echter Sinneswahrnehmung."
            )
        ),
        quotes = listOf(
            PodcastQuote(
                quote = "Keine Meditationstechnik der Welt kann dir das geben, was deine Sinne dir ohnehin schon schenken, wenn du ihnen zuhörst.",
                author = "Gelassenheit Deep Dive",
                timestamp = "17:53"
            ),
            PodcastQuote(
                quote = "Ein Datenpunkt reagiert nur. Ein Individuum agiert.",
                author = "Gelassenheit Deep Dive",
                timestamp = "12:48"
            ),
            PodcastQuote(
                quote = "Du bist hier. Du bist sicher. Niemand verlangt jetzt etwas von dir. Das ist deine Freiheit.",
                author = "Gelassenheit Deep Dive",
                timestamp = "15:35"
            ),
            PodcastQuote(
                quote = "Vergiss das Software-Update. Die Hardware, die du suchst, ist bereits ab Werk in dir verbaut.",
                author = "Gelassenheit Deep Dive",
                timestamp = "02:02"
            )
        ),
        keyTakeaways = listOf(
            "Entziehe dem Stressor die emotionale Ladung: Höre Geräusche als reine Schwingung ohne sofortiges Urteil.",
            "Nutze den Tastsinn als Sofort-Notbremse: Spüre deinen Körper auf dem Stuhl oder die Fußsohlen am Boden.",
            "Der Atem ist die einzige Brücke zwischen unbewusstem Autopilot und bewusster Steuerung.",
            "Augen schließen kappt sofort bis zu 80% der Informationslast im Gehirn.",
            "Frage dich nicht 'Was muss ich als nächstes tun?', sondern 'Was sehe, höre und spüre ich jetzt?'"
        )
    )
}
