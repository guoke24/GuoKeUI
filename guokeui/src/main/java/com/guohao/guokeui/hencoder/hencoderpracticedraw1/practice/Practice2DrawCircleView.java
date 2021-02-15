package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Practice2DrawCircleView extends View {


    private int width;//view宽

    private int height;//view高


    private int offsetWidth;//圆心

    private int offsetHeight;//圆心

    private Paint paint;

    public Practice2DrawCircleView(Context context) {
        super(context);
    }

    public Practice2DrawCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice2DrawCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        init();


    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        offsetWidth = width / 4;
        offsetHeight = height / 4;

//        练习内容：使用 canvas.drawCircle() 方法画圆
//        一共四个圆：1.实心圆 2.空心圆 3.蓝色实心圆 4.线宽为 20 的空心圆


        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(offsetWidth, offsetHeight, 150, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.drawCircle(offsetWidth * 3, offsetHeight, 150, paint);

        paint.setColor(Color.parseColor("#880000FF"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(offsetWidth, offsetHeight * 3, 150, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(50);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(offsetWidth * 3, offsetHeight * 3, 150, paint);


    }
}
