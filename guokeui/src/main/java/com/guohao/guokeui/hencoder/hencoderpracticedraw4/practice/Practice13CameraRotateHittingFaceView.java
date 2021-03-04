package com.guohao.guokeui.hencoder.hencoderpracticedraw4.practice;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class Practice13CameraRotateHittingFaceView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point = new Point(200, 50);
    Camera camera = new Camera();
    Matrix matrix = new Matrix();
    int degree;
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 360);

    public Practice13CameraRotateHittingFaceView(Context context) {
        super(context);
    }

    public Practice13CameraRotateHittingFaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice13CameraRotateHittingFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2, bitmap.getHeight() * 2, true);
//        bitmap.recycle();
//        bitmap = scaledBitmap;

        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
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

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int centerX = point.x + bitmapWidth / 2;
        int centerY = point.y + bitmapHeight / 2;
        // 当图片以 point.x, point.y 为左顶点时，centerX, centerY 就是图片的中心

        camera.save();
        matrix.reset();
        camera.rotateX(degree);
        camera.getMatrix(matrix); // 关联一个 matrix
        camera.restore();

        // 由于缩放是以(0,0)为中心的,所以为了把 bitmap 的中心与(0,0)对齐,就要preTranslate(-centerX, -centerY),
        matrix.preTranslate(-centerX, -centerY); // camera.rotateX(degree); 前平移
        matrix.postTranslate(centerX, centerY);  // camera.rotateX(degree); 后平移

        canvas.save();
        // 基于 Canvas 当前的变换，叠加上 matrix 中的变换，
        // 此时的 matrix 带有 camera.rotateX(degree) 的操作，投影到 bitmap 的中心（若为 point.x, point.y 为顶点的话）
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, point.x, point.y, paint);
        canvas.restore();
    }
}