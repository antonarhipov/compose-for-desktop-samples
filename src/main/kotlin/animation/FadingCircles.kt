package animation

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Animations",
        state = rememberWindowState(width = 800.dp, height = 800.dp)
    ) {
        val drawModifier = Modifier.fillMaxSize().background(Color.White).clipToBounds()

        val green = remember { Animatable(Color.Green) }
        val blue = remember { Animatable(Color.Blue) }
        val yellow = remember { Animatable(Color.Yellow) }

        LaunchedEffect(key1 = true) {
            green.animateTo(Color.Green.copy(alpha = 0f), animationSpec = tween(1000, easing = LinearEasing))
        }

        LaunchedEffect(key1 = true) {
            blue.animateTo(Color.Blue.copy(alpha = 0f), animationSpec = tween(1000, easing = LinearEasing))
        }

        LaunchedEffect(key1 = true) {
            yellow.animateTo(Color.Yellow.copy(alpha = 0f), animationSpec = tween(1000, easing = LinearEasing))
        }

        Canvas(modifier = drawModifier) {
            drawCircle(green.value, radius = 100f, center = Offset(size.width / 2, size.height / 2))
            drawCircle(blue.value, radius = 100f, center = Offset(size.width / 4, size.height / 2))
            drawCircle(yellow.value, radius = 100f, center = Offset(size.width / 4 * 3, size.height / 2))
        }

    }

}
