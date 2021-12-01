package com.tommannson.bodystats.feature.configuration.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(modifier: Modifier = Modifier, onSubmit: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(16.dp)
            .then(modifier),
        onClick = onSubmit
    ) {
        Text("Zatwierdź".uppercase())
    }
}