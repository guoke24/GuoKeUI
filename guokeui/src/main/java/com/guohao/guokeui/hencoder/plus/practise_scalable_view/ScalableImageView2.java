package com.guohao.guokeui.hencoder.plus.practise_scalable_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.guohao.guokeui.hencoder.plus.Utils;

/**
 * 一个 Demo，可放缩的ImageView
 * 支持双击放大，缩小
 * 支持放大后拖动，惯性滑动
 * 支持手势捏合放缩
 *
 * 带有动画过渡，偏移修正，边界控制，提供更好的体验
 *
 * 还未实现的：仅仅双击放大后，才能拖动和滑动
 *
 */
public class ScalableImageView2 extends View {
    private static final float IMAGE_SIZE = Utils.dp2px(200);
    private static final float SCALE_OVER_FACTOR = 2;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float imageWidth = IMAGE_SIZE;
    float imageHeight;
    Bitmap bitmap;
    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY; // 为了让图片居中，不然图片的上边就会贴顶部
    float smallScale; // 图片放缩的最小倍数
    float bigScale;   // 图片放缩的最大倍数
    boolean big;
    float currentScale = 0; // currentScale 就是图片放缩多少倍
    ObjectAnimator scalingAnimator;

    GestureDetector gestureDetector;
    GestureDetector.OnGestureListener gestureListener = new HenGestureListener();
    GestureDetector.OnDoubleTapListener doubleTapListener = new HenDoubleTapListener();
    Runnable flingRunner = new FlingRunner();
    OverScroller scroller;
    HenOnScaleGestureListener henOnScaleGestureListener = new HenOnScaleGestureListener();
    ScaleGestureDetector mScaleGestureDetector;


    public ScalableImageView2(Context context) {
        super(context);
        init(context);
    }

    public ScalableImageView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        bitmap = Utils.getAvatar(getResources(), (int) imageWidth);
        imageHeight = bitmap.getHeight();

        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setOnDoubleTapListener(doubleTapListener);
        scroller = new OverScroller(context);

