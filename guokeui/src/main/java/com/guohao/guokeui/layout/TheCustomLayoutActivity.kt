package com.guohao.guokeui.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guohao.guokeui.R

class TheCustomLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TheCustomLayout(this))
    }
}