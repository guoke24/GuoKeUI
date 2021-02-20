package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;


public class Sample05ComposeShaderView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Shader shader1;
    Shader shader2;

    public Sample05ComposeShaderView(Context context) {
        super(context);
    }

    public Sample05ComposeShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample05ComposeShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.batman);
        shader1 = new BitmapShader(bitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.batman_logo);
        shader2 = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //  引用：https://www.douban.com/note/143111853/
        //  最近一直在忙于图形方面的工作，在SKIA代码中看到了Porter-Duff 操作，通过翻译软件怎么也翻不出来，于是上网搜了搜，才恍然大悟。摘录片段如下：
        //
        //  Porter-Duff 操作是 1 组 12 项用于描述数字图像合成的基本手法，包括
        //  Clear、Source Only、Destination Only、Source Over、Source In、Source
        //  Out、Source Atop、Destination Over、Destination In、Destination
        //  Out、Destination Atop、XOR。通过组合使用 Porter-Duff 操作，可完成任意 2D
        //  图像的合成。
        //
        //  这里有 Thomas Porter 和 Tom Duff 发表于 1984
        //  年原始论文的扫描版本 http://keithp.com/~keithp/porterduff/p253-porter.pdf
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        paint.setShader(shader1);
//        canvas.drawCircle(200, 600, 200, paint);
//
//        paint.setShader(shader2);
//        canvas.drawCircle(600, 200, 200, paint);

        paint.setShader(new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_IN));// 按照后者的轮廓，扣出前者，并只显示扣出的部分
        canvas.drawCircle(200, 200, 200, paint);

        canvas.save();
        canvas.translate(400,0);
        paint.setShader(new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OUT));// 按照后者的轮廓，扣出前者，并去掉这部分
        canvas.drawCircle(200, 200, 200, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0,400);
        paint.setShader(new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OVER));// 覆盖，前者覆盖后者
        canvas.drawCircle(200, 200, 200, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(400,400);
        paint.setShader(new ComposeShader(shader2, shader1, PorterDuff.Mode.DST_OVER));
        canvas.drawCircle(200, 200, 200, paint);
        canvas.restore();



    }
}
