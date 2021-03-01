package com.guohao.guokeui.view.flingball

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 单纯的添加两个小球
 */
class FlingBallLayout2(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var ball : FlingBall? = null
    private var ball2 : FlingBall2? = null

    init {

        ball = FlingBall(context)
        val params2 = ViewGroup.LayoutParams(200, 200)
        ball?.layoutParams = params2
        addView(ball)

        ball2 = FlingBall2(context)
        val params3 = ViewGroup.MarginLayoutParams(200, 200) // 带边距的布局参数，是纯布局参数的子类
        params3.setMargins(0,200,0,0)
        ball2?.layoutParams = params3
        addView(ball2)

    }
}