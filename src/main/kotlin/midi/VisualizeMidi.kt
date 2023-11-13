@file:OptIn(ExperimentalStdlibApi::class)

package midi

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.sound.midi.MidiMessage
import javax.sound.midi.ShortMessage
import javax.sound.midi.ShortMessage.NOTE_OFF
import javax.sound.midi.ShortMessage.NOTE_ON
import kotlin.random.Random


data class Circle(
    val noteNumber: Int,
    val xPercent: Float,
    val yPercent: Float,
    val baseColor: Color
) {
    var alpha = mutableStateOf<Float>(0f)

    val color by derivedStateOf {
        baseColor.copy(alpha = alpha.value)
    }
}

suspend fun Circle.fade() {
    animate(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = tween(1000)
    ) { value, /* velocity */ _ ->
        // Update alpha mutable state with the current animation value
        alpha.value = value
    }
}


@OptIn(ExperimentalStdlibApi::class)
fun main() = application {
    val mpkMini2 = 48..72
    val keystepPro = 36..72
    val keyboardRange = keystepPro

    val flow = MutableSharedFlow<MidiMessage>(extraBufferCapacity = 100)
    val midiSource = remember {
        MidiSource(flow).apply {
            start()
//            playFlow()
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        Column {
            val scope = rememberCoroutineScope()
            val circles = remember {
                mutableStateListOf<Circle>().apply {
                    (keyboardRange).forEach {
                        add(
                            Circle(
                                noteNumber = it,
                                xPercent = Random.nextFloat(),
                                yPercent = Random.nextFloat(),
                                baseColor = Color(
                                    red = (0..255).random(),
                                    green = (0..255).random(),
                                    blue = (0..255).random(),
                                ),
                            )
                        )
                    }
                }
            }

            //val message by flow.collectAsState(DummyMidiMessage)
            LaunchedEffect(flow) {
                flow.collect { message ->
                    if (message.length < 3 || message.length % 2 == 0) return@collect // Bad MIDI message

                    val bytes = message.message
                    println("Message: ${bytes.toHexString()}")
                    val noteNumber = byteToInt(bytes[1])
                    if (noteNumber !in keyboardRange) return@collect

                    scope.launch {
                        circles[noteNumber - keyboardRange.first].fade()
                    }
                }
            }

            //TODO: explore shaders for effects https://github.com/drinkthestars/shady
            //TODO: fade out animations for the individual shapes

            Row {
                for (i in keyboardRange) {
                    val noteIndex = i - keyboardRange.first
                    val circle = circles[noteIndex]
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            scope.launch {
                                midiSource.send(
                                    ShortMessage().apply {
                                        setMessage(NOTE_ON, 0, i, 79)
                                    },
                                    System.nanoTime() / 1000
                                )
                                delay(300)
                                midiSource.send(
                                    ShortMessage().apply {
                                        setMessage(NOTE_OFF, 0, i, 79)
                                    },
                                    System.nanoTime() / 1000
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = circle.color,
                        )
                    ) {
                        Text(i.toString())
                    }
                }
            }

            Canvas(modifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()) {
                circles.forEach { circle ->
                    displayNoteOn(circle)
                }
            }
        }
    }
}

private fun DrawScope.displayNoteOn(circle: Circle) {
    //TODO: map & scatter the shapes horizontally across canvas
    drawCircle(
        color = circle.color,
        radius = (circle.noteNumber * 10).toFloat(),
        center = center,
        style = Stroke(width = 20f),
    )
}

private fun byteToInt(b: Byte) = b.toInt() and 0xff
