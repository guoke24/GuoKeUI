package com.guohao.guokeui.hencoder.hencoderpracticedraw4.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import com.guohao.guokeui.R;

public class Sample02ClipPathView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Path path1 = new Path();
    Path path2 = new Path();
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);

    public Sample02ClipPathView(Context context) {
        super(context);
    }

    public Sample02ClipPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample02ClipPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        // 添加一个圆心（point1.x + 200, point1.y + 200），半径为 150 的圆形通道
        path1.addCircle(point1.x + 200, point1.y + 200, 150, Path.Direction.CW);// CW 顺时针

        path2.addCircle(point2.x + 200, point2.y + 200, 150, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();// 存档当前状态
        canvas.clipPath(path1);//先剪裁，再绘制时被限制在裁切范围内
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();// 恢复之前的存档

        canvas.save();
        path2.setFillType(Path.FillType.INVERSE_WINDING);//反转缠绕，绘制时被限制在裁切范围外
        canvas.clipPath(path2);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
