package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyCharts() {
    Text("Moje statystyki", style = MaterialTheme.typography.h5)

    SimpleChart(chartName = "Waga", date = "12.10.2021", difference = "-12.0")
    SimpleChart(chartName = "Waga", date = "12.10.2021", difference = "-12.0")
    SimpleChart(chartName = "Waga", date = "12.10.2021", difference = "-12.0")
    SimpleChart(chartName = "Waga", date = "12.10.2021", difference = "-12.0")
}

@Composable
fun SimpleChart(chartName: String, date: String, difference: String) {
    Box(modifier = Modifier.height(150.dp).fillMaxWidth()) {
    Card(modifier = Modifier.padding(vertical = 4.dp), elevation = 3.dp) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                Text(chartName, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(date, style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(difference, color = Color.Green, style = MaterialTheme.typography.body2)
            }
            Box(modifier = Modifier.weight(1.0f)) {
//                Text("asdasdasd")
//                Text("asdasdasd")
//                Text("asdasdasd")
                Chart()
            }
        }
    }
    }
}

@Preview
@Composable
fun PreviewSimpleChart() {
    SimpleChart(chartName = "A", date = "sdads", difference = "fff")
}

@Composable
fun Chart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}