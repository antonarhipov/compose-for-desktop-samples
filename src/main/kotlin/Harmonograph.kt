import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        MaterialTheme {
            val drawModifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clipToBounds()

            Canvas(modifier = drawModifier) {

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

                val random = Random(1000)

                for (i in 0..11000) {

                    val xh = window.height / 3
                    val yh = window.height / 3

//                    val xcor =
//                        (center.x + xh * sin(10 * (t)) * exp(-d1 * t) + xh * sin(3 * (t - p2)) * exp(p1 * t)).toFloat()
//
//                    val ycor =
//                        (center.y + yh * cos(t - theta) * exp(p1 * t) + yh * cos(2 * t) * exp(-d3 * t)).toFloat()

                    val xcor =
                        (center.x + window.width / 3 * sin(f1 * (t - p1)) * exp(-d1 * t) + window.width / 2 * sin(f2 * (t - p2)) * exp(
                            -d2 * t
                        )).toFloat()
                    val ycor =
                        center.y + window.height / 3 * cos(f3 * (t - p3)) * exp(-d3 * t) + window.height / 2 * cos(f4 * (t - p4)) * exp(
                            -d4 * t
                        ); //oscillate vertically


                    drawCircle(
                        color = Color.Gray,
                        radius = 77f,
                        center = Offset(xcor, ycor),
                        style = Stroke(width = 0.03f),
                    )

                    drawCircle(
                        radius = 91f,
                        center = Offset(xcor, ycor),
                        brush = Brush.horizontalGradient(listOf(Color.Green, Color.Blue, Color.Magenta, Color.Blue, Color.Gray, Color.Red)),
                        style = Stroke(width = 0.3f),
                        colorFilter = ColorFilter.tint(Color.DarkGray, blendMode = BlendMode.Luminosity)
                    )

                    t += 0.0033f
                }

            }
        }
    }
}
