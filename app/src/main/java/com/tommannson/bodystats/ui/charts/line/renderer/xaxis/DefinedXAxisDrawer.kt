package com.github.tehras.charts.line.renderer.xaxis

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

class DefinedXAxisDrawer<DATATYPE>(
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black,
  /** 1 means we draw everything. 2 means we draw every other, and so on. */
  private val labelRatio: Int = 1,
  private val axisLineThickness: Dp = 1.dp,
  private val axisLineColor: Color = Color.Black,
  private val chartLabels: List<DATATYPE>,
) : XAxisDrawer {
  private val axisLinePaint = Paint().apply {
    isAntiAlias = true
    color = axisLineColor
    style = PaintingStyle.Stroke
  }

  private val textPaint = android.graphics.Paint().apply {
    isAntiAlias = true
    color = labelTextColor.toLegacyInt()
  }

  override fun requiredHeight(drawScope: DrawScope): Float {
    return with(drawScope) {
      (3f / 2f) * (labelTextSize.toPx() + axisLineThickness.toPx())
    }
  }

  override fun drawAxisLine(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect
  ) {
    with(drawScope) {
      val lineThickness = axisLineThickness.toPx()
      val y = drawableArea.top + (lineThickness / 2f)

      canvas.drawLine(
        p1 = Offset(
          x = drawableArea.left,
          y = y
        ),
        p2 = Offset(
          x = drawableArea.right,
          y = y
        ),
        paint = axisLinePaint.apply {
          strokeWidth = lineThickness
        }
      )

      with(drawScope) {
        val labelPaint = textPaint.apply {
          textSize = labelTextSize.toPx()
          textAlign = android.graphics.Paint.Align.CENTER
        }

        val labelIncrements = drawableArea.width / (chartLabels.size - 1)
        chartLabels.forEachIndexed { index, label ->
          if (index.rem(labelRatio) == 0) {
            val x = drawableArea.left + (labelIncrements * (index))
            val y = drawableArea.bottom

            canvas.nativeCanvas.drawLine(x, drawableArea.top, x, 0f, labelPaint)
          }
        }
      }
    }
  }

  override fun drawAxisLabels(
    data: LineChartData2D<Any>,
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect,
    labels: List<String>
  ) {

    with(drawScope) {
      val labelPaint = textPaint.apply {
        textSize = labelTextSize.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
      }

      val labelIncrements = (drawableArea.width) / (chartLabels.size - 1)
      chartLabels.forEachIndexed { index, label ->
        if (index.rem(labelRatio) == 0) {
          val x = (drawableArea.left ) + (labelIncrements * (index))
          val y = drawableArea.bottom

          canvas.nativeCanvas.drawText(data.displayX(label as Any), x, y, labelPaint)
        }
      }
    }
  }
}