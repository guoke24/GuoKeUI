package com.guohao.guokeui.hencoder.hencoderpracticedraw7.sample.sample06;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.guohao.guokeui.R;

/**
 * PropertyValuesHolders.ofKeyframe() 把同一个属性拆分
 *
 * 除了合并多个属性和调配多个动画，你还可以在 PropertyValuesHolder 的基础上更进一步，
 * 通过设置 Keyframe （关键帧），把同一个动画属性拆分成多个阶段。
 * 例如，你可以让一个进度增加到 100% 后再「反弹」回来。
 *
 */
public class Sample06KeyframeLayout extends RelativeLayout {
    Sample06KeyframeView view;
    Button animateBt;

    public Sample06KeyframeLayout(Context context) {
        super(context);
    }

    public Sample06KeyframeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample06KeyframeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Sample06KeyframeView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 在 0% 处开始
                Keyframe keyframe1 = Keyframe.ofFloat(0, 0); // 开始：progress 为 0

                // 时间经过 50% 的时候，动画完成度 100%
                Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100); // 进行到一半是，progres 为 100

                // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
                Keyframe keyframe3 = Keyframe.ofFloat(1, 80); // 结束时倒回到 80

                // 三个关键帧，绑定 progress 字段
                PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("progress", keyframe1, keyframe2, keyframe3);

                // 再绑定 view 实例
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, holder);
                animator.setDuration(2000);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.start();
            }
        });
    }
}
