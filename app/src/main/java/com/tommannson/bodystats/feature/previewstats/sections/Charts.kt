package com.tommannson.bodystats.feature.previewstats.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData2D
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.DefinedXAxisDrawer
import com.github.tehras.charts.line.renderer.xaxis.PrescaledXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.GridYAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.PrescaledGridYAxisDrawer
import com.tommannson.bodystats.feature.home.sections.listOfDates
import com.tommannson.bodystats.feature.previewstats.MonthGridData
import com.tommannson.bodystats.feature.previewstats.WeekGridData
import com.tommannson.bodystats.infrastructure.SavedStats
import org.threeten.bp.LocalDate

@Composable
fun DayChart(listOfItems: MutableList<SavedStats>) {
    LineChart(
        lineChartData = LineChartData2D<LocalDate>(
            points = listOfItems.map {
                val textToDisplay =
                    "${it.submitedAt.dayOfMonth} ${it.submitedAt.monthValue}"
                LineChartData2D.Point(
                    it.value,
                    textToDisplay,
                    it.submitedAt
                )
            },
            converter = { it.toEpochDay().toFloat() },
            displayX = { "${it.dayOfMonth} ${it.monthValue}" },
            minX = LocalDate.now().minusDays(60),
            maxX = LocalDate.now(),
        ),

        // Optional properties.
        modifier = Modifier
            .padding(16.dp)
            .padding(end = 16.dp)
            .fillMaxWidth()
            .height(300.dp),
        pointDrawer = FilledCircularPointDrawer(
            color = MaterialTheme.colors.primary,
            diameter = 8.dp
        ),
        lineDrawer = SolidLineDrawer(
            color = Color.LightGray,
            thickness = 2.dp
        ),
        xAxisDrawer = DefinedXAxisDrawer(
            chartLabels = listOfDates
        ),
        yAxisDrawer = GridYAxisDrawer(),
        horizontalOffset = 0f
    )
}

@Composable
fun MonthGrid(data: MonthGridData) {
    LineChart(
        lineChartData = LineChartData2D<Float>(
            points = data.calculatedValues.map {
                LineChartData2D.Point(
                    it.value,
                    "",
                    it.scale
                )
            },
            converter = { it },
            displayX = { it.toString() },
            minX = 0f,
            maxX = 1f,
        ),

        // Optional properties.
        modifier = Modifier
            .padding(16.dp)
            .padding(end = 16.dp)
            .fillMaxWidth()
            .height(300.dp),
        pointDrawer = FilledCircularPointDrawer(
            color = MaterialTheme.colors.primary,
            diameter = 8.dp
        ),
        lineDrawer = SolidLineDrawer(
            color = Color.LightGray,
            thickness = 2.dp
        ),
        xAxisDrawer = PrescaledXAxisDrawer(
            chartLabels = data.labels,
            gridPoints = data.gridLocation
        ),
        yAxisDrawer = PrescaledGridYAxisDrawer(),
        horizontalOffset = 0f
    )
}

@Composable
fun WeekGrid(data: WeekGridData) {
    LineChart(
        lineChartData = LineChartData2D<Float>(
            points = data.calculatedValues.map {
                LineChartData2D.Point(
                    it.value,
                    "",
                    it.scale
                )
            },
            converter = { it },
            displayX = { it.toString() },
            minX = 0f,
            maxX = 1f,
        ),

        // Optional properties.
        modifier = Modifier
            .padding(16.dp)
            .padding(end = 16.dp)
            .fillMaxWidth()
            .height(300.dp),
        pointDrawer = FilledCircularPointDrawer(
            color = MaterialTheme.colors.primary,
            diameter = 8.dp
        ),
        lineDrawer = SolidLineDrawer(
            color = Color.LightGray,
            thickness = 2.dp
        ),
        xAxisDrawer = PrescaledXAxisDrawer(
            chartLabels = data.labels,
            gridPoints = data.gridLocation
        ),
        yAxisDrawer = PrescaledGridYAxisDrawer(),
        horizontalOffset = 0f
    )
}

