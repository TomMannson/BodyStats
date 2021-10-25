package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyProgress() {
    Column() {
        Box(Modifier.fillMaxWidth()) {
            Text(
                "-15 kg",
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
                .background(MaterialTheme.colors.secondary)
        ) {
            Box(
                Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth(.045f)
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
            Text("90 kg")
            Box(Modifier.fillMaxWidth()) {
                Text("65 kg", modifier = Modifier.align(Alignment.CenterEnd))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview()
@Composable
fun PreviewProgress() {
    MyProgress()
}