package com.guohao.guokeui.hencoder.hencoderpracticedraw3.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Sample14GetFontMetricsView extends View {
    Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    String[] texts = {"A", "a", "J", "j", "Â", "â"};
    float yOffset;
    int top = 200;
    int bottom = 400;

    public Sample14GetFontMetricsView(Context context) {
        super(context);
    }

    public Sample14GetFontMetricsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample14GetFontMetricsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(20);
        paint1.setColor(Color.parseColor("#E91E63"));
        paint2.setTextSize(160);

        Paint.FontMetrics fontMetrics = paint2.getFontMetrics();
        yOffset = - (fontMetrics.ascent + fontMetrics.descent) / 2;
        // 给 paint2 设置字体大小后，就能拿到 fontMetrics，字体大小的指标
        // fontMetrics 几个字段：
        // ascent / descent: 它们的作用是限制普通字符的顶部和底部范围。
        // 普通的字符，上不会高过 ascent ，下不会低过 descent
        //
        // top / bottom: 它们的作用是限制所有字形（ glyph ）的顶部和底部范围。
        // 除了普通字符，有些字形的显示范围是会超过 ascent 和 descent 的，而 top 和 bottom 则限制的是所有字形的显示范围，包括这些特殊字形。
        //
        // leading: 指的是行的额外间距，即对于上下相邻的两行，上行的 bottom 线和下行的 top 线的距离，
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(50, top, getWidth() - 50, bottom, paint1);

        int middle = (top + bottom) / 2;
        canvas.drawText(texts[0], 100, middle + yOffset, paint2);
        canvas.drawText(texts[1], 200, middle + yOffset, paint2);
        canvas.drawText(texts[2], 300, middle + yOffset, paint2);
        canvas.drawText(texts[3], 400, middle + yOffset, paint2);
        canvas.drawText(texts[4], 500, middle + yOffset, paint2);
        canvas.drawText(texts[5], 600, middle + yOffset, paint2);
    }
}
