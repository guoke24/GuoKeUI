package com.guohao.guokeui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.guokeui_activity_main.*

class GuokeUIMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guokeui_activity_main)
        btn_quanquan.setOnClickListener({
            v -> startActivity(Intent(this, TestStrokMoveActivity::class.java))
        })

        btn_Gradient.setOnClickListener {
            startActivity(Intent(this, TestShaderActivity::class.java))
        }

        btn_Marquee.setOnClickListener {
            startActivity(Intent(this, TestMarqueeActivity::class.java))
        }
    }
}
