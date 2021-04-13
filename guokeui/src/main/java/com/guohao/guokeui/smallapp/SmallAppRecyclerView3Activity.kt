package com.guohao.guokeui.smallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.adapter.SmallAdapter3
import kotlinx.android.synthetic.main.activity_small_app_recycler_view3.*

/**
 * item 的 child 的 点击设置，方案总结
 */
class SmallAppRecyclerView3Activity : AppCompatActivity() {

    val adapter = SmallAdapter3(this,null)
    val layout = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_app_recycler_view3)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        rv_item_list3.layoutManager = layout
        rv_item_list3.adapter = adapter
    }

}