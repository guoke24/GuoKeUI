package com.guohao.guokeui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestFlingBallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "弹弹球"
        setContentView(R.layout.activity_test_fling_ball)
    }
}