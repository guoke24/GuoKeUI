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

import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Recycle stored spinnerwheel items to reuse.
 */
public class WheelRecycler {


    @SuppressWarnings("unused")
    private static final String LOG_TAG = WheelRecycler.class.getName();

    // Cached items
    private List<View> items;

    // Cached empty items
    private List<View> emptyItems;

    // Wheel view
    private AbstractWheel wheel;

    /**
     * Constructor
     * @param wheel the spinnerwheel view
     */
    public WheelRecycler(AbstractWheel wheel) {
        this.wheel = wheel;
    }

    /**
     * Recycles items from specified layout.
     * There are saved only items not included to specified range.
     * All the cached items are removed from original layout.
     *
     * @param layout the layout containing items to be cached
     * @param firstItem the number of first item in layout
     * @param range the range of current spinnerwheel items
     * @return the new value of first item number
     */
    // 简单理解：回收并删除哪些不在新的序列中的元素，range 代表新序列，圆形数组的结构
    public int recycleItems(LinearLayout layout, int firstItem, ItemsRange range) {
        //Log.i("guohao-spin","===> recycleItems ");

        int index = firstItem; // 上边缘的下标，旧值
        for (int i = 0; i < layout.getChildCount();) {
            if (!range.contains(index)) { // 新的序列中，不包含 index 指向的元素
                recycleView(layout.getChildAt(i), index); // 先缓存
                layout.removeViewAt(i); // 再移除
                if (i == 0) { // first item
                    //如果移除的不是第一个，说明第一个还在，则不用更新 firstItem
                    firstItem++;
                }
            } else {
                i++; // go to next item
            }
            index++;//每次循环下标都会增加
        }
        return firstItem;
    }

    /**
     * Gets item view
     * @return the cached view
     */
    public View getItem() {
        return getCachedView(items);
    }

    /**
     * Gets empty item view
     * @return the cached empty view
     */
    public View getEmptyItem() {
        return getCachedView(emptyItems);
    }

    /**
     * Clears all views
     */
    public void clearAll() {
        if (items != null) {
            items.clear();
        }
        if (emptyItems != null) {
            emptyItems.clear();
        }
    }

    /**
     * Adds view to specified cache. Creates a cache list if it is null.
     * @param view the view to be cached
     * @param cache the cache list
     * @return the cache list
     */
    private List<View> addView(View view, List<View> cache) {
        if (cache == null) {
            cache = new LinkedList<View>();
        }

        cache.add(view);
        return cache;
    }

    /**
     * Adds view to cache. Determines view type (item view or empty one) by index.
     * @param view the view to be cached
     * @param index the index of view
     */
    private void recycleView(View view, int index) {
        int count = wheel.getViewAdapter().getItemsCount();

        if ((index < 0 || index >= count) && !wheel.isCyclic()) {
            // empty view
            emptyItems = addView(view, emptyItems);
        } else {
            while (index < 0) {
                index = count + index;
            }
            index %= count;
            items = addView(view, items);
        }
    }

    /**
     * Gets view from specified cache.
     * @param cache the cache
     * @return the first view from cache.
     */
    private View getCachedView(List<View> cache) {
        if (cache != null && cache.size() > 0) {
            View view = cache.get(0);
            cache.remove(0);
            return view;
        }
        return null;
    }

}
