package com.hencoder.drag.view

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.util.*

private const val COLUMNS = 2
private const val ROWS = 3

/**
 * View 的 startDrag + OnDragListener
 *
 * startDrag 和 ViewCompat.startDragAndDrop 类似，关注的是拖起和放下的位置；
 * 参数解释：
 * ClipData，可以跨进程拖拽！只有松手才可以拿到，毕竟跨进程嘛，比较重
 * DragShadowBuilder，即拖起来的那个半透明的物体，就长 DragShadowBuilder 这样
 * localState，本地数据，可以随时拿到
 *
 * startDrag 开始拖动的时候，整个 ViewTree 的子View，都能收到拖动事件
 *
 * OnDragListener 可以接收所在视图树下的拖动事件，包括六种状态：
 * ACTION_DRAG_STARTED：当拖放操作开始时，View 收到的事件。
 * ACTION_DRAG_ENTERED：拖拽进入了某个 v 的区域内的时候
 * ACTION_DRAG_LOCATION：当ACTION_DRAG_ENTERED之后，拖影仍在 View 的边界内，就发出这个信号。getX() 和 getY() 函数可以获得，Drag 操作在 View 内的坐标。
 *                       View 接收到 ACTION_DRAG_ENTERED 事件始终在接受到 ACTION_DRAG_LOCATION 事件之前。
 * ACTION_DROP：用户释放拖放时，拖放点在某个 View 内部，就会发送 ACTION_DROP 事件。该 View 可以通过调用 getClipData() 来检索 DragEvent 中的数据
 * ACTION_DRAG_EXITED：当拖放操作离开某个 View 的边界框时的，该 View 收到的事件。
 * ACTION_DRAG_ENDED：一般 View 收到 ACTION_DRAG_STARTED 的事件后，都会在整个 DragEvent 事件结束时收到 ACTION_DRAG_ENDED 事件；
 *                    除非，在拖放事件结束时，次 View 不可见。
 *
 */
class DragListenerGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private var dragListener: OnDragListener = HenDragListener()
  private var draggedView: View? = null
  private var orderedChildren: MutableList<View> = ArrayList()

  init {
    isChildrenDrawingOrderEnabled = true
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    for (child in children) {
      orderedChildren.add(child) // 初始化位置
      child.setOnLongClickListener { v ->
        draggedView = v
        v.startDrag(null, DragShadowBuilder(v), v, 0) // 开启拖拽，自动跟着手指走，不用写额外的代码
        // 第一个参数，只有松手的时候可以拿到
        // 第三个参数，随时随地都能拿到，即 onDrag 回调函数里的 event.localState，这里传了 v 本身，表示被拖拽的 view
        false
      }
      child.setOnDragListener(dragListener) // 如需监听拖拽的过程，才需要设置
    }
  }

//  override fun onDragEvent(event: DragEvent?): Boolean {
//    return super.onDragEvent(event)
//  }

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
      child.layout(0, 0, childWidth, childHeight)
      child.translationX = childLeft.toFloat()
      child.translationY = childTop.toFloat()
      // 为何不在 layout 函数中直接指定四边的位置？
      // 为了好做动画！
    }
  }

  // 接收拖拽的状态，这里有 6 个 view 都会收到回调通知，即 一对多 模型
  private inner class HenDragListener : OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
      when (event.action) {
        // 有一个 view 被拖起来了
        DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {
          // event.localState 即 startDrag 函数的第三个参数，这里表示此处收到回调的 v 就是被拖拽的 view
          v.visibility = View.INVISIBLE
        }
        // 拖拽进入了 v 的区域内
        DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) {
          // 这个 v 是谁？当时监听回调的那 6 个view；
          sort(v)
        }
        DragEvent.ACTION_DRAG_EXITED -> {
        }
        DragEvent.ACTION_DRAG_ENDED -> if (event.localState === v) {
          v.visibility = View.VISIBLE
        }
      }
      return true
    }
  }

  // 对 targetView，即被入侵区域的 view，重排列
  private fun sort(targetView: View) {
    // 两步走：第一，插队；第二，重排列

    // 第一，插队
    var draggedIndex = -1
    var targetIndex = -1
    for ((index, child) in orderedChildren.withIndex()) {
      if (targetView === child) {
        targetIndex = index
      } else if (draggedView === child) {
        draggedIndex = index
      }
    }
    orderedChildren.removeAt(draggedIndex)
    orderedChildren.add(targetIndex, draggedView!!)// 被拖拽的 view，插入到目标 view 的位置，其他的 view 则往后挪

    // 第二，重排列
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in orderedChildren.withIndex()) { // 遍历排列
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.animate()
        .translationX(childLeft.toFloat())
        .translationY(childTop.toFloat())
        .setDuration(150)
    }
  }
}
