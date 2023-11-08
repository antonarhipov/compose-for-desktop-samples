import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {

        val harmonographPaths = remember {
            mutableStateOf(listOf<Offset>())
        }

        LaunchedEffect(Unit) {
            var t = 0f
            while (true) {
                harmonographPaths.value += Offset(
                    400 * sin(3 * t) + 100 * sin(t),
                    400 * cos(2 * t) + 100 * cos(t)
                )
                t += (PI / 180f).toFloat()
                delay(15)
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()
        ) {
            harmonographPaths.value.forEach { point ->
                drawCircle(color = Color.Magenta, center = center + point, radius = 7.0f)
            }
        }
        
    }
}
