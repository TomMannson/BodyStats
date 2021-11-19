package com.tommannson.bodystats.feature.previewstats.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.feature.previewstats.TimeKind

@Composable
fun TimeUnitChipsSelector(selectedKind: MutableState<TimeKind>, onSelected: (TimeKind) -> Unit) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SelectableChips("Dziennie", selectedKind, TimeKind.DAYLY, onSelected)
        Spacer(modifier = Modifier.width(2.dp))
        SelectableChips("Tygodniowo", selectedKind, TimeKind.WEAKLY, onSelected)
        Spacer(modifier = Modifier.width(2.dp))
        SelectableChips("Miesięcznie", selectedKind, TimeKind.MONTHLY, onSelected)

    }
}

@Composable
fun SelectableChips(
    label: String,
    value: MutableState<TimeKind>,
    expected: TimeKind,
    onSelected: (TimeKind) -> Unit
) {
    val chipsModifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
    Box(
        modifier = Modifier
            .clickable {
                value.value = expected
                onSelected(expected)
            }
            .clip(RoundedCornerShape(4.dp))
            .run {
                return@run if (value.value == expected) {
                    background(MaterialTheme.colors.primary)
                } else {
                    this
                }
            }

    ) {
        Text(label, modifier = chipsModifier, style = LocalTextStyle
            .current
            .run {
                return@run if (value.value == expected) {
                    copy(Color.White)
                } else {
                    this
                }
            })
    }
}

inline fun Modifier.composeModifier(block: (Modifier) -> Modifier): Modifier {
    return block(this)
}