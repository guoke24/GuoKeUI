package com.guohao.guokeui.hencoder.hencoderpracticedraw4.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;

public class Sample11CameraRotateView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point1 = new Point(100, 100);
    Point point2 = new Point(100, 600);
    Camera camera = new Camera();

    public Sample11CameraRotateView(Context context) {
        super(context);
    }

    public Sample11CameraRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample11CameraRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        camera.save();
        camera.rotateX(30); // 旋转 Camera 的三维空间
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas，以（0，0）点为中心
        camera.restore();
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        canvas.save();
        camera.save();
        camera.rotateY(30);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
