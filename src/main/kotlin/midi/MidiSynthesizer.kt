package midi

import javax.sound.midi.*

private val TRANS_DEV_NAME = "javax.sound.midi.Transmitter#MPKmini2"
private val SYNTH_DEV_NAME = "javax.sound.midi.Synthesizer#Gervill"

/** See [MidiSystem] for other classes  */
private val TRANS_PROP_KEY = "javax.sound.midi.Transmitter"
private val SYNTH_PROP_KEY = "javax.sound.midi.Synthesizer"

fun main() {

    // Get a transmitter and synthesizer from their device names
    // using system properties or defaults
    val trans = transmitter
    val synth = synthesizer

    // The synthesizer is your MIDI device, which needs to be opened
    if (!synth.isOpen) {
        synth.open()
        println("Loaded instruments:")
        println(synth.loadedInstruments.contentToString())
    }

    // You get your receiver from the synthesizer, then set it in
    // your transmitter.  Optionally, you can create an implementation
    // of Receiver to display the messages before they're sent.
    val receiver = synth.receiver
//        val message = ShortMessage()
//        receiver.send(message, 10000)
    val logger = MidiLogger(receiver) // optional
    trans.receiver = logger // or just "receiver"

    // You should be able to play on your musical keyboard (transmitter)
    // and hear sounds through your PC synthesizer (receiver)
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

private class MidiLogger(private val receiver: Receiver) : Receiver {
    var isSystemExclusiveData = false
    override fun send(message: MidiMessage, timeStamp: Long) {
        displayMessage(message, timeStamp)
        println("timestamp=$timeStamp")
        receiver.send(message, timeStamp / 1000000)
    }

    override fun close() {
        receiver.close()
    }

    // Display MIDI message
    private fun displayMessage(message: MidiMessage, timeStamp: Long) {

        // Check: Are we printing system exclusive data?
        if (isSystemExclusiveData) {
            displayRawData(message)
            return
        }
        val status = message.getStatus()

        // These statuses clutter the display
        if (status == 0xf8) {
            return
        } // ignore timing messages
        if (status == 0xfe) {
            return
        } // ignore status active
        System.out.printf(
            "%d - Status: 0x%s",
            timeStamp, Integer.toHexString(status)
        )

        // Strip channel number out of status
        val leftNibble = status and 0xf0
        when (leftNibble) {
            0x80 -> displayNoteOff(message)
            0x90 -> displayNoteOn(message)
            0xa0 -> displayKeyPressure(message)
            0xb0 -> displayControllerChange(message)
            0xc0 -> displayProgramChange(message)
            0xd0 -> displayChannelPressure(message)
            0xe0 -> displayPitchBend(message)
            0xf0 -> displaySystemMessage(message)
            else -> {
                println(" Unknown status")
                displayRawData(message)
            }
        }
    }

    // Displays raw data as integers, if any
    private fun displayRawData(message: MidiMessage) {
        val bytes = message.getMessage()
        if (message.length > 1) {
            print("\tRaw data: ")
            for (i in 1 until bytes.size) {
                print(byteToInt(bytes[i]).toString() + " ")
            }
            println()
        }
    }

    // Display status and data of a NoteOn message.  Data may come
    // in pairs after the status byte.
    //
    // Note that a NoteOn with a velocity of 0 is synonymous with
    // a NoteOff message.
    private fun displayNoteOn(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println(" Bad MIDI message")
            return
        }
        val bytes = message.getMessage()

        // Zero velocity
        if (bytes[2].toInt() == 0) {
            print(" = Note off")
        } else {
            print(" = Note on")
        }
        print(", Channel " + midiChannelToInt(message))
        if (bytes[2].toInt() == 0) {
            println(", Note " + byteToInt(bytes[1]))
            return
        }
        print("\n\t")
        var i = 1
        while (i < message.length) {
            if (i > 1) {
                print("; ")
            }
            System.out.printf(
                "Number %d, Velocity %d",
                byteToInt(bytes[i]), byteToInt(bytes[i + 1])
            )
            i += 2
        }
        println()
    }

    // Display status and data of a NoteOff message.
    private fun displayNoteOff(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println(" Bad MIDI message")
        } else {
            val bytes = message.getMessage()
            System.out.printf(
                " = Note off, Channel %d, Note %d%n",
                midiChannelToInt(message), byteToInt(bytes[1])
            )
            println()
        }
    }

    // Display status and data of a ControllerChange message.  Data may come
    // in pairs after the status byte.
    private fun displayControllerChange(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println(" Bad MIDI message")
            return
        }
        print(
            " = Controller Change, Channel "
                    + midiChannelToInt(message) + "\n\t"
        )
        val bytes = message.getMessage()
        var i = 1
        while (i < message.length) {
            if (i > 1) {
                print("; ")
            }
            System.out.printf(
                "Controller %d, Value %d",
                byteToInt(bytes[i]), byteToInt(bytes[i + 1])
            )
            i += 2
        }
        println()
    }

    // Display status and data of a KeyPressure message.  Data may come
    // in pairs after the status byte.
    private fun displayKeyPressure(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println(" Bad MIDI message")
            return
        }
        print(
            " = Key Pressure, Channel "
                    + midiChannelToInt(message) + "\n\t"
        )
        val bytes = message.getMessage()
        var i = 1
        while (i < message.length) {
            if (i > 1) {
                print("; ")
            }
            System.out.printf(
                "Note Number %d, Pressure %d",
                byteToInt(bytes[i]), byteToInt(bytes[i + 1])
            )
            i += 2
        }
        println()
    }

    // Display status and data of a PitchBend message.  Data may come
    // in pairs after the status byte.
    private fun displayPitchBend(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println(" Bad MIDI message")
            return
        }
        print(
            (" = Pitch Bend, Channel "
                    + midiChannelToInt(message) + "\n\t")
        )
        val bytes = message.getMessage()
        var i = 1
        while (i < message.length) {
            if (i > 1) {
                print("; ")
            }
            System.out.printf(
                "Value %d",
                bytesToInt(bytes[i], bytes[i + 1])
            )
            i += 2
        }
        println()
    }

    // Display status and data of a ProgramChange message
    private fun displayProgramChange(message: MidiMessage) {
        if (message.length < 2) {
            println(" Bad MIDI message")
            return
        }
        print(
            (" = Program Change, Channel "
                    + midiChannelToInt(message) + "\n\t")
        )
        val bytes = message.getMessage()
        for (i in 1 until message.length) {
            if (i > 1) {
                print(", ")
            }
            println("Program Number " + byteToInt(bytes[i]))
        }
    }

    // Display status and data of a ChannelPressure message
    private fun displayChannelPressure(message: MidiMessage) {
        if (message.length < 2) {
            println(" Bad MIDI message")
            return
        }
        print(
            (" = Channel Pressure, Channel "
                    + midiChannelToInt(message) + "\n\t")
        )
        val bytes = message.getMessage()
        for (i in 1 until message.length) {
            if (i > 1) {
                print(", ")
            }
            println("Pressure " + byteToInt(bytes[i]))
        }
    }

    // Display system messages.  Some may have data.
    //
    // "Begin System Exclusive" stops data interpretation, "End of
    // System Exclusive" starts it again
    private fun displaySystemMessage(message: MidiMessage) {
        val bytes = message.getMessage()
        when (message.getStatus()) {
            0xf0 -> {
                println(" = Begin System Exclusive")
                isSystemExclusiveData = true
            }

            0xf1 -> if (bytes.size < 2) {
                println(" Bad Data")
            } else {
                println(
                    (" = MIDI Time Code 1/4 Frame, Time Code "
                            + byteToInt(bytes[1]))
                )
            }

            0xf2 -> {
                if (bytes.size < 3) {
                    println(" Bad Data")
                } else {
                    println(
                        (" = Song Position, Pointer "
                                + bytesToInt(bytes[1], bytes[2]))
                    )
                }
                if (bytes.size < 2) {
                    println(" Bad Data")
                } else {
                    println(
                        (" = Song Select, Song "
                                + byteToInt(bytes[1]))
                    )
                }
            }

            0xf3 -> if (bytes.size < 2) {
                println(" Bad Data")
            } else {
                println(
                    (" = Song Select, Song "
                            + byteToInt(bytes[1]))
                )
            }

            0xf6 -> println(" = Tune Request")
            0xf7 -> {
                println(" = End of System Exclusive")
                isSystemExclusiveData = false
            }

            0xf8 -> println(" = Timing Clock") // ignored
            0xfa -> println(" = Start")
            0xfb -> println(" = Continue")
            0xfc -> println(" = Stop")
            0xfe -> println(" = Active Sensing") // ignored
            0xff -> println(" = System Reset")
            else -> {
                println(" Unknow System Message")
                displayRawData(message)
            }
        }
    }

    private fun byteToInt(b: Byte) = b.toInt() and 0xff

    // Two 7-bit bytes
    private fun bytesToInt(msb: Byte, lsb: Byte) = byteToInt(msb) * 128 + byteToInt(lsb)

    private fun midiChannelToInt(message: MidiMessage) = (message.getStatus() and 0x0f) + 1
}

