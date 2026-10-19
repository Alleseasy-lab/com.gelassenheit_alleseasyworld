package com.example.podcast

data class PodcastChapter(
    val timeSeconds: Int,
    val title: String,
    val summary: String,
    val narration: String = ""
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
    val isProOnly: Boolean = false,
    val category: String = "Podcast", // "Podcast", "Soundbath", "Sleep"
    val ambientFrequencyHz: Double = 432.0,
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
        isProOnly = false,
        category = "Podcast",
        chapters = listOf(
            PodcastChapter(
                timeSeconds = 0,
                title = "00:00 • Das ungleiche Duell",
                summary = "Tausende Köpfe programmieren gegen dein Nervensystem. Aufmerksamkeit ist die wertvollste Währung der Welt.",
                narration = "Willkommen zum Gelassenheit Deep Dive Podcast. Episode eins: Die Biologie der Befreiung. Jeden einzelnen Tag kämpfen tausende der klügsten Köpfe im Silicon Valley gegen dein Nervensystem. Jeder Algorithmus, jede Push-Mitteilung und jede rote Zahl zielt auf deine wertvollste Währung: deine Aufmerksamkeit. Doch dein Gehirn ist nicht kaputt. Du brauchst kein weiteres Software-Update. Die Rettung ist bereits ab Werk in deiner Biologie verbaut."
            ),
            PodcastChapter(
                timeSeconds = 96,
                title = "01:36 • Das App-Paradoxon",
                summary = "Warum eine Push-Benachrichtigung für '3 Minuten Entspannung' kognitive Dissonanz und noch mehr Dopamin-Spikes erzeugt.",
                narration = "Hast du jemals bemerkt, wie paradox das moderne Leben geworden ist? Eine Push-Benachrichtigung erinnert dich daran, dich für drei Minuten zu entspannen. Du nimmst das Smartphone in die Hand, siehst noch drei ungelesene E-Mails, und schon schießt dein Cortisolspiegel nach oben. Wir versuchen, ein digitales Feuer mit digitalem Benzin zu löschen. Das Paradoxon herkömmlicher Achtsamkeits-Apps ist, dass sie uns noch fester an den Bildschirm ketten."
            ),
            PodcastChapter(
                timeSeconds = 283,
                title = "04:43 • Das Default Mode Network (DMN)",
                summary = "Das Ruhezustandsnetzwerk des Gehirns läuft auf Hochtouren und hält uns in Erwartungen und Sorgen gefangen.",
                narration = "Neurowissenschaftler nennen es das Default Mode Network – das Ruhezustandsnetzwerk unseres Gehirns. Wenn wir nichts Konkretes tun, schaltet das Gehirn keineswegs ab, sondern beginnt unbewusst zu kreisen: Was war gestern? Was muss ich morgen abliefern? Was denken die anderen über mich? Der ständige Strom digitaler Reize hält dieses Netzwerk in einem permanenten Alarmzustand."
            ),
            PodcastChapter(
                timeSeconds = 363,
                title = "06:03 • Die 5 Sinne als Hardware-Schutzschild",
                summary = "Deine Sinne haben keine Algorithmen und keine Erwartungen. Sie fordern keine Leistung, sondern sind reine physische Wahrheit.",
                narration = "Hier kommt die Biologie der Befreiung ins Spiel. Deine fünf Sinne – Sehen, Hören, Fühlen, Riechen und Schmecken – sind dein angeborenes Hardware-Schutzschild. Deine Sinne haben keine Algorithmen. Sie kennen keine Erwartungshaltung, keinen Zeitdruck und keine Zielvorgaben. Sie fordern keine Leistung von dir, sondern sind pure, ungefilterte Gegenwart."
            ),
            PodcastChapter(
                timeSeconds = 471,
                title = "07:51 • Reine Frequenz vs. Bewertung",
                summary = "Das Tor zur Welt: Hören ist passiv. Ein lautes Geräusch ist nur Physik – erst das Gehirn stempelt es als 'nervig' ab.",
                narration = "Nehmen wir das Hören. Hören geschieht völlig passiv. Eine Schallwelle trifft auf dein Trommelfell. Das ist reine Physik. Erst dein Gehirn stempelt das Geräusch als stressig oder nervig ab. Wenn du lernst, ein Geräusch einfach nur als Schwingung wahrzunehmen, ohne es vorschnell zu bewerten, verliert es sofort seine emotionale Macht über dein Gemüt."
            ),
            PodcastChapter(
                timeSeconds = 596,
                title = "09:56 • Der Tastsinn als Echtzeit-Anker",
                summary = "Die Haut kann nicht gestern oder morgen fühlen. Der physische Kontakt zu Stuhl oder Boden beweist: Jetzt in diesem Moment bist du sicher.",
                narration = "Dein verlässlichster Anker ist der Tastsinn. Deine Haut kann weder in der Vergangenheit noch in der Zukunft fühlen. Sie fühlt nur genau jetzt. Spüre den Kontakt deiner Füße auf dem Boden oder deinen Rücken an der Stuhllehne. Dieser physische Druck ist das physiologische Signal an dein Nervensystem: Ich bin hier. Ich bin in Sicherheit."
            ),
            PodcastChapter(
                timeSeconds = 644,
                title = "10:44 • Respekt & Zwischenmenschlichkeit",
                summary = "Empathie erfordert Präsenz. Wer sich selbst spürt, verliert nicht den Anschluss und reagiert nicht mehr wie eine programmierte Maschine.",
                narration = "Wahre Empathie und Gelassenheit entstehen nur, wenn wir präsent sind. Wenn du mit einem anderen Menschen sprichst, aber in Gedanken schon beim nächsten Chat bist, verlierst du die Verbindung. Wer sich selbst in seinen Sinnen spürt, reagiert nicht mehr wie ein programmierter Automat, sondern begegnet seinen Mitmenschen mit echter Klarheit und Respekt."
            ),
            PodcastChapter(
                timeSeconds = 814,
                title = "13:34 • Die Notbremse: Reale Praxis",
                summary = "Augen schließen (80% Input-Drosselung), kühle Luft einatmen, warme Luft ausatmen. 'Niemand verlangt jetzt etwas von dir.'",
                narration = "Wenn der Stress überwältigend wird, ziehe sofort die biologische Notbremse. Schließe für zwanzig Sekunden die Augen – damit drosselst du sofort bis zu achtzig Prozent des sensorischen Inputs im Gehirn. Atme durch die Nase ein, spüre die kühle Luft an den Nasenflügeln. Atme langsam aus, spüre die Wärme. Sage dir innerlich: Niemand verlangt jetzt etwas von mir."
            ),
            PodcastChapter(
                timeSeconds = 1186,
                title = "19:46 • Die Zukunft & Synthetische Sinne",
                summary = "Was passiert, wenn VR und Metaverse beginnen, unsere biologischen Sinne zu manipulieren? Der Wert echter Sinneswahrnehmung.",
                narration = "In einer Welt von Virtual Reality und künstlicher Intelligenz wird die Fähigkeit, echte Sinne von synthetischen Reizen zu unterscheiden, zur höchsten Superkraft. Nimm dir heute bewusst einen Moment Zeit. Schau dich um, atme tief ein und genieße deine unverfälschte biologische Freiheit."
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

    val allEpisodes = listOf(
        currentEpisode,
        PodcastEpisode(
            id = "ep_2_metaverse_der_sinne",
            title = "Episode 02: Das Metaverse der Sinne",
            showName = "Gelassenheit Deep Dive Podcast",
            subtitle = "Wie wir in einer KI-geprägten Welt biologisch unmanipulierbar bleiben",
            durationSeconds = 1680, // 28 min
            description = "Exklusive PRO-Episode: Tiefgehende Analyse über sensorische Manipulation, Deepfakes und wie echtes Fühlen zur letzten Bastion menschlicher Freiheit wird.",
            isProOnly = true,
            category = "Podcast",
            chapters = listOf(
                PodcastChapter(
                    timeSeconds = 0,
                    title = "00:00 • Synthetische Reizüberflutung",
                    summary = "Warum KI und generative Feeds unsere biologischen Belohnungssysteme kapern.",
                    narration = "Willkommen zur exklusiven zweiten Episode. Wenn Maschinen lernen, menschliche Gefühle zu imitieren, wird die Wahrnehmung deiner eigenen physischen Sinne zum höchsten Gut."
                ),
                PodcastChapter(
                    timeSeconds = 420,
                    title = "07:00 • Die Neurobiologie der Intuition",
                    summary = "Bauchgefühl und Vagusnerv: Warum der Körper vor dem Verstand Bescheid weiß.",
                    narration = "Dein Bauchhirn mit über 500 Millionen Neuronen sendet 90 Prozent aller Signale nach oben zum Gehirn. Lerne, dieser somatischen Intelligenz wieder zu vertrauen."
                )
            ),
            quotes = listOf(
                PodcastQuote(
                    quote = "Wer sich selbst spürt, lässt sich nicht von Algorithmen steuern.",
                    author = "Gelassenheit Deep Dive",
                    timestamp = "04:12"
                )
            ),
            keyTakeaways = listOf(
                "Somatische Signale erkennen, bevor der Verstand reagiert.",
                "Echtes Hören von digitaler Reizung unterscheiden."
            )
        ),
        PodcastEpisode(
            id = "sound_432_ambient_pure",
            title = "Reines Zuhören: 432 Hz Ambient Resonanz",
            showName = "Reines Zuhören & Klangbäder",
            subtitle = "Einfach nur zuhören – ohne Sprache, nur pure Frequenz & Harfe",
            durationSeconds = 1800, // 30 min
            description = "Schließe die Augen und tauche ein. 432 Hz wird traditionell für harmonische Zellentspannung und Pulsberuhigung genutzt.",
            isProOnly = true,
            category = "Soundbath",
            ambientFrequencyHz = 432.0,
            chapters = listOf(
                PodcastChapter(
                    timeSeconds = 0,
                    title = "00:00 • Pure 432 Hz Welle",
                    summary = "Reines Hören. Lasse den Klang durch deinen Körper schwingen.",
                    narration = "Entspanne dich und lausche einfach nur den harmonischen Frequenzen."
                )
            ),
            quotes = listOf(
                PodcastQuote(
                    quote = "Musik beginnt da, wo das Wort aufhört.",
                    author = "Gelassenheit Klangraum",
                    timestamp = "00:00"
                )
            ),
            keyTakeaways = listOf(
                "Kein Denken erforderlich: Reine auditive Entspannung.",
                "Ideal bei Kopfschmerzen, Reizüberflutung und abendlicher Erschöpfung."
            )
        ),
        PodcastEpisode(
            id = "sound_theta_sleep_bath",
            title = "Schlaf-Klangbad: Theta Waves (6Hz) & Regen",
            showName = "Schlaf & Regeneration",
            subtitle = "Binaurale Schwingungen für ungestörten Tiefschlaf",
            durationSeconds = 2700, // 45 min
            description = "Exklusives PRO-Schlafaudio: Theta-Wellen bringen das Gehirn in den Zustand zwischen Wachsein und Schlaf, während sanfter Regen den Geist beruhigt.",
            isProOnly = true,
            category = "Sleep",
            ambientFrequencyHz = 396.0,
            chapters = listOf(
                PodcastChapter(
                    timeSeconds = 0,
                    title = "00:00 • Übergang in die Nacht",
                    summary = "Sanftes Gleiten in den erholsamen Tiefschlaf.",
                    narration = "Lass los. Die Nacht nimmt dich auf."
                )
            ),
            quotes = listOf(
                PodcastQuote(
                    quote = "Guter Schlaf ist die stärkste Medizin der Natur.",
                    author = "Schlaflabor",
                    timestamp = "00:00"
                )
            ),
            keyTakeaways = listOf(
                "Fördert nachweislich Delta- und Theta-Wellen.",
                "Schaltet kreisende Gedanken zuverlässig aus."
            )
        )
    )
}
