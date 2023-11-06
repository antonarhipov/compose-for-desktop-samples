import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Circles",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        draw()
    }
}

@Preview
@Composable
private fun draw() {
    val drawModifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()


    Canvas(modifier = drawModifier) {
        val radius = 300.0f

        var t = 0f

        val f1 = 2.51f
        val f2 = 3.4f;
        val f3 = 3.9f;
        val f4 = 1f;

        val d1 = 0.0185f
        val d2 = 0.007f
        val d3 = 0.0015f
        val d4 = 0.008f

        val p1 = 0.01f
        val p2 = 5 * PI / 16
        val p3 = 0.177f
        val p4 = 0.333f

        val theta = PI / 2

        for (ang in 0..2000) {

            val rad = Math.toRadians(ang.toDouble())
            val offsetX = (radius * cos(rad)).toFloat()
            val offsetY = (radius * sin(rad)).toFloat()

            val randX = Random.nextDouble(1.0).toFloat()
            val randY = Random.nextDouble(1.0).toFloat()

            val xcor = (sin(f1 * (t - p1)) * exp(-d1 * t) + 400 * sin(f2 * (t - p2)) * exp(-d2 * t)).toFloat()
            val ycor = cos(f3 * (t - p3)) * exp(-d3 * t) + 400 * cos(f4 * (t - p4)) * exp(-d4 * t)


            //            val xcor = (radius * sin((f1 * t + theta) * exp(-d1 * t) + randX * sin((f2 * t + theta) * exp(-d2 * t))))
            //            val ycor = (radius * cos((f3 * t + theta) * exp(-d1 * t) + randY * cos((f4 * t + theta) * exp(-d2 * t))))


            drawCircle(
                color = Color.DarkGray,
                radius = 1f,
                center = Offset(
                    center.x + offsetX + xcor,
                    center.y + offsetY + xcor //ycor
                ),
                style = Stroke(width = 1.1f),
                )

            t += 0.0033f
        }
    }
}


