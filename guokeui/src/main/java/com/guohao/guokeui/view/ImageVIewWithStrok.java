package com.guohao.guokeui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 增加了功能：边框有多个小线段在跑动
 */
public class ImageVIewWithStrok extends androidx.appcompat.widget.AppCompatImageView {

    Paint mRedPaint;
    Paint mBluePaint2;
    Paint mWhitePaint3;
    Handler mHandler;

    float spotX = 0; // 锚点
    float spotY = 0;
    float interval ; // 线段的间隔，也是线段的长
    float beginX = 0; // 开始位置
    float beginY = 0;
    float strokeWidth ; // 线段的宽度
    float forward ; // 线段的宽度

    boolean isMove = false;

    float defaultWidth ;
    float defaultHeight ;


    public ImageVIewWithStrok(@NonNull Context context) {
        this(context, null);
    }

    public ImageVIewWithStrok(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageVIewWithStrok(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mHandler = new Handler(context.getMainLooper());// 在主线程中排队
        mRedPaint = new Paint();
        mBluePaint2 = new Paint();
        mWhitePaint3 = new Paint();
        mRedPaint.setColor(Color.parseColor("#ff0000"));
        mBluePaint2.setColor(Color.parseColor("#0000ff"));
        mWhitePaint3.setColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // -1 是 match parent
        // -2 是 wrap content
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.i("guohao","onMeasure width = " + width);
        Log.i("guohao","onMeasure widthSpecMode = " + widthSpecMode);
        Log.i("guohao","onMeasure height = " + height);
        Log.i("guohao","onMeasure heightSpecMode = " + heightSpecMode);


        // 给一个默认值？什么条件下？没有精确值的之后？
        //setMeasuredDimension();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        strokeWidth = getWidth() / 55;
        forward = strokeWidth;
        interval = strokeWidth * 3;

//        Log.i("guohao","width = " + width);
//        Log.i("guohao","height = " + height);
//        Log.i("guohao","interval = " + interval);
//        Log.i("guohao","forward = " + forward);
//        Log.i("guohao","strokeWidth = " + strokeWidth);

        canvas.save();

//        canvas.drawLine(0,0,width,height,mRedPaint); // 交叉对脚线
//        canvas.drawLine(0,height,width,0,mRedPaint);

        canvas.drawRect(0, 0, width, 10, mRedPaint);// top
        canvas.drawRect(0, 0, 10, height, mRedPaint); // left
        canvas.drawRect(0, height - 10, width, height, mRedPaint); // bottom
        canvas.drawRect(width - 10, 0, width, height, mRedPaint); // right

        // 画出跑动的横边框
        spotX = beginX;
        while (spotX < width) {
            canvas.drawRect(spotX,
                    0,
                    spotX + interval,
                    strokeWidth,
                    mBluePaint2);// top

            canvas.drawRect(width - spotX,
                    height - strokeWidth,
                    width - spotX - interval,
                    height,
                    mBluePaint2);// bottom

            spotX += 2 * interval;
        }
        beginX += forward;
        if (beginX > interval) beginX -= 2 * interval;


        // 画出跑动的竖边框
        spotY = beginY;
        while (spotY < height) {

            canvas.drawRect(width - strokeWidth,
                    spotY,
                    width,
                    spotY + interval,
                    mBluePaint2);// right

            canvas.drawRect(0,
                    height - spotY,
                    strokeWidth,
                    height - spotY - interval,
                    mBluePaint2);// left

            spotY += 2 * interval;
        }
        beginY += forward;
        if (beginY > interval) beginY -= 2 * interval;

        if (isMove) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 100);
        }
    }

    public void optMove(boolean ismove) {
        this.isMove = ismove;
        if (ismove) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 50);
        }
    }

    public boolean isMove() {
        return isMove;
    }
}
