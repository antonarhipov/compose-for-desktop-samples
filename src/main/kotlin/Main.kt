import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


const val ACTION_IDLE = 0
const val ACTION_DOWN = 1
const val ACTION_MOVE = 2
const val ACTION_UP = 3

typealias Command = (Offset) -> Unit

val listOfFigures = mutableListOf<Pair<Command, Offset>>()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 500.dp, height = 500.dp)
    ) {
        MaterialTheme {
            var motionEvent by remember { mutableStateOf(ACTION_IDLE) }
            var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
            Canvas(
                modifier = Modifier.fillMaxSize()
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        forEachGesture {
                            awaitPointerEventScope {
                                awaitFirstDown().also {
                                    motionEvent = ACTION_DOWN
                                    currentPosition = it.position
                                }
                            }
                        }
                    }
            ) {
                fun draw(offset: Offset) =
                    drawCircle(
                        color = Color.Magenta,
                        center = offset,
                        radius = size.minDimension / 4,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                when (motionEvent) {
                    ACTION_DOWN -> {
                        listOfFigures.add(Pair(::draw, currentPosition))
                        listOfFigures.forEach { (command, offset) ->
                            command(offset)
                        }
                    }
//                    else -> Unit
                }
            }
        }
    }
}