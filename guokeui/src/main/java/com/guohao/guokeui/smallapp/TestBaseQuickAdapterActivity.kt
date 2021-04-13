package com.guohao.guokeui.smallapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.adapter.TestBaseQuickAdapter
import com.guohao.guokeui.smallapp.model.ItemModel
import kotlinx.android.synthetic.main.activity_test_base_quick_adapter.*

class TestBaseQuickAdapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_base_quick_adapter)
        initRv()
    }

    private fun initRv(){

        val datas = ArrayList<ItemModel>()
        datas.add(ItemModel(1, "hello", 18, false))
        datas.add(ItemModel(1, "hello", 18, false))
        datas.add(ItemModel(1, "hello", 18, false))
        datas.add(ItemModel(1, "hello", 18, false))
        datas.add(ItemModel(1, "hello", 18, false))

        // 万能适配器的使用
        rv_item_list_quick.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TestBaseQuickAdapter(this@TestBaseQuickAdapterActivity, datas).apply {
                addChildClickViewIds(R.id.tv_item_name,R.id.tv_item_name2)
                setOnItemChildClickListener { _, view, position ->
                    when(view.id){
                        R.id.tv_item_name -> {
                            Log.i("guohao-rv", "hello $position")
                        }
                        R.id.tv_item_name2 -> {
                            Log.i("guohao-rv", "world $position")
                        }
                    }
                }
            }
        }

    }

}