package com.example.meditation

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.*

class MeditationAudioEngine {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var playJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startAmbient(frequencyHz: Double = 432.0, volume: Float = 0.5f) {
        stop()
        isPlaying = true

        playJob = scope.launch {
            try {
                val sampleRate = 44100
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(sampleRate / 2)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.setVolume(volume)
                track.play()

                val buffer = ShortArray(bufferSize)
                var phase1 = 0.0
                var phase2 = 0.0
                var lfoPhase = 0.0
                val freq1 = frequencyHz
                val freq2 = frequencyHz * 1.5 // Perfect fifth harmonic (warm singing bowl feel)
                val lfoFreq = 0.1 // 10 second wave swell

                while (isPlaying && isActive) {
                    for (i in buffer.indices) {
                        // Ambient swell envelope via slow LFO
                        val lfo = (sin(lfoPhase) + 1.0) * 0.5 * 0.3 + 0.7
                        // Fundamental sine + harmonic
                        val sampleVal = (sin(phase1) * 0.65 + sin(phase2) * 0.35) * lfo

                        buffer[i] = (sampleVal * 18000).toInt().coerceIn(-32767, 32767).toShort()

                        phase1 += 2.0 * PI * freq1 / sampleRate
                        if (phase1 > 2.0 * PI) phase1 -= 2.0 * PI

                        phase2 += 2.0 * PI * freq2 / sampleRate
                        if (phase2 > 2.0 * PI) phase2 -= 2.0 * PI

                        lfoPhase += 2.0 * PI * lfoFreq / sampleRate
                        if (lfoPhase > 2.0 * PI) lfoPhase -= 2.0 * PI
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (e: Exception) {
                // Audio safely handled
            }
        }
    }

    fun playSingingBowlBell() {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationMs = 2500
                val samples = sampleRate * durationMs / 1000
                val pcm = ShortArray(samples)
                for (i in 0 until samples) {
                    val t = i.toDouble() / sampleRate
                    // Exponential decay envelope
                    val decay = exp(-t * 1.8)
                    // Bell overtone mix (528Hz + 1056Hz + 1584Hz)
                    val s = (sin(2 * PI * 528.0 * t) * 0.6 +
                             sin(2 * PI * 1056.0 * t) * 0.3 +
                             sin(2 * PI * 1584.0 * t) * 0.1) * decay
                    pcm[i] = (s * 28000).toInt().coerceIn(-32767, 32767).toShort()
                }

                val bellTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(samples * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                bellTrack.write(pcm, 0, samples)
                bellTrack.play()
                delay(2600)
                bellTrack.release()
            } catch (e: Exception) {
                // Ignored gracefully
            }
        }
    }

    fun stop() {
        isPlaying = false
        playJob?.cancel()
        playJob = null
        try {
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            // Ignored
        }
        audioTrack = null
    }
}
