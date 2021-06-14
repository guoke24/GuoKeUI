package com.guohao.guokeui.smallapp

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.adapter.SmallAdapter
import com.guohao.guokeui.smallapp.model.ItemModel
import kotlinx.android.synthetic.main.activity_small_app_recycler_view.*

/**
 * RecyclerView 的 demo1
 *
 * 闪屏问题的原因：
 * 当调用 notifyItemChanged() 时，会调用 DefaultItemAnimator 的 animateChangeImpl() 执行 change 动画，
 * 该动画会使得 Item 的透明度从 0 变为 1，从而造成闪屏。
 *
 * 解决局部刷新的闪烁问题：
 *
 * 方法一：payloads 法
 * notifyItemChanged(position,payload) +
 * onBindViewHolder(holder, position , payloads: MutableList<Any>) 的函数回调
 * 解决思路：Adapter 的 getChangePayload() 方法返回不为 null 的话，onChange 采用 Partial bind，就不会出现闪烁问题，反之就有。
 *
 * 方法二：setHasStableIds 法
 * adapter.setHasStableIds(true)
 * 重写 adapter 的 getItemId 函数
 * 配合 adapter 的 notifyDataSetChanged() 函数，就能获得 notifyItemChanged 的效果，且不会有闪烁动画。
 * 扔物线的 Drakeet 博主，写过 Stable ID，是跟动画触发有关。
 *
 * 方法三：禁用 change 动画
 * (rv_item_list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
 *
 *
 * 在此demo中，第一种方法生效，第二种没生效，为何？
 * 两种方法的原理又是什么？
 * 局部刷新的原理又是什么？
 *
 */
class SmallAppRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_app_recycler_view)
        initWidget()
    }

    private fun initWidget(){
        initRecyclerView()
    }

    private fun initRecyclerView(){

        val datas = ArrayList<ItemModel>()
        datas.add(ItemModel(111,"小明",18,false))
        datas.add(ItemModel(222,"小红",19,false))
        datas.add(ItemModel(333,"小黄",20,false))

        val adapter = SmallAdapter(this,datas)
        adapter.setHasStableIds(true)
        val layout = LinearLayoutManager(this)
        layout.orientation = RecyclerView.VERTICAL
        rv_item_list.layoutManager = layout
        rv_item_list.adapter = adapter
        //(rv_item_list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

}