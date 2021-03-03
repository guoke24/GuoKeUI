package com.guohao.guokeui.hencoder.plus

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ControlScrollViewPager: ViewPager {

    private var isCanScroll = true;

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    public fun setCanScroll(isCanScroll:Boolean){
        this.isCanScroll = isCanScroll;
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
