package com.guohao.guokeui.hencoder.plus.layoutlayout.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
  private val childrenBounds = mutableListOf<Rect>()

  /**
   * 测量的关键逻辑：
   * 逐个测量，累加宽高，实现从左到右，从上到下的布局
   * 在测量阶段，就能确定每个 子View 的位置，将其记录在一个数组中；
   * 然后在布局阶段，填入，即可
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var widthUsed = 0
    var heightUsed = 0
    var lineWidthUsed = 0
    var lineMaxHeight = 0
    val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
    val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
    for ((index, child) in children.withIndex()) {

      // 测量子 view
      measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)

      if (specWidthMode != MeasureSpec.UNSPECIFIED &&
        lineWidthUsed + child.measuredWidth > specWidthSize) { // 换行，重新测量
        lineWidthUsed = 0 // 从左边开始
        heightUsed += lineMaxHeight // 累加上一行的高
        lineMaxHeight = 0 // 单行最大高度清零
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
      }

      if (index >= childrenBounds.size) {
        childrenBounds.add(Rect())
      }

      val childBounds = childrenBounds[index]
      childBounds.set(lineWidthUsed, heightUsed, lineWidthUsed + child.measuredWidth, heightUsed + child.measuredHeight)
      // 设置每一个子 view 对应的背景矩形的边

      lineWidthUsed += child.measuredWidth // 累加当前子 view 的宽度
      widthUsed = max(widthUsed, lineWidthUsed) // 更新单行已使用的宽度
      lineMaxHeight = max(lineMaxHeight, child.measuredHeight) // 更新单行的最大高度
    }
    val selfWidth = widthUsed
    val selfHeight = heightUsed + lineMaxHeight
    setMeasuredDimension(selfWidth, selfHeight) // 测量阶段最终要调用的函数
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    for ((index, child) in children.withIndex()) {
      val childBounds = childrenBounds[index]
      // 按照 childrenBounds 数组的记录，摆放个子 view
      child.layout(childBounds.left, childBounds.top, childBounds.right, childBounds.bottom)
    }
  }

//  override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
//    return MarginLayoutParams(context, attrs)
//  }
}