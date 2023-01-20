import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.cos
import kotlin.math.sin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Creative Coding",
        state = rememberWindowState(width = 1200.dp, height = 1200.dp)
    ) {
        MaterialTheme {
            val modifier = Modifier.fillMaxSize().background(Color.White)

            Canvas(modifier) {
                for (ang in 0 until 10 * 360 step 3) {
                    val rad = Math.toRadians(ang.toDouble())
                    val x = (200f + ang / 5) * cos(rad).toFloat()
                    val y = (200f + ang / 5) * sin(rad).toFloat()

                    drawCircle(
                        color = Color(
                            (0..ang).random(),
                            (0..ang).random(),
                            (0..ang).random(),
                        ),
                        radius = (1..10).random().toFloat(),
                        style = Fill,
                        center = Offset(
                            center.x + x, center.y + y
                        )
                    )
                }
            }

        }
    }
}

