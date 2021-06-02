package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample04BitmapShaderView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.batman);

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
        // bitmap：用来做模板的 Bitmap 对象
        // tileX：横向的 TileMode
        // tileY：纵向的 TileMode。
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // Shader 默认是从顶点（0，0）开始着色的
        // 可以通过
        Matrix matrix = new Matrix();
        matrix.preTranslate(0,bitmap.getHeight());
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,0,0,paint);
        canvas.drawCircle(250, bitmap.getHeight() + 250, 250, paint);

        // 画文字时，不会用到 paint 的 Shader
//        paint.setTextSize(48);
//        paint.setColor(Color.RED);
//        canvas.drawText("Hello",10,40,paint);
    }
}
