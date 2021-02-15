package com.guohao.guokeui.hencoder.hencoderpracticedraw4.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import com.guohao.guokeui.R;


public class Sample06SkewView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point0 = new Point(0, 0);
    Point point1 = new Point(350, 0);
    Point point2 = new Point(650, 0);

    public Sample06SkewView(Context context) {
        super(context);
    }

    public Sample06SkewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample06SkewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * @params sx 将画布在x方向上倾斜相应的角度，sx倾斜角度的tan值，其实就是将y逆时针旋转相应的角度
         * @params sy 将画布在y方向上倾斜相应的角度，sx倾斜角度的tan值，其实就是将x顺时针旋转相应的角度
         */

        canvas.save();
        canvas.skew(0, 0.5f);// 垂直错切
        canvas.drawBitmap(bitmap, point0.x, point0.y, paint);
        canvas.restore();

        canvas.save();
        canvas.skew(0, 0.5f);// 垂直错切
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);// 注意，此处错切，还是以 canvas 的顶点（0,0）作为参照
        canvas.restore();

        canvas.save();
        canvas.skew(0.5f, 0);// 水平错切
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);// 注意，此处错切，还是以 canvas 的顶点（0,0）作为参照
        canvas.restore();
    }
}
