package midi

import javax.sound.midi.*

private val TRANS_DEV_NAME = "javax.sound.midi.Transmitter#MPKmini2"
private val SYNTH_DEV_NAME = "javax.sound.midi.Synthesizer#Gervill"

private val TRANS_PROP_KEY = "javax.sound.midi.Transmitter"
private val SYNTH_PROP_KEY = "javax.sound.midi.Synthesizer"

fun main() {
    val trans = transmitter
    val synth = synthesizer

    if (!synth.isOpen) {
        synth.open()
        println("Loaded instruments:")
        println(synth.loadedInstruments.contentToString())
    }

    val receiver = synth.receiver
    val logger = MidiLogger(receiver)
    trans.receiver = logger

    println("Play on your musical keyboard...")
}

private val synthesizer: Synthesizer
    get() {
        if (SYNTH_DEV_NAME.isNotEmpty() || !"default".equals(SYNTH_DEV_NAME, ignoreCase = true)) {
            System.setProperty(SYNTH_PROP_KEY, SYNTH_DEV_NAME)
        }
        return MidiSystem.getSynthesizer()
    }

private val transmitter: Transmitter
    get() {
        if (TRANS_DEV_NAME.isNotEmpty() && !"default".equals(TRANS_DEV_NAME, ignoreCase = true)) {
            System.setProperty(TRANS_PROP_KEY, TRANS_DEV_NAME)
        }
        return MidiSystem.getTransmitter()
    }
