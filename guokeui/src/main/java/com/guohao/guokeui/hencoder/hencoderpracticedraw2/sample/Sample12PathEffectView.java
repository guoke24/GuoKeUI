package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;

public class Sample12PathEffectView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    PathEffect cornerPathEffect = new CornerPathEffect(20);
    // 所有拐角变成圆角。

    PathEffect discretePathEffect = new DiscretePathEffect(20, 5);
    // 线条进行随机的偏离，让轮廓变得乱七八糟。乱七八糟的方式和程度由参数决定。
    // segmentLength 是用来拼接的每个线段的长度， deviation 是偏离量。这两个值设置得不一样，显示效果也会不一样

    PathEffect dashPathEffect = new DashPathEffect(new float[]{20, 10, 5, 10}, 0);
    // 第一个参数 intervals 是一个数组，它指定了虚线的格式：数组中元素必须为偶数（最少是 2 个），按照「画线长度、空白长度、画线长度、空白长度」……的顺序排列，
    // 例如上面代码中的 20, 5, 10, 5 就表示虚线是按照「画 20 像素、空 5 像素、画 10 像素、空 5 像素」的模式来绘制；第二个参数 phase 是虚线的偏移量。

    PathEffect pathDashPathEffect;
    // 绘制「虚线」

    PathEffect sumPathEffect = new SumPathEffect(dashPathEffect, discretePathEffect);
    // 组合效果类的 PathEffect 。它的行为特别简单，就是分别按照两种 PathEffect 分别对目标进行绘制。

    PathEffect composePathEffect = new ComposePathEffect(dashPathEffect, discretePathEffect);
    // 这也是一个组合效果类的 PathEffect 。不过它是先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect。

    public Sample12PathEffectView(Context context) {
        super(context);
    }

    public Sample12PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample12PathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);

        path.moveTo(50, 100);
        path.rLineTo(50, 100);
        path.rLineTo(80, -150);
        path.rLineTo(100, 100);
        path.rLineTo(70, -120);
        path.rLineTo(150, 80);

        Path dashPath = new Path();
        dashPath.lineTo(20, -30);
        dashPath.lineTo(40, 0);
        dashPath.close();
        pathDashPathEffect = new PathDashPathEffect(dashPath, 50, 0, PathDashPathEffect.Style.MORPH);
        // PathDashPathEffect，绘制「虚线」，「虚线」可以指定形状！由 shape 参数 决定
        //
        // shape 参数是用来绘制的 Path，即 「虚线」的形状
        //
        // advance 是两个相邻的 shape 段之间的间隔，不过注意，这个间隔是两个 shape 段的起点的间隔，而不是前一个的终点和后一个的起点的距离；
        //
        // phase 和 DashPathEffect 中一样，是虚线的偏移；
        //
        // 最后一个参数 style，是用来指定拐弯改变的时候 shape 的转换方式。
        // style 的类型为 PathDashPathEffect.Style ，是一个 enum ，具体有三个值：
        // 1,TRANSLATE：位移
        // 2,ROTATE：旋转
        // 3,MORPH：变体
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.setPathEffect() 来设置不同的 PathEffect
        // 使用 PathEffect 来给图形的轮廓设置效果。
        // 对 Canvas 所有的图形绘制有效，也就是 drawLine() drawCircle() drawPath() 这些方法

        // CornerPathEffect
        paint.setPathEffect(cornerPathEffect);
        canvas.drawPath(path, paint);

        canvas.save();
        canvas.translate(500, 0);
        // DiscretePathEffect
        paint.setPathEffect(discretePathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 200);
        // DashPathEffect
        paint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 200);
        // PathDashPathEffect
        paint.setPathEffect(pathDashPathEffect);
        //canvas.drawPath(path, paint);
        // 另外画一个做比较
        Path path1 = new Path();
        path1.moveTo(50, 50);
        path1.lineTo(500, 50); // 画水平或垂直的线就不是三角形，就一根线。为啥呢？
        canvas.drawPath(path1,paint);
        Path path2 = new Path();
        path2.moveTo(50, 50);
        path2.lineTo(50, 200);
        canvas.drawPath(path2,paint);
        Path path3 = new Path();
        path3.moveTo(50, 50);
        path3.lineTo(500, 200);
        canvas.drawPath(path3,paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 400);
        // SumPathEffect
        paint.setPathEffect(sumPathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 400);
        // ComposePathEffect
        paint.setPathEffect(composePathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        paint.setPathEffect(null);
    }
}
