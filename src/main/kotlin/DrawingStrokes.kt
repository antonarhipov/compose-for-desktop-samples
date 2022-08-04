import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

// copied from https://stackoverflow.com/a/71090112/431270

fun main() = application {

    val ACTION_IDLE = 0
    val ACTION_DOWN = 1
    val ACTION_MOVE = 2
    val ACTION_UP = 3

    Window(
        onCloseRequest = ::exitApplication,
        title = "Drawing strokes",
        state = rememberWindowState(width = 500.dp, height = 500.dp)
    ) {
        MaterialTheme {

            val path = remember { Path() }
            var motionEvent by remember { mutableStateOf(ACTION_IDLE) }
            var currentPosition by remember { mutableStateOf(Offset.Unspecified) }

            // color and text are for debugging and observing state changes and position
            var gestureColor by remember { mutableStateOf(Color.LightGray) }
            var gestureText by remember { mutableStateOf("Touch to Draw") }

            val drawModifier = Modifier
                .fillMaxSize()
                .background(gestureColor)
                .clipToBounds()
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {

                            // Wait for at least one pointer to press down, and set first contact position
                            val down: PointerInputChange = awaitFirstDown().also {
                                motionEvent = ACTION_DOWN
                                currentPosition = it.position
                                gestureColor = Color.Blue
                            }

                            do {
                                // This PointerEvent contains details including events, id, position and more
                                val event: PointerEvent = awaitPointerEvent()

                                var eventChanges =
                                    "DOWN changedToDown: ${down.changedToDown()} changedUp: ${down.changedToUp()}\n"
                                event.changes
                                    .forEachIndexed { index: Int, pointerInputChange: PointerInputChange ->
                                        eventChanges += "Index: $index, id: ${pointerInputChange.id}, " +
                                                "changedUp: ${pointerInputChange.changedToUp()}" +
                                                "pos: ${pointerInputChange.position}\n"

                                        // This necessary to prevent other gestures or scrolling
                                        // when at least one pointer is down on canvas to draw
                                        pointerInputChange.consumePositionChange()
                                    }

                                gestureText = "EVENT changes size ${event.changes.size}\n" + eventChanges

                                gestureColor = Color.Green
                                motionEvent = ACTION_MOVE
                                currentPosition = event.changes.first().position
                            } while (event.changes.any { it.pressed })

                            motionEvent = ACTION_UP
                            gestureColor = Color.LightGray

                            gestureText += "UP changedToDown: ${down.changedToDown()} " +
                                    "changedUp: ${down.changedToUp()}\n"
                        }
                    }
                }


            Canvas(modifier = drawModifier) {

                when (motionEvent) {
                    ACTION_DOWN -> {
                        path.moveTo(currentPosition.x, currentPosition.y)
                    }

                    ACTION_MOVE -> {
                        if (currentPosition != Offset.Unspecified) {
                            path.lineTo(currentPosition.x, currentPosition.y)
                        }
                    }

                    ACTION_UP -> {
                        path.lineTo(currentPosition.x, currentPosition.y)
                        // Change state to idle to not draw in wrong position
                        // if recomposition happens
                        motionEvent = ACTION_IDLE
                    }

                    else -> Unit
                }

                drawPath(
                    color = Color.Red,
                    path = path,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }
    }
}




