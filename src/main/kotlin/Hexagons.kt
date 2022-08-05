import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
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
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        MaterialTheme {
            val drawModifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clipToBounds()

            Canvas(modifier = drawModifier) {
                for (i in 0..500) {
                    val coefficient = 0.75
                    val x = window.width + Random.nextDouble(-window.width * coefficient, window.width * coefficient)
                    val y = window.height + Random.nextDouble(-window.height * coefficient, window.width * coefficient)

                    polygon(x.toFloat(), y.toFloat(), 250f, 6)
                }
            }
        }
    }
}

private fun DrawScope.polygon(x: Float, y: Float, radius: Float, npoints: Int) {
    val angle = 2 * PI / npoints

    val randomRotationAngle = Random.nextDouble(-0.1, 0.1)

    var nextAngle = 0.0 + randomRotationAngle

    var ssx = x + cos(nextAngle) * radius
    var ssy = y + sin(nextAngle) * radius

    val color = Color(
        Random.nextInt(0xFF),
        Random.nextInt(0xFF),
        Random.nextInt(0xFF),
    )

    while (nextAngle < 2 * PI + randomRotationAngle) {
        nextAngle += angle
        val sx = x + cos(nextAngle) * radius
        val sy = y + sin(nextAngle) * radius

        drawLine(
            color,
            Offset(ssx.toFloat(), ssy.toFloat()),
            Offset(sx.toFloat(), sy.toFloat()),
            strokeWidth = Random.nextDouble(1.1, 2.2).toFloat()
        )
        ssx = sx
        ssy = sy
    }
}