package midi

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver

private const val TRANSMITTER_DEVICE_NAME = "javax.sound.midi.Transmitter#MPKmini2"
private const val TRANSMITTER_PROPERTY = "javax.sound.midi.Transmitter"


class MidiSource(private val flow: MutableSharedFlow<MidiMessage>) : Receiver {
    init {
        System.setProperty(TRANSMITTER_PROPERTY, TRANSMITTER_DEVICE_NAME)
    }

    fun start() {
        val transmitter = MidiSystem.getTransmitter()
        println("Transmitter: $transmitter")
//        transmitter.receiver = MidiLogger(this) // tracing
        transmitter.receiver = this
        println("Play on your musical keyboard...")
    }

    override fun close() {}

    override fun send(message: MidiMessage, timeStamp: Long) {
        val status = message.getStatus()

        if (status == 0xf8) return // ignore timing messages
        if (status == 0xfe) return // ignore status active

        // Strip channel number out of status
        val leftNibble = status and 0xf0
        when (leftNibble) {
            // only emit "note on" messages
            0x90 -> flow.tryEmit(message)
        }
    }

}