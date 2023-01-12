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
                TODO("Hexagon connected one to each other make honeycombs")
            }
        }
    }
}
