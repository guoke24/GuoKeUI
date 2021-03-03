package com.guohao.guokeui.view.flingball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.doOnNextLayout

/**
 * 不依赖手势接口实现的拖动和滑动的小球，效果勉强出来，但问题很多
 *
 * 该小球希望被做成可以独立但添加到 其他容器中，都保证效果正常
 *
 */
class FlingBall2 : View {

    private var lastX = 0
    private var lastY = 0

    private val paint = Paint()
    private val circleRadius = 100F
    private var centreX = circleRadius
    private var centreY = circleRadius

    private var velocityTracker: VelocityTracker
    private lateinit var scroller : OverScroller
    private lateinit var flingRunner : FlingRunner

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?
    ) : super(context, attrs)
    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        velocityTracker = VelocityTracker.obtain()
        scroller = OverScroller(context)
        flingRunner = FlingRunner()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centreX = (w/2).toFloat()
        centreY = (h/2).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                postOnAnimation{
                    if(!scroller.computeScrollOffset()){
                        val offsetX = x - lastX
                        val offsetY = y - lastY
                        layout(
                            left + offsetX, top + offsetY,
                            right + offsetX, bottom + offsetY
                        )
                    }
                }
                // post 的方式，保证了：
                // 如果后续有 UP 事件，则此处的 if(!scroller.computeScrollOffset()) 判断会在 scroller.fling(...) 之后执行，
                // 也就是说保证了 UP 事件后不会再有 MOVE 事件的 layout 执行，避免了冲突
                // ---
                // layout -> setFrame -> invalidate -> invalidateInternal -> mParent.invalidateChild
                // scheduleTraversals -> post 一个 mTraversalRunnable 到消息队列 -> performTraversals -> 三大流程
            }
            MotionEvent.ACTION_UP -> {

                velocityTracker.computeCurrentVelocity(1000);

                //Toast.makeText(context,"速度 x = " + velocityTracker.xVelocity + "，速度 y = " + velocityTracker.yVelocity,Toast.LENGTH_LONG).show()
                //Log.i("guohao","速度 x = " + velocityTracker.xVelocity + "，速度 y = " + velocityTracker.yVelocity)

                var parent : ViewGroup = parent as ViewGroup

                // 惯性滑动偶尔能出来，但不自然，而且有时候会归原位
                scroller.fling(
                    left,
                    top,
                    velocityTracker.xVelocity.toInt(),
                    velocityTracker.yVelocity.toInt(),
                    0, // 不限制边界，为了做镜像模型和反弹
                    parent.width - width,
                    0,
                    parent.height - height
                )


                Log.i("guohao","ACTION_UP，left = " + left + "，top = " + top)
                postOnAnimation(flingRunner)
            }
        }
        return true
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

    inner class FlingRunner : Runnable{
        override fun run() {
            if (scroller.computeScrollOffset()) {

                Log.i("guohao","computeScrollOffset，scroller.currX = " + scroller.currX + "，scroller.currY = " + scroller.currY)

                // 这个写法可以，问题不在这
                layout(
                    scroller.currX, scroller.currY,
                     scroller.currX + width, scroller.currY + height
                )

                // 这个写法不行
//                left = scroller.currX
//                top = scroller.currY
//                right = scroller.currX + width
//                bottom = scroller.currY + height
//                invalidate()

                postOnAnimation(this)
            }
        }
    }


}