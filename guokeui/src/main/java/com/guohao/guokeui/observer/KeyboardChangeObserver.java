package com.guohao.guokeui.observer;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardChangeObserver implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "KeyboardChangeListener";
    private View mRootView;
    private int mOriginHeight;
    private int mPreHeight;
    private KeyBoardListener mKeyBoardListen;
    private long delay = 100; // 默认延迟100毫秒
    private boolean isKeyBoardShow = false;

    public interface KeyBoardListener {
        /**
         * @param isShow 软键盘状态，true 显示，false 隐藏
         * @param keyboardHeight 软键盘高度
         */
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isKeyBoardShow(){
        return isKeyBoardShow;
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        this.mKeyBoardListen = keyBoardListen;
    }

    public KeyboardChangeObserver(Activity context) {
        if (context == null) {
            return;
        }
        mRootView = context.getWindow().getDecorView();
    }

    public void addContentTreeObserver() {
        if (mRootView != null)
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void removeKeyBoardListener() {
        if (mRootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        mRootView.postDelayed(()->{
            checkKeyboardShow();
        },delay);
    }

    private void checkKeyboardShow(){

        //int currHeight = mContentView.getHeight();
        final Rect r =new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);
        int currHeight = r.height();
        if (currHeight == 0) {
            return;
        }
        //布局高度是否发生变化
        boolean hasChange = false;
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
            mOriginHeight = currHeight;
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true;
                mPreHeight = currHeight;
            } else {
                hasChange = false;
            }
        }

        if (hasChange) {//判断软键盘高度变化是否显示还是隐藏
            int keyboardHeight = 0;
            if (mOriginHeight == currHeight) {
                isKeyBoardShow = false;
            } else {
                keyboardHeight = mOriginHeight - currHeight;
                isKeyBoardShow = true;
            }

            if (mKeyBoardListen != null) {
                mKeyBoardListen.onKeyboardChange(isKeyBoardShow, keyboardHeight);
            }
        }
    }
}
