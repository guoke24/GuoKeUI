package com.guohao.guokeui.hencoder.hencoderpracticedraw3.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Sample02StaticLayoutView extends View {
    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello\nHenCoder";
    StaticLayout staticLayout;

    public Sample02StaticLayoutView(Context context) {
        super(context);
    }

    public Sample02StaticLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample02StaticLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        textPaint.setTextSize(60);
        // 这两行的位置不能换哟
        staticLayout = new StaticLayout(text, textPaint, 600,
                Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        // StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行
        // width 是文字区域的宽度，文字到达这个宽度后就会自动换行；
        // align 是文字的对齐方向；
        // spacingmult 是行间距的倍数，通常情况下填 1 就好；
        // spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
        // includepad 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(50, 40);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
