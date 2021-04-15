package com.guohao.guokeui.layout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.guohao.guokeui.R
import com.guohao.guokeui.dp

/**
 * 使用 kotlin 更好的写布局
 * 0，避免要加载 xml的 IO耗时，解析 xml耗时，反射耗时，创建 View的构造函数的耗时
 * 1，不需要 ID
 * 2，final / inmutable type，类型安全
 * 3，不需要 fingViewById
 * PS，addView 是不会触发 layout 流程
 */
open class TheCustomLayout(context: Context) : ViewGroup(context) {

    /**
     * 声明 View，设置布局参数
     * padding 是 View 自带到
     * margin 是 ViewGroup.MarginLayoutParams 中带有的属性
     */

    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.cat)
        layoutParams = MarginLayoutParams(MATCH_PARENT, 280.dp.toInt())
        addView(this)
    }

    val fab = FloatingActionButton(context).apply {
        layoutParams = MarginLayoutParams(40.dp.toInt(), 40.dp.toInt()).also {
            it.rightMargin = 16.dp.toInt()
        }
        setImageResource(R.drawable.google_logo)
        customSize = 40.dp.toInt()
        //setImageDrawable(ColorDrawable(Color.RED))
        //scaleType = ImageView.ScaleType.CENTER_INSIDE
        addView(this)
    }

    val avatar = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = MarginLayoutParams(80.dp.toInt(), 80.dp.toInt()).also {
            it.leftMargin = 16.dp.toInt()
            it.topMargin = 16.dp.toInt()
        }
        setImageResource(R.drawable.avatar_rengwuxian)
        addView(this)
    }

    val reply = AppCompatImageView(context).apply {
        setImageResource(R.drawable.ic_launcher)
        layoutParams = MarginLayoutParams(40.dp.toInt(), 40.dp.toInt()).also {
            it.rightMargin = 16.dp.toInt()
            it.topMargin = 32.dp.toInt()
        }
        addView(this)
    }

    val itemTitle = AppCompatTextView(context).apply {
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.leftMargin = 16.dp.toInt()
            it.rightMargin = 16.dp.toInt()
        }
        textSize = 20f
        text = "似火年华"
        setTextColor(Color.parseColor("#000000"))
        addView(this)
    }

    val itemMessage = AppCompatTextView(context).apply {
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.leftMargin = 16.dp.toInt()
            it.rightMargin = 16.dp.toInt()
        }
        textSize = 16f
        text = "再不疯狂我们就老了，喔欧欧~再不疯狂我们就慌了，喔欧欧欧～总有一天你要回忆吧，庆幸你这似火的年华～～～"
        setTextColor(Color.parseColor("#000000"))
        addView(this)
    }

    /**
     * ViewGroup 的拓展类中，往往通过 for 循环逐个测量子 View
     * 无差别的通用方法，往往意味着要考虑所有的情况
     * 因此 for 循环里面会很复杂
     *
     * 但是此处并不需要做一个通用的布局逻辑
     * 所以我们可以直接对 View，显示的调用 measure 方法即可
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 第一步，写出测量 header 的一般代码
//        header.measure(
//            header.defaultWidthMeasureSpec(parent = this),
//            header.defaultHeightMeasureSpec(parent = this)
//        )
        // 第二步，作用域函数优化为：
//        header.let {
//            it.measure(
//                it.defaultWidthMeasureSpec(parent = this),
//                it.defaultHeightMeasureSpec(parent = this)
//            )
//        }
        // 第三步，拓展函数优化
        header.autoMeasure()
        fab.autoMeasure()
        avatar.autoMeasure()
        reply.autoMeasure()

        // 先算出 text 能用的宽度
        val itemTextWidth = (measuredWidth
                - avatar.measureWidthWithMargins
                - reply.measureWidthWithMargins
                - itemTitle.marginLeft
                - itemTitle.marginRight)
        // 因为宽度不是默认的测量逻辑，需要自定义
        itemTitle.measure(
            itemTextWidth.toExactlyMeassureSpec(),
            itemTitle.defaultHeightMeasureSpec(this)
        )
        itemMessage.measure(
            itemTextWidth.toExactlyMeassureSpec(),
            itemMessage.defaultHeightMeasureSpec(this)
        )

        // 测量完子 View，测量自己
        // 此时容器自身的 measuredWidth、measuredHeight 在 super.onMeasure() 函数中设置好类，是屏幕宽高
        // 但我们想要重新测量容器的高，使其包裹住子 view 即可。
        val max = (avatar.marginTop
                + itemTitle.measureHeightWithMargins
                + itemMessage.measureHeightWithMargins
                ).coerceAtLeast(avatar.measureHeightWithMargins) // 考虑到 title 和 message 加起来到高度小于 avatar 到高度到情况
        val wrapContentHeight = header.measureHeightWithMargins + max // 包裹内容的高
        setMeasuredDimension(measuredWidth, wrapContentHeight) // 设置给自身

        // 还有一些情况没有处理，比如：pading 的处理；一些子 view 依赖另一个子 view 的可见性等等
        // 其实这些情况，多加几个 if 判断就好了。
    }

    /**
     * 尺寸已经测量好了，接着摆放位置即可。
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        header.let{
//            it.layout(0,0, it.measuredWidth, it.measuredHeight)
//        }
        // 拓展函数优化：
        header.layout(x = 0,y = 0)
        fab.let { it.layout( x = it.marginRight, y = header.bottom - it.measuredHeight/2, fromRight = true) }
        avatar.let{ it.layout(x = it.marginLeft,y = header.bottom + it.marginTop) }
        itemTitle.let { it.layout(x = avatar.measureHeightWithMargins + it.marginLeft, y = avatar.top + it.marginTop) }
        itemMessage.let { it.layout(x = avatar.measureHeightWithMargins + it.marginLeft, y = itemTitle.bottom + it.marginTop) }
        reply.let { it.layout(x = it.marginRight, y = header.bottom + it.marginTop,fromRight = true) }
    }

    // ----------
    // Extensions
    // 这些拓展的方法，是有通用性质的
    // ----------

    private fun View.layout(x:Int,y:Int,fromRight:Boolean = false){
        if(!fromRight){
            layout(x,y,x + measuredWidth,y + measuredHeight)
        }else{
            // 从右摆放，假设 x = 10，表示到最右边到距离为 10
            // 下面到算法，是换算成了距离最左边到距离（也就是常规算法）
            // 然后递归调用，执行上面 if 到逻辑
            layout(this@TheCustomLayout.measuredWidth - x - measuredWidth,y)
        }
    }

    private val View.measureWidthWithMargins: Int
        get() {
            return this.measuredWidth + this.marginStart + this.marginEnd
        }

    private val View.measureHeightWithMargins: Int
        get() {
            return this.measuredHeight + this.marginTop + this.marginBottom
        }

    private fun View.autoMeasure() {
        measure(
            this.defaultWidthMeasureSpec(parent = this@TheCustomLayout),
            this.defaultHeightMeasureSpec(parent = this@TheCustomLayout)
        )
    }

    private fun View.defaultWidthMeasureSpec(parent: ViewGroup): Int {
        return when (layoutParams.width) {
            MATCH_PARENT -> parent.measuredWidth.toExactlyMeassureSpec()
            WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeassureSpec() //包裹自身，无视父容器限制
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeassureSpec()
        }
    }

    private fun View.defaultHeightMeasureSpec(parent: ViewGroup): Int {
        return when (layoutParams.height) {
            MATCH_PARENT -> parent.measuredWidth.toExactlyMeassureSpec()
            WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeassureSpec() //包裹自身，无视父容器限制
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeassureSpec()
        }
    }

    private fun Int.toExactlyMeassureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    private fun Int.toAtMostMeassureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

}
/**
 * 对比 ConstrainLayout 写布局
 * 在有拓展函数的支持下：
 * 此处声明子 view，跟 xml 声明引入子 view 的工作量差不多
 *
 * 但是这里需要自己写 onMeasure 和 onLayout 函数，这是比 xml 多出来的工作量
 *
 * 这里可以通过拓展函数，把常见的逻辑封装起来，可以简化部分工作
 *
 * 剩余的一些需要自己做的工作，包括：
 * 1，一些尺寸为 Wrap_content 的子 View，它们的测量大小就需要自己写，需要考虑剩余空间，考虑其他控件的大小
 * 2，测量完成后，布局阶段，会相对简单，只是把相应的子 View，摆在我想要的位置
 *
 * */