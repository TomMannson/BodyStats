package com.tommannson.bodystats.feature.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R

@Preview
@Composable
fun PreviewNumberSelector() {
    NumberSelector(value = 1, onValueChange = {})
}

@Composable
fun NumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { onValueChange(value - 1) }),
            painter = painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
            contentDescription = null // decorative element
        )
        Box(modifier = Modifier.size(24.dp)) {
            Text(
                "$value",
                Modifier.align(Alignment.Center),
                style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            )
        }
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { onValueChange(value + 1) }),
            painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
            contentDescription = null
        )
    }
}