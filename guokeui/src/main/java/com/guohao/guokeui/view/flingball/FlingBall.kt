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
import android.view.ViewGroup
import android.widget.OverScroller

/**
 *
 * 该小球希望被做成可以独立但添加到 其他容器中，都保证效果正常
 *
 * 依赖手势接口实现的，目前拖动和惯性滑动的触发冲突
 *
 * 关掉拖动，惯性滑动触发正常
 * 开启拖动，试了很多种写法，都冲突。。。待完善
 *
 */
class FlingBall : View {

    private var lastX = 0
    private var lastY = 0
    private var parentWidth = 0
    private var parentHeight = 0


    private val paint = Paint()
    private val circleRadius = 100F
    private var centreX = circleRadius
    private var centreY = circleRadius

    private lateinit var gestureListener : GestureDetector
    private val flingListener = BallGestureListener()

    private lateinit var scroller : OverScroller
    private lateinit var flingRunner : FlingRunner

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init(){
        paint.color = Color.parseColor("#990033")
        gestureListener = GestureDetector(context,flingListener)
        flingRunner = FlingRunner()
        scroller = OverScroller(context)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // 这样写还是影响滑动
//        val x = event.x.toInt()
//        val y = event.y.toInt()
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                lastX = x
//                lastY = y
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val offsetX = x - lastX
//                val offsetY = y - lastY
//                layout(
//                    left + offsetX, top + offsetY,
//                    right + offsetX, bottom + offsetY
//                )
//            }
//        }

        return gestureListener.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centreX = (w/2).toFloat()
        centreY = (h/2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        parentHeight = MeasureSpec.getSize(heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(
            centreX,
            centreY,
            circleRadius,
            paint
        )
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
            // 写法一：超出了 layout 的边界，就看不到了
//            centreX -= distanceX
//            centreY -= distanceY
//            invalidate()

            // 写法二：超出了 layout 的边界，就看不到了
            //scrollBy((distanceX).toInt(), (distanceY).toInt())

            // 通过这个回调实现的拖动，会有晃动，而且不跟手的情况
//            layout((left - distanceX).toInt(), (top - distanceY).toInt(),
//                (right - distanceX).toInt(), (bottom - distanceY).toInt()
//            )

            // 写法三：自己算距离，拖动跟手，但是影响滑动
//            var x = event.x - down.x
//            var y = event.y - down.y
//            layout((left + x).toInt(), (top + y).toInt(),
//                (right + x).toInt(), (bottom + y).toInt()
//            )

            // 写法四：滚动父控件，会晃动，也会影响到滑动
//            var parent : View = parent as View
//            parent.scrollBy(distanceX.toInt(), distanceY.toInt())

            // 写法五：不晃动，也会影响到滑动；若直接用 distanceX 也会晃动
            var x = event.x - down.x
            var y = event.y - down.y
            setX(getX() + x)
            setY(getY() + y)

            return false
        }

        override fun onFling(
            down: MotionEvent,
            event: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            var parent : ViewGroup = parent as ViewGroup

            scroller.fling(
                left, top, velocityX.toInt()/2, velocityY.toInt()/2,
                0, // 不限制边界，为了做镜像模型和反弹
                parent.width - width,
                0,
                parent.height - height
            )
            postOnAnimation(flingRunner)
            return true
        }
    }

    // 问题一：通过 layout 函数改变了一个 view 的 left、top、right、bottom，会改变这个 view 的坐标系吗？
    // 目前看来不会

    inner class FlingRunner : Runnable{
        override fun run() {
            if (scroller.computeScrollOffset()) {

                layout(
                    scroller.currX, scroller.currY,
                    scroller.currX + width, scroller.currY + height
                )

                postOnAnimation(this)
            }
        }
    }
}