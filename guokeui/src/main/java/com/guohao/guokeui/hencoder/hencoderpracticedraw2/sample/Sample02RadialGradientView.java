package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample02RadialGradientView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Sample02RadialGradientView(Context context) {
        super(context);
    }

    public Sample02RadialGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample02RadialGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        // 设置6和参数，圆形的x/y坐标，半径，起始和终止的颜色，颜色的平铺模式
        // 想涟漪一样，由中间向四周扩散
        paint.setShader(new RadialGradient(300, 300, 200, Color.parseColor("#E91E63"),
                Color.parseColor("#2196F3"), Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(300, 300, 200, paint);
    }
}
