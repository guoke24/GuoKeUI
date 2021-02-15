package com.guohao.guokeui.hencoder.hencoderpracticedraw4;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import com.guohao.guokeui.R;

public class HenCoderPractive4MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();

    {
        pageModels.add(new PageModel(R.layout.sample_clip_rect, R.string.title_clip_rect, R.layout.sample_clip_rect));
        pageModels.add(new PageModel(R.layout.sample_clip_path, R.string.title_clip_path, R.layout.sample_clip_path));
        pageModels.add(new PageModel(R.layout.sample_translate, R.string.title_translate, R.layout.sample_translate));
        pageModels.add(new PageModel(R.layout.sample_scale, R.string.title_scale, R.layout.sample_scale));
        pageModels.add(new PageModel(R.layout.sample_rotate, R.string.title_rotate, R.layout.sample_rotate));
        pageModels.add(new PageModel(R.layout.sample_skew, R.string.title_skew, R.layout.sample_skew));
        pageModels.add(new PageModel(R.layout.sample_matrix_translate, R.string.title_matrix_translate, R.layout.sample_matrix_translate));
        pageModels.add(new PageModel(R.layout.sample_matrix_scale, R.string.title_matrix_scale, R.layout.sample_matrix_scale));
        pageModels.add(new PageModel(R.layout.sample_matrix_rotate, R.string.title_matrix_rotate, R.layout.sample_matrix_rotate));
        pageModels.add(new PageModel(R.layout.sample_matrix_skew, R.string.title_matrix_skew, R.layout.sample_matrix_skew));
        pageModels.add(new PageModel(R.layout.sample_camera_rotate, R.string.title_camera_rotate, R.layout.sample_camera_rotate));
        pageModels.add(new PageModel(R.layout.sample_camera_rotate_fixed, R.string.title_camera_rotate_fixed, R.layout.sample_camera_rotate_fixed));
        pageModels.add(new PageModel(R.layout.sample_camera_rotate_hitting_face, R.string.title_camera_rotate_hitting_face, R.layout.practice_camera_rotate_hitting_face));
        pageModels.add(new PageModel(R.layout.sample_flipboard, R.string.title_flipboard, R.layout.sample_flipboard));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hencoderpractivedraw1_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return PageFragment.newInstance(pageModel.sampleLayoutRes, pageModel.practiceLayoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class PageModel {
        @LayoutRes
        int sampleLayoutRes;
        @StringRes
        int titleRes;
        @LayoutRes int practiceLayoutRes;

        PageModel(@LayoutRes int sampleLayoutRes, @StringRes int titleRes, @LayoutRes int practiceLayoutRes) {
            this.sampleLayoutRes = sampleLayoutRes;
            this.titleRes = titleRes;
            this.practiceLayoutRes = practiceLayoutRes;
        }
    }
}
