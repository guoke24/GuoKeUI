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
 * 滑动的小球，带反弹功能
 *
 * 依靠手势接口实现，效果还可以
 *
 */
class FlingBallLayout4(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val circleRadius = 100F

    private var gestureListener : GestureDetector
    private val flingListener = BallGestureListener()
    private var scroller : OverScroller
    private var flingRunner : FlingRunner

    private var offsetX = circleRadius
    private var offsetY = circleRadius
    private var povitWidth = 0
    private var povitHeight = 0

    init{
        gestureListener = GestureDetector(context,flingListener)
        scroller = OverScroller(context)
        flingRunner = FlingRunner()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        povitWidth = (w - 2 * circleRadius).toInt()
        povitHeight = (h - 2 * circleRadius).toInt()
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
                Int.MIN_VALUE, // 不限制边界，为了做镜像模型和反弹
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE
            )
            postOnAnimation(flingRunner)
            return false
        }
    }

    inner class FlingRunner : Runnable{
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetY = fixOffsetY(scroller.currY.toFloat())
                offsetX = fixOffsetX(scroller.currX.toFloat())
                invalidate()
                postOnAnimation(this)
            }
        }
    }

    // 计算反弹其实是一个镜像模型
    private fun fixOffsetX(temp:Float):Float{
        var tx = temp
        var res = 0f

        if(tx < circleRadius){ // 说明出了左边界

            tx -= circleRadius // 取超过边界净值，便于计算
            tx *= -1 // 负数变正数

            var screenIndex = tx.toInt() / povitWidth // 哪个镜像屏幕
            var screenOffset = tx.toInt() % povitWidth // 偏移了多少

            if(screenIndex % 2 ==1){ // 奇数，说明由右向左移动
                res = povitWidth + circleRadius - screenOffset
            }else{  // 偶数或0，说明由左向右移动
                res = circleRadius + screenOffset
            }

        }else if(tx > circleRadius + povitWidth ){ // 说明出了右边界
            tx -= circleRadius + povitWidth // 取超过边界净值，便于计算

            var screenIndex = tx.toInt() / povitWidth // 哪个镜像屏幕
            var screenOffset = tx.toInt() % povitWidth // 偏移了多少

            if(screenIndex % 2 ==1){ // 奇数，说明由左向右移动
                res = circleRadius + screenOffset
            }else{  // 偶数或0， 说明由右向左移动
                res = povitWidth + circleRadius - screenOffset
            }
        }
        else{ // 暂时说明没边界
            res = temp
        }

        return res
    }

    private fun fixOffsetY(temp:Float):Float{
        var tx = temp
        var res = 0f

        if(tx < circleRadius){ // 说明出了上边界

            tx -= circleRadius // 取超过边界净值，便于计算
            tx *= -1 // 负数变正数

            var screenIndex = tx.toInt() / povitHeight // 哪个镜像屏幕
            var screenOffset = tx.toInt() % povitHeight // 偏移了多少

            if(screenIndex % 2 ==1){ // 奇数，说明向上移动
                res = povitHeight + circleRadius - screenOffset
            }else{  // 偶数或0，说明向下移动
                res = circleRadius + screenOffset
            }

        }else if(tx > circleRadius + povitHeight ){ // 说明出了下边界
            tx -= circleRadius + povitHeight // 取超过边界净值，便于计算

            var screenIndex = tx.toInt() / povitHeight // 哪个镜像屏幕
            var screenOffset = tx.toInt() % povitHeight // 偏移了多少

            if(screenIndex % 2 ==1){ // 奇数，说明向下移动
                res = circleRadius + screenOffset
            }else{  // 偶数或0， 说明向上移动
                res = povitHeight + circleRadius - screenOffset
            }
        }
        else{ // 暂时说明没边界
            res = temp
        }

        return res
    }


}