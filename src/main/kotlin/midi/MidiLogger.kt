package midi

import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage


class MidiLogger(private val receiver: Receiver) : Receiver {

    override fun send(message: MidiMessage, timeStamp: Long) {
        println("Timestamp = $timeStamp")
        displayMessage(message)
        receiver.send(message, timeStamp / 1000000)
    }

    override fun close() {
        receiver.close()
    }

    // Display MIDI message
    private fun displayMessage(message: MidiMessage) {
        val status = message.getStatus()

        if (status == 0xf8) return // ignore timing messages
        if (status == 0xfe) return // ignore status active

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
            else -> {
                println("Unknown status")
                displayRawData(message)
            }
        }
    }

    private fun displayRawData(message: MidiMessage) {
        val bytes = message.getMessage()
        if (message.length > 1) {
            println("Raw data: ")
            for (i in 1 ..< bytes.size) {
                print(byteToInt(bytes[i]).toString())
            }
            println("----------------------------")
        }
    }

    private fun displayNoteOn(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println("Bad MIDI message")
            return
        }
        val bytes = message.getMessage()

        val info = buildString {
            append("Note on. Channel ${midiChannelToInt(message)}. ")

            if (bytes[2].toInt() == 0) {
                append("Note: ${byteToInt(bytes[1])}. ")
            }

            var i = 1
            while (i < message.length) {
                append("Number ${byteToInt(bytes[i])}. Velocity ${byteToInt(bytes[i + 1])}")
                i += 2
            }
        }
        println(info)
        println("----------------------------")
    }

    private fun displayNoteOff(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println("Bad MIDI message")
        } else {
            val bytes = message.getMessage()
            println("Note off. Channel ${midiChannelToInt(message)}, Note ${byteToInt(bytes[1])}")
            println("----------------------------")
        }
    }

    private fun displayControllerChange(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println("Bad MIDI message")
            return
        }

        val info = buildString {
            append("CC. Channel ${midiChannelToInt(message)}. ")

            val bytes = message.getMessage()
            var i = 1
            while (i < message.length) {
                val value = byteToInt(bytes[i + 1])
                append("Controller ${byteToInt(bytes[i])}. Value $value")

                //changing the instrument on CC
                changeInstrument(value)

                i += 2
            }
        }
        println(info)
        println("----------------------------")
    }

    private fun changeInstrument(value: Int) {
        val sm = ShortMessage()
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, value, 0)
        receiver.send(sm, 0)
    }

    private fun displayKeyPressure(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println("Bad MIDI message")
            return
        }
        println("Key Pressure. Channel ${midiChannelToInt(message)}")
        val bytes = message.getMessage()
        var i = 1
        while (i < message.length) {
            println("Note Number ${byteToInt(bytes[i])}. Pressure ${byteToInt(bytes[i + 1])}")
            i += 2
        }
        println("----------------------------")
    }

    private fun displayPitchBend(message: MidiMessage) {
        if (message.length < 3 || message.length % 2 == 0) {
            println("Bad MIDI message")
            return
        }
        println("Pitch Bend. Channel ${midiChannelToInt(message)}")
        val bytes = message.getMessage()
        var i = 1
        while (i < message.length) {
            println("Value ${bytesToInt(bytes[i], bytes[i + 1])}")
            i += 2
        }
        println("----------------------------")
    }

    private fun displayProgramChange(message: MidiMessage) {
        if (message.length < 2) {
            println("Bad MIDI message")
            return
        }
        println("Program Change. Channel ${midiChannelToInt(message)}")
        val bytes = message.getMessage()
        for (i in 1 until message.length) {
            println("Program Number ${byteToInt(bytes[i])}")
        }
    }

    private fun displayChannelPressure(message: MidiMessage) {
        if (message.length < 2) {
            println("Bad MIDI message")
            return
        }
        println("Channel Pressure. Channel ${midiChannelToInt(message)}")
        val bytes = message.getMessage()
        for (i in 1 until message.length) {
            println("Pressure ${byteToInt(bytes[i])}")
        }
    }

    private fun byteToInt(b: Byte) = b.toInt() and 0xff

    // Two 7-bit bytes
    private fun bytesToInt(msb: Byte, lsb: Byte) = byteToInt(msb) * 128 + byteToInt(lsb)

    private fun midiChannelToInt(message: MidiMessage) = (message.getStatus() and 0x0f) + 1
}

