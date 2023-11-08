package demo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
        title = "FooConf",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        draw()
    }
}

@Preview
@Composable
fun draw() {
    val drawModifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()

    val radius = 300f

    Canvas(modifier = drawModifier) {
//        drawCircle(
//            color = Color.Blue,
//            radius = radius,
//            center = Offset(
//                center.x,
//                center.y
//            ),
//            style = Stroke(width = 20f),
//        )

        for (ang in 0..10 * 359 step 2) {
            val rad = Math.toRadians(ang.toDouble())

            val randomX = Random.nextDouble(1.1)
            val randomY = Random.nextDouble(1.1)

            val offsetX = (radius + ang/10 * cos(rad)).toFloat() + randomX
            val offsetY = (radius + ang/10 * sin(rad)).toFloat() + randomY

            val colorRange = (0..255)
            drawCircle(
                color = Color(
                    colorRange.random(),
                    colorRange.random(),
                    colorRange.random(),
                ),
                radius = (10..20).random().toFloat(),
                center = Offset(
                    (center.x + offsetX).toFloat(),
                    (center.y + offsetY).toFloat()
                ),
                style = Stroke(width = 1f),
            )
        }


    }
}

