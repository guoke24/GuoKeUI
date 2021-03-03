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
 * 接力型的多点触控
 * 本质是抢夺算法，只要有新的手指落下，就能抢占 view 的控制权，即以此来计算偏移量
 *
 * 前置知识补充，在多点触控中，每一次落下手指，都有一个 MotionEvent 事件；
 * MotionEvent 事件内，带有一个手指序列，即包含当前洛在屏幕上的手指，
 * 每个手指带有新 x，y，index，id 四个字段，其中 x，y 表示坐标；
 * index 即在序列中的下标，当有手指抬起，原有手指就会往前挪，index 随着新手指的起落而改变，有可能重复；
 * id 则是不会重复的，在需要唯一识别的时候用到 id；
 *
 * event.actionIndex 可以拿到当前手指的 index，再通过 event.getPointerId(index) 拿到 id
 *
 */
class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = getAvatar(resources, 200.dp.toInt())
  private var originalOffsetX = 0f
  private var originalOffsetY = 0f
  private var offsetX = 0f
  private var offsetY = 0f
  private var downX = 0f
  private var downY = 0f
  private var trackingPointerId = 0

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      // 手指移动，以追踪的那根手指为准
      // 所以后续的核心逻辑，就是要在 down 和 up 事件的时候，确认追踪手指的 id
      MotionEvent.ACTION_MOVE -> {
        val index = event.findPointerIndex(trackingPointerId)
        offsetX = event.getX(index) - downX + originalOffsetX
        offsetY = event.getY(index) - downY + originalOffsetY
        invalidate()
      }
      // 第一根手指的 down 事件，记录追踪手指的 id
      MotionEvent.ACTION_DOWN -> {
        updateOffsetByIndex(event,0)
      }
      // 第二根及以后的手指的 down 事件，记录追踪手指的 id
      MotionEvent.ACTION_POINTER_DOWN -> {
        val actionIndex = event.actionIndex
        updateOffsetByIndex(event,actionIndex)
      }
      // 非第一根手指抬起，则要找到新的追踪手指的id
      MotionEvent.ACTION_POINTER_UP -> {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)
        // 若抬起手指的 id，就是追踪的手指的 id，才需要重新选择跟踪的手指
        if (pointerId == trackingPointerId) {
          val newIndex = if (actionIndex == event.pointerCount - 1) {
            // 若抬起手指的 index，刚好是手指序列的最后一位，则新追踪的手指取倒数第二位
            event.pointerCount - 2
          } else {
            // 否则直接取手指序列的最后一位
            event.pointerCount - 1
          }
          // 这里的算法，就是说越后面按下的手指，越应该被选为跟踪的手指

          updateOffsetByIndex(event,newIndex)
        }
      }
    }
    return true
  }

  private fun updateOffsetByIndex(event:MotionEvent,index:Int){
    trackingPointerId = event.getPointerId(index)
    downX = event.getX(index)
    downY = event.getY(index)
    originalOffsetX = offsetX
    originalOffsetY = offsetY
  }

}