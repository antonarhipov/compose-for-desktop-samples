import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
        title = "Circles with Settings",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        MaterialTheme {
            var settings by remember { mutableStateOf(Settings()) }

            Row(modifier = Modifier.padding(5.dp)) {
                Canvas(
                    Modifier
                        .fillMaxHeight()
                        .width(800.dp)
                        .background(Color.White)
                ) {
                    if(settings.drawOuterOrbit){
                        outerOrbit(settings)
                    }
                    if (settings.drawInnerOrbit) {
                        innerOrbit(settings)
                    }
                }
                SettingsPanel(settings) { settings = it }
            }
        }
    }
}

private fun DrawScope.outerOrbit(settings: Settings) {
    for (ang in 0..359 step settings.stepsOuterOrbit) {
        drawOffsetCircle(
            ang,
            settings.orbitRadius.toFloat(),
            settings.outerCirclesRadius.toFloat(),
            randomCoefficient = settings.randomCoefficient
        )
    }
}

private fun DrawScope.innerOrbit(settings: Settings) {
    for (ang in 0..359 step settings.stepsInnerOrbit) {
        drawOffsetCircle(
            ang,
            settings.orbitRadius.toFloat(),
            settings.innerCirclesRadius.toFloat(),
            strokeWidth = 1.3f,
            randomCoefficient = settings.randomCoefficient
        )
    }
}

private fun DrawScope.drawOffsetCircle(
    angle: Int,
    offsetRadius: Float,
    circleRadius: Float,
    strokeWidth: Float = 1.1f,
    randomCoefficient: Double = 1.0
) {
    val rad = Math.toRadians(angle.toDouble())

    //The offsets create minor glitches in the overall drawing
    //that make it look as if these are hand-drawn
    val offsetX = (offsetRadius * cos(rad) + Random.nextDouble(randomCoefficient)).toFloat()
    val offsetY = (offsetRadius * sin(rad) + Random.nextDouble(randomCoefficient)).toFloat()

    drawCircle(
        color = Color.Gray,
        radius = circleRadius,
        center = Offset(
            this.center.x + offsetX,
            this.center.y + offsetY
        ),
        style = Stroke(width = strokeWidth),
    )
}
