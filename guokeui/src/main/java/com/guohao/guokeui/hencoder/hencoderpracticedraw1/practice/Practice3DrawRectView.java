package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Practice3DrawRectView extends View {

    private int width;//view宽

    private int height;//view高


    private Paint paint;


    public Practice3DrawRectView(Context context) {
        super(context);
        init();
    }

    public Practice3DrawRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice3DrawRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

//        练习内容：使用 canvas.drawRect() 方法画矩形

        canvas.save();
        paint.setColor(Color.BLACK);
        canvas.translate(width / 2, height / 2);
        RectF r = new RectF(-300, -300, 300, 300);
        canvas.drawRect(r, paint);
        canvas.restore();
    }
}
