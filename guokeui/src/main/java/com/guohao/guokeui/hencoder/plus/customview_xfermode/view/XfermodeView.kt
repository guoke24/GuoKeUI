package com.guohao.guokeui.hencoder.plus.customview_xfermode.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.guohao.guokeui.hencoder.plus.customview_xfermode.px

private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

class XfermodeView(context: Context?, attrs: AttributeSet?) :
  View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bounds = RectF(150f.px, 50f.px, 300f.px, 200f.px)
  private val circleBitmap = Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)
  private val squareBitmap = Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)

  var xfermode : PorterDuff.Mode? = null

  init {
    val canvas = Canvas(circleBitmap)
    paint.color = Color.parseColor("#D81B60")
    canvas.drawOval(50f.px, 0f.px, 150f.px, 100f.px, paint)
    paint.color = Color.parseColor("#2196F3")
    canvas.setBitmap(squareBitmap)
    canvas.drawRect(0f.px, 50f.px, 100f.px, 150f.px, paint)
  }

  override fun onDraw(canvas: Canvas) {

    // 注意，bounds 是一个公共的区域，即上述的 bounds = RectF(150f.px, 50f.px, 300f.px, 200f.px)
    val count = canvas.saveLayer(bounds, null)

    // 圆和方，在 bounds 这个区域下，又有着自己的顶点，记录在 circleBitmap 和 squareBitmap 内
    // 先画圆
    canvas.drawBitmap(circleBitmap, 150f.px, 50f.px, paint)
    paint.xfermode = PorterDuffXfermode(if(xfermode != null) xfermode else PorterDuff.Mode.DST_OVER)
    // 再画方
    canvas.drawBitmap(squareBitmap, 150f.px, 50f.px, paint)
    paint.xfermode = null
    canvas.restoreToCount(count)
  }
}