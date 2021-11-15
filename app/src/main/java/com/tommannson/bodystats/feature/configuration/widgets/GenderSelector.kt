package com.tommannson.bodystats.feature.configuration.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.infrastructure.configuration.Gender

@Composable
fun SenderSelector(value: Int, onGenderSelected: (Int) -> Unit) {
    Row {
        Box(
            modifier = Modifier
                .height(72.dp)
                .width(54.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.gender),
                contentDescription = null
            )
        }
        Column() {
            Text(text = "Płeć", style = MaterialTheme.typography.caption)
            Row() {
                val maleSelected =
                    if (value == Gender.MALE) Modifier.background(MaterialTheme.colors.primary) else Modifier
                val femaleSelected =
                    if (value == Gender.FEMALE) Modifier.background(MaterialTheme.colors.primary) else Modifier
                Box(
                    modifier = Modifier
                        .clickable {
                            onGenderSelected(Gender.MALE)
                        }
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .then(maleSelected)
                ) {
                    Text(
                        "Meżczyzna",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = if (value == Gender.MALE) Color.White else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clickable {
                            onGenderSelected(Gender.FEMALE)
                        }
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .then(femaleSelected)
                ) {
                    Text(
                        "Kobieta",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = if (value == Gender.FEMALE) Color.White else Color.Black
                    )
                }
            }
        }
    }
}