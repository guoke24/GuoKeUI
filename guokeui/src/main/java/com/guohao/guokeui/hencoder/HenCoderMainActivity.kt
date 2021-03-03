package com.guohao.guokeui.hencoder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guohao.guokeui.R
import com.guohao.guokeui.hencoder.hencoderpracticedraw1.HenCoderPractive1Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw2.HenCoderPractive2Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw3.HenCoderPractive3Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw4.HenCoderPractive4Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw5.HenCoderPractive5Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw6.HenCoderPractive6Activity
import com.guohao.guokeui.hencoder.hencoderpracticedraw7.HenCoderPractive7Activity
import com.guohao.guokeui.hencoder.plus.customview_multitouch.MultitouchActivity
import com.guohao.guokeui.hencoder.plus.customview_touchdrag.TouchDragActivity
import com.guohao.guokeui.hencoder.plus.practise_scalable_view.TestScalableViewActivity
import kotlinx.android.synthetic.main.activity_hen_coder_main.*

class HenCoderMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hen_coder_main)

        btn_hencode1.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive1Activity::class.java))
        }

        btn_hencode2.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive2Activity::class.java))
        }

        btn_hencode3.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive3Activity::class.java))
        }

        btn_hencode4.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive4Activity::class.java))
        }

        btn_hencode5.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive5Activity::class.java))
        }

        btn_hencode6.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive6Activity::class.java))
        }

        btn_hencode7.setOnClickListener {
            startActivity(Intent(this, HenCoderPractive7Activity::class.java))
        }

        btn_scalable.setOnClickListener {
            startActivity(Intent(this, TestScalableViewActivity::class.java))
        }

        btn_touch_drag.setOnClickListener {
            startActivity(Intent(this, TouchDragActivity::class.java))
        }

        btn_multitouch_touch.setOnClickListener {
            startActivity(Intent(this, MultitouchActivity::class.java))
        }

    }
}