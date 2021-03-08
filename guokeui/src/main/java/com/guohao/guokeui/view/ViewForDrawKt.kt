package com.guohao.guokeui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class ViewForDrawKt : View {

    var paint = Paint()
    var beginLef = 30f
    var beginTop = 30f
    var interval = 300f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // 正方形
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        customDraw(canvas)
    }

    protected fun customDraw(canvas: Canvas?){
        val rect = RectF() //RectF对象
        rect.left   = beginLef + 0 * interval //左边
        rect.top    = beginTop + 0 * interval //上边
        rect.right  = beginLef + 1 * interval //右边
        rect.bottom = beginTop + 1 * interval //下边
        paint.setColor(Color.CYAN) //设置画笔颜色
        canvas?.drawRect(rect,paint)

        paint.setAntiAlias(true) //设置画笔为无锯齿
        paint.setColor(Color.BLACK) //设置画笔颜色
        paint.setStrokeWidth(5.0.toFloat()) //线宽

        paint.style = Paint.Style.STROKE
        canvas!!.drawArc(rect, 225F, 90F, false, paint) //绘制圆弧

        moveRectToRight(rect)
        canvas.drawRect(rect,paint)
        canvas.drawArc(rect, 225F, 90F, true, paint) //绘制空心扇形

        moveRectToRight(rect)
        canvas.drawRect(rect,paint)
        paint.style = Paint.Style.FILL
        canvas.drawArc(rect, 225F, 90F, true, paint) //绘制实心扇形

        moveRectToBottom(rect)
        paint.style = Paint.Style.STROKE
        canvas.drawRect(rect,paint)
        paint.style = Paint.Style.FILL
        canvas.drawArc(rect, 225F, 90F, false, paint) //绘制新月

    }

    private fun moveRectToRight(rect : RectF){
        rect.left   = rect.left + interval //左边
        rect.top    = rect.top  //上边
        rect.right  = rect.right + interval //右边
        rect.bottom = rect.bottom //下边
    }

    private fun moveRectToBottom(rect : RectF){
        rect.left   = rect.left  //左边
        rect.top    = rect.top + interval //上边
        rect.right  = rect.right  //右边
        rect.bottom = rect.bottom + interval //下边
    }
}