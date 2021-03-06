package com.guohao.guokeui.hencoder.hencoderpracticedraw7.sample.sample02;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.guohao.guokeui.R;


public class Sample02HsvEvaluatorLayout extends RelativeLayout {
    Sample02HsvEvaluatorView view;
    Button animateBt;

    public Sample02HsvEvaluatorLayout(Context context) {
        super(context);
    }

    public Sample02HsvEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample02HsvEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Sample02HsvEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                animator.setEvaluator(new HsvEvaluator());
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    // HSV 色相、饱和度、明度
    // HSL 色相、饱和度、亮度
    // RGB 红色、绿色、蓝色
    private class HsvEvaluator implements TypeEvaluator<Integer> {
        float[] startHsv = new float[3];
        float[] endHsv = new float[3];
        float[] outHsv = new float[3];

        // fraction 完成度
        // startValue 起始值
        // endValue 终点值
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            // 把 ARGB 转换成 HSV
            Color.colorToHSV(startValue, startHsv);
            Color.colorToHSV(endValue, endHsv);
            // 数据结构解析：
            // startValue 是表示 RGB 的 int 值，而 RGB 有三种颜色，加上透明度，一共四种值；
            // 这四种值，占据了 32 int 值的四个部分：
            // 0 - 7，8 - 15，16 - 23，24 - 32
            // 上述 Color.colorToHSV 函方法，就是通过移位运算和&运算，把每一部分的值提取出来，
            // 存到一个数组中，使得 RGB 每一个值都能用一个单独的变量存储，便于后续计算

            // 核心思路：
            // 让每个颜色的数值独立出来，单独的计算它们的渐变
            // 而不是在一个 int 值里，让这个 int 值渐变

            // 计算当前动画完成度（fraction）所对RGB应的颜色值
            if (endHsv[0] - startHsv[0] > 180) {
                endHsv[0] -= 360;
            } else if (endHsv[0] - startHsv[0] < -180) {
                endHsv[0] += 360;
            }
            outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction;
            if (outHsv[0] > 360) {
                outHsv[0] -= 360;
            } else if (outHsv[0] < 0) {
                outHsv[0] += 360;
            }
            outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction;
            outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction;

            // 计算当前动画完成度（fraction）所对应的透明度
            int alpha = startValue >> 24 + (int) ((endValue >> 24 - startValue >> 24) * fraction);

            // 把 HSV 转换回 ARGB 返回
            return Color.HSVToColor(alpha, outHsv);
        }
    }
}
