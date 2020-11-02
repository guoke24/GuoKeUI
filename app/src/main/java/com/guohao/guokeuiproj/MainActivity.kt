package com.guohao.guokeuiproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guohao.guokeui.GuokeUIMainActivity
import com.guohao.guokeui.TestShaderActivity
import com.guohao.guokeui.TestUIActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        tv_hello.setOnClickListener({
//            v -> startActivity(Intent(this,
//            GuokeUIMainActivity::class.java))
//        })
        startActivity(Intent(this,
            TestShaderActivity::class.java))
    }
}

