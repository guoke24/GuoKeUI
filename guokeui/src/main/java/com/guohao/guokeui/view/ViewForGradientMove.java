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
 * ViewForGradient 动起来的版本
 */
public class ViewForGradientMove extends View {
    public ViewForGradientMove(Context context) {
        super(context);
        init();
    }

    public ViewForGradientMove(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewForGradientMove(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ViewForGradientMove(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint paint = new Paint(); //画笔
    private int color1 = Color.parseColor("#660099");
    private int color2 = Color.parseColor("#ffcc33");

    private void init(){
        initPaint();
        thread.start();
    }

    private Thread thread = new Thread(){
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    };

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
    }


    private float addtion = 0f; //当前旋转角度
    private float addtionLine = 0f;
    /**
     * 认识 Shader.TileMode 的三种模式
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(addtionLine >= 200){
            addtionLine = 1;
        }else{
            addtionLine += 1;
        }

        // 第一种，shader 的范围跟 canvas绘制范围一致，普通渐变
        Shader shader =
                new LinearGradient(200, 0, 200, addtionLine + 200, color1, color2, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawCircle(200, 200, 200, paint);

        initPaint();

        // 第二种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复边缘 ，即 CLAMP 模式
        Shader shader2 =
                new LinearGradient(600, 0, 600, addtionLine, color1, color2, Shader.TileMode.CLAMP);
        paint.setShader(shader2);
        canvas.drawCircle(600, 200, 200, paint);

        initPaint();

        // 第三种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复一遍「开头到结尾」 ，即 REPEAT 模式
        Shader shader3 =
                new LinearGradient(200, 400, 200, 400 + addtionLine, color1, color2, Shader.TileMode.REPEAT);
        paint.setShader(shader3);

        canvas.drawCircle(200, 600, 200, paint);

        initPaint();

        // 第四种，shader 的范围小于 canvas绘制范围，
        // 超出 shader 的范围，重复「镜像」 ，即 MIRROR 模式
        Shader shader4 =
                new LinearGradient(600, 400, 600, 400 + addtionLine, color1, color2, Shader.TileMode.MIRROR);
        paint.setShader(shader4);
        canvas.drawCircle(600, 600, 200, paint);
        // --- end LinearGradient ---

        // --- begin RadialGradient ---
        // 当 绘制范围 超出 Shader 范围，Shader.TileMode 才会起作用
        // 否则就是均匀当渐变

        // 第5种，
        initPaint();
        paint.setShader(new RadialGradient(
                200, 1000, 200, color1, color2, Shader.TileMode.CLAMP));
        canvas.drawCircle(200, 1000, 200, paint);

        // 第6种，
        initPaint();
        // 边缘重复
        paint.setShader(new RadialGradient(
                600, 1000, addtionLine, color1, color2, Shader.TileMode.CLAMP));
        canvas.drawCircle(600, 1000, 200, paint);

        initPaint();
        // 从头重复
        paint.setShader(new RadialGradient(
                200, 1400, addtionLine, color1, color2, Shader.TileMode.REPEAT));
        canvas.drawCircle(200, 1400, 200, paint);

        initPaint();
        // 镜像重复
        paint.setShader(new RadialGradient(
                600, 1400, addtionLine, color1, color2, Shader.TileMode.MIRROR));
        canvas.drawCircle(600, 1400, 200, paint);
        // --- end RadialGradient ---

        // --- begin SweepGradient ---
        initPaint();
        paint.setShader(new SweepGradient(200,1800,color1,color2));
        canvas.drawCircle(200, 1800, 200, paint);

        if(0.6f + addtion >= 1){
            addtion = 0;
        }
        addtion += 0.01f;

        initPaint();
        // positionArray 对应 colorArray 中颜色结束的位置
        // 如 color1 的结束位置为 0.35f * 360
        // 0.0 --color1- 0.35 -BLUE- 0.45 -YELLOW- 0.5 -color2- 0.6
        int[] colorArray = new int[]{color1, Color.BLUE, Color.YELLOW, color2};
        float[] positionArray = new float[]{0.35f + addtion, 0.45f + addtion , 0.5f + addtion, 0.6f + addtion};
        paint.setShader(new SweepGradient(600, 1800,
                colorArray, positionArray));
        canvas.drawCircle(600, 1800, 200, paint);
        // --- end SweepGradient ---

        // LinearGradient + 数组控制渐变
        initPaint();
        //colorArray = new int[]{color1, Color.BLUE, Color.YELLOW, color2};
        //positionArray = new float[]{0.35f, 0.45f, 0.5f, 0.6f};
        paint.setShader(new LinearGradient(200, 2000,200,2400,
                colorArray, positionArray, Shader.TileMode.CLAMP));
        canvas.drawCircle(200, 2200, 200, paint);

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
