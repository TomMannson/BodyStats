package com.github.tehras.charts.line

data class LineChartData(
    val points: List<Point>,
    /** This is percentage we pad yValue by.**/
    val padBy: Float = 20f,
    val startAtZero: Boolean = false
) {
    init {
        require(padBy in 0f..100f)
    }

    private val yMinMax: Pair<Float, Float>
        get() {
            val min = points.minByOrNull { it.value }?.value ?: 0f
            val max = points.maxByOrNull { it.value }?.value ?: 0f

            return min to max
        }

    internal val maxYValue: Float =
        yMinMax.second + ((yMinMax.second - yMinMax.first) * padBy / 100f)
    internal val minYValue: Float
        get() {
            return if (startAtZero) {
                0f
            } else {
                yMinMax.first - ((yMinMax.second - yMinMax.first) * padBy / 100f)
            }
        }
    internal val yRange = maxYValue - minYValue

    data class Point(val value: Float, val label: String)
}

class Point2D<T>(
    val yValue: Float,
    val xValue: T
)

interface Converter<T> {

    fun convertValueToScalar(notScalar: T): Float
}

data class LineChartData2D<T>(
    val points: List<Point<T>>,
    val converter: (T) -> Float,
    val displayX: (T) -> String,
    /** This is percentage we pad yValue by.**/
    val padBy: Float = 20f,
    val startAtZero: Boolean = false,
    val minY: Float = 0f,
    val maxY: Float = 0f,
    val minX: T,
    val maxX: T,
    val rangeOfXValues: PointRange<T> = PointRange(minX, maxX, 1)

) {
    init {
        require(padBy in 0f..100f)
    }

    private val yMinMax: Pair<Float, Float>
        get() {
            val min = points.minByOrNull { it.value }?.value ?: minY
            val max = points.maxByOrNull { it.value }?.value ?: maxY

            return min to max
        }

    internal val maxYValue: Float =
        yMinMax.second + ((yMinMax.second - yMinMax.first) * padBy / 100f)

    internal val minYValue: Float
        get() {
            return if (startAtZero) {
                0f
            } else {
                yMinMax.first - ((yMinMax.second - yMinMax.first) * padBy / 100f)
            }
        }
    internal val yRange = maxYValue - minYValue

    private val xMinMax: Pair<Float, Float>
        get() {
            val min = points.minByOrNull { converter(it.xValue) }?.xValue ?: rangeOfXValues.min
            val max = points.maxByOrNull { converter(it.xValue) }?.xValue ?: rangeOfXValues.max

            return converter(min) to converter(max)
        }

    internal val maxXValue: Float =
        yMinMax.second + ((yMinMax.second - yMinMax.first) * padBy / 100f)

    internal val minXValue: Float
        get() {
            return if (startAtZero) {
                0f
            } else {
                yMinMax.first - ((yMinMax.second - yMinMax.first) * padBy / 100f)
            }
        }
    internal val xRange = maxYValue - minYValue

    data class Point<T>(val value: Float, val label: String, val xValue: T)
    data class PointRange<T>(val min: T, val max: T, val steps: Int)
}