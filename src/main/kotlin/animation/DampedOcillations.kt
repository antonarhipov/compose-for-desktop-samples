import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlin.math.sin

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {
        val time = remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                time.value += 0.02f
                delay(20)
            }
        }

        val modifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (i in 0..< size.width.toInt()) {
                    val damping = 0.7f * (i / size.width)
                    val amplitude = (size.height / 4) * (1 - damping)
                    val frequency = 20f * (1 - damping)
                    val y = size.height / 2 + amplitude * sin(frequency * (i / size.width + time.value))
                    drawCircle(color = Color.Red, radius = 2f, center = Offset(i.toFloat(), y))
                }
            }
        }
    }
}