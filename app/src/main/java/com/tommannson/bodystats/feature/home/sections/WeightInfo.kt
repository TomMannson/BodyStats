package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.home.WeightInfo

@Composable
fun UserWeightInfo(weightInfo: WeightInfo, incrementWeight: () -> Unit, decrementWeight: () -> Unit, ) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), backgroundColor = MaterialTheme.colors.primary) {
        Column(
            modifier = Modifier.padding(32.dp),

            ) {

            Text(
                "Waga",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "Cel: ${weightInfo.targetWeight} kg",
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)){
                WeightAssigner(weightInfo.weight, incrementWeight, decrementWeight)
            }
        }
    }
}

@Composable
fun WeightAssigner(weight:String, incrementWeight: () -> Unit, decrementWeight: () -> Unit) {
    Box {
        Row() {
            Icon(
                tint = Color.White,
                modifier = Modifier.size(48.dp).clickable(onClick = decrementWeight),
                painter = painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
                contentDescription = null // decorative element
            )
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1.0f)
            ) {
                Text(
                    text = "$weight kg",
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Icon(
                tint = Color.White,
                modifier = Modifier.size(48.dp).clickable(onClick = incrementWeight),
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewUserWeightInfo() {
    UserWeightInfo(WeightInfo("1", "2"), {}, {})
}