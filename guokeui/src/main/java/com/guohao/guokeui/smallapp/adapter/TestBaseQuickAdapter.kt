package com.guohao.guokeui.smallapp.adapter

import android.app.Activity
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.model.ItemModel

class TestBaseQuickAdapter(val activity: Activity, datas: MutableList<ItemModel>)
    : BaseQuickAdapter<ItemModel, BaseViewHolder>(R.layout.layout_small_app_item, datas) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //addChildClickViewIds(R.id.tv_item_name,R.id.tv_item_name2)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(holder: BaseViewHolder, item: ItemModel) {
    }

}