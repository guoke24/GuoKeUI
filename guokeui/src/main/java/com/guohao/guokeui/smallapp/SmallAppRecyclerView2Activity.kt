package com.guohao.guokeui.smallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.adapter.SmallAdapter
import com.guohao.guokeui.smallapp.model.ItemModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_small_app_recycler_view2.*


/**
 * RecyclerView 的 demo2
 *
 * 自定义添加、删除 item 的动画
 *
 */
class SmallAppRecyclerView2Activity : AppCompatActivity() {

    // MutableList 是 Kotlin 定义的一个接口，是可变的列表。
    // ArrayList 是 Java 那边的 API
    // 目前在 Android 这块，MutableList 的一般实现就是 ArrayList
    val datas = ArrayList<ItemModel>()
    val adapter = SmallAdapter(this,datas)
    val layout = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_app_recycler_view2)
        initWidget()
    }

    private fun initWidget(){
        initRecyclerView()
        initButton()
    }

    private fun initRecyclerView(){

        datas.add(ItemModel(111,"小明",18,false))
        datas.add(ItemModel(222,"小红",19,false))
        datas.add(ItemModel(333,"小黄",20,false))

        adapter.setHasStableIds(true)
        rv_item_list2.layoutManager = layout
        rv_item_list2.adapter = adapter

        //设置上浮动画
        rv_item_list2.itemAnimator = SlideInUpAnimator()

        //时长设置500ms，便于观察
        rv_item_list2.itemAnimator?.addDuration = 1500
        rv_item_list2.itemAnimator?.removeDuration = 500

    }

    private fun initButton(){

        btn_add.setOnClickListener {

            val size = adapter.itemCount
            val id = size.toString() + size.toString() + size.toString();
            datas.add(0,ItemModel(id.toInt(), "小黄$size",20,false))
            adapter.notifyItemInserted(0)// 这个index写错，会有奇怪的效果

            rv_item_list2.smoothScrollToPosition(0)
        }

        btn_remove.setOnClickListener {
            if(adapter.itemCount == 0){
                return@setOnClickListener
            }

            //rv_item_list2.smoothScrollToPosition(0)

            datas.removeAt(0);
            adapter.notifyItemRemoved(0)

        }

        btn_add_tail.setOnClickListener {

            val size = adapter.itemCount
            val id = size.toString() + size.toString() + size.toString();
            datas.add(ItemModel(id.toInt(), "小黄$size",20,false))
            adapter.notifyItemInserted(size)

            rv_item_list2.smoothScrollToPosition(adapter.itemCount)// 多滚一个位置，能看到动画

        }

        btn_remove_tail.setOnClickListener {
            if(adapter.itemCount == 0){
                return@setOnClickListener
            }

            val index = adapter.itemCount - 1
            datas.removeAt(index);
            adapter.notifyItemRemoved(index)

            if(adapter.itemCount == 0){
                return@setOnClickListener
            }
            rv_item_list2.smoothScrollToPosition(adapter.itemCount - 1)//效果看起来：删除来才滚到底部
        }

    }



}