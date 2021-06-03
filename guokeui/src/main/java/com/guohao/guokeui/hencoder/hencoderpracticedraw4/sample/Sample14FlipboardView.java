package com.guohao.guokeui.hencoder.hencoderpracticedraw4.sample;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;

/**
 * 1，用属性动画 ObjectAnimator 动态更改 degree，从 0 - 180
 *
 * 2，用 Camera 对图片对下半部分，做 0 - 180 的翻转
 *
 *
 */
public class Sample14FlipboardView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    int degree;
    //ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 180);
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, -180);

    public Sample14FlipboardView(Context context) {
        super(context);
    }

    public Sample14FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample14FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        animator.setDuration(2500);                        // 时长 2.5 秒
        animator.setInterpolator(new LinearInterpolator());// 线性差值器
        animator.setRepeatCount(ValueAnimator.INFINITE);   // 重复次数，无限
        animator.setRepeatMode(ValueAnimator.REVERSE);     // 重复模式：RESTART 从头开始；REVERSE 从尾倒转
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

    // 翻转下半页
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        int bitmapWidth = bitmap.getWidth();
//        int bitmapHeight = bitmap.getHeight();
//        int centerX = getWidth() / 2;
//        int centerY = getHeight() / 2;
//        int x = centerX - bitmapWidth / 2;  // 左边顶点的x值
//        int y = centerY - bitmapHeight / 2; // 左边顶点的y值
//
//        // 第一遍绘制：上半部分
//        canvas.save();
//        // 剪裁整个 view 的上半部分
//        canvas.clipRect(0, 0, getWidth(), centerY);
//        // 从 x，y 画一个图，但因为剪裁但原因，只会显示一半
//        canvas.drawBitmap(bitmap, x, y, paint);
//        canvas.restore();
//
//        // 第二遍绘制：下半部分
//        canvas.save();
//
//        if (degree < 90) {
//            // 剪裁整个 view 的下半部分
//            canvas.clipRect(0, centerY, getWidth(), getHeight());
//        } else {
//            // 超过 90 度反转，其实已经翻到上半区，所以依旧剪裁整个 view 的上半部分
//            canvas.clipRect(0, 0, getWidth(), centerY);
//        }
//        camera.save();
//        camera.rotateX(degree); // 绕 x 轴转，正数，则下面往屏幕外移动
//        canvas.translate(centerX, centerY); // canvas 移到 view 的中心点
//        camera.applyToCanvas(canvas); // 上述的 rotateX 应用到 view 的中心点
//        canvas.translate(-centerX, -centerY); // canvas 移到 view 的左顶点
//        camera.restore();
//
//        canvas.drawBitmap(bitmap, x, y, paint); // 依旧从 x，y 画一个图，但因为剪裁和选择的原因，会出现翻页效果
//        canvas.restore();
//    }

    // 翻转上半页
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;  // 左边顶点的x值
        int y = centerY - bitmapHeight / 2; // 左边顶点的y值

        // 绘制：下半部分
        canvas.save();
        // 剪裁整个 view 的下半部分
        //canvas.clipRect(0, 0, getWidth(), centerY);
        canvas.clipRect(0, centerY, getWidth(), getHeight());
        // 从 x，y 画一个图，但因为剪裁但原因，只会显示一半
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        // 绘制：上半部分
        canvas.save();

        if (degree > -90) {
            // 剪裁整个 view 的上半部分
            canvas.clipRect(0, 0, getWidth(), centerY);

        } else {
            // 超过 90 度反转，其实已经翻到上半区，所以依旧剪裁整个 view 的上半部分
            canvas.clipRect(0, centerY, getWidth(), getHeight());
        }
        camera.save();
        camera.rotateX(degree); // 绕 x 轴转，正数，则下面往屏幕外移动
        canvas.translate(centerX, centerY); // canvas 移到 view 的中心点
        camera.applyToCanvas(canvas); // 上述的 rotateX 应用到 view 的中心点
        canvas.translate(-centerX, -centerY); // canvas 移到 view 的左顶点
        camera.restore();

        canvas.drawBitmap(bitmap, x, y, paint); // 依旧从 x，y 画一个图，但因为剪裁和选择的原因，会出现翻页效果
        canvas.restore();

    }
}
