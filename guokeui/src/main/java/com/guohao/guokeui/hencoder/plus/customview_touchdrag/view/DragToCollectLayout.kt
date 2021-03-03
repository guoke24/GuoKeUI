package com.hencoder.drag.view

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.drag_to_collect.view.*

class DragToCollectLayout(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
  private var dragStarter = OnLongClickListener { v ->

    val imageData = ClipData.newPlainText("name", v.contentDescription)
    // contentDescription 这个变量，是为残障人士准备的


    ViewCompat.startDragAndDrop(v, imageData, DragShadowBuilder(v), null, 0)
    // 开启后，会拿到整个视图树的 ViewRootImpl，所以，该树下的所有子 view，都能收到拖拽回调
    // 第二个参数，ClipData，可以跨进程拖拽！比如相册的照片拖到其他应用，锤子的一步
    // ClipData 这个数据，只有松手才可以拿到，毕竟跨进程嘛，比较重
    // 第三个参数，DragShadowBuilder，即拖起来的那个半透明的物体，就长 DragShadowBuilder 这样

    // startDragAndDrop 这个函数，主要关注拖起和放下两个动作，即在哪里拖起，在哪里放下，即表示数据从哪里到哪里去
    // DragShadowBuilder 起到了预览的作用，原图像不需要消失；
    // DragShadowBuilder 不是一个 view，而是一坨像素，绘制在屏幕的最顶部
    // 所以这个也可以成为数据拖拽

  }
  private var dragListener: OnDragListener = CollectListener()

  override fun onFinishInflate() {
    super.onFinishInflate()

    avatarView.setOnLongClickListener(dragStarter)
    logoView.setOnLongClickListener(dragStarter)

    collectorLayout.setOnDragListener(dragListener)
  }

  inner class CollectListener : OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
      when (event.action) {
        DragEvent.ACTION_DROP -> if (v is LinearLayout) {
          val textView = TextView(context)
          textView.textSize = 16f
          textView.text = event.clipData.getItemAt(0).text // 获取元数据
          v.addView(textView)
        }
      }
      return true
    }
  }
}
