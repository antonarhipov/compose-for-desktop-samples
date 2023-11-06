package midi

import java.lang.Thread.sleep
import javax.sound.midi.*
import javax.sound.midi.ShortMessage.*


class Midi

fun main() {
    val s = createMidiSequencer {
        val channel = 0
        val velocity = 64
        val note = 61
        val tick = 0

        track {
            event(NOTE_ON, channel, note, velocity, tick)
            event(NOTE_OFF, channel, note, 0, tick + 3)
            event(NOTE_ON, channel, note - 2, velocity, tick + 4)
            event(NOTE_OFF, channel, note - 2, 0, tick + 7)
            event(NOTE_ON, channel, note - 4, velocity, tick + 8)
            event(NOTE_OFF, channel, note - 4, 0, tick + 11)
            event(NOTE_ON, channel, note - 2, velocity, tick + 12)
            event(NOTE_OFF, channel, note - 2, 0, tick + 15)
            event(NOTE_ON, channel, note - 5, velocity, tick + 16)
            event(NOTE_OFF, channel, note - 5, 0, tick + 19)
        }
    }

    s.open()
    s.tempoInBPM = 200.0f;
    s.start()

    sleep(500)

    while (s.isRunning) {
        sleep(1000);
    }

    sleep(500)

    s.close()
}

private fun createMidiSequencer(s: Sequence.() -> Unit): Sequencer {
    val seq = Sequence(Sequence.PPQ, 4).apply(s)
    return MidiSystem.getSequencer().apply { sequence = seq }
}

private fun Sequence.track(track: Track.() -> Unit) {
    createTrack().apply(track)
}


// Create a MIDI event and add it to the track
private fun Track.event(command: Int, channel: Int, note: Int, velocity: Int, tick: Int) {
    val message = ShortMessage()
    try {
        message.setMessage(command, channel, note, velocity)
    } catch (e: InvalidMidiDataException) {
        e.printStackTrace()
    }
    add(MidiEvent(message, tick.toLong()))
}



