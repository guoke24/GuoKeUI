package com.guohao.guokeui.smallapp.animator;

import android.view.animation.Interpolator;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;

/**
 * 借用来强大的轮子：github - RecyclerView Animators，
 * https://github.com/wasabeef/recyclerview-animators
 *
 *
 *
 */
class SmallItemAnimator extends BaseItemAnimator {

    public SmallItemAnimator(){

    }

    public SmallItemAnimator(Interpolator interpolator) {
        setInterpolator(interpolator);
    }

    // 移除动画的最终状态
    @Override protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        holder.itemView.animate()
                .translationY(holder.itemView.getHeight()) // 向下移动一个 item 的高度
                .alpha(0)                                  // 变淡为 0
                .setDuration(getRemoveDuration())   // 设置移除时长
                .setInterpolator(getInterpolator()) // 设置插值器
                .setListener(new DefaultRemoveAnimatorListener(holder)) // 移除后的监听
                .setStartDelay(getRemoveDelay(holder)) // 设置开始的延时
                .start();
    }

    // 添加动画开始前的状态
    @Override protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
//        ViewCompat.setTranslationY(holder.itemView, holder.itemView.getHeight());
//        ViewCompat.setAlpha(holder.itemView, 0);

        holder.itemView.animate().translationY(holder.itemView.getHeight());
        holder.itemView.animate().alpha(0);
    }

    // 添加动画的最终状态
    @Override protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        holder.itemView.animate()
                .translationY(0)// 移动到 0 的位置
                .alpha(1)       // 不透明
                .setDuration(getAddDuration())     // 设置移除时长
                .setInterpolator(getInterpolator())// 设置插值器
                .setListener(new DefaultAddAnimatorListener(holder)) // 添加后的监听
                .setStartDelay(getAddDelay(holder)) // 设置开始的延时
                .start();
    }
}

