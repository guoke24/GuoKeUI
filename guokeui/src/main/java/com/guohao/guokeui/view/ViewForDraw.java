package com.guohao.guokeui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewForDraw extends View {

    public ViewForDraw(Context context) {
        super(context);
    }

    public ViewForDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewForDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

    protected void customDraw(Canvas canvas){

    }

}
