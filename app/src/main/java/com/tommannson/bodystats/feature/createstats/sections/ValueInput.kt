package com.tommannson.bodystats.feature.createstats.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.createstats.State
import com.tommannson.bodystats.feature.createstats.textstyling.UnitVisualTransformation

@Composable
fun ValueInput(
    state: State,
    onValue: (String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .padding(start = 32.dp, end = 8.dp)
                .size(48.dp)
                .clickable(onClick = onDecrease),
            painter = painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
            contentDescription = null // decorative element
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {

            var textForDisplay by remember(
                state.currentParamKey,
                state.currentValue
            ) {
                mutableStateOf(
                    state.currentValueText
                )
            }

            OutlinedTextField(
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center
                ),
                value = textForDisplay,
                onValueChange = {
                    textForDisplay = it
                    onValue(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.align(Alignment.Center),
                visualTransformation = UnitVisualTransformation(state.paramUnit)
            )
        }
        Icon(
            modifier = Modifier
                .padding(start = 8.dp, end = 32.dp)
                .size(48.dp)
                .clickable(onClick = onIncrease),
            painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
            contentDescription = null
        )
    }
}