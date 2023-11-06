package animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Animations",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        val drawModifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()

        val pendulumState1 = remember { mutableStateOf(0f) }
        val pendulumState2 = remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            snapshotFlow { pendulumState1.value }
                .collect { angle ->
                    pendulumState1.value = (angle + 0.01f) % (2 * Math.PI.toFloat())
                    delay(10)
                }
        }

        LaunchedEffect(Unit) {
            snapshotFlow { pendulumState2.value }
                .collect { angle ->
                    pendulumState2.value = (angle + 0.02f) % (2 * Math.PI.toFloat())
                    delay(10)
                }
        }

        Canvas(modifier = drawModifier) {
            val pivot = Offset(size.width / 2, size.height / 2)
            val pendulum1 = Offset(
                pivot.x + 150 * sin(pendulumState1.value),
                pivot.y + 150 * cos(pendulumState1.value)
            )
            val pendulum2 = Offset(
                pendulum1.x + 100 * sin(pendulumState2.value),
                pendulum1.y + 100 * cos(pendulumState2.value)
            )

            drawCircle(color = Color.Blue, center = pivot, radius = 10f)
            drawCircle(color = Color.Blue, center = pendulum1, radius = 10f)
            drawCircle(color = Color.Blue, center = pendulum2, radius = 10f)
            drawLine(Color.Black, start = pivot, end = pendulum1, strokeWidth = 1f)
            drawLine(Color.Black, start = pendulum1, end = pendulum2, strokeWidth = 1f)
        }
    }

}
