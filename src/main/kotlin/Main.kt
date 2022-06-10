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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


const val ACTION_IDLE = 0
const val ACTION_DOWN = 1
const val ACTION_MOVE = 2
const val ACTION_UP = 3

sealed class Shape {
    abstract fun DrawScope.draw()
}

class Ellipse(
    private val color: Color,
    private val offset: Offset,
    private val rotation: Float
) : Shape() {
    override fun DrawScope.draw() {
        rotate(rotation, offset) {
            rotate(rotation, offset) {
                drawOval(
                    color = color,
                    topLeft = offset.copy(offset.x - 50, offset.y - 65),
                    size = Size(100f, 130f)
                )
            }
        }
    }
}

class Square(
    private val color: Color,
    private val offset: Offset,
    private val rotation: Float
) : Shape() {
    override fun DrawScope.draw() {
        rotate(rotation, offset) {
            drawRect(
                color = color,
                topLeft = offset.copy(offset.x - 50, offset.y - 50),
                size = Size(100f, 100f)
            )
        }
    }
}

class Circle(
    private val color: Color,
    private val offset: Offset
) : Shape() {
    override fun DrawScope.draw() {
        drawCircle(
            color = color,
            center = offset,
            radius = 50f,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}


val shapes = mutableListOf<Shape>()

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

                                do {
                                    // This PointerEvent contains details including events, id, position and more
                                    val event: PointerEvent = awaitPointerEvent()
                                    event.changes.forEach { pointerInputChange: PointerInputChange ->
                                        pointerInputChange.consumePositionChange()
                                    }
                                    motionEvent = ACTION_MOVE
                                    currentPosition = event.changes.first().position
                                } while (event.changes.any { it.pressed })
                                motionEvent = ACTION_UP
                            }
                        }
                    }
            ) {
                fun redraw() {
                    shapes.add(randomShape(currentPosition))
                    shapes.forEach {
                        with(it) { draw() }
                    }
                }

                when (motionEvent) {
                    ACTION_DOWN -> redraw()
                    ACTION_MOVE -> {
                        if (currentPosition != Offset.Unspecified) {
                            redraw()
                        }
                    }

                    ACTION_UP -> {
//                        motionEvent = ACTION_IDLE
                        redraw()
                    }
//                    else -> Unit
                }
            }
        }
    }
}

fun randomShape(currentPosition: Offset): Shape =
    when ((0..50).random()) {
        in 0..30 -> Circle(randomColor(), currentPosition)
        in 31..45 -> Square(randomColor(), currentPosition, randomRotation())
        else -> Ellipse(randomColor(), currentPosition, randomRotation())
    }

fun randomColor() : Color {
    val colorRange = 0..255
    val r = colorRange.random()
    val g = colorRange.random()
    val b = colorRange.random()
    return Color(r, g, b)
}

fun randomRotation() = (0..180).random().toFloat()