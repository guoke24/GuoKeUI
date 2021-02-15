package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class Practice10HistogramView extends View {
    private String[] s = {"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
    private Paint paint;
    int offset = 120;//开始画第一个字体和柱状图的位置
    int width = 140;//柱状图的间距
    int textY = 840;//画text的纵坐标
    int lineY = 800;//画text的纵坐标

    List<String> deviceNames;

    public Practice10HistogramView(Context context) {
        super(context);
        init();
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        deviceNames = Arrays.asList(s);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.dawXXX() 方法画直方图

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        // 横线
        canvas.drawLine(50, 800, 1050, 800, paint);
        // 竖线
        canvas.drawLine(50, 800, 50, 100, paint);


        for (int i = 0; i < deviceNames.size(); i++) {
            //画柱状图
            paint.setColor(Color.parseColor("#8800FF00"));
            paint.setStrokeWidth(120);
            canvas.drawLine(offset + width * i, lineY, offset + width * i, 700 - (100 * i), paint);

            //画字体
            paint.setTextSize(30);
            paint.setColor(Color.WHITE);
            String text = deviceNames.get(i);
            Rect mBound = new Rect();
            paint.getTextBounds(text, 0, text.length(), mBound);
            //字体的中间点和柱状图的中间点一直
            canvas.drawText(text, offset + width * i - mBound.width() / 2, textY, paint);

        }
    }
}
