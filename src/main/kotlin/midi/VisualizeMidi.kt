package midi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.sound.midi.MidiMessage

fun main() = application {
    val flow = MutableStateFlow<MidiMessage>(DummyMidiMessage)
    MidiSource(flow).start()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        val message by flow.collectAsState(DummyMidiMessage)

        //TODO: explore shaders for effects https://github.com/drinkthestars/shady
        //TODO: fade out animations for the individual shapes

        Canvas(modifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()) {
            displayNoteOn(message)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun DrawScope.displayNoteOn(message: MidiMessage, ) {
    if (message.length < 3 || message.length % 2 == 0) return // Bad MIDI message

    val bytes = message.message
    println("Message: ${bytes.toHexString()}")
    val noteNumber = byteToInt(bytes[1])

    val color =  Color(
        (0..255).random(),
        (0..255).random(),
        (0..255).random(),
    )

    //TODO: map & scatter the shapes horizontally across canvas
    drawCircle(
        color = color,
        radius = (noteNumber * 10).toFloat(),
        center = center,
        style = Stroke(width = 20f),
    )
}

private fun byteToInt(b: Byte) = b.toInt() and 0xff

// not supposed to be used for anything
object DummyMidiMessage : MidiMessage(ByteArray(0)) {
    override fun clone(): Any {
        TODO("This is a dummy message")
    }
}
