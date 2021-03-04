package com.guohao.guokeui.hencoder.hencoderpracticedraw3.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Sample11GetFontSpacingView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";

    public Sample11GetFontSpacingView(Context context) {
        super(context);
    }

    public Sample11GetFontSpacingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample11GetFontSpacingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float spacing = paint.getFontSpacing();
        // 获取推荐的行距。
        //
        // 即推荐的两行文字的 baseline 的距离。这个值是系统根据文字的字体和字号自动计算的。
        // 它的作用是当你要手动绘制多行文字（而不是使用 StaticLayout）的时候，可以在换行的时候给 y 坐标加上这个值来下移文字。

        canvas.drawText(text, 50, 100, paint);

        canvas.drawText(text, 50, 100 + spacing * 2, paint);

        canvas.drawText(text, 50, 100 + spacing * 5 , paint);
    }
}
