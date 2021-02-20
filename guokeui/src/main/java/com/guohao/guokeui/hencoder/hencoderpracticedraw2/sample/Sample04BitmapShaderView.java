package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample04BitmapShaderView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Sample04BitmapShaderView(Context context) {
        super(context);
    }

    public Sample04BitmapShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample04BitmapShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.batman);
        // bitmap：用来做模板的 Bitmap 对象
        // tileX：横向的 TileMode
        // tileY：纵向的 TileMode。
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(200, 200, 200, paint);
    }
}
