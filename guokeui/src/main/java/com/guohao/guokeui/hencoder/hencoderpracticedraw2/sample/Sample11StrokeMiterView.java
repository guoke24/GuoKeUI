package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample11StrokeMiterView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    public Sample11StrokeMiterView(Context context) {
        super(context);
    }

    public Sample11StrokeMiterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample11StrokeMiterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);

        path.rLineTo(200, 0);
        path.rLineTo(-160, 120);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        // setStrokeJoin 函数设置的拐点默认为 MITER
        // setStrokeMiter 这个方法是对于 setStrokeJoin() 的一个补充，它用于设置 MITER 型拐角的延长线的最大值。

        // 为了避免意料之外的过长的尖角出现， MITER 型连接点有一个额外的规则：当尖角过长时，自动改用 BEVEL 的方式来渲染连接点。

        // 至于多尖的角属于过于尖，尖到需要转为使用 BEVEL 来绘制，则是由一个属性控制的，
        // 而这个属性就是 setStrokeMiter(miter) 方法中的 miter 参数。miter 参数是对于转角长度的限制，

        // 这个 miter limit 的默认值是 4，对应的是一个大约 29° 的锐角：
        // 默认情况下，大于这个角的尖角会被保留，而小于这个夹角的就会被「削成平头」

        // 小结：
        // 若想让被削的角出现，说明需要提高限制，把 miter limit 改大
        // 若想让尖角被削，说明需要降低限制，把 miter limit 改小
        // 所以，miter limit 越大，角更尖！
        // 所以，miter limit 越小，角更钝
        //
        // 有图解的扔物线原文的参考：https://rengwuxian.com/105.html

        canvas.translate(100, 100);
        paint.setStrokeMiter(1); // miter limit 改为 1，比默认 4 小，容忍度变小，更钝的角都会被削
        canvas.drawPath(path, paint);

        canvas.translate(300, 0);
        paint.setStrokeMiter(3); // miter limit 改为 3，大于 3 的就被「削成平头」
        canvas.drawPath(path, paint);

        canvas.translate(300, 0);
        paint.setStrokeMiter(5);
        // miter limit 改为 5，大于 5 的就被「削成平头」，
        // 而原本默认为 4 的就被「削成平头」的拐角，改成 5 后，尖角就得以保留
        // 说明 miter limit 的值越大，角就可以更尖，容忍度更大
        canvas.drawPath(path, paint);

        canvas.restore();
    }

    // 到此，4 个关于线条形状的方法：
    // setStrokeWidth(width) 线的宽度
    // setStrokeCap(cap)  线头的形状
    // setStrokeJoint(join) 线的夹角类型
    // setStrokeMiter(miter) 线的夹角尖角限制
}
