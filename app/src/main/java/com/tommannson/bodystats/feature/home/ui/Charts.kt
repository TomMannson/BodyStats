package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData2D
import com.github.tehras.charts.line.LineChartData2D.Point
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.DefinedXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.model.statistics.getStatFormatter
import com.tommannson.bodystats.model.statistics.getStatUnit
import com.tommannson.bodystats.feature.createstats.model.Configurations.PARAMS_UI_MAP
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.model.statistics.Statistic
import com.tommannson.bodystats.utils.fmt
import org.threeten.bp.LocalDate
import kotlin.math.absoluteValue


val listOfDates: List<LocalDate> = (0 until 5).map {
    return@map LocalDate.now().minusDays(15L * it)
}.reversed()


@Composable
fun MyCharts(
    naviController: NavController,
    mapOfStats: Map<String, List<SavedStats>>,
    onAddClicked: () -> Unit,
    onMoreClicked: () -> Unit,

    ) {

    Card(
        elevation = 3.dp
    ) {
        Column {
            Row {
                Text(
                    "Moje statystyki",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(onClick = onAddClicked) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_add_circle_24),
                        tint = MaterialTheme.colors.primary, contentDescription = null
                    )
                }
            }
            val listToDisplay =
                listOf(Statistic.WEIGHT, Statistic.WAIST_STATISTIC, Statistic.BELLY_STATISTIC)

            for (item in listToDisplay) {
                val config = PARAMS_UI_MAP[item]
                SimpleChart(
                    chartName = config?.name ?: "NO_NAME",
                    data = mapOfStats[item] ?: listOf(),
                    typeOfdata = item
                ) {
                    naviController.navigate(Screen.PreviewScreen.routeWithParam(item))
                }
                Divider()
            }
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onMoreClicked
            ) {
                Text(text = "WIĘCEJ")
            }
        }
    }
}

@Composable
fun TryChart(list: List<Point<LocalDate>>) {
    LineChart(
        lineChartData = LineChartData2D(
            points = list,
            converter = { it.toEpochDay().toFloat() },
            displayX = { "${it.dayOfMonth} ${it.monthValue}" },
            minX = LocalDate.now().minusDays(60),
            maxX = LocalDate.now(),
        ),
        // Optional properties.
        modifier = Modifier.fillMaxSize(),
        pointDrawer = FilledCircularPointDrawer(
            color = MaterialTheme.colors.primary,
            diameter = 8.dp
        ),
        lineDrawer = SolidLineDrawer(
            color = Color.LightGray,
            thickness = 1.dp
        ),
        xAxisDrawer = DefinedXAxisDrawer(
            chartLabels = listOfDates
        ),
        yAxisDrawer = SimpleYAxisDrawer(),
        horizontalOffset = 0f
    )
}

@Composable
fun SimpleChart(
    chartName: String,
    data: List<SavedStats>,
    typeOfdata: String,
    onChartClicked: () -> Unit
) {
    if (data.isEmpty()) {
        Text("Brak danych do wyświetlenia wykresu: $chartName", modifier = Modifier.padding(16.dp))
    } else {

        val firstItem = data.first()
        val lastItem = data.last()
        val differenceFromStart = lastItem.value - firstItem.value
        Box(
            modifier = Modifier.clickable(onClick = onChartClicked)
        ) {
            val sign = differenceFromStart.arrowDirectionAtSign()
            val formated = differenceFromStart.absoluteValue fmt getStatFormatter(typeOfdata)
            val unit = getStatUnit(typeOfdata)
            SimpleChart(
                chartName = chartName,
                date = firstItem.submitedAt.toString(),
                difference = "$formated$sign $unit",
                savedStats = data
            )
        }
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
                Text("Od startu", style = MaterialTheme.typography.body2)
                Text(date, style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    difference,
                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
                )
            }
            Box(modifier = Modifier.weight(1.3f)) {
//                    Chart()
                TryChart(savedStats.map {
                    Point(
                        it.value,
                        "${it.submitedAt.dayOfMonth}.${it.submitedAt.monthValue}",
                        it.submitedAt
                    )
                })
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