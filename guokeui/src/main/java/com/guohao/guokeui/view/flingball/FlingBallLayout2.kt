package com.guohao.guokeui.view.flingball

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 单纯的添加三个小球
 * 需求是：拖动、惯性滑动逻辑写在小球内部
 * 小球可以添加到任意界面，目前只有 ball2 基本满足需求
 */
class FlingBallLayout2(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var ball : FlingBall? = null
    private var ball2 : FlingBall2? = null
    private var ball3 : FlingBall3? = null


    init {

        // GestureDetector + layout 实现拖动会晃，fing 倒是可以
        ball = FlingBall(context)
        val params = ViewGroup.LayoutParams(200, 200)
        ball?.layoutParams = params
        addView(ball)

        // layout + VelocityTracker + post 等方式，基本可以
        ball2 = FlingBall2(context)
        val params2 = ViewGroup.MarginLayoutParams(200, 200) // 带边距的布局参数，是纯布局参数的子类
        params2.setMargins(0,200,0,0)
        ball2?.layoutParams = params2
        addView(ball2)

        ball3 = FlingBall3(context)
        val params3 = ViewGroup.MarginLayoutParams(200, 200) // 带边距的布局参数，是纯布局参数的子类
        params3.setMargins(0,400,0,0)
        ball3?.layoutParams = params3
        addView(ball3)

        // scrollTo 实现不行
//        var testView = TestView(context)
//        val params4 = ViewGroup.MarginLayoutParams(200, 200) // 带边距的布局参数，是纯布局参数的子类
//        params4.setMargins(0,400,0,0)
//        testView.layoutParams = params4
//        addView(testView)
    }
}