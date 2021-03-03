package com.hencoder.drag.view


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlinx.android.synthetic.main.drag_up_down.view.*


class DragUpDownLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
  private var dragListener: ViewDragHelper.Callback = DragCallback()
  private var dragHelper: ViewDragHelper = ViewDragHelper.create(this, dragListener)
  private var viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return dragHelper.shouldInterceptTouchEvent(ev)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dragHelper.processTouchEvent(event)
    return true
  }

  override fun computeScroll() {
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this)
    }
  }

  internal inner class DragCallback : ViewDragHelper.Callback() {
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return child === draggedView
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return top
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      if (Math.abs(yvel) > viewConfiguration.scaledMinimumFlingVelocity) { // 速度大于阈值，用速度判断
        if (yvel > 0) {
          // 向下的速度，移到下面
          dragHelper.settleCapturedViewAt(0, height - releasedChild.height)
        } else {
          // 向上的速度，移到上面
          dragHelper.settleCapturedViewAt(0, 0)
        }
      } else { // 速度小于阈值，用位置判断
        if (releasedChild.top < height - releasedChild.bottom) {
          // 靠上，则放到顶部
          dragHelper.settleCapturedViewAt(0, 0)
        } else {
          // 靠下，则放到底部
          dragHelper.settleCapturedViewAt(0, height - releasedChild.height)
        }
      }
      postInvalidateOnAnimation()
    }
  }
}