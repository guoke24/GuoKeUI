package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;

import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Path.FillType.WINDING;

public class Practice9DrawPathView extends View {

    private int width;//view宽

    private int height;//view高

    private Path pathLeft;//左心

    private Path pathRight;//有心

    private Paint paint;

    public Practice9DrawPathView(Context context) {
        super(context);
        init();
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        pathRight= new Path();
        pathLeft = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawPath() 方法画心形

        width = getWidth();
        height = getHeight();

        int heartWidth = width / 3;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        canvas.save();

        canvas.translate(width / 2, height / 2);
        RectF rectF1 = new RectF(-heartWidth / 2, -heartWidth / 4, 0, heartWidth / 4);
        pathLeft.setFillType(Path.FillType.WINDING);
        pathLeft.arcTo(rectF1, 0, -220);
        pathLeft.lineTo(0, heartWidth * 0.65f); // Style 是 FILL ，所以绘制时会自动封口
        canvas.drawPath(pathLeft, paint);

        RectF f2 = new RectF(0, -heartWidth / 4, heartWidth / 2, heartWidth / 4);
        pathRight.arcTo(f2, 180, 220);
        pathRight.lineTo(0, heartWidth * 0.65f);
        canvas.drawPath(pathRight, paint);

        canvas.restore();


    }
}
