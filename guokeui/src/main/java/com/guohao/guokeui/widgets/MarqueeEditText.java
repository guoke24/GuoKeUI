package com.guohao.guokeui.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guohao.guokeui.observer.KeyboardChangeObserver;
import com.guohao.guokeui.view.MarqueeView2;
import com.guohao.guokeuiproj.R;

/**
 * 基于 MarqueeView2 和 EditText 实现的，带跑马灯效果的编辑框
 * 输入完内容，当键盘隐藏后，开启跑马灯效果
 *
 * 目前还有许多待完善的地方：
 * 提供 xml 属性配置能力，包括：
 * 1，编辑框字体和背景颜色，编辑框字体大小
 * 2，跑马灯字体和背景颜色，编辑框字体大小
 * 3，或者把上述统一为：字体和背景颜色，字体大小
 * 4，跑马灯的速度，方向，起点等
 *
 * 开放一些接口能力：
 * 1，开启，停止跑马灯
 * 2，是否自动滚动（监听键盘隐藏后滚动）
 *
 * Activity 的生命周期问题，暂时通过 onWindowVisibilityChanged 解决
 *
 */
public class MarqueeEditText extends FrameLayout {

    public MarqueeEditText(@NonNull Context context) {
        super(context);
        init(context,null,0,0);
    }

    public MarqueeEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public MarqueeEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    public MarqueeEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }

    Activity mActivity;
    MarqueeView2 marqueeView2;
    EditText editText;
    KeyboardChangeObserver mKeyboardChangeObserve;

    protected void init(Context context,AttributeSet attrs,int defStyleAttr,int defStyleRes) {
        mActivity = (Activity) context;
        View view = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
        marqueeView2 = view.findViewById(R.id.marquee_view);
        editText = view.findViewById(R.id.edit_content);
        initData();
        initListener();
        initKeyBoardObserver();
        addView(view);
    }

    protected int getLayoutId(){
        return R.layout.guokeui_widget_marquee_edittext;
    }

    protected void initData(){
        marqueeView2.setmTextSize(editText.getTextSize());
        marqueeView2.setmTextColor(editText.getCurrentTextColor());
    }

    protected void initListener(){
        editText.setOnFocusChangeListener((v,hasFocus) -> {
            if(hasFocus){

            }else{
                //
            }
        });

        marqueeView2.setOnClickListener( v -> {
            stopMarquee();
        });

    }

    protected void initKeyBoardObserver(){
        addKeyBoardListener();
    }

    private void startMarquee(){
        if(editText.getText()!=null && editText.getText().length() > 0){
            editText.requestFocus();
            editText.setVisibility(GONE);
            marqueeView2.setVisibility(VISIBLE);
            marqueeView2.setText(editText.getText().toString());
            marqueeView2.startScroll();
        }
    }

    private void stopMarquee(){
        removeKeyBoardListener();
        marqueeView2.stopScroll();
        marqueeView2.reset();
        marqueeView2.setVisibility(GONE);
        editText.setVisibility(VISIBLE);
        editText.requestFocus();
        // 拉起键盘，接收输入
        //showKeyBoard();
        //
        mActivity.getWindow().getDecorView().postDelayed(()->{
            addKeyBoardListener();
        },0);
    }

    private void addKeyBoardListener(){
        if(mKeyboardChangeObserve == null){
            mKeyboardChangeObserve = new KeyboardChangeObserver(mActivity);//持有一个实例，里面记录有之前的宽高
            mKeyboardChangeObserve.setKeyBoardListener(keyBoardListener);// 回调
            mKeyboardChangeObserve.setDelay(500);// 延迟500ms判断键盘的隐藏，避免键盘还没起来就判断
        }
        mKeyboardChangeObserve.addContentTreeObserver();
    }

    private void removeKeyBoardListener(){
        if(mKeyboardChangeObserve != null)
            mKeyboardChangeObserve.removeKeyBoardListener();
    }

    KeyboardChangeObserver.KeyBoardListener keyBoardListener = (isShow,keyboardHeight) -> {
            Log.i("guohao","onKeyboardChange isShow " + isShow + "，keyboardHeight " + keyboardHeight);
            if (isShow){
            }else{
                startMarquee();
            }
    };

    private void showKeyBoard(){
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(mActivity.INPUT_METHOD_SERVICE);
        if(imm != null)
            imm.showSoftInput(editText,0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("guohao-marquee","onAttachedToWindow");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("guohao-marquee","onDetachedFromWindow");

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.i("guohao-marquee","onWindowVisibilityChanged visibility " + visibility);
        if(visibility == VISIBLE){
            if(marqueeView2.getVisibility() == VISIBLE)
                marqueeView2.startScroll();
        }else{
            if(marqueeView2.isRunning())
                marqueeView2.stopScroll();
        }
    }
}
