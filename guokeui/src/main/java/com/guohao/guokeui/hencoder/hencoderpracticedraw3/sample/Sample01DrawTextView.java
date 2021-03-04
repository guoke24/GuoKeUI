package com.guohao.guokeui.hencoder.hencoderpracticedraw3.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Sample01DrawTextView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";

    public Sample01DrawTextView(Context context) {
        super(context);
    }

    public Sample01DrawTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample01DrawTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 注意： (50,100) 是文字的左底点，严格来说，是基线( baseline )
        canvas.drawText(text, 50, 100, paint);

        // (50,100) 是矩形的左顶点
        canvas.drawRect(50,100,100,150,paint);
    }
}
