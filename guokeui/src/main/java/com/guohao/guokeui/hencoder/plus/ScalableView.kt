package com.guohao.guokeui.hencoder.plus

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ScalableView : View {

    private val paint = Paint();

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var w : Float = width.toFloat()
        var h : Float = height.toFloat()

        paint.color = Color.parseColor("#000000")
        paint.textSize = 60F
        canvas?.drawText("hello",w/2,h/2,paint)
    }

}