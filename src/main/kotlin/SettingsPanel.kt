import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepSlider(
    initialValue: Int,
    minValue: Int,
    maxValue: Int,
    label: String,
    onChange: (Int) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%d".format(value.toInt()), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = { value = it; onChange(value.toInt()) },
            onValueChangeFinished = { onChange(value.toInt()) }
        )
    }
}

val padding = Modifier.padding(10.dp)

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
        modifier = padding
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%.2f".format(value), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = { value = it; onChange(value.toDouble()) },
            onValueChangeFinished = { onChange(value.toDouble()) }
        )
    }
}

const val MIN_RANDOM = 0.1
const val MAX_RANDOM = 50.0
const val MIN_ORBIT_RADIUS = 100.00
const val MAX_ORBIT_RADIUS = 600.00
const val MIN_INNER_RADIUS = 100.00
const val MAX_INNER_RADIUS = 200.00
const val MIN_OUTER_RADIUS = 200.00
const val MAX_OUTER_RADIUS = 300.00
const val MIN_STEPS = 1
const val MAX_STEPS = 360

data class Settings(
    val orbitRadius: Double = 300.0,
    val innerCirclesRadius: Double = MIN_INNER_RADIUS,
    val outerCirclesRadius: Double = MIN_OUTER_RADIUS,
    val randomCoefficient: Double = MIN_RANDOM,
    val stepsInnerOrbit: Int = 3,
    val stepsOuterOrbit: Int = 5,
    val drawInnerOrbit: Boolean = true,
    val drawOuterOrbit: Boolean = true,
)

@Preview
@Composable
fun SettingsPanel(settings: Settings, onValueChange: (Settings) -> Unit) {
    LazyColumn(
        modifier = Modifier.width(400.dp)
    ) {
        item {
            Text(
                "Settings",
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )
        }
        item {
            Text(modifier = padding, text = "Draw inner orbit")
            Switch(modifier = padding, checked = settings.drawInnerOrbit, onCheckedChange = { newValue ->
                onValueChange(settings.copy(drawInnerOrbit = newValue))
            })
        }
        item {
            Text(modifier = padding, text = "Draw outer orbit")
            Switch(modifier = padding, checked = settings.drawOuterOrbit, onCheckedChange = { newValue ->
                onValueChange(settings.copy(drawOuterOrbit = newValue))
            })
        }
        item {
            PropertySlider(
                settings.orbitRadius,
                MIN_ORBIT_RADIUS,
                MAX_ORBIT_RADIUS,
                "Orbits radius"
            ) { newValue -> onValueChange(settings.copy(orbitRadius = newValue)) }
        }
        item {
            StepSlider(
                settings.stepsInnerOrbit,
                MIN_STEPS,
                MAX_STEPS,
                "Steps for inner orbit"
            ) { newValue -> onValueChange(settings.copy(stepsInnerOrbit = newValue)) }
        }
        item {
            StepSlider(
                settings.stepsOuterOrbit,
                MIN_STEPS,
                MAX_STEPS,
                "Steps for outer orbit"
            ) { newValue -> onValueChange(settings.copy(stepsOuterOrbit = newValue)) }
        }
        item {
            PropertySlider(
                settings.innerCirclesRadius,
                MIN_INNER_RADIUS,
                MAX_INNER_RADIUS,
                "Inner orbit circles radius"
            ) { newValue -> onValueChange(settings.copy(innerCirclesRadius = newValue)) }
        }
        item {
            PropertySlider(
                settings.outerCirclesRadius,
                MIN_OUTER_RADIUS,
                MAX_OUTER_RADIUS,
                "Outer orbit circles radius"
            ) { newValue -> onValueChange(settings.copy(outerCirclesRadius = newValue)) }
        }
        item {
            PropertySlider(
                settings.randomCoefficient,
                MIN_RANDOM,
                MAX_RANDOM,
                "Random"
            ) { newValue -> onValueChange(settings.copy(randomCoefficient = newValue)) }
        }
    }
}


