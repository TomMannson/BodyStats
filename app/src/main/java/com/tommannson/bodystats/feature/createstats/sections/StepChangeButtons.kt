package com.tommannson.bodystats.feature.createstats.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.feature.createstats.State

@Preview
@Composable
fun PreviewStepChangeButtons() {
    StepChangeButtons(
        state = State(selectedStep = 1, invalidData = false, orderOfItemsToSave = listOf("", ""))
    )
}

@Composable
fun StepChangeButtons(
    state: State?,
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if ((state?.selectedStep ?: 0) != 0) {
            Button(onClick = onPrevious) {
                Text("Poprzedni".uppercase())
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
        Button(
            enabled = !(state?.invalidData ?: true),
            onClick = onNext,
        ) {
            Text(state?.nextButtonText?.uppercase() ?: "")
        }
    }
}