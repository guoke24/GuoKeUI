package com.guohao.guokeui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.guohao.guokeui.view.MarqueeView2;
import com.guohao.guokeui.R;

public class TestMarqueeActivity extends AppCompatActivity {

    TextView tv_comparison;
    MarqueeView2 marqueeView2;

    boolean srcoll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guokeui_activity_test_marquee);

        String content = "浪里个浪，我要输入很多字，很多很多的字字字字字字，啦啦啦啦啦啦，有哟有撒哒哒哒哒哒哒哒哒";

        marqueeView2 = findViewById(R.id.marqueeView2);
        marqueeView2.setText(content);

        tv_comparison = findViewById(R.id.tv_comparison);
        tv_comparison.setText(content);
        tv_comparison.setTextSize(TypedValue.COMPLEX_UNIT_PX,48);

    }

    public void btCrtlOnClick(View v){
        if(!srcoll){
            srcoll = true;
            marqueeView2.startScroll();
            tv_comparison.setSelected(true);
        }else{
            srcoll = false;
            marqueeView2.stopScroll();
            tv_comparison.setSelected(false);
        }
    }

    public void btSlowOnClick(View v){
        marqueeView2.setSepX(marqueeView2.getSepX() - 1);
    }

    public void btFastOnClick(View v){
        marqueeView2.setSepX(marqueeView2.getSepX() + 1);
    }

    // todo 头追尾效果，头追尾距离
    // textpaint 的属性
    // 画文字的时间，配一个实验室 acty
}
