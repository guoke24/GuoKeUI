package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample03SweepGradientView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Sample03SweepGradientView(Context context) {
        super(context);
    }

    public Sample03SweepGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample03SweepGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        // cx cy ：扫描的中心
        // color0：扫描的起始颜色
        // color1：扫描的终止颜色
        paint.setShader(new SweepGradient(300, 300, Color.parseColor("#E91E63"),
                Color.parseColor("#2196F3")));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(300, 300, 200, paint);
    }
}