        mScaleGestureDetector = new ScaleGestureDetector(context,henOnScaleGestureListener);
    }

    // 属性动画需要
    public float getCurrentScale() {
        return currentScale;
    }
    // 属性动画需要
    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    // 记录原始偏移量（让图片居中）
    // 根据宽高比，算出「偏胖和偏廋的图片」的放大缩小的比例
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - imageWidth) / 2;
        originalOffsetY = (getHeight() - imageHeight) / 2;

        if (imageWidth / imageHeight > (float) getWidth() / getHeight()) {
            // 图片比View更扁，用宽做计算
            smallScale = getWidth() / imageWidth;// 最小放缩到图片的宽跟控件的宽一致
            bigScale = getHeight() / imageHeight * SCALE_OVER_FACTOR; // 最大放缩到图片的高是控件的2倍
        } else {
            smallScale = getHeight() / imageHeight;
            bigScale = getWidth() / imageWidth * SCALE_OVER_FACTOR;
        }
        currentScale = smallScale;//确定控件的尺寸时，当前放缩倍数就是最小值
        getScalingAnimator().setFloatValues(smallScale,bigScale);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        if(!mScaleGestureDetector.isInProgress()){
            gestureDetector.onTouchEvent(event);
        }
        return true;
    }

    // 最后的绘制，所有的动画，放缩，拖动滑动，都得走绘制流程，才能实现
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 缩小时，偏移量为 0，图片默认居中；
        // 放大时，偏移量不为 0，可拖动；
        // scalingFraction 表示动画的进度，0f -> 1f；
        float scalingFraction = (currentScale - smallScale)/(bigScale - smallScale);

        // offsetX 偏移量跟动画的进度成正比，当放缩时，过渡比较自然。
        canvas.translate(offsetX * scalingFraction, offsetY * scalingFraction);

        // 图片的放缩程度通过 currentScale 受到 ObjectAnimator 的控制
        // 默认以控件的中心点作为放缩的中心，若有偏移，则放缩中心也会偏移
        canvas.scale(currentScale, currentScale, getWidth() / 2, getHeight() / 2);

        // 图片默认居中
        canvas.translate(originalOffsetX, originalOffsetY);

        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    // 动画过渡
    ObjectAnimator getScalingAnimator() {
        if (scalingAnimator == null) {
            scalingAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale);
            scalingAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    if (isReverse) {
                        offsetX = offsetY = 0; // 缩小时，偏移量为 0，图片默认居中；
                    }
                }
            });
        }
        return scalingAnimator;
    }

    // 拖动 和 惯性滑动
    class HenGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // 手指拖动
        // 这里的边界计算逻辑，都是遵循：放大后的拖动或滑动，边界不得有缝
        // distanceX、distanceY 的值是旧位置减去新位置得到的，而其他一般设计为新位置减去旧位置
        @Override
        public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX, float distanceY) {

            if(big){
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffsets();
                invalidate();
            }

            return false;
        }

        // 惯性滑动（手指离开后），传入的参数是速度
        // 需要借助 scroller 的 fling 方法，计算速度和位移。
        @Override
        public boolean onFling(MotionEvent down, MotionEvent event, float velocityX, float velocityY) {
            if(big){
                // offsetX，offsetY 作为其实位置，再给一个速度，就能计算随时间的变化了。
                scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        (int) (getWidth() - imageWidth * bigScale) / 2   /*往左最多移这么多*/,
                        (int) (imageWidth * bigScale - getWidth()) / 2   /*往右最多移这么多*/,
                        (int) (getHeight() - imageHeight * bigScale) / 2 /*往上最多移这么多*/,
                        (int) (imageHeight * bigScale - getHeight()) / 2 /*往下最多移这么多*/);
                postOnAnimation(flingRunner);
            }
            return false;
        }
    }

    // 偏移量修正
    // 演绎：图片放大后，比屏幕大。
    // 当往左拖动时，如果图片右边碰到屏幕右边，就不能再往左拖（左偏移量有最小值）。
    // 修正就是在做这个事情，控制 offsetX 的最小值。
    private void fixOffsets(){
        offsetX = Math.min(offsetX, (imageWidth * bigScale - getWidth()) / 2);
        offsetX = Math.max(offsetX, - (imageWidth * bigScale - getWidth()) / 2);
        offsetY = Math.min(offsetY, (imageHeight * bigScale - getHeight()) / 2);
        offsetY = Math.max(offsetY, - (imageHeight * bigScale - getHeight()) / 2);
    }

    // 双击放缩
    class HenDoubleTapListener implements GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            big = !big;
            if (big) {

                offsetX = (e.getX() - getWidth()/2) * (1 - bigScale/smallScale);
                offsetY = (e.getY() - getHeight()/2) * (1 - bigScale/smallScale);
                fixOffsets();

                getScalingAnimator().start();
            } else {
                getScalingAnimator().reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

    // 惯性滑动的自动连续执行
    class FlingRunner implements Runnable {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()/*计算在继续，滑动继续*/) {
                offsetX = scroller.getCurrX(); // 获取当前这一刻的位置，scroller 的 fling 方法传入了 偏移量
                offsetY = scroller.getCurrY();
                invalidate();

                postOnAnimation(this);
            }
        }
    }

    // 手势放缩
    class HenOnScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // detector 的 getScaleFactor() 返回的是
            // 以「前一个」为参照得到的方式比例
            float temp = currentScale * detector.getScaleFactor();
            if(temp <= bigScale && temp >= smallScale){
                currentScale = temp;
                invalidate();
                return true;
            }else{
                return false;
            }

            // true 表示消费，这导致 detector.getScaleFactor() 下次返回的是跟此刻状态的比值
            // false 表示不消费，这导致 detector.getScaleFactor() 下次返回的是跟初始状态的比值
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 从点击的地方放缩！
            offsetX = (detector.getFocusX() - getWidth()/2) * (1 - bigScale/smallScale);
            offsetY = (detector.getFocusY() - getHeight()/2) * (1 - bigScale/smallScale);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) { }
    }
}