package com.guohao.guokeui.hencoder.plus

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.guohao.guokeui.R
import java.util.*

/**
 * 用来复制代码的
 */
open class ControlScrollViewPagerActivity<T : PageFragment> : AppCompatActivity() {
    var tabLayout: TabLayout? = null
    var pager: ControlScrollViewPager? = null
    protected var pageModels: MutableList<PageModel> =
        ArrayList()

    companion object{
        fun newInstance(
            @LayoutRes sampleLayoutRes: Int,
            @LayoutRes practiceLayoutRes: Int
        ): Fragment {
            val clz = T::class
            val fragment : PageFragment = clz.objectInstance as PageFragment
            val args = Bundle()
            args.putInt("sampleLayoutRes", sampleLayoutRes)
            args.putInt("practiceLayoutRes", practiceLayoutRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_view_not_scroll)
        pager = findViewById<View>(R.id.pager) as ControlScrollViewPager
        pager!!.setCanScroll(true)
        pager!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val pageModel = pageModels[position]
                return newInstance(
                    pageModel.sampleLayoutRes,
                    pageModel.practiceLayoutRes
                )
            }

            override fun getCount(): Int {
                return pageModels.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return getString(pageModels[position].titleRes)
            }
        }
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout!!.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    inner class PageModel internal constructor(
        var sampleLayoutRes: Int,
        var titleRes: Int,
        var practiceLayoutRes: Int
    )

//    init {
//        pageModels.add(
//            PageModel(
//                0,
//                R.string.title_relay,
//                0
//            )
//        )
//    }
}