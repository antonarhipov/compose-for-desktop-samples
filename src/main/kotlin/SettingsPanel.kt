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

val padding = Modifier.padding(10.dp)

@Composable
fun PropertySwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Text(modifier = padding, text = label)
    Switch(modifier = padding, checked = checked, onCheckedChange = onChange)
}

@Composable
fun IntSlider(
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
        Slider(value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = { value = it; onChange(value.toInt()) },
            onValueChangeFinished = { onChange(value.toInt()) })
    }
}

@Composable
fun FloatSlider(
    initialValue: Float,
    minValue: Float,
    maxValue: Float,
    label: String,
    onChange: (Float) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = padding
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%.2f".format(value), fontSize = 14.sp)
        }
        Slider(value = value,
            valueRange = minValue..maxValue,
            onValueChange = { value = it; onChange(value) },
            onValueChangeFinished = { onChange(value) })
    }
}

const val MIN_RANDOM = 0.1f
const val MAX_RANDOM = 50.0f
const val MIN_ORBIT_RADIUS = 100.00f
const val MAX_ORBIT_RADIUS = 600.00f
const val MIN_INNER_RADIUS = 100.00f
const val MAX_INNER_RADIUS = 200.00f
const val MIN_OUTER_RADIUS = 200.00f
const val MAX_OUTER_RADIUS = 300.00f
const val MIN_STEPS = 1
const val MAX_STEPS = 360

data class Settings(
    @FloatConstraint("Orbit radius", 500f, 50f) val orbitRadius: Float = 300.0f,
    @FloatConstraint("Inner circles radius", 300f, 10f) val innerCirclesRadius: Float = MIN_INNER_RADIUS,
    @FloatConstraint("Outer circles radius", 400f, 10f) val outerCirclesRadius: Float = MIN_OUTER_RADIUS,
    @FloatConstraint("Random", 50f, 0f) val randomCoefficient: Float = MIN_RANDOM,
    @IntConstraint("Gap for circles at inner orbit", 360, 1) val stepsInnerOrbit: Int = 3,
    @IntConstraint("Gap for circles at outer orbit", 360, 1) val stepsOuterOrbit: Int = 5,
    @BooleanConstraint("Draw inner") val drawInnerOrbit: Boolean = true,
    @BooleanConstraint("Draw outer") val drawOuterOrbit: Boolean = true,
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
            PropertySwitch("Draw inner orbit", settings.drawInnerOrbit) { newValue ->
                onValueChange(settings.copy(drawInnerOrbit = newValue))
            }
        }
        item {
            PropertySwitch("Draw outer orbit", settings.drawOuterOrbit) { newValue ->
                onValueChange(settings.copy(drawOuterOrbit = newValue))
            }
        }
        item {
            FloatSlider(
                settings.orbitRadius, MIN_ORBIT_RADIUS, MAX_ORBIT_RADIUS, "Orbits radius"
            ) { newValue ->
                onValueChange(settings.copy(orbitRadius = newValue))
            }
        }
        item {
            IntSlider(
                settings.stepsInnerOrbit, MIN_STEPS, MAX_STEPS, "Steps for inner orbit"
            ) { newValue ->
                onValueChange(settings.copy(stepsInnerOrbit = newValue))
            }
        }
        item {
            IntSlider(
                settings.stepsOuterOrbit, MIN_STEPS, MAX_STEPS, "Steps for outer orbit"
            ) { newValue ->
                onValueChange(settings.copy(stepsOuterOrbit = newValue))
            }
        }
        item {
            FloatSlider(
                settings.innerCirclesRadius, MIN_INNER_RADIUS, MAX_INNER_RADIUS, "Inner orbit circles radius"
            ) { newValue ->
                onValueChange(settings.copy(innerCirclesRadius = newValue))
            }
        }
        item {
            FloatSlider(
                settings.outerCirclesRadius, MIN_OUTER_RADIUS, MAX_OUTER_RADIUS, "Outer orbit circles radius"
            ) { newValue ->
                onValueChange(settings.copy(outerCirclesRadius = newValue))
            }
        }
        item {
            FloatSlider(
                settings.randomCoefficient, MIN_RANDOM, MAX_RANDOM, "Random"
            ) { newValue -> onValueChange(settings.copy(randomCoefficient = newValue)) }
        }
    }
}


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FloatConstraint(val label: String, val max: Float, val min: Float)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntConstraint(val label: String, val max: Int, val min: Int)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class BooleanConstraint(val label: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumeratedConstraint(val label: String, vararg val options: String)


@Preview
@Composable
fun DynamicSettingsPanel(settings: Settings, onValueChange: (Settings) -> Unit) {
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

        for (member in settings.javaClass.declaredFields) {
            member.trySetAccessible()

            if (member.isAnnotationPresent(FloatConstraint::class.java)) {
                val initialValue = member.get(settings) as Float
                val annotation = member.getDeclaredAnnotation(FloatConstraint::class.java)
                item {
                    FloatSlider(
                        initialValue, annotation.min, annotation.max, annotation.label
                    ) { newValue ->
                        member.set(settings, newValue)
                    }
                }
            }
            if (member.isAnnotationPresent(IntConstraint::class.java)) {
                val initialValue = member.get(settings) as Int
                val annotation = member.getDeclaredAnnotation(IntConstraint::class.java)
                item {
                    IntSlider(initialValue, annotation.min, annotation.max, annotation.label) { newValue ->
                        member.set(settings, newValue)
                    }
                }
            }
            if (member.isAnnotationPresent(BooleanConstraint::class.java)) {
                val value = member.get(settings) as Boolean
                val annotation = member.getDeclaredAnnotation(BooleanConstraint::class.java)
                item {
                    //TODO: ugly but works. is there a better way?
                    PropertySwitch(annotation.label, value) { newValue ->
                        val copy = settings.copy()
                        val m = copy.javaClass.getDeclaredField(member.name)
                        m.trySetAccessible()
                        m.set(copy, newValue)
                        onValueChange(copy)
                    }
                }
            }
            if (member.isAnnotationPresent(EnumeratedConstraint::class.java)) {
                val initialValue = member.get(settings) as Array<*>
                val annotation = member.getDeclaredAnnotation(EnumeratedConstraint::class.java)
                //TODO: dropdown switch
            }
        }
    }
}
