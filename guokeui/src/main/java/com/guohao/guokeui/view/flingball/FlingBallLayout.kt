package com.guohao.guokeui.view.flingball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView

class FlingBallLayout : ViewGroup {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        // 动态添加 view
        val textView = TextView(context)
        val params = LayoutParams(259,180)
        textView.text = "hello"
        textView.textSize = 45f
        textView.setTextColor(Color.parseColor("#000000"))
        textView.layoutParams = params
        textView.background = ColorDrawable(Color.parseColor("#666666"))
        addView(textView)
        //invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for(i in 0 until childCount){
            val child = getChildAt(i)
            child.measure(
                MeasureSpec.makeMeasureSpec(child.layoutParams.width,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(child.layoutParams.height,MeasureSpec.EXACTLY)
            )
            //child.measure( widthMeasureSpec, heightMeasureSpec )
        }
    }

    // 需要定义自己和子view的布局
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for(i in 0 until childCount){
            val child = getChildAt(i)
            child.layout(0,0,child.measuredWidth,child.measuredHeight)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(i in 0 until childCount){
            val child = getChildAt(i)
            child.draw(canvas)
        }
    }
}