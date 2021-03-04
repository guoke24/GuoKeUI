package com.guohao.guokeui.hencoder.hencoderpracticedraw3.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Sample08setTextSkewXView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";

    public Sample08setTextSkewXView(Context context) {
        super(context);
    }

    public Sample08setTextSkewXView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample08setTextSkewXView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);

        paint.setTextSkewX(-0.5f);
        // 错切 x 轴的边，即扯 x 轴的所在的边，扯到什么程度呢？
        // y 轴所在的边，跟 y 轴的夹角的 tan 值为 -0.5f
        // tan 即 对边/邻边
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(text, 50, 100, paint);
    }
}
