package com.tommannson.bodystats.feature.createstats.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.createstats.State
import com.tommannson.bodystats.ui.theme.lighPrimary

@Preview
@Composable
fun PreviewStepper() {
    Stepper(
        state = State(orderOfItemsToSave = listOf("A", "A", "A", "A", "A", "A", "A")),
        currentNumber = 2
    )
}

@Composable
fun Stepper(
    state: State?,
    currentNumber: Int
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (item in 0 until (state?.orderOfItemsToSave?.size ?: 0)) {
            val selected = item < currentNumber
            val current = item <= currentNumber

            val color =
                if (current) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                    alpha = .0f
                )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
            ) {
                val modifier = Modifier
                    .align(Alignment.Center)
                if (selected) {
                    Icon(
                        tint = MaterialTheme.colors.primary,
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.ic_baseline_check_24),
                        contentDescription = null,
                    )
                } else {
                    Text(
                        "${item + 1}", style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.lighPrimary
                        ), modifier = modifier
                    )
                }
            }
        }
    }
}