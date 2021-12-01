package com.tommannson.bodystats.feature.configuration.widgets

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.tommannson.bodystats.R

@Composable
fun DreamWeightField(
    dreamWeightState: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    IconTextField(
        value = dreamWeightState,
        label = "Waga marzeń (kg)",
        iconPainter = painterResource(id = R.drawable.ic_baseline_emoji_events_24),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus(force = true)
            }
        )
    )
}

@Composable
fun WeightField(weightState: String, onChanged: (String) -> Unit) {
    IconTextField(
        value = weightState,
        label = "Waga (kg)",
        iconPainter = painterResource(id = R.drawable.ic_baseline_scale_24),
        onValueChange = onChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
    )
}

@Composable
fun HeightField(heightState: String, onChanged: (String) -> Unit) {
    IconTextField(
        value = heightState,
        label = "Wzrost (cm)",
        iconPainter = painterResource(id = R.drawable.ic_baseline_height_24),
        onValueChange = onChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
    )
}

@Composable
fun NameField(nameState: String, onChanged: (String) -> Unit) {

    IconTextField(
        value = nameState,
        label = "Imię",
        iconPainter = painterResource(id = R.drawable.ic_baseline_face_24),
        onValueChange = onChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
    )
}