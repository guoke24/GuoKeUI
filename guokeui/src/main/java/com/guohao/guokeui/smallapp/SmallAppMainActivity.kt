package com.guohao.guokeui.smallapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.guohao.guokeui.R
import kotlinx.android.synthetic.main.activity_small_app_main.*

/**
 * 小app，集成各个常见的UI组件
 *
 * 这里是一个主界面，应该有「底部 navigation」+「fragment」
 * 中间是多级嵌套的 recyclerView
 * 顶部是「搜索栏」
 *
 */
// 默认是 public 和 final
class SmallAppMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //继承 Activity 用这个
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //去掉顶部标题
        getSupportActionBar()?.hide();
        //去掉最上面时间、电量等
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_app_main)

        initWidget()

    }

    private fun initWidget(){
        btn_recyclerView.setOnClickListener {
            startActivity(Intent(this,SmallAppRecyclerViewActivity::class.java))
        }

        btn_recyclerView2.setOnClickListener {
            startActivity(Intent(this,SmallAppRecyclerView2Activity::class.java))
        }

        btn_recyclerView3.setOnClickListener {
            startActivity(Intent(this,TestBaseQuickAdapterActivity::class.java))
        }
    }

}