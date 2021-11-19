package com.github.tehras.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.line.LineChartData2D
import com.github.tehras.charts.piechart.utils.toLegacyInt
import kotlin.math.max
import kotlin.math.roundToInt


class GridYAxisDrawer(
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black,
  private val labelValueFormatter: LabelFormatter = { value -> "%.1f".format(value) },
  private val axisLineThickness: Dp = 1.dp,
  private val axisLineColor: Color = Color.Black
) : YAxisDrawer {
  private val axisLinePaint = Paint().apply {
    isAntiAlias = true
    color = axisLineColor
    style = PaintingStyle.Stroke
  }
  private val textPaint = android.graphics.Paint().apply {
    isAntiAlias = true
    color = labelTextColor.toLegacyInt()
  }
  private val textBounds = android.graphics.Rect()

  override fun drawAxisLine(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect
  ) = with(drawScope) {
    val lineThickness = axisLineThickness.toPx()
    val x = drawableArea.right - (lineThickness / 2f)

    canvas.drawLine(
      p1 = Offset(
        x = x,
        y = drawableArea.top
      ),
      p2 = Offset(
        x = x,
        y = drawableArea.bottom
      ),
      paint = axisLinePaint.apply {
        strokeWidth = lineThickness
      }
    )
  }

  override fun drawAxisLabels(
    drawScope: DrawScope,
    canvas: Canvas,
    lineChartData: LineChartData2D<Any>,
    drawableArea: Rect,
    minValue: Float,
    maxValue: Float
  ) = with(drawScope) {
    val labelPaint = textPaint.apply {
      textSize = labelTextSize.toPx()
      textAlign = android.graphics.Paint.Align.RIGHT
    }
    val totalHeight = drawableArea.height
    val labelCount = 4

    for (i in 0..labelCount) {
      var value = minValue + (i * ((maxValue - minValue) / labelCount))
      if(lineChartData.points.size == 1){
        val calculatedMax = lineChartData.points[0].value + (0.10f * lineChartData.points[0].value)
        val calculatedMin = lineChartData.points[0].value - (0.10f * lineChartData.points[0].value)

        value = calculatedMin + (i * ((calculatedMax - calculatedMin) / labelCount))
      }

      val label = labelValueFormatter(value)
      val x =
        drawableArea.right - axisLineThickness.toPx() - (labelTextSize.toPx() / 2f)

      labelPaint.getTextBounds(label, 0, label.length, textBounds)

      val y =
        drawableArea.bottom - (i * (totalHeight / labelCount)) + (textBounds.height() / 2f)

      canvas.nativeCanvas.drawText(label, x, y, labelPaint)
    }

    for (i in 0..labelCount) {
      val y =
        drawableArea.bottom - (i * (totalHeight / labelCount))

      canvas.nativeCanvas.drawLine(drawableArea.width, y, canvas.nativeCanvas.width.toFloat() - drawableArea.width - 14.dp.toPx() , y, textPaint)
    }
  }
}