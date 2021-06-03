package com.guohao.guokeui.hencoder.hencoderpracticedraw4.practice;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class TextFlipboardView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();
    int degree;
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 180);
    int currentNum = 0;
    Rect rect = new Rect();
    int padding = 10;

    public TextFlipboardView(Context context) {
        super(context);
    }

    public TextFlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextFlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(120);
        paint.getTextBounds("00",0,2,rect);

        paint2.setColor(Color.parseColor("#660099"));//紫色
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.parseColor("#ffcc33"));//金色
        paint3.setStyle(Paint.Style.FILL);

        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("guohao","onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i("guohao","onAnimationRepeat");
                int temp = paint2.getColor();
                paint2.setColor(paint3.getColor());
                paint3.setColor(temp);// 交换颜色

                if(currentNum + 1 >= 60) currentNum = 0;
                else currentNum++;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int textWidth = rect.width();
        int textHeight = rect.height();

        int x = centerX - textWidth/2;
        int y = centerY + textHeight/2;
        int y2 = centerY - textHeight/2;

        // 绘制静态部分
        // 上半页
        canvas.save();
        canvas.clipRect(0,0,getWidth(),centerY);
        canvas.drawRect(x - padding , y2 - padding,x + textWidth + padding, y2 + textHeight + padding, paint2);
        canvas.drawText(getCurrentNum(true),x,y,paint);
        canvas.restore();

        // 下半页
        canvas.save();
        canvas.clipRect(0,centerY,getWidth(),getHeight());
        canvas.drawRect(x - padding , y2 - padding,x + textWidth + padding, y2 + textHeight + padding, paint3);
        canvas.drawText(getCurrentNum(false),x,y,paint);
        canvas.restore();

        // 绘制动态翻页部分，
        // 下半页和上半页翻转的绘制逻辑有区别：
        // 下半页翻转时，camera.rotateX 从 0 - 90
        // 上半页翻转时，camera.rotateX 从 -90 - 0
        // 视觉上是连贯的，代码中则是有区别
        canvas.save();
        if(degree < 90) canvas.clipRect(0,centerY,getWidth(),getHeight()); // 剪裁下半页
        else canvas.clipRect(0,0,getWidth(),centerY); // 剪裁上半页

        camera.save();
        camera.rotateX(degree < 90 ? degree : transferDegree(degree)); // 绕 x 轴翻转
        canvas.translate(centerX, centerY); // 从中心点翻转
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY); // 回到原点
        camera.restore();

        // 画出边框
        canvas.drawRect(x - padding , y2 - padding,x + textWidth + padding, y2 + textHeight + padding,
                degree < 90 ? paint2 : paint3);

        canvas.drawText(getCurrentNum(degree < 90),x,y,paint);

        canvas.restore();
    }

    // 转换，当 degree 从 90 - 180 变化时，返回 -90 - 0 的值
    private int transferDegree(int src){
        return src - 180;
    }

    // 获取当前数，或者下一个数
    private String getCurrentNum(boolean current){
        if(current){
            String str = String.valueOf(currentNum);
            if(currentNum < 10) str = "0" + str;
            return str;
        }else{
            String str = String.valueOf(currentNum + 1);
            if(currentNum + 1 < 10) str = "0" + str;
            return str;
        }
    }
}
