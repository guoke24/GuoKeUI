package com.guohao.guokeui.view.flingball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guohao.guokeui.R

class TestFlingBallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "弹弹球"
        setContentView(R.layout.activity_test_fling_ball)
    }
}