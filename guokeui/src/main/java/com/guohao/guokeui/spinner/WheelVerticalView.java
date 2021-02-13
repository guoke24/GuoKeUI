/*
 * android-spinnerwheel
 * https://github.com/ai212983/android-spinnerwheel
 *
 * based on
 *
 * Android Wheel Control.
 * https://code.google.com/p/android-wheel/
 *
 * Copyright 2011 Yuri Kanivets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guohao.guokeui.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.guohao.guokeui.R;


/**
 * Spinner wheel vertical view.
 */
public class WheelVerticalView extends AbstractWheelView {

    private static int itemID = -1;

    @SuppressWarnings("unused")
    private final String LOG_TAG = WheelVerticalView.class.getName() + " #" + (++itemID);

    /**
     * The height of the selection divider.
     */
    protected int mSelectionDividerHeight;

    // Cached item height
    private int mItemHeight = 0;

    //--------------------------------------------------------------------------
    //
    //  Constructors
    //
    //--------------------------------------------------------------------------

    /**
     * Create a new wheel vertical view.
     *
     * @param context The application environment.
     */
    public WheelVerticalView(Context context) {
        this(context, null);
    }

    /**
     * Create a new wheel vertical view.
     *
     * @param context The application environment.
     * @param attrs A collection of attributes.
     */
    public WheelVerticalView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.abstractWheelViewStyle);
    }

    /**
     * Create a new wheel vertical view.
     *
     * @param context the application environment.
     * @param attrs a collection of attributes.
     * @param defStyle The default style to apply to this view.
     */
    public WheelVerticalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    //--------------------------------------------------------------------------
    //
    //  Initiating assets and setter for selector paint
    //
    //--------------------------------------------------------------------------

    @Override
    protected void initAttributes(AttributeSet attrs, int defStyle) {
        super.initAttributes(attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WheelVerticalView, defStyle, 0);
        mSelectionDividerHeight = a.getDimensionPixelSize(R.styleable.WheelVerticalView_selectionDividerHeight, DEF_SELECTION_DIVIDER_SIZE);
        a.recycle();
    }

    public void setSelectionDividerHeight(int dividerHeight) {
        this.mSelectionDividerHeight = dividerHeight;
    }

    // coeff 为透明度，从 0 到 1
    @Override
    public void setSelectorPaintCoeff(float coeff) {
        LinearGradient shader; // 线性渐变

        int h = getMeasuredHeight(); // 自身高度
        int ih = getItemDimension(); // item高度

        // 位置计算逻辑是啥？假设 item 高度 1，整体高度 5；
        float p1 = (1 - ih/(float) h)/2; // 2/5 = 0.4
        float p2 = (1 + ih/(float) h)/2; // 3/5 = 0.6

        // 颜色渐变的计算逻辑，
        // mItemsDimmedAlpha 默认 50，假设 coeff 0.6，
        float z = mItemsDimmedAlpha * (1 - coeff); // z = 20
        float c1f = z + 255 * coeff; // c1f = 20 + 153 = 173，这是挨着选中栏的边缘，也是非选中区的默认透明度

        if (mVisibleItems == 2) {
            int c1 = Math.round( c1f ) << 24; // 173 的透明度
            int c2 = Math.round( z ) << 24;   // 20 的透明度
            int[] colors =      {c2, c1, 0xff000000, 0xff000000, c1, c2};
            float[] positions = { 0, p1,     p1,         p2,     p2,  1};
            // p1,p2之间是黑色的字体

            shader = new LinearGradient(0, 0, 0, h, colors, positions, Shader.TileMode.CLAMP); // 重复边缘色
        } else {
            // 假设 item 高度 1，整体高度 5；
            float p3 = (1 - ih*3/(float) h)/2; // 1/5 = 0.3
            float p4 = (1 + ih*3/(float) h)/2; // 4/5 = 0.8

            float s = 255 * p3/p1; // 255 * 0.3 / 0.4 = 191.25
            // here goes some optimized stuff
            float c3f = s * coeff ; // 191.25 * 0.6 = 114.75
            float c2f = z + c3f;    // 20 + 114.75 = 134.75

            // round 函数，上加0.5然后进行下取整；左移24位，就是移到透明度的位置，一个颜色的16进制如 ff009900；
            int c1 = Math.round( c1f ) << 24; // 173，一环的透明度（挨着选中栏的边缘）
            int c2 = Math.round( c2f ) << 24; // 134.75，二环的透明度
            int c3 = Math.round( c3f ) << 24; // 114.75，三环的透明度

            int[] colors =      {0, c3, c2, c1, 0xff000000, 0xff000000, c1, c2, c3, 0};
            float[] positions = {0, p3, p3, p1,     p1,         p2,     p2, p4, p4, 1};
                                  //0.3,0.3,0.4,    0.4         0.6    0.6, 0.8, 0.8
            // p1,p2之间是黑色的字体

            shader = new LinearGradient(0, 0, 0, h, colors, positions, Shader.TileMode.CLAMP);
        }
        mSelectorWheelPaint.setShader(shader);
        invalidate();
    }


    //--------------------------------------------------------------------------
    //
    //  Scroller-specific methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected WheelScroller createScroller(WheelScroller.ScrollingListener scrollingListener) {
        return new WheelVerticalScroller(getContext(), scrollingListener);
    }

    @Override
    protected float getMotionEventPosition(MotionEvent event) {
        return event.getY();
    }

    //--------------------------------------------------------------------------
    //
    //  Base measurements
    //
    //--------------------------------------------------------------------------

    // 获取 自身的高
    @Override
    protected int getBaseDimension() {
        return getHeight();
    }

    // 获取 item 的高
    @Override
    protected int getItemDimension() {
        Log.i("guohao-spin-aw","===> getItemDimension ");
        if (mItemHeight != 0) {
            return mItemHeight;
        }

        // 存在子 view 即滚轮试图，直接用其 item 的高度
        if (mItemsLayout != null && mItemsLayout.getChildAt(0) != null) {
            mItemHeight = mItemsLayout.getChildAt(0).getMeasuredHeight();
            Log.i("guohao-spin-aw","直接用 item 的高度 ");
            Log.i("guohao-spin-aw","<=== getItemDimension ");
            return mItemHeight;
        }

        Log.i("guohao-spin-aw","总高度 / 可见 item 数 ");
        Log.i("guohao-spin-aw","<=== getItemDimension ");

        // 总高度 / 可见 item 数
        return getBaseDimension() / mVisibleItems;
    }

    //--------------------------------------------------------------------------
    //
    //  Layout creation and measurement operations
    //
    //--------------------------------------------------------------------------

    /**
     * Creates item layout if necessary
     */
    @Override
    protected void createItemsLayout() {
        if (mItemsLayout == null) {
            mItemsLayout = new LinearLayout(getContext());
            mItemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    // 每次 draw 的时候，调用此函数，重新确定位置
    @Override
    protected void doItemsLayout() {
        // 真正决定子 view 的大小
        mItemsLayout.layout(0, 0, getMeasuredWidth() - 2 * mItemsPadding, getMeasuredHeight());
    }


    // 子 view 的测量
    // 每次 draw 的时候，如果重建来 items，则调用此函数
    @Override
    protected void measureLayout() {
        mItemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // 宽度有精确值，高度不做限制
        mItemsLayout.measure(
                MeasureSpec.makeMeasureSpec(getWidth() - 2 * mItemsPadding, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        
    }

    // 自身的测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        rebuildItems(); // rebuilding before measuring

        // 宽度逻辑
        // 测量子 view 的宽，来作为自己的宽
        int width = calculateLayoutWidth(widthSize, widthMode);

        // 高度逻辑
        //
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            // UNSPECIFIED ，求最大值
            height = Math.max(
                    getItemDimension() * (mVisibleItems - mItemOffsetPercent / 100),
                    getSuggestedMinimumHeight()
            );

            // AT_MOST ，求最小值
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        // 保存宽和高
        setMeasuredDimension(width, height);
    }

    // 计算 view 的宽
    // 在 自身的测量的 onMeasure 的时候调用
    private int calculateLayoutWidth(int widthSize, int mode) {
        mItemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mItemsLayout.measure(
                MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        int width = mItemsLayout.getMeasuredWidth();

        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width += 2 * mItemsPadding;

            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }

        // forcing recalculating
        mItemsLayout.measure(
                MeasureSpec.makeMeasureSpec(width - 2 * mItemsPadding, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        // 注意，宽度是 EXACTLY，高度是 UNSPECIFIED，即不做限制。

        return width;
    }


    //--------------------------------------------------------------------------
    //
    //  Drawing items
    //  绘制自身选中item的这一栏的上下分割线，
    //  触发 子veiw 即 LinearLayout 的绘制流程，交给其一个 canvas，
    //
    //--------------------------------------------------------------------------
    @Override
    protected void drawItems(Canvas canvas) {
        canvas.save();
        // 整个滚轮的宽
        int w = getMeasuredWidth();
        // 整个滚轮的高
        int h = getMeasuredHeight();
        // 每个item的高
        int ih = getItemDimension();

        // resetting intermediate bitmap and recreating canvases
        mSpinBitmap.eraseColor(0);// 用指定颜色填满 bitmap，相当于重置的作用
        Canvas c = new Canvas(mSpinBitmap);     // 「滚轮canvas（给LinearLayout用的）」绑定「滚轮位图」
        Canvas cSpin = new Canvas(mSpinBitmap); // 「滚轮canvas」绑定「滚轮位图」

        // 计算出 top 偏移量，子view 即 LinearLayout 偏移后再绘制；这就是能滑动的处理逻辑的最后一环
        // 那怎么处理循环的逻辑？
        int top = (mCurrentItemIdx - mFirstItemIdx) * ih + (ih - getHeight()) / 2;
        c.translate(mItemsPadding, - top + mScrollingOffset);
        mItemsLayout.draw(c);

        mSeparatorsBitmap.eraseColor(0);
        Canvas cSeparators = new Canvas(mSeparatorsBitmap);

        // 选择分割线
        if (mSelectionDivider != null) {
            // draw the top divider，选中栏的上端分割线
            int topOfTopDivider = (getHeight() - ih - mSelectionDividerHeight) / 2;
            int bottomOfTopDivider = topOfTopDivider + mSelectionDividerHeight;
            mSelectionDivider.setBounds(0, topOfTopDivider, w, bottomOfTopDivider);
            mSelectionDivider.draw(cSeparators); // 「Drawable 的内容」 -> 「分割线canvas」

            // draw the bottom divider，选中栏的下端分割线
            int topOfBottomDivider =  topOfTopDivider + ih;
            int bottomOfBottomDivider = bottomOfTopDivider + ih;
            mSelectionDivider.setBounds(0, topOfBottomDivider, w, bottomOfBottomDivider);
            mSelectionDivider.draw(cSeparators); // 「Drawable 的内容」 -> 「分割线canvas」
        }

        // 画一个矩形到 -> 「滚轮位图 mSpinBitmap」
        cSpin.drawRect(0, 0, w, h, mSelectorWheelPaint);// 着色器有渐变效果
        // 画一个矩形到 -> 「分割线位图 mSeparatorsBitmap」
        cSeparators.drawRect(0, 0, w, h, mSeparatorsPaint);
        //「滚轮位图 mSpinBitmap」的内容 ->「主canvas」
        canvas.drawBitmap(mSpinBitmap, 0, 0, null);
        //「分割线位图 mSeparatorsBitmap」的内容 ->「主canvas」
        canvas.drawBitmap(mSeparatorsBitmap, 0, 0, null);
        canvas.restore();
    }

}
