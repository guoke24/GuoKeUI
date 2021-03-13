package com.guohao.guokeui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guohao.guokeui.hencoder.HenCoderMainActivity
import com.guohao.guokeui.smallapp.SmallAppMainActivity
import com.guohao.guokeui.spinner.TestTimerSpinnerActivity
import com.guohao.guokeui.view.flingball.TestFlingBallActivity
import kotlinx.android.synthetic.main.guokeui_activity_main.*

class GuokeUIMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guokeui_activity_main)
        btn_quanquan.setOnClickListener{
            // 对入参重命名
            v -> startActivity(Intent(this, TestStrokMoveActivity::class.java))
        }

        btn_Gradient.setOnClickListener {
            startActivity(Intent(this, TestShaderActivity::class.java))
        }

        btn_Marquee.setOnClickListener {
            startActivity(Intent(this, TestMarqueeActivity::class.java))
        }

        // 参考：https://blog.csdn.net/blovecat/article/details/103767059
        // 第一种写法，使用object来创建一个OnClickListener的匿名类对象，创建对象就需要实现接口对应的方法，这部分合Java写法类似。
//        findViewById<Button>(R.id.btn_TimeSpinner).setOnClickListener (object: View.OnClickListener{
//            override fun onClick(v: View?) {
//                //do something
//            }
//        })
//
//      // 第二种写法，对于单个方法的java接口，可以简写成以接口类型作为前缀的Lambda表达式
//        findViewById<Button>(R.id.btn_TimeSpinner).setOnClickListener (View.OnClickListener {
//            //do something
//        })
//
        // 第三种写法
        // OnClickListener 是一个 SAM，即 Single Abstract Method，单个抽象方法，
        // Java 中的 SAM 在 Kotlin 中可以直接用 Lambda 来表示，因此可以这么写：
//        findViewById<Button>(R.id.btn_TimeSpinner).setOnClickListener ({
//            //do something
//        })

        // 再省了括号，就是这样
        btn_TimeSpinner.setOnClickListener {
            startActivity(Intent(this, TestTimerSpinnerActivity::class.java))
        }

        btn_draw.setOnClickListener {
            startActivity(Intent(this, TestDrawActivity::class.java))
        }



        btn_fling_ball.setOnClickListener {
            startActivity(Intent(this, TestFlingBallActivity::class.java))
        }

        btn_hencode_main.setOnClickListener {
            startActivity(Intent(this, HenCoderMainActivity::class.java))
        }

        btn_smallapp_main.setOnClickListener {
            startActivity(Intent(this, SmallAppMainActivity::class.java))
        }
    }
}
