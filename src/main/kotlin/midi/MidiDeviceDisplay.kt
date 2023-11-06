package midi

import javax.sound.midi.*

fun main() {
    val deviceInfo = MidiSystem.getMidiDeviceInfo()
    println("Found ${deviceInfo.size} MIDI devices")

    for (info in deviceInfo) {
        println("**********************")
        println("Device name: ${info.name}")
        println("Description: ${info.description}")
        println("Vendor: ${info.vendor}")
        println("Version: ${info.version}")
        try {
            val device = MidiSystem.getMidiDevice(info)
            println(device.deviceType())
        } catch (e: MidiUnavailableException) {
            println("Can't get MIDI device")
            e.printStackTrace()
        }
    }
}

private fun MidiDevice.deviceType() {
    fun inPort() = if (maxReceivers != 0) "IN " else ""
    fun outPort() = if (maxTransmitters != 0) "OUT " else ""

    buildString {
        append(
            when (this@deviceType) {
                is Sequencer -> "This is a sequencer"
                is Synthesizer -> "This is a synthesizer"
                else -> "This is a MIDI port " + inPort() + outPort()
            }
        )
        append("Maximum receivers: ${maxToString(maxReceivers)}")
        append("Maximum transmitters: ${maxToString(maxTransmitters)}")
    }
}

private fun maxToString(max: Int): String {
    return if (max == -1) "Unlimited" else max.toString()
}

