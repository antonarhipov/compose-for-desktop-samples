package animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.cos
import kotlin.math.sin


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Animations",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        val drawModifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()


        val transition = rememberInfiniteTransition()
        val angle by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 3000, easing = FastOutSlowInEasing),
                RepeatMode.Restart
            )
        )

        val oscillation by transition.animateFloat(
            initialValue = -250f,
            targetValue = 250f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 500, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )

        val offsetX = oscillation.dp * cos(Math.toRadians(angle.toDouble()).toFloat())
        val offsetY = oscillation.dp * sin(Math.toRadians(angle.toDouble()).toFloat())

        Canvas(drawModifier) {
            drawCircle(
                color = Color.Red,
                radius = 20f,
                center = center + Offset(offsetX.toPx(), offsetY.toPx())
            )
        }
    }

}
