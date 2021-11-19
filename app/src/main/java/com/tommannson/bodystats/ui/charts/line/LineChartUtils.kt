package com.github.tehras.charts.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

object LineChartUtils {
    fun calculateDrawableArea(
        xAxisDrawableArea: Rect,
        yAxisDrawableArea: Rect,
        size: Size,
        offset: Float
    ): Rect {
        val horizontalOffset = xAxisDrawableArea.width * offset / 100f

        return Rect(
            left = yAxisDrawableArea.right + horizontalOffset,
            top = 0f,
            bottom = xAxisDrawableArea.top,
            right = size.width - horizontalOffset
        )
    }

    fun calculateXAxisDrawableArea(
        yAxisWidth: Float,
        labelHeight: Float,
        size: Size
    ): Rect {
        val top = size.height - labelHeight

        return Rect(
            left = yAxisWidth,
            top = top,
            bottom = size.height,
            right = size.width
        )
    }

    fun calculateXAxisLabelsDrawableArea(
        xAxisDrawableArea: Rect,
        offset: Float
    ): Rect {
        val horizontalOffset = xAxisDrawableArea.width * offset / 100f

        return Rect(
            left = xAxisDrawableArea.left + horizontalOffset,
            top = xAxisDrawableArea.top,
            bottom = xAxisDrawableArea.bottom,
            right = xAxisDrawableArea.right - horizontalOffset
        )
    }

    fun Density.calculateYAxisDrawableArea(
        xAxisLabelSize: Float,
        size: Size
    ): Rect {
        // Either 50dp or 10% of the chart width.
        val right = minOf(50.dp.toPx(), size.width * 10f / 100f)

        return Rect(
            left = 0f,
            top = 0f,
            bottom = size.height - xAxisLabelSize,
            right = right
        )
    }

    fun <T> calculatePointLocation(
        drawableArea: Rect,
        lineChartData: LineChartData2D<T>,
        point: LineChartData2D.Point<T>,
        index: Int
    ): Offset {

        val minX = lineChartData.minX
        val maxX = lineChartData.maxX

        val diffrence = lineChartData.converter(maxX) - lineChartData.converter(minX)
        val xValue =
            lineChartData.converter(lineChartData.points[index].xValue) - lineChartData.converter(
                minX
            )


//    val x = if(lineChartData.points.size == 1) index.toFloat() else (index.toFloat() / (lineChartData.points.size - 1))
        val x = xValue / diffrence
        var y = ((point.value - lineChartData.minYValue) / lineChartData.yRange)

        if (y == 0f) {
            val newY = ((point.value - lineChartData.minY) / lineChartData.yRange)
            if (newY != 0f) {
                y = newY
            }
        }
        if(lineChartData.maxY == lineChartData.minY && lineChartData.points.size == 1){
            val calculatedMax = lineChartData.points[0].value + (0.10f * lineChartData.points[0].value)
            val calculatedMin = lineChartData.points[0].value - (0.10f * lineChartData.points[0].value)

            y = (point.value - calculatedMin)  / (calculatedMax - calculatedMin)
        }


        return Offset(
            x = (x * drawableArea.width) + drawableArea.left,
            y = drawableArea.height - (y * drawableArea.height)
        )
    }

    fun <T> withProgress(
        index: Int,
        lineChartData: LineChartData2D<T>,
        transitionProgress: Float,
        showWithProgress: (progress: Float) -> Unit
    ) {
        val size = lineChartData.points.size
        val toIndex = (size * transitionProgress).toInt() + 1

        if (index == toIndex) {
            // Get the left over.
            val sizeF = lineChartData.points.size.toFloat()
            val perIndex = (1f / sizeF)
            val down = (index - 1) * perIndex

            showWithProgress((transitionProgress - down) / perIndex)
        } else if (index < toIndex) {
            showWithProgress(1f)
        }
    }

    fun <T> calculateLinePath(
        drawableArea: Rect,
        lineChartData: LineChartData2D<T>,
        transitionProgress: Float
    ): Path = Path().apply {
        var prevPointLocation: Offset? = null
        lineChartData.points.forEachIndexed { index, point ->
            withProgress(
                index = index,
                transitionProgress = transitionProgress,
                lineChartData = lineChartData
            ) { progress ->
                val pointLocation = calculatePointLocation(
                    drawableArea = drawableArea,
                    lineChartData = lineChartData,
                    point = point,
                    index = index
                )

                if (index == 0) {
                    moveTo(pointLocation.x, pointLocation.y)
                } else {
                    if (progress <= 1f) {
                        // We have to change the `dy` based on the progress
                        val prevX = prevPointLocation!!.x
                        val prevY = prevPointLocation!!.y

                        val x = (pointLocation.x - prevX) * progress + prevX
                        val y = (pointLocation.y - prevY) * progress + prevY

                        lineTo(x, y)
                    } else {
                        lineTo(pointLocation.x, pointLocation.y)
                    }
                }

                prevPointLocation = pointLocation
            }
        }
    }

    fun <T> calculateFillPath(
        drawableArea: Rect,
        lineChartData: LineChartData2D<T>,
        transitionProgress: Float
    ): Path = Path().apply {

        // we start from the bottom left
        moveTo(drawableArea.left, drawableArea.bottom)
        var prevPointX: Float? = null
        var prevPointLocation: Offset? = null
        lineChartData.points.forEachIndexed { index, point ->
            withProgress(
                index = index,
                transitionProgress = transitionProgress,
                lineChartData = lineChartData
            ) { progress ->
                val pointLocation = calculatePointLocation(
                    drawableArea = drawableArea,
                    lineChartData = lineChartData,
                    point = point,
                    index = index
                )

                if (index == 0) {
                    lineTo(drawableArea.left, pointLocation.y)
                    lineTo(pointLocation.x, pointLocation.y)
                } else {
                    if (progress <= 1f) {
                        // We have to change the `dy` based on the progress
                        val prevX = prevPointLocation!!.x
                        val prevY = prevPointLocation!!.y

                        val x = (pointLocation.x - prevX) * progress + prevX
                        val y = (pointLocation.y - prevY) * progress + prevY

                        lineTo(x, y)

                        prevPointX = x
                    } else {
                        lineTo(pointLocation.x, pointLocation.y)
                        prevPointX = pointLocation.x
                    }
                }

                prevPointLocation = pointLocation
            }
        }
        // We need to connect the line to the end of the drawable area
        prevPointX?.let { x ->
            lineTo(x, drawableArea.bottom)
            lineTo(drawableArea.right, drawableArea.bottom)
        } ?: lineTo(drawableArea.left, drawableArea.bottom)
    }
}
