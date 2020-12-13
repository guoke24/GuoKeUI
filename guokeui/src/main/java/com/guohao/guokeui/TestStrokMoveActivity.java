package com.guohao.guokeui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guohao.guokeui.view.ImageVIewWithStrok;
import com.guohao.guokeui.R;

public class TestStrokMoveActivity extends AppCompatActivity {

    ImageVIewWithStrok imageVIewWithStrok;
    Button btnMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guokeui_activity_testui);
        imageVIewWithStrok = findViewById(R.id.img_stroke);
        btnMove = findViewById(R.id.btn_move);
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVIewWithStrok.optMove(!imageVIewWithStrok.isMove());
            }
        });
    }
}
