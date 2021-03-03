package com.guohao.guokeui.view.flingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 一个反面教材：
 * 单独一个 view 内，
 * 只靠用 mScroller.startScroll 和 scrollTo 等函数，不改变 layout，会移动到 layout 之外的区域导致不可见
 */
public class TestView extends View {

    //惯性滑动速度追踪类
    private VelocityTracker velocityTracker;
    private float mDownX = 0;
    private float mDonwY = 0;
    private float move_x = 0;
    private float move_y = 0;
    private int finalX = 0;
    private int finalY = 0;
    private int xVelocity = 0;
    private int yVelocity = 0;

    private Paint mPaint;

    private Scroller mScroller;
    private OverScroller mOverScroller;

    private float circleRadius = 100f;
    private float centreX = circleRadius;
    private float centreY = circleRadius;

    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context mContext){
        mPaint = new Paint();
        mPaint.setTextSize(80);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(50);
        mScroller = new Scroller(mContext);
        mOverScroller = new OverScroller(mContext);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawText("我是中国人！", 0, 100, mPaint);
        canvas.drawCircle(
                centreX,
                centreY,
                circleRadius,
                mPaint
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //标志着第一个手指按下
                mDownX = x;//获取按下时x坐标值
                mDonwY = y;//获取按下时y坐标值

                //创建惯性滑动速度追踪类对象
                velocityTracker = VelocityTracker.obtain();



                break;
            case MotionEvent.ACTION_MOVE:
                //按住一点手指开始移动
                move_x = mDownX - x;//计算当前已经移动的x轴方向的距离
                move_y = mDonwY - y;//计算当前已经移动的y轴方向的距离

                //开始滚动动画
                //第一个参数：x轴开始位置
                //第二个参数：y轴开始位置
                //第三个参数：x轴偏移量
                //第四个参数：y轴偏移量
                if(mScroller.isFinished()){
                    mScroller.startScroll(finalX, finalY, (int) move_x, (int) move_y, 0);
                }
                invalidate();//目的是重绘view，是的执行computeScroll方法


                //将事件加入到VelocityTracker类实例中
                velocityTracker.addMovement(event);
                //计算1秒内滑动的像素个数
                velocityTracker.computeCurrentVelocity(1000);
                //X轴方向的速度
                xVelocity = (int) velocityTracker.getXVelocity();
                //Y轴方向的速度
                yVelocity = (int) velocityTracker.getYVelocity();

                break;
            case MotionEvent.ACTION_UP:

                //获取认为是fling的最小速率
                int mMinimumFlingVelocity=  ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity() / 10;


                if (Math.abs(xVelocity) >= mMinimumFlingVelocity || Math.abs(yVelocity) > mMinimumFlingVelocity) {
                    Log.d("yunchong", "触发惯性滑动");
                    mScroller.fling(getScrollX(), getScrollY(), -xVelocity, -yVelocity, -getWidth()+100, 0,  -getHeight()+100,  0);
                } else {//缓慢滑动不处理
                }


                finalX = mScroller.getFinalX();
                finalY = mScroller.getFinalY();

                velocityTracker.recycle();
                velocityTracker.clear();
                velocityTracker = null;

                break;


            case MotionEvent.ACTION_CANCEL:

                velocityTracker.recycle();
                velocityTracker.clear();
                velocityTracker = null;

                break;

        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){//判断滚动是否完成，true说明滚动尚未完成，false说明滚动已经完成
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());//将view直接移动到当前滚动的位置
            invalidate();//触发view重绘
        }
    }
}