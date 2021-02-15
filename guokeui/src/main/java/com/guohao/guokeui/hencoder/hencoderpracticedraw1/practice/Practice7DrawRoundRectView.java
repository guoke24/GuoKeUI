package com.guohao.guokeui.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice7DrawRoundRectView extends View {
    private int width;//view宽

    private int height;//view高


    private Paint paint;

    public Practice7DrawRoundRectView(Context context) {
        super(context);
        init();
    }

    public Practice7DrawRoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice7DrawRoundRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawRoundRect() 方法画圆角矩形


        width = getWidth();
        height = getHeight();
        paint.setStyle(Paint.Style.FILL);

        canvas.save();
        canvas.translate(width / 2, height / 2);
        RectF f = new RectF(-300, -200, 300, 200);
        canvas.drawRoundRect(f, 50, 50, paint);
        // 在每个角加一个十字坐标，横x轴，竖y轴；
        // 当 rx，ry 皆为 0 时，矩形的近x边和近y边都是跟 x 轴，y 轴重合的；
        // rx 越大，矩形的近x边「因为变圆角」而离开x轴的部分就越多；
        // ry 同理
        canvas.restore();
    }
}
