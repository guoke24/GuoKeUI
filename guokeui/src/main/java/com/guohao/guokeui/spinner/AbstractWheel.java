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
import android.database.DataSetObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;


import com.guohao.guokeui.R;
import com.guohao.guokeui.spinner.adapters.WheelViewAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract spinner spinnerwheel view.
 * This class should be subclassed.
 *
 * 该顶层父类，定义了：
 *
 * 滚动相关逻辑：配合 scroller 计算偏移量，滚动状态的回调，选中栏的计算；
 * 子item的重建，新增，回收复用，逻辑；
 * 子item的测量，布局，绘制时机的控制；
 *
 * 特别注意：
 * 可见的 items 会被添加到 itemsLayout（LinearLayout），
 * 为此封装了一个 ItemsRange 类，用于标记可见 items 的区间，
 * 映射 items 的列表，采用环形列表逻辑的下标计算，可以有负数。
 *
 */
public abstract class AbstractWheel extends View {

    private static int itemID = -1;

    @SuppressWarnings("unused")
    private final String LOG_TAG = AbstractWheel.class.getName() + " #" + (++itemID);

    //----------------------------------
    //  Default properties values
    //----------------------------------

    /**
     * Default count of visible items
     */
    private static final int DEF_VISIBLE_ITEMS = 4;
    private static final boolean DEF_IS_CYCLIC = false;

    //----------------------------------
    //  Class properties
    //----------------------------------

    protected int mCurrentItemIdx = 0;

    // Count of visible items
    protected int mVisibleItems;
    // Should all items be visible
    protected boolean mIsAllVisible;

    protected boolean mIsCyclic;

    // Scrolling
    protected WheelScroller mScroller;
    protected boolean mIsScrollingPerformed;
    protected int mScrollingOffset;

    // Items layout
    protected LinearLayout mItemsLayout;

    // The number of first item in layout
    // 第一个元素的环形序列下标
    protected int mFirstItemIdx;

    // View adapter
    protected WheelViewAdapter mViewAdapter;

    protected int mLayoutHeight;
    protected int mLayoutWidth;

    // Recycle
    private WheelRecycler mRecycler = new WheelRecycler(this);

    // Listeners
    private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
    private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();

    //XXX: I don't like listeners the way as they are now. -df

