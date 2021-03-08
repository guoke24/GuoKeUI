package com.guohao.guokeui.hencoder.hencoderpracticedraw7.sample;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.guohao.guokeui.R;

public class Sample04PropertyValuesHolderLayout extends RelativeLayout {
    View view;
    Button animateBt;

    public Sample04PropertyValuesHolderLayout(Context context) {
        super(context);
    }

    public Sample04PropertyValuesHolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample04PropertyValuesHolderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

                // PropertyValuesHolder 的意思从名字可以看出来，它是一个属性值的批量存放地。
                // 所以你如果有多个属性需要修改，可以把它们放在不同的 PropertyValuesHolder 中，
                // 然后使用 ofPropertyValuesHolder() 统一放进 Animator。
                // 这样你就不用为每个属性单独创建一个 Animator 分别执行了。
                PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
                PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
                PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha", 0, 1);

                // 复合型的动画效果
                ObjectAnimator.ofPropertyValuesHolder(view, holder1, holder2, holder3).start();
            }
        });
    }
}
