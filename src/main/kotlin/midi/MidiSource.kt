package midi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver

//private const val TRANSMITTER_DEVICE_NAME = "javax.sound.midi.Transmitter#MPKmini2"
//private const val TRANSMITTER_DEVICE_NAME = "javax.sound.midi.Transmitter#Arturia DrumBrute Impact"
private const val TRANSMITTER_DEVICE_NAME = "javax.sound.midi.Transmitter#KeyStep Pro"
private const val TRANSMITTER_PROPERTY = "javax.sound.midi.Transmitter"

private val SYNTH_DEV_NAME = "javax.sound.midi.Synthesizer#Gervill"
private val SYNTH_PROP_KEY = "javax.sound.midi.Synthesizer"


class MidiSource(private val flow: MutableSharedFlow<MidiMessage>) : Receiver {
    init {
        System.setProperty(TRANSMITTER_PROPERTY, TRANSMITTER_DEVICE_NAME)
        System.setProperty(SYNTH_PROP_KEY, SYNTH_DEV_NAME)
    }

    var synthesizer: Receiver? = null
    var logger: MidiLogger? = null

    fun start() {
        val transmitter = MidiSystem.getTransmitter()
        println("Transmitter: $transmitter")

        val synth = MidiSystem.getSynthesizer()

        if (!synth.isOpen) {
            synth.open()
            println("Loaded instruments:")
            println(synth.loadedInstruments.contentToString())
        }

        val receiver = synth.receiver
        synthesizer = synth.receiver
        logger = MidiLogger(receiver)

        transmitter.receiver = MidiLogger(this) // tracing
//        transmitter.receiver = this
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
            0x90 -> {
                flow.tryEmit(message)
                synthesizer?.send(message, timeStamp / 1000000)
            }
        }
    }

}