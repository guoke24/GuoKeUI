package com.guohao.guokeui.smallapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guohao.guokeui.R
import com.guohao.guokeui.smallapp.model.ItemModel

/**
 * 适配器，提供数据
 */
class SmallAdapter : RecyclerView.Adapter<SmallViewholder> {

    var mContext : Context
    var datas : ArrayList<ItemModel>

    constructor(context : Context, list : ArrayList<ItemModel>) : super() {
        mContext = context
        datas = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallViewholder {

        val rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_small_app_item, parent,false)
        val viewholder = SmallViewholder(rootView)

        viewholder.textView1?.setOnClickListener {
            // 第一次点击会闪，第二次点击才会生效；滞后性！应该改动一个外部的值，比如一个数组中某个某个元素的 boolean，
            // 然后在 onBindViewHolder 函数中更新！
            //viewholder.textView2?.visibility = if(viewholder.textView2?.visibility == VISIBLE) GONE else VISIBLE

            // 改动的值，后续不生效，会被 onBindViewHolder 中重新赋值
            //viewholder.textView2?.text = "hello"

            val position = viewholder.textView2?.tag as Int

            val item = datas.get(position)

            item.Name = item.Name + "1"

            notifyItemChanged(position)// 配合 supportsChangeAnimations = false 也可以解决闪烁问题

            //notifyItemChanged(position,item.Name)
            // 第二个参数，会被封装到 MutableList<Any> 中，传递给 onBindViewHolder 函数
            // 但不一定要使用
        }

        return viewholder
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(
        holder: SmallViewholder,
        position: Int
    ) {
        Log.i("guohao-rv","onBindViewHolder without payloads")

        val item = datas.get(position)

        val textView1 =  holder.get(R.id.tv_item_name) as TextView
        val textView2 =  holder.get(R.id.tv_item_name2) as TextView

        textView1.text = item.Name
        textView2.text = item.Age.toString()

        textView1.tag = position
        textView2.tag = position

    }

    override fun onBindViewHolder(
        holder: SmallViewholder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        Log.i("guohao-rv","onBindViewHolder with payloads")

        val item = datas.get(position)

        val textView1 =  holder.get(R.id.tv_item_name) as TextView
        val textView2 =  holder.get(R.id.tv_item_name2) as TextView

        textView1.text = item.Name
        textView2.text = item.Age.toString()

        textView1.tag = position
        textView2.tag = position
    }

    override fun getItemId(position: Int): Long {
        return datas.get(position).Id!!.toLong()
    }
}

/**
 * 封装一个 view 的集合类
 */
class SmallViewholder : RecyclerView.ViewHolder {

    var textView1 : TextView? = null
    var textView2 : TextView? = null

    constructor(itemView: View) : super(itemView){
        textView1 = itemView.findViewById(R.id.tv_item_name)
        textView2 = itemView.findViewById(R.id.tv_item_name2)
    }

    fun get(id : Int): View {
        return itemView.findViewById<TextView>(id)
    }

}