package com.hencoder.drag.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper

private const val COLUMNS = 2
private const val ROWS = 3

/**
 * ViewDragHelper 一个关注 view 本身拖动的辅助类，需在 ViewGroup 内使用；
 *
 * ViewDragHelper 的 create 函数：
 * 参数1 就是 ViewGroup；
 * 这样可以免去自定义子 view 的 onTouch 函数
 *
 * 参数2 是回调，ViewDragHelper.Callback() 的拓展类；有十余个接口，表示可以收到十余种回调；
 * 主要有：
 * tryCaptureView(): 该函数是必须实现的，表示：你的手是不是要拖动这个view
 * clampViewPositionHorizontal(): clamp，钳子，这里表示对拖动的水平距离的限制
 * clampViewPositionVertical(): 同上，垂直距离的限制
 * onViewCaptured(): 当 view 被 拖起来的时候调用
 * onViewPositionChanged(): 当 view 的坐标改变的时候调用
 * onViewReleased(): 当 view 被 放下来的时候调用
 *
 * ---
 * ViewDragHelper 的 settleCapturedViewAt 函数：类似于 Scroller 的 fling 函数；
 * ViewDragHelper 的 continueSettling 函数，类似于 Scroller 的 computeScrollOffset 函数；
 * 不同点是，ViewDragHelper 内部持有 captureView 的引用，可以隐式改动其 left，top 的值，
 * 所以，在 settleCapturedViewAt 函数只要为 left、top 设置一个最终值，
 * continueSettling 函数就能计算 captureView 的 left，top 的当前值到最终值的变化，以此来做动画
 *
 */
class DragHelperGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private var dragHelper = ViewDragHelper.create(this, DragCallback())

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val specWidth = MeasureSpec.getSize(widthMeasureSpec)
    val specHeight = MeasureSpec.getSize(heightMeasureSpec)
    val childWidth = specWidth / COLUMNS
    val childHeight = specHeight / ROWS
    measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
    setMeasuredDimension(specWidth, specHeight)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in children.withIndex()) {
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
    }
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return dragHelper.shouldInterceptTouchEvent(ev)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dragHelper.processTouchEvent(event) // 跟 GestureListener 类似，外挂的侦测器
    return true
  }

  override fun computeScroll() {
    // continueSettling 函数内会对 mCapturedView 的 left 和 top 更新
    // 然后 this 指向的view 重绘，它的子 view 也会重绘
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this)
      // 会导致 layout 吗？还是仅仅调用 draw ？
      // 仅仅引发本身以及它的子view的重绘
    }
  }

  // 拖动回调
  private inner class DragCallback : ViewDragHelper.Callback() {
    var capturedLeft = 0f
    var capturedTop = 0f

    // 该函数是必须实现的，表示：你的手是不是要拖动这个view
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return true
    }

    // 钳子？拖动后触发
    // left 即水平的拖动偏移量，
    // return 即 view 的实际偏移量，
    // 若返回 left，即拖动多少就移动多少；若返回 0 ，即拖不动
    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
      return left
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return top
    }

    // 当 view 被 拖起来的时候调用
    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
      capturedChild.elevation = elevation + 1 // 纵向高度加1，可以盖住别人
      capturedLeft = capturedChild.left.toFloat() // 记录被拖起view的初始位置，待会要挪回去
      capturedTop = capturedChild.top.toFloat()
    }

    // 当 view 的坐标改变的时候调用，
    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
    }

    // 当 view 被 放下来的时候调用
    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      // 开始挪回初始位置的操作
      // 跟 overScroller 的用法差不多
      // dragHelper 在拖动 captureView 时，就会改变它的 left 和 top 的值，这里则是设置 left 和 top 的最终值，然后通过 computeScroll 函数慢慢计算
      dragHelper.settleCapturedViewAt(capturedLeft.toInt(), capturedTop.toInt())
      postInvalidateOnAnimation() // 该函数的执行，会导致 computeScroll 函数的执行
    }
  }
}
