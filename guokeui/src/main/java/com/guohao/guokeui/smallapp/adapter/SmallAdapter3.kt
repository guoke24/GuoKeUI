package com.guohao.guokeui.smallapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.model.ItemModel

class SmallAdapter3 : RecyclerView.Adapter<SmallViewHolder3> {

    private var mContext : Context

    constructor(context : Context, list : ArrayList<ItemModel>?) : super() {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallViewHolder3 {

        val rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_small_app_item, parent,false)
        return SmallViewHolder3(rootView)
    }

    override fun onBindViewHolder(holder: SmallViewHolder3, position: Int) {

        var layoutParams =  holder.itemView.layoutParams as RecyclerView.LayoutParams
        //layoutParams.get
        // 可以不绑定任何数据
    }

    override fun getItemCount(): Int {

        return 3;

    }
}

class SmallViewHolder3 : RecyclerView.ViewHolder {

    constructor(itemView: View) : super(itemView){

    }

}