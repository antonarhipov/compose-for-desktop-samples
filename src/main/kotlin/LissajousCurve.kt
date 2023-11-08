import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Lissajous curve",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp)
    ) {

        Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {
            val curveColor = Color.Blue
            val alpha = 3.0
            val beta = 5.0
            val delta = Math.PI / 2.0
            val steps = 1000
            val twoPi = 2.0 * Math.PI
            val factor = twoPi / steps
            val size = this.size

            val offsetX = size.width / 2.0f
            val offsetY = size.height / 2.0f
            val scale = listOf(size.width, size.height).min() / 2.5f

            drawLine(
                curveColor,
                Offset(offsetX, offsetY),
                Offset(
                    offsetX + (scale * Math.sin(alpha * 0.0 + delta)).toFloat(),
                    offsetY - (scale * Math.sin(beta * 0.0)).toFloat()
                ),
                strokeWidth = 3.0f
            )

            for (t in 1..steps) {
                drawLine(
                    curveColor,
                    Offset(
                        offsetX + (scale * Math.sin(alpha * (t - 1) * factor + delta)).toFloat(),
                        offsetY - (scale * Math.sin(beta * (t - 1) * factor)).toFloat()
                    ),
                    Offset(
                        offsetX + (scale * Math.sin(alpha * t * factor + delta)).toFloat(),
                        offsetY - (scale * Math.sin(beta * t * factor)).toFloat()
                    ),
                    strokeWidth = 3.0f
                )
            }
        }
    }
}
