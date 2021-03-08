package com.guohao.guokeui.hencoder.hencoderpracticedraw7.sample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.guohao.guokeui.R;

/**
 * AnimatorSet 多个动画配合执行
 *
 * 有的时候，你不止需要在一个动画中改变多个属性，还会需要多个动画配合工作，
 * 比如，在内容的大小从 0 放大到 100% 大小后开始移动。
 * 这种情况使用 PropertyValuesHolder 是不行的，因为这些属性如果放在同一个动画中，
 * 需要共享动画的开始时间、结束时间、Interpolator 等等一系列的设定，这样就不能有先后次序地执行动画了。
 *
 * 这就需要用到 AnimatorSet 了。
 *
 */
public class Sample05AnimatorSetLayout extends RelativeLayout {
    View view;
    Button animateBt;

    public Sample05AnimatorSetLayout(Context context) {
        super(context);
    }

    public Sample05AnimatorSetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample05AnimatorSetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setTranslationX(-200f);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationX", -200, 200);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "rotation", 0, 1080);
                animator3.setDuration(1000);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animator1).before(animator2); // 先执行 1 再执行 2
                animatorSet.playTogether(animator2, animator3); // 2 和 3 同时开始

                animatorSet.start();
            }
        });
    }
}
