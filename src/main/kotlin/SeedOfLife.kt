import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.*

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Seed Of Life",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        MaterialTheme {
            val drawModifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clipToBounds()

            Canvas(modifier = drawModifier) {
                val radius = 100.0f

                val circles = mutableListOf<CircleData>()
                circles.add(
                    CircleData(
                        offset = Offset( x = this.center.x, y = this.center.y ),
                        radius = radius
                    )
                )

                circles.add(
                    CircleData(
                        offset = Offset( x = this.center.x, y = this.center.y + radius ),
                        radius = radius
                    )
                )

                for (i in 0..4) {
                    val int1 = intersection(circles[0], circles.last())
                    circles.add(
                        CircleData(
                            offset = int1[0],
                            radius = radius
                        )
                    )
                }

                circles.forEach { circle ->
                    drawCircle(
                        color = randomColor(),
                        radius = circle.radius,
                        center = circle.offset,
                        style = Stroke(width = 2f),
                    )
                }
            }
        }
    }
}

data class CircleData (
    val offset: Offset,
    val radius: Float
)

// reference: https://stackoverflow.com/a/12221389/1275497
private fun intersection(c1: CircleData, c2: CircleData): List<Offset> {
    val dx = c2.offset.x - c1.offset.x
    val dy = c2.offset.y - c1.offset.y

    val dist = hypot(dy, dx)

    val centroid = (c1.radius * c1.radius - c2.radius * c2.radius + dist * dist) / (2.0 * dist)

    val x2 = c1.offset.x + (dx * centroid) / dist
    val y2 = c1.offset.y + (dy * centroid) / dist

    val h = sqrt(c1.radius * c1.radius - centroid * centroid)

    val rx = -dy * (h / dist)
    val ry = dx * (h / dist)

    return listOf(
        Offset((x2 + rx).toFloat(), (y2 + ry).toFloat()),
        Offset((x2 - rx).toFloat(), (y2 - ry).toFloat())
    )
}
