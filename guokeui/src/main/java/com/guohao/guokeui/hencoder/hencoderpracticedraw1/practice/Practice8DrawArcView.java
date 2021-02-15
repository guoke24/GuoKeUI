package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Practice8DrawArcView extends View {

    private int width;//view宽

    private int height;//view高


    private Paint paint;

    public Practice8DrawArcView(Context context) {
        super(context);
        init();
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形

        width = getWidth();
        height = getHeight();

        canvas.save();
        canvas.translate(width / 2, height / 2);
        RectF rectF = new RectF(-200, -150, 200, 150);

        // 不连接圆心，填充样式，则会得到一轮新月
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, 30, 130, false, paint);
        // 0度默认是一四象限的分界线，正角度，随着顺时针扫过

        // 不连接圆心，不填充样式，则会得到一段弧线
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 180, 60, false, paint);

        // 连接圆心，填充样式，则会得到一个扇形
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, 270, 80, true, paint);

        canvas.restore();
    }
}