    // Adapter listener
    private DataSetObserver mDataObserver;


    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Create a new AbstractWheel instance
     *
     * @param context  the application environment.
     * @param attrs    a collection of attributes.
     * @param defStyle The default style to apply to this view.
     */
    public AbstractWheel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initAttributes(attrs, defStyle);
        initData(context);
    }

    //--------------------------------------------------------------------------
    //
    //  Initiating data and assets at start up
    //
    //--------------------------------------------------------------------------

    /**
     * Initiates data and parameters from styles
     *
     * @param attrs    a collection of attributes.
     * @param defStyle The default style to apply to this view.
     */
    protected void initAttributes(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AbstractWheelView, defStyle, 0);
        mVisibleItems = a.getInt(R.styleable.AbstractWheelView_visibleItems, DEF_VISIBLE_ITEMS);
        mIsAllVisible = a.getBoolean(R.styleable.AbstractWheelView_isAllVisible, false);
        mIsCyclic = a.getBoolean(R.styleable.AbstractWheelView_isCyclic, DEF_IS_CYCLIC);

        a.recycle();
    }

    /**
     * Initiates data
     *
     * @param context the context
     */
    protected void initData(Context context) {

        mDataObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                invalidateItemsLayout(false);
            }

            @Override
            public void onInvalidated() {
                invalidateItemsLayout(true);
            }
        };

        // creating new scroller
        // touchEvent 事件交给 scroller 处理，再回调来这里
        mScroller = createScroller(new WheelScroller.ScrollingListener() {

            // 开始滚动的时候调用
            public void onStarted() {
                mIsScrollingPerformed = true;
                notifyScrollingListenersAboutStart();
                onScrollStarted();
            }

            public void onTouch() {
                onScrollTouched();
            }

            public void onTouchUp() {
                if (!mIsScrollingPerformed) {
                    onScrollTouchedUp(); // if scrolling IS performed, whe should use onFinished instead
                }
            }

            // 滚动过程中会调用多次，具体来说，每一次 Move 事件，就调用一次
            public void onScroll(int distance) {

                // 交给专门的处理函数
                doScroll(distance);

                // 处理偏移量，问题是 mScrollingOffset 表示什么含义？
                // 该部分去掉好像也没啥问题
                int dimension = getBaseDimension();
                if (mScrollingOffset > dimension) {
                    mScrollingOffset = dimension;
                    mScroller.stopScrolling();
                } else if (mScrollingOffset < -dimension) {
                    mScrollingOffset = -dimension;
                    mScroller.stopScrolling();
                }
            }

            public void onFinished() {
                if (mIsScrollingPerformed) {
                    notifyScrollingListenersAboutEnd();
                    mIsScrollingPerformed = false;
                    onScrollFinished();
                }

                mScrollingOffset = 0;
                invalidate();
            }

            public void onJustify() {
                if (Math.abs(mScrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                    mScroller.scroll(mScrollingOffset, 0);
                }
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        //end

        ss.currentItem = this.getCurrentItem();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        mCurrentItemIdx = ss.currentItem;

        // dirty hack to re-draw child items correctly
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateItemsLayout(false);
            }
        }, 100);
    }

    static class SavedState extends BaseSavedState {
        int currentItem;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentItem = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentItem);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    abstract protected void recreateAssets(int width, int height);


    //--------------------------------------------------------------------------
    //
    //  Scroller operations
    //
    //--------------------------------------------------------------------------

    /**
     * Creates scroller appropriate for specific wheel implementation.
     *
     * @param scrollingListener listener to be passed to the scroller
     * @return Initialized scroller to be used
     */
    abstract protected WheelScroller createScroller(WheelScroller.ScrollingListener scrollingListener);

    /* These methods are not abstract, as we may want to override only some of them */
    protected void onScrollStarted() {
    }

    protected void onScrollTouched() {
    }

    protected void onScrollTouchedUp() {
    }

    protected void onScrollFinished() {
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        mScroller.stopScrolling();
    }

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        mScroller.setInterpolator(interpolator);
    }

    /**
     * Scroll the spinnerwheel
     *
     * @param itemsToScroll items to scroll
     * @param time          scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        int distance = itemsToScroll * getItemDimension() - mScrollingOffset;
        onScrollTouched(); // we have to emulate touch when scrolling spinnerwheel programmatically to light up stuff
        mScroller.scroll(distance, time);
    }

    /**
     * Scrolls the spinnerwheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        // delta 是有正负之分的

        mScrollingOffset += delta;

        int itemDimension = getItemDimension();
        int count = mScrollingOffset / itemDimension; // 注释1，偏移了多少个

        int pos = mCurrentItemIdx - count; // 偏移后的选中下标
        int itemCount = mViewAdapter.getItemsCount();

        int fixPos = mScrollingOffset % itemDimension; // 修正偏移后的选中下标，因为注释1处是向下取整的，
        if (Math.abs(fixPos) <= itemDimension / 2) {   // 此处先取余，做一个四舍五入的判断来做修正
            fixPos = 0;
        }
        if (mIsCyclic && itemCount > 0) { // 循环模式

            if (fixPos > 0) { // 说明偏移量是正数，
                pos--;        // 选中的 item 下标，往上挪一格
                count++;      // 滚轮和手指向下多滚一格
            } else if (fixPos < 0) {
                pos++;
                count--;
            }

            // start 环形列表式的修正逻辑
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
            // end 环形列表式的修正逻辑

        } else { // 非循环模式

            if (pos < 0) { // 滚轮往下滚过底，选中栏往上挪到头
                count = mCurrentItemIdx;
                pos = 0;
            } else if (pos >= itemCount) { // 滚轮往上滚到头，选中栏往上挪到底
                count = mCurrentItemIdx - itemCount + 1;
                pos = itemCount - 1;
            } else if (pos > 0 && fixPos > 0) { // 说明偏移量是正数，
                pos--;   // 选中的 item 下标，往上挪一格
                count++; // 滚轮和手指向下多滚一格
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++;
                count--;
            }
        }

        int offset = mScrollingOffset;

        if (pos != mCurrentItemIdx) {
            setCurrentItem(pos, false); // 更新选中的下标 mCurrentItemIdx，会影响到 rebuildItem 函数中，新序列的推算
        } else {
            invalidate();
        }

        // update offset
        int baseDimension = getBaseDimension();

        mScrollingOffset = offset - count * itemDimension;// 滚动完成后，要减去已滚动的item的偏移量，得到剩余的偏移量

        // 万一滚动太多，剩余偏移量超出整个 滚动控件 的高度，则需要修正
        if (mScrollingOffset > baseDimension) {
            // 能进这里，说明 count 负数
            // 问题是，这样计算出的 mScrollingOffset 还是 大于 baseDimension ？？？
            mScrollingOffset = mScrollingOffset % baseDimension + baseDimension;
        }
        // drawItems 函数，会用到 mScrollingOffset 变量
    }

    //--------------------------------------------------------------------------
    //
    //  Base measurements
    //
    //--------------------------------------------------------------------------

    /**
     * Returns base dimension of the spinnerwheel — width for horizontal spinnerwheel, height for vertical
     *
     * @return width or height of the spinnerwheel
     */
    abstract protected int getBaseDimension();

    /**
     * Returns base dimension of base item — width for horizontal spinnerwheel, height for vertical
     *
     * @return width or height of base item
     */
    abstract protected int getItemDimension();

    /**
     * Processes MotionEvent and returns relevant position — x for horizontal spinnerwheel, y for vertical
     *
     * @param event MotionEvent to be processed
     * @return relevant position of the MotionEvent
     */
    abstract protected float getMotionEventPosition(MotionEvent event);


    //--------------------------------------------------------------------------
    //
    //  Layout creation and measurement operations
    //
    //--------------------------------------------------------------------------

    /**
     * Creates item layouts if necessary
     */
    abstract protected void createItemsLayout();

    /**
     * Sets layout width and height
     */
    abstract protected void doItemsLayout();


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int w = r - l;
            int h = b - t;
            doItemsLayout();
            if (mLayoutWidth != w || mLayoutHeight != h) {
                recreateAssets(getMeasuredWidth(), getMeasuredHeight());
            }
            mLayoutWidth = w;
            mLayoutHeight = h;
        }
    }

    /**
     * Invalidates items layout
     *
     * @param clearCaches if true then cached views will be cleared
     */
    public void invalidateItemsLayout(boolean clearCaches) {
        if (clearCaches) {
            mRecycler.clearAll();
            if (mItemsLayout != null) {
                mItemsLayout.removeAllViews();
            }
            mScrollingOffset = 0;
        } else if (mItemsLayout != null) {
            // cache all items
            mRecycler.recycleItems(mItemsLayout, mFirstItemIdx, new ItemsRange());
        }
        invalidate();
    }


    //--------------------------------------------------------------------------
    //
    //  Getters and setters
    //
    //--------------------------------------------------------------------------

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return mVisibleItems;
    }

    /**
     * Sets the desired count of visible items.
     * Actual amount of visible items depends on spinnerwheel layout parameters.
     * To apply changes and rebuild view call measure().
     *
     * @param count the desired count for visible items
     */
    public void setVisibleItems(int count) {
        mVisibleItems = count;
    }

    /**
     * Sets all items to have no dim and makes them visible
     *
     * @param isAllVisible
     */
    public void setAllItemsVisible(boolean isAllVisible) {
        mIsAllVisible = isAllVisible;
        invalidateItemsLayout(false);
    }

    /**
     * Gets view adapter
     *
     * @return the view adapter
     */
    public WheelViewAdapter getViewAdapter() {
        return mViewAdapter;
    }


    /**
     * Sets view adapter. Usually new adapters contain different views, so
     * it needs to rebuild view by calling measure().
     *
     * @param viewAdapter the view adapter
     */
    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.mViewAdapter != null) {
            this.mViewAdapter.unregisterDataSetObserver(mDataObserver);
        }
        this.mViewAdapter = viewAdapter;
        if (this.mViewAdapter != null) {
            this.mViewAdapter.registerDataSetObserver(mDataObserver);
        }
        invalidateItemsLayout(true);
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    public int getCurrentItem() {
        return mCurrentItemIdx;
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    public void setCurrentItem(int index, boolean animated) {
        if (mViewAdapter == null || mViewAdapter.getItemsCount() == 0) {
            return; // throw?
        }

        int itemCount = mViewAdapter.getItemsCount();
        if (index < 0 || index >= itemCount) {
            if (mIsCyclic) {
                while (index < 0) {
                    index += itemCount;
                }
                index %= itemCount;
            } else {
                return; // throw?
            }
        }
        if (index != mCurrentItemIdx) {
            if (animated) {
                int itemsToScroll = index - mCurrentItemIdx;
                if (mIsCyclic) {
                    int scroll = itemCount + Math.min(index, mCurrentItemIdx) - Math.max(index, mCurrentItemIdx);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
            } else {
                mScrollingOffset = 0;
                final int old = mCurrentItemIdx;
                mCurrentItemIdx = index;
                notifyChangingListeners(old, mCurrentItemIdx);
                invalidate();
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * Tests if spinnerwheel is cyclic. That means before the 1st item there is shown the last one
     *
     * @return true if spinnerwheel is cyclic
     */
    public boolean isCyclic() {
        return mIsCyclic;
    }

    /**
     * Set spinnerwheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.mIsCyclic = isCyclic;
        invalidateItemsLayout(false);
    }


    //--------------------------------------------------------------------------
    //
    //  Listener operations
    //
    //--------------------------------------------------------------------------

    /**
     * Adds spinnerwheel changing listener
     *
     * @param listener the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        if (listener != null && !changingListeners.contains(listener)) {
            changingListeners.add(listener);
        }
    }

    /**
     * Removes spinnerwheel changing listener
     *
     * @param listener the listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        if (listener != null && changingListeners.contains(listener)) {
            changingListeners.remove(listener);
        }
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old spinnerwheel value
     * @param newValue the new spinnerwheel value
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        List<OnWheelChangedListener> listeners = new ArrayList<>(changingListeners);
        for (OnWheelChangedListener listener : listeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * Adds spinnerwheel scrolling listener
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        if (listener != null && !scrollingListeners.contains(listener)) {
            scrollingListeners.add(listener);
        }
    }

    /**
     * Removes spinnerwheel scrolling listener
     *
     * @param listener the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        if (listener != null && scrollingListeners.contains(listener)) {
            scrollingListeners.remove(listener);
        }
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
        List<OnWheelScrollListener> listeners = new ArrayList<>(scrollingListeners);
        for (OnWheelScrollListener listener : listeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
        List<OnWheelScrollListener> listeners = new ArrayList<>(scrollingListeners);
        for (OnWheelScrollListener listener : listeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * Adds spinnerwheel clicking listener
     *
     * @param listener the listener
     */
    public void addClickingListener(OnWheelClickedListener listener) {
        clickingListeners.add(listener);
    }

    /**
     * Removes spinnerwheel clicking listener
     *
     * @param listener the listener
     */
    public void removeClickingListener(OnWheelClickedListener listener) {
        clickingListeners.remove(listener);
    }

    /**
     * Notifies listeners about clicking
     *
     * @param item clicked item
     */
    protected void notifyClickListenersAboutClick(int item) {
        List<OnWheelClickedListener> listeners = new ArrayList<>(clickingListeners);
        for (OnWheelClickedListener listener : listeners) {
            listener.onItemClicked(this, item);
        }
    }


    //--------------------------------------------------------------------------
    //
    //  Rebuilding items
    //
    //--------------------------------------------------------------------------

    /**
     * Rebuilds spinnerwheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    protected boolean rebuildItems() {
        Log.i("guohao-spin","===> rebuildItems ");

        boolean updated;
        ItemsRange range = getItemsRange();

        if (mItemsLayout != null) {
            // 回收并删除 mItemsLayout 中那些不在新的环形序列中的 item，返回第一个元素的环形序列下标，不是 mItemsLayout 的下标
            int first = mRecycler.recycleItems(mItemsLayout, mFirstItemIdx, range);
            updated = mFirstItemIdx != first;
            mFirstItemIdx = first;
        } else {
            createItemsLayout();
            updated = true;
        }

        if (!updated) {
            updated = mFirstItemIdx != range.getFirst() || mItemsLayout.getChildCount() != range.getCount();
        }

        // 添加顶部空缺的 items
        if (mFirstItemIdx > range.getFirst() && mFirstItemIdx <= range.getLast()) {
            // 添加「当前的第一个元素 和 新环形序列的第一个元素 之间空缺的元素」到 mItemsLayout
            for (int i = mFirstItemIdx - 1; i >= range.getFirst(); i--) {
                if (!addItemView(i, true)) {
                    break;
                }
                mFirstItemIdx = i;// 更新第一个元素的环形序列下标
            }
        } else {
            mFirstItemIdx = range.getFirst();
        }

        int first = mFirstItemIdx;
        // 添加底部空缺的 items
        for (int i = mItemsLayout.getChildCount(); i < range.getCount(); i++) {
            //
            if (!addItemView(mFirstItemIdx + i, false) && mItemsLayout.getChildCount() == 0) {
                first++;
            }
        }
        mFirstItemIdx = first;

        Log.i("guohao-spin","rebuildItems: " + updated);
        return updated;
    }

    //----------------------------------
    //  ItemsRange operations
    //----------------------------------

    /**
     * Calculates range for spinnerwheel items
     *
     * @return the items range
     */

    /**
     * 主要根据 mCurrentItemIdx 推算出要显示的序列，而 mCurrentItemIdx 是在滚动的时候被更新的
     *
     * 默认把第一位放中间并显示，下标为 0；
     * 往上，下标递减；往下，下标递增；
     *
     * @return ItemsRange，封装了三个值：上边缘的下标，下边缘的下标，可见数量
     */
    private ItemsRange getItemsRange() {
        Log.i("guohao-spin-aw","===> getItemsRange ");

        if (mIsAllVisible) {
            int baseDimension = getBaseDimension();
            int itemDimension = getItemDimension();
            Log.i("guohao-spin-aw"," baseDimension = " + baseDimension);
            Log.i("guohao-spin-aw"," itemDimension = " + itemDimension);
            if (itemDimension != 0) {
                mVisibleItems = baseDimension / itemDimension + 1;
            }
        }
        // 根据总高度和每个item的高度，算出可见 item 的数量

        Log.i("guohao-spin-aw"," mVisibleItems = " + mVisibleItems);

        int start = mCurrentItemIdx - mVisibleItems / 2;
        int end = start + mVisibleItems - (mVisibleItems % 2 == 0 ? 0 : 1); // 可见为奇数，下边缘的下标要减1
        Log.i("guohao-spin-aw"," 0 start = " + start + "，end = " + end);

        // 有偏移，item 的数量，调整边缘的下标，要么 start 往上挪一位，要么 end 往下挪一位
        if (mScrollingOffset != 0) {
            Log.i("guohao-spin-aw","mScrollingOffset =  " + mScrollingOffset);
            if (mScrollingOffset > 0) { // 朝下偏移
                start--; // 开始下标再上移一位
                Log.i("guohao-spin-aw","start--");
            } else {
                end++;
                Log.i("guohao-spin-aw","end++");
            }
        }
        Log.i("guohao-spin-aw"," 1 start = " + start + "，end = " + end);

        // 不支持循环，就修正 start 和 end 的数值
        if (!isCyclic()) {
            if (start < 0) {
                start = 0;
            }
            if (end > mViewAdapter.getItemsCount()) {
                end = mViewAdapter.getItemsCount();
            }
        }

        return new ItemsRange(start, end - start + 1);
    }

    /**
     * Checks whether item index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the spinnerwheel is cyclic
     */
    protected boolean isValidItemIndex(int index) {
        return (mViewAdapter != null) && (mViewAdapter.getItemsCount() > 0) &&
                (mIsCyclic || (index >= 0 && index < mViewAdapter.getItemsCount()));
    }

    //----------------------------------
    //  Operations with item view
    //----------------------------------

    /**
     * Adds view for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private boolean addItemView(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            if (first) {
                mItemsLayout.addView(view, 0);
            } else {
                mItemsLayout.addView(view);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns view for specified item
     *
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private View getItemView(int index) {
        if (mViewAdapter == null || mViewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = mViewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return mViewAdapter.getEmptyItem(mRecycler.getEmptyItem(), mItemsLayout);
        } else {
            while (index < 0) {
                index = count + index;// 关键点1，处理了小于最小值的下标
            }
        }
        index %= count;// 关键点2，处理了大于最大值的下标
        // 经过关键点1和关键点2，把环形序列下标，转换成正常的下标，实现循环的滚动
        return mViewAdapter.getItem(index, mRecycler.getItem(), mItemsLayout);
    }


    //--------------------------------------------------------------------------
    //
    //  Intercepting and processing touch event
    //
    //--------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:

                if (!mIsScrollingPerformed) { // 如果不是在处理滚动，则进入

                    // 手指抬起的位置 和 中线 的距离（此处以垂直滚轮为例）
                    int distance = (int) getMotionEventPosition(event) - getBaseDimension() / 2;

                    if (distance > 0) { // 在下半区
                        distance += getItemDimension() / 2;
                    } else { // 在上半区
                        distance -= getItemDimension() / 2;
                    }
                    // 为何补上半个 item 的高？
                    // 因为中间 item 被中线分为上下两半，
                    // 抬起的点，只要偏离中线半个 item 的高以上，则认为偏离了一个 item，
                    // 补上半个 item 的高，为了下面的运算；
                    // 若 x > 0.5，则 x + 0.5 > 1；int 的 除运算，向下取整；

                    // 反正，items 表示 手指抬起的位置 偏移了多少个 item
                    int items = distance / getItemDimension();

                    // 若确实偏移了 item，且偏移到的位置所在，是合法的 item，则回调点击，
                    // 即点击事件的 itemClick 的逻辑，注意只有 up 事件和非滚动状态会走这里
                    if (items != 0 && isValidItemIndex(mCurrentItemIdx + items)) {
                        notifyClickListenersAboutClick(mCurrentItemIdx + items);
                    }
                }
                break;
        }
        //触摸手势交给 WheelScroller
        return mScroller.onTouchEvent(event);
    }

}
