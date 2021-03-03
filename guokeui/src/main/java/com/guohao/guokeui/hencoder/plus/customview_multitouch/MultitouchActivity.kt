package com.guohao.guokeui.hencoder.plus.customview_multitouch
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.guohao.guokeui.R
import com.guohao.guokeui.hencoder.plus.ControlScrollViewPager
import java.util.*

/**
 * 做成一个 pageview，可以对比
 */
class MultitouchActivity : AppCompatActivity() {
  var tabLayout: TabLayout? = null
  var pager: ControlScrollViewPager? = null
  private var pageModels: MutableList<PageModel> =
    ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pager_view_not_scroll)
    pager = findViewById<View>(R.id.pager) as ControlScrollViewPager
    pager!!.setCanScroll(false)
    pager!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
      override fun getItem(position: Int): Fragment {
        val pageModel = pageModels[position]
        return PageFragment.newInstance(
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

  private inner class PageModel internal constructor(
    var sampleLayoutRes: Int,
    var titleRes: Int,
    var practiceLayoutRes: Int
  )

  init {
    pageModels.add(
      PageModel(
        0,
        R.string.title_relay,
        0
      )
    )
    pageModels.add(
      PageModel(
        1,
        R.string.title_cooperation,
        0
      )
    )
    pageModels.add(
      PageModel(
        2,
        R.string.title_self,
        0
      )
    )
  }
}