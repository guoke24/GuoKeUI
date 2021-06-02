package com.guohao.guokeui.hencoder.plus.customview_multitouch.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.guohao.guokeui.hencoder.plus.customview_multitouch.dp

/**
 * 各自为战型
 *
 * 本质是一对一独立处理。
 * 因此得为每一根手指保存一份记录，如下的 paths 集合，记录了每一根手指的路径
 */
class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var paths = SparseArray<Path>()

  init {
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 4.dp
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeJoin = Paint.Join.ROUND
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    // 画出所有的 path，若 path 被 remove，之前画的 path 的轨迹也会消失
    for (i in 0 until paths.size()) {
      val path = paths.valueAt(i)
      canvas.drawPath(path, paint)
    }
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      // 凡是有手指落下，新建 path 对象并移到落点，并以 id - path 的mao存储
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
        val actionIndex = event.actionIndex
        val path = Path()
        path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
        paths.append(event.getPointerId(actionIndex), path)
        invalidate()
      }
      // 凡是有一个手指移动，遍历所有手指，通过  id - path 取到 path，更新 path 的路径
      MotionEvent.ACTION_MOVE -> {
        for (i in 0 until paths.size()) {
          val pointerId = event.getPointerId(i)
          val path = paths.get(pointerId)
          path.lineTo(event.getX(i), event.getY(i))
        }
        invalidate()
      }
      // 凡是有一个手指抬起，清除其 path
      MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)
        paths.remove(pointerId)
        invalidate()
      }
    }
    return true
  }
}