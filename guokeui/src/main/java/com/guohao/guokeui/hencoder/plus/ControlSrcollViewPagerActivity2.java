package com.guohao.guokeui.hencoder.plus;

import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.guohao.guokeui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 后续继承这个类，写 demo 就很方便啦，哈哈哈
 * 只需「要指定类型」和「实现一个抽象函数」
 * 在自己实现 PageFragment 的拓展类，添加 View，不用下 xml 布局！
 *
 * @param <T>
 */
public abstract class ControlSrcollViewPagerActivity2<T extends PageFragment> extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    protected List<PageModel> pageModels = new ArrayList<>();

    /**
     * 创建泛型实例的一种写法，参考：https://www.jianshu.com/p/e618b81aadb4，还有另一种接口形式的写法
     *
     * @param sampleLayoutRes
     * @param practiceLayoutRes
     * @return
     */
    private PageFragment newInstance(int sampleLayoutRes, int practiceLayoutRes) {

        PageFragment fragment = null;
        try {
            fragment = (PageFragment) getClassT().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putInt("sampleLayoutRes", sampleLayoutRes);
        args.putInt("practiceLayoutRes", practiceLayoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    protected abstract Class<T> getClassT();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hencoderpractivedraw_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return newInstance(pageModel.sampleLayoutRes, pageModel.practiceLayoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageModels.get(position).titleRes;
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
