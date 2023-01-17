import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PropertySlider(
    initialValue: Double,
    minValue: Double,
    maxValue: Double,
    label: String,
    onChange: (Double) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = Modifier.padding(2.dp)
    ) {
        Row {
            Text(text = "$label: ", fontSize = 10.sp)
            Text(text = "%.2f".format(value), fontSize = 10.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = { value = it; onChange(value.toDouble()) },
            onValueChangeFinished = { onChange(value.toDouble()) }
        )
    }
}

const val MIN_ORBIT_RADIUS = 240.00
const val MAX_ORBIT_RADIUS = 400.00
const val MIN_INNER_RADIUS = 100.00
const val MAX_INNER_RADIUS = 200.00
const val MIN_OUTER_RADIUS = 200.00
const val MAX_OUTER_RADIUS = 300.00

data class Settings(
    val orbitRadius: Double = MIN_ORBIT_RADIUS,
    val innerCirclesRadius: Double = MIN_INNER_RADIUS,
    val outerCirclesRadius: Double = MIN_OUTER_RADIUS,
    val randomCoefficient: Double = 2.0
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsPanel(style: Settings, active: MutableState<Boolean>, onValueChange: (Settings) -> Unit) {
    LazyColumn(modifier = Modifier
        .width(300.dp)
        .onPointerEvent(eventType = PointerEventType.Enter) { active.value = true }
        .onPointerEvent(eventType = PointerEventType.Exit) { active.value = false }
    ) {
        item {
            Text(
                "Settings",
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        item {
            PropertySlider(
                style.orbitRadius,
                MIN_ORBIT_RADIUS,
                MAX_ORBIT_RADIUS,
                "Orbit radius"
            ) { newValue -> onValueChange(style.copy(orbitRadius = newValue)) }
        }
        item {
            PropertySlider(
                style.innerCirclesRadius,
                MIN_INNER_RADIUS,
                MAX_INNER_RADIUS,
                "Inner orbit circle radius"
            ) { newValue -> onValueChange(style.copy(innerCirclesRadius = newValue)) }
        }
        item {
            PropertySlider(
                style.outerCirclesRadius,
                MIN_OUTER_RADIUS,
                MAX_OUTER_RADIUS,
                "Outer orbit circle radius"
            ) { newValue -> onValueChange(style.copy(outerCirclesRadius = newValue)) }
        }
        item {
            PropertySlider(
                style.randomCoefficient,
                0.1,
                10.0,
                "Random"
            ) { newValue -> onValueChange(style.copy(randomCoefficient = newValue)) }
        }
    }
}
