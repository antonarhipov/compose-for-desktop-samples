import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Polygons",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        MaterialTheme {
            val drawModifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clipToBounds()

            Canvas(modifier = drawModifier) {
                for (i in 0..1000) {
                    val coefficient = 0.75
                    val x = window.width + Random.nextDouble(-window.width * coefficient, window.width * coefficient)
                    val y = window.height + Random.nextDouble(-window.height * coefficient, window.width * coefficient)

                    polygon(x.toFloat(), y.toFloat(), 100f, 6)
                }
            }
        }
    }
}

private fun DrawScope.polygon(x: Float, y: Float, radius: Float, npoints: Int) {
    val angle = 2 * PI / npoints
    var nextAngle = 0.0

    var ssx = x + cos(nextAngle) * radius
    var ssy = y + sin(nextAngle) * radius

    while (nextAngle < 2 * PI) {
        nextAngle += angle
        val sx = x + cos(nextAngle) * radius
        val sy = y + sin(nextAngle) * radius
        drawLine(Color.Blue, Offset(ssx.toFloat(), ssy.toFloat()), Offset(sx.toFloat(), sy.toFloat()))
        ssx = sx
        ssy = sy
    }
}