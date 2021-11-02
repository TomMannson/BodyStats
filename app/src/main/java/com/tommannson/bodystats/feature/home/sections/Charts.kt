package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.tommannson.bodystats.infrastructure.configuration.SavedStats
import com.tommannson.bodystats.infrastructure.configuration.Statistic

@Composable
fun MyCharts(mapOfStats: Map<String, List<SavedStats>>) {
    Text("Moje statystyki", style = MaterialTheme.typography.h5)
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        elevation = 3.dp
    ) {
        Column {
            SimpleChart(
                chartName = "Waga", //WAGA
                data = mapOfStats[Statistic.WEIGHT] ?: listOf()
            )
            Divider()
            SimpleChart(
                chartName = "Brzuch", //Brzuch
                data = mapOfStats[Statistic.BELLY_STATISTIC] ?: listOf()
            )
            Divider()
            SimpleChart(
                chartName = "Talia",
                data = mapOfStats[Statistic.ARM_STATISTIC] ?: listOf()
            )
            Divider()
            SimpleChart(
                chartName = "Procernt tłuszczu",
                data = mapOfStats[Statistic.FAT_PERCENT] ?: listOf()
            )
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ }) {
                Text(text = "MORE")
            }
        }
    }
}

@Composable
fun TryChart(list: List<Point>) {
    LineChart(
        lineChartData = LineChartData(
            points = list
        ),
        // Optional properties.
        modifier = Modifier.fillMaxSize(),
        pointDrawer = FilledCircularPointDrawer(
            color = MaterialTheme.colors.primary,
            diameter = 8.dp
        ),
        lineDrawer = SolidLineDrawer(
            color = Color.LightGray,
            thickness = 2.dp
        ),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        horizontalOffset = 5f
    )
}

@Composable
fun SimpleChart(chartName: String, data: List<SavedStats>) {
    if (data.isEmpty()) {
        Text("data for $chartName is empty")
    } else {
        val dataTodisplay = mutableListOf<SavedStats>()
        dataTodisplay.addAll(data)
        if (data.size == 1) {
            dataTodisplay.add(0, data.first())
        }

        val firstItem = data.first()
        val lastItem = data.last()
        val differenceFromStart = lastItem.value - firstItem.value
        SimpleChart(
            chartName = chartName,
            date = lastItem.submitedAt.toString(),
            difference = "$differenceFromStart",
            savedStats = dataTodisplay
        )
    }
}

@Composable
fun SimpleChart(
    chartName: String,
    date: String,
    difference: String,
    savedStats: List<SavedStats> = listOf()
) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(chartName, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(date, style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(difference, color = Color.Green, style = MaterialTheme.typography.body2)
            }
            Box(modifier = Modifier.weight(1.3f)) {
//                    Chart()
                TryChart(savedStats.map { Point(it.value, "${it.submitedAt.dayOfMonth}.${it.submitedAt.monthValue}") })
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

private fun randomYValue(): Float = (100f * Math.random()).toFloat() + 45f