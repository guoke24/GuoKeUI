package com.guohao.guokeui.view.flingball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller

/**
 * 滑动的小球，没有反弹功能
 */
class FlingBallLayout3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
//    private var circleX = 100F;
//    private var circleY = 100F;
    private var circleRadius = 100F;

    private var gestureListener : GestureDetector
    private val flingListener = BallGestureListener()
    private var scroller : OverScroller
    private var flingRunner : FlingRunner

    private var offsetX = circleRadius
    private var offsetY = circleRadius

    init{
        gestureListener = GestureDetector(context,flingListener)
        scroller = OverScroller(context)
        flingRunner = FlingRunner()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureListener.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.parseColor("#990066")
        canvas.drawCircle(offsetX, offsetY, circleRadius,paint)
    }

    inner class BallGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            down: MotionEvent,
            event: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.i("guohao","onScroll " + distanceX + " " + distanceY)
            offsetX -= distanceX
            offsetY -= distanceY
            if(offsetX < circleRadius) offsetX = circleRadius
            if(offsetY < circleRadius) offsetY = circleRadius
            if(offsetX > width - circleRadius ) offsetX = width - circleRadius
            if(offsetY > height - circleRadius ) offsetY = height - circleRadius
            invalidate()
            return false
        }

        override fun onFling(
            down: MotionEvent,
            event: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // 这个惯性滑动，速度是会衰减的，不会一直运动下去
            scroller.fling(
                offsetX.toInt(), offsetY.toInt(), velocityX.toInt()/2, velocityY.toInt()/2,
                circleRadius.toInt(),
                (width - circleRadius).toInt(),
                circleRadius.toInt(),
                (height - circleRadius).toInt()
            )
            postOnAnimation(flingRunner)
            return false
        }
    }

    inner class FlingRunner : Runnable{
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                postOnAnimation(this)
            }
        }
    }

}