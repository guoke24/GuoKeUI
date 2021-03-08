package com.guohao.guokeui.hencoder.hencoderpracticedraw7.sample.sample03;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.guohao.guokeui.R;


public class Sample03OfObjectLayout extends RelativeLayout {
    Sample03OfObjectView view;
    Button animateBt;

    public Sample03OfObjectLayout(Context context) {
        super(context);
    }

    public Sample03OfObjectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample03OfObjectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Sample03OfObjectView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 细节一： ofObject 函数，不是 ofInt 或 ofFloat 函数，因为
                // 绑定的 view 的 position 字段的 setter 函数接收的参数类型是自定义类型，即 PointF 类型
                // 细节二：PointFEvaluator 是 TypeEvaluator 的子类，用来计算进度动画的
                // 细节三：view 的 position 字段的 getter 和 setter 函数，
                // 会接收到 PointFEvaluator 的 evaluate 函数返回的 PointF 类型的实例
                ObjectAnimator animator = ObjectAnimator.ofObject(view, "position",
                        new PointFEvaluator(), new PointF(0, 0), new PointF(1, 1));
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(1000);
                animator.start();
            }
        });
    }

    private class PointFEvaluator implements TypeEvaluator<PointF> {
        PointF newPoint = new PointF();

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + (fraction * (endValue.x - startValue.x));
            float y = startValue.y + (fraction * (endValue.y - startValue.y));

            newPoint.set(x, y);

            return newPoint;
        }
    }
}
