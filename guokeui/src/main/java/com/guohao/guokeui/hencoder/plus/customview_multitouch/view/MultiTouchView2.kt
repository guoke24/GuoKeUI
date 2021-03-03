package com.guohao.guokeui.hencoder.plus.customview_multitouch.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.guohao.guokeui.hencoder.plus.Utils.getAvatar
import com.guohao.guokeui.hencoder.plus.customview_multitouch.dp

/**
 * 配合型的多点触控
 *
 * 本质是平均值算法；
 * 核心理念就是：算出所有手指的平均触摸点，再以此触摸点计算偏移量
 * 要注意区分抬起手指
 *
 */
class MultiTouchView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = getAvatar(resources, 200.dp.toInt())
  private var originalOffsetX = 0f
  private var originalOffsetY = 0f
  private var offsetX = 0f
  private var offsetY = 0f
  private var downX = 0f
  private var downY = 0f

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val focusX: Float
    val focusY: Float
    var pointerCount = event.pointerCount
    var sumX = 0f
    var sumY = 0f
    val isPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP

    for (i in 0 until pointerCount) { // 累加不是抬起的手指的位置
      if (!(isPointerUp && i == event.actionIndex)) {
        sumX += event.getX(i)
        sumY += event.getY(i)
      }
    }

    if (isPointerUp) { // 抬起的手指不算入总数
      pointerCount--
    }

    focusX = sumX / pointerCount // 关键点：算出平均的焦点即可
    focusY = sumY / pointerCount

    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
        downX = focusX // 更新落点
        downY = focusY
        originalOffsetX = offsetX // 更新偏移量
        originalOffsetY = offsetY
      }
      MotionEvent.ACTION_MOVE -> {
        offsetX = focusX - downX + originalOffsetX // 计算偏移量
        offsetY = focusY - downY + originalOffsetY
        invalidate()
      }
    }
    return true
  }
}