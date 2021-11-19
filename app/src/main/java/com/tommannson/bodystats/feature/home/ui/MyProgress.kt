package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.feature.createstats.getStatFormatter
import com.tommannson.bodystats.infrastructure.configuration.Statistic
import com.tommannson.bodystats.utils.fmt

@Composable
fun MyProgress(start: Float, end: Float, current: Float) {

    Column() {
        Box(Modifier.fillMaxWidth()) {
            Text(
                "${Math.abs(start - current) fmt "#.#"}${calculateSign(current, start)}  kg",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .height(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                .background(Color.LightGray)
        ) {
            Box(
                Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth(calculateProgressPosition(start, end, current))
            )
            Box(
                Modifier
                    .padding(start = 6.dp)
                    .align(Alignment.CenterStart)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .align(Alignment.CenterEnd)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row() {
            Text("${start fmt getStatFormatter(Statistic.WEIGHT)} kg")
            Box(Modifier.fillMaxWidth()) {
                Text("${end fmt getStatFormatter(Statistic.WEIGHT)} kg", modifier = Modifier.align(Alignment.CenterEnd))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun calculateSign(value: Float, reference: Float) =  if (value < reference) {
    "\u2193"
} else if (value > reference) {
    "↑"
} else {
    ""
}

fun calculateProgressPosition(start: Float, end: Float, current: Float) =

    if(start > end){
       if(current < end){
           1f;
       } else if(current > start){
           0f
       } else {
           ((start - end) - (current - end)) / (start - end)
       }
    } else if((start < end)) {
        if(current <= start){
            0f;
        } else if(current > end){
            1f
        } else {
            (current - start) / (end - start)
        }
    } else {
        1f
    }



@Preview()
@Composable
fun PreviewProgress() {
    MyProgress(10f, 50f, 35f)
}

fun Float.arrowDirectionAtSign() =  if (this < 0) {
    "\u2193"
} else if (this > 0) {
    "↑"
} else {
    ""
}