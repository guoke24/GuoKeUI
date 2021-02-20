package com.guohao.guokeui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * LinearGradient
 * RadialGradient
 * SweepGradient
 */
public class ViewForGradient extends View {
    public ViewForGradient(Context context) {
        super(context);
        init();
    }

    public ViewForGradient(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewForGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ViewForGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint paint = new Paint(); //画笔
    private int color1 = Color.parseColor("#660099");
    private int color2 = Color.parseColor("#ffcc33");
    private int potX = 200;
    private int potY = 200;

    private void init(){
        initPaint();
    }

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
    }

    /**
     * 整个view 充满屏幕
     * @param canvas
     */

    /**
     * 认识 Shader.TileMode 的三种模式
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 第一种，shader 的范围跟 canvas绘制范围一致，普通渐变
        Shader shader =
                new LinearGradient(200, 0, 200, 400, color1, color2, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawCircle(potX, potY, 200, paint);

        initPaint();

        // 第二种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复边缘 ，即 CLAMP 模式
        Shader shader2 =
                new LinearGradient(600, 0, 600, 200, color1, color2, Shader.TileMode.CLAMP);
        paint.setShader(shader2);
        potX += 400;
        canvas.drawCircle(potX, potY, 200, paint);

        initPaint();

        // 第三种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复一遍「开头到结尾」 ，即 REPEAT 模式
        Shader shader3 =
                new LinearGradient(200, 400, 200, 600, color1, color2, Shader.TileMode.REPEAT);
        paint.setShader(shader3);
        potX -= 400;
        potY += 400;
        canvas.drawCircle(potX, potY, 200, paint);

        initPaint();

        // 第四种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复「镜像」 ，即 MIRROR 模式
        Shader shader4 =
                new LinearGradient(600, 400, 600, 600, color1, color2, Shader.TileMode.MIRROR);
        paint.setShader(shader4);
        canvas.drawCircle(600, 600, 200, paint);
        // --- end LinearGradient ---

        // --- begin RadialGradient ---
        // 放射渐变
        // 当 绘制范围 超出 Shader 范围，Shader.TileMode 才会起作用
        // 否则就是均匀当渐变

        // 第5种
        initPaint();
        paint.setShader(new RadialGradient(
                200, 1000, 200, color1, color2, Shader.TileMode.CLAMP));
        canvas.drawCircle(200, 1000, 200, paint);

        // 第6种
        initPaint();
        // 边缘重复
        paint.setShader(new RadialGradient(
                600, 1000, 100, color1, color2, Shader.TileMode.CLAMP));
        canvas.drawCircle(600, 1000, 200, paint);

        // 第7种
        initPaint();
        // 从头重复
        paint.setShader(new RadialGradient(
                200, 1400, 100, color1, color2, Shader.TileMode.REPEAT));
        canvas.drawCircle(200, 1400, 200, paint);

        // 第8种
        initPaint();
        // 镜像重复
        paint.setShader(new RadialGradient(
                600, 1400, 100, color1, color2, Shader.TileMode.MIRROR));
        canvas.drawCircle(600, 1400, 200, paint);
        // --- end RadialGradient ---

        // --- begin SweepGradient ---
        // 雷达渐变

        // 第9种
        initPaint();
        paint.setShader(new SweepGradient(200,1800,color1,color2));
        canvas.drawCircle(200, 1800, 200, paint);

        // 第10种
        initPaint();
        // positionArray 对应 colorArray 中颜色结束的位置
        // 如 color1 的结束位置为 0.35f * 360
        // 0.0 --color1- 0.35 -BLUE- 0.45 -YELLOW- 0.5 -color2- 0.6
        int[] colorArray = new int[]{color1, Color.BLUE, Color.YELLOW, color2};
        float[] positionArray = new float[]{0.35f, 0.45f, 0.5f, 0.6f};
        paint.setShader(new SweepGradient(600, 1800,
                colorArray, positionArray));
        canvas.drawCircle(600, 1800, 200, paint);
        // --- end SweepGradient ---

        // 第11种
        // LinearGradient + 数组控制渐变
        initPaint();
        colorArray = new int[]{color1, Color.BLUE, Color.YELLOW, color2};
        positionArray = new float[]{0.35f, 0.45f, 0.5f, 0.6f};
        paint.setShader(new LinearGradient(200, 2000,200,2400,
                colorArray, positionArray, Shader.TileMode.CLAMP));
        canvas.drawCircle(200, 2200, 200, paint);

        // 第12种
        // RadialGradient + 数组控制渐变
        initPaint();
        paint.setShader(new RadialGradient(600, 2200, 200,
                colorArray, positionArray, Shader.TileMode.CLAMP));
        canvas.drawCircle(600, 2200, 200, paint);


        // 紫金配色！！
        initPaint();
        RectF rectF = new RectF(0,2400,400,2800);
        paint.setColor(color1);
        paint.setStyle(Paint.Style.FILL);
        // 上半圆，默认在 rectF 的中心开始
        canvas.drawArc(rectF,0,-180,true,paint);

        initPaint();
        paint.setColor(color2);
        paint.setStyle(Paint.Style.FILL);
        // 下半圆
        canvas.drawArc(rectF,0,180,true,paint);

        initPaint();
        RectF rectF2 = new RectF(400,2400,800,2800);
        paint.setColor(color1);
        paint.setStyle(Paint.Style.FILL);
        // 左半圆，默认在 rectF 的中心开始
        canvas.drawArc(rectF2,-90,-180,true,paint);

        initPaint();
        paint.setColor(color2);
        paint.setStyle(Paint.Style.FILL);
        // 右半圆
        canvas.drawArc(rectF2,-90,180,true,paint);

    }

    private static final int DEFAULT_MIN_WIDTH = 400; //View默认最小宽度

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
