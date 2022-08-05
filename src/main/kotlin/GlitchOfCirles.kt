import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Glitch Circles",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        MaterialTheme {
            val drawModifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clipToBounds()

            Canvas(modifier = drawModifier) {
                circle(Color.Cyan)
                circle(Color.Magenta)
                circle(Color.Green)
            }
        }
    }
}

private fun DrawScope.circle(color: Color) {
    val radius = 300.0f

    val randomCenterOffsetX = Random.nextInt(15, 55)
    val randomCenterOffsetY = Random.nextInt(35, 40)
    val newCenter = this.center.copy(this.center.x + randomCenterOffsetX, this.center.x + randomCenterOffsetY)


    for (ang in 0..359 step 6) {
        drawOffsetCircle(
            newCenter,
            ang, radius, 200f, strokeWidth = 1.3f, color = color
        )
    }
}

private fun DrawScope.drawOffsetCircle(
    center: Offset,
    angle: Int,
    offsetRadius: Float,
    circleRadius: Float,
    strokeWidth: Float = 1.1f,
    color: Color,
) {
    val rad = Math.toRadians(angle.toDouble())

    //The offsets create minor glitches in the overall drawing
    //that make it look as if these are hand-drawn
    val offsetX = (offsetRadius * cos(rad) + Random.nextDouble(1.7)).toFloat()
    val offsetY = (offsetRadius * sin(rad) + Random.nextDouble(1.9)).toFloat()

    drawCircle(
        color = color,
        radius = circleRadius,
        center = Offset(
            center.x + offsetX,
            center.y + offsetY
        ),
        style = Stroke(width = strokeWidth),
    )
}
