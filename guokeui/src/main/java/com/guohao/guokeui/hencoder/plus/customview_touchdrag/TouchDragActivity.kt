package com.guohao.guokeui.hencoder.plus.customview_touchdrag

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.guohao.guokeui.R
import com.hencoder.drag.view.DragListenerGridView
import com.hencoder.drag.view.DragUpDownLayout

class TouchDragActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.drag_up_down)            // 上下拖动，回弹
//    setContentView(R.layout.drag_to_collect)         //
//    setContentView(R.layout.drag_helper_grid_view)   // 6宫格，拖动
//    setContentView(R.layout.drag_listener_grid_view) // 6宫格，拖动并自适应

    // 手动添加子view，如下
//    val view = DragUpDownLayout(this,null)
//    val child = View(this)
//    child.id = R.id.draggedView
//    val params = ViewGroup.LayoutParams(MATCH_PARENT,200) // 200 未转成dp的值
//    child.layoutParams = params
//    child.background = ColorDrawable(Color.parseColor("#388E3C"))
//    view.addView(child)
//    setContentView(view, ViewGroup.LayoutParams(MATCH_PARENT,MATCH_PARENT))
    // 效果等价：
    // setContentView(R.layout.drag_up_down)
  }
}