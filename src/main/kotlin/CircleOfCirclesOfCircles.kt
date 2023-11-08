import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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

        val drawModifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clipToBounds()

        Canvas(modifier = drawModifier) {
            val radius = 200

            for (ang in 0..7 * 359 step 10) {

                val rad = Math.toRadians(ang.toDouble())
                val offsetX = ((radius + ang/2) * cos(rad)).toFloat()
                val offsetY = ((radius + ang/2) * sin(rad)).toFloat()

                drawCircle(
                    color = Color.LightGray,
                    radius = 3f,
                    center = Offset(center.x + offsetX, center.y + offsetY),
                    style = Stroke(width = 2f)
                )

                //region more circles
                val nradius = 60
                for (n in 0..359 step 3) {
                    val nRad = Math.toRadians(n.toDouble())
                    val nOffsetX = (nradius * n/(100+n) * cos(nRad)).toFloat()
                    val nOffsetY = (nradius * n/(100+n) * sin(nRad)).toFloat()

                    drawCircle(
                        color = Color.Gray,
                        radius = 30f,
                        center = Offset(center.x + offsetX + nOffsetX, center.y + offsetY + nOffsetY),
                        style = Stroke(width = 0.5f)
                    )
                }
                //endregion

            }

        }
    }
}
