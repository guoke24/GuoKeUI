package com.guohao.guokeui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.guohao.guokeui.R;
import com.guohao.guokeui.view.ViewForGradientMove;

public class TestShaderActivity extends AppCompatActivity {

    private ViewForGradientMove viewForGradientMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guokeui_activity_test_shader);
        viewForGradientMove = findViewById(R.id.view_gradient_move);
    }

    public void switchMove(View v){
        viewForGradientMove.setMove(!viewForGradientMove.isMove());
    }
}
