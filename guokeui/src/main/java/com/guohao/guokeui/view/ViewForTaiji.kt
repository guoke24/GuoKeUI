package com.guohao.guokeui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ViewForTaiji(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private var degree = 0f;
    private val rotateRunnable = RotateRunnable()

    init{
        paint.color = Color.parseColor("#000000")
        postOnAnimation(rotateRunnable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // 正方形
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centreX: Float = (width/2).toFloat()
        val centreY: Float = (height/2).toFloat()

        canvas.rotate(degree,centreX,centreY)

        // 太极圆所在的矩形的左顶点
        canvas.translate(centreX/2,centreY/2)

        // 画一个右半圆，黑色
        val radius1 = width.toFloat()/2f/2f;
        val rect = RectF(0f,0f, radius1*2, radius1*2);
        canvas.drawArc(rect,-90f,180f,true,paint)
        // 画一个左半圆，白色
        paint.color = Color.parseColor("#ffffff")
        canvas.drawArc(rect,90f,180f,true,paint)

        // 下半部分画一个小半圆，黑色
        val radius2 = radius1/2f;
        val rect2 = RectF(0f,0f, radius2*2, radius2*2);
        canvas.translate(radius2,radius1)
        paint.color = Color.parseColor("#000000")
        canvas.drawArc(rect2,90f,180f,true,paint)

        // 画下半部分的小白圆
        paint.color = Color.parseColor("#ffffff")
        canvas.drawCircle(radius2, radius2,radius2/4,paint)

        // 上半部分画一个小半圆，白色
        canvas.translate(0f,-radius1)
        paint.color = Color.parseColor("#ffffff")
        canvas.drawArc(rect2,-90f,180f,true,paint)

        // 画上半部分的小黑圆
        paint.color = Color.parseColor("#000000")
        canvas.drawCircle(radius2, radius2,radius2/4,paint)

    }

    inner class RotateRunnable : Runnable{
        override fun run() {
            degree += 2f
            postOnAnimation(rotateRunnable)
            invalidate()
        }
    }
}