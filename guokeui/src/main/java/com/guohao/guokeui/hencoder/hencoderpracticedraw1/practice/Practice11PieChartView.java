package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class Practice11PieChartView extends View {
    private String[] s = {"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
    private Integer[] percent = {2, 8, 10, 50, 80, 160, 50};
    private Integer[] color = {Color.BLACK, Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.LTGRAY, Color.YELLOW};


    List<String> deviceNames;//名字
    List<Integer> colorInteger;//颜色
    List<Integer> precentInteger;//百分比

    Paint paintPie;//画饼

    Paint paintText;//画字体

    Paint paintLine;//画线


    RectF rectfCommon;//不移动的部分

    RectF rectfMove;//移动的部分

    public Practice11PieChartView(Context context) {
        super(context);
        init();
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {

        deviceNames = Arrays.asList(s);
        colorInteger = Arrays.asList(color);
        precentInteger = Arrays.asList(percent);


        paintPie = new Paint(Paint.ANTI_ALIAS_FLAG);


        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setStrokeWidth(5);
        paintLine.setColor(Color.LTGRAY);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(30);
        paintText.setColor(Color.LTGRAY);

        rectfCommon = new RectF(-300, -300, 300, 300);
        rectfMove = new RectF(-350, -350, 250, 250);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        canvas.translate(width / 2, height / 2);

        float startAngle = 0;
        float sweepAngle = 0;
        for (int i = 0; i < deviceNames.size(); i++) {

            // 画饼图
            paintPie.setStrokeWidth(10);
            paintPie.setColor(colorInteger.get(i));

            sweepAngle = precentInteger.get(i);

            String deviceName = deviceNames.get(i);

            float textAngle = startAngle + precentInteger.get(i) / 2;//获取每个弧度中点，中点话延长线

            // 第五个饼会离开一段距离
            if (i == 5) {
                canvas.drawArc(rectfMove, startAngle + 1, sweepAngle - 1, true, paintPie);
            } else {
                canvas.drawArc(rectfCommon, startAngle + 1, sweepAngle - 1, true, paintPie);
            }

            // 画斜线
            float pointY = (float) (Math.sin(textAngle * Math.PI / 180) * 300.0);
            float pointX = (float) (Math.cos(textAngle * Math.PI / 180) * 300.0);

            float lineY = (float) (Math.sin(textAngle * Math.PI / 180) * 350.0);
            float lineX = (float) (Math.cos(textAngle * Math.PI / 180) * 350.0);


            if (i == 5) {
                canvas.translate(-50, -50);
                canvas.drawLine(pointX, pointY, lineX, lineY, paintLine);
                canvas.translate(50, 50);
            } else {
                canvas.drawLine(pointX, pointY, lineX, lineY, paintLine);
            }


            // 画直线
            if (lineX < 0) {
                Rect textRect = getTextRect(deviceName, paintText);
                if (i == 5) {
                    canvas.translate(-50, -50);
                    canvas.drawLine(lineX, lineY, -400, lineY, paintLine);
                    canvas.drawText(deviceName, -420 - textRect.width(), lineY, paintText);
                    canvas.translate(50, 50);
                } else {
                    canvas.drawLine(lineX, lineY, -400, lineY, paintLine);
                    canvas.drawText(deviceName, -420 - textRect.width(), lineY, paintText);

                }
            } else {

                canvas.drawLine(lineX, lineY, 400, lineY, paintLine);
                canvas.drawText(deviceName, 400, lineY, paintText);
            }


            // 初始化每个饼的起始角度
            startAngle = startAngle + sweepAngle;
        }

    }

    public Rect getTextRect(String deviceName, Paint paint) {

        Rect mBound = new Rect();
        paint.getTextBounds(deviceName, 0, deviceName.length(), mBound);

        return mBound;
    }

    //        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图

}
