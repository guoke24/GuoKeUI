package com.guohao.guokeui.spinner

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guohao.guokeui.R
import com.guohao.guokeui.spinner.adapters.AbstractWheelTextAdapter
import kotlinx.android.synthetic.main.guokeui_activity_test_time_spinner.*

class TestTimerSpinnerActivity : AppCompatActivity() {

    //protected var wvvYear: WheelVerticalView? = null // '?'可以为空

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guokeui_activity_test_time_spinner)
        initView()
        initListener()
    }

    private fun initListener(){
        btn_time_spinner_dialog.setOnClickListener {
            Toast.makeText(this,"time spinner",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView(){
        wheel_year.setViewAdapter(TimeAdapter(this))
    }

}


/**
 * 适配器
 *
 * 需要提供 item 的 字符串，可拿到 index
 *
 * 提供总数
 *
 */
class TimeAdapter : AbstractWheelTextAdapter {

    constructor(context: Context?) : super(context)

    override fun getItemText(index: Int): CharSequence {
        return "${index}"
    }

    override fun getItemsCount(): Int {
        return 10
    }
}
