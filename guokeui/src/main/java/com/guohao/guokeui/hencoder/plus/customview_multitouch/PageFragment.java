package com.guohao.guokeui.hencoder.plus.customview_multitouch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guohao.guokeui.R;
import com.guohao.guokeui.hencoder.plus.customview_multitouch.view.MultiTouchView1;
import com.guohao.guokeui.hencoder.plus.customview_multitouch.view.MultiTouchView1Compare;
import com.guohao.guokeui.hencoder.plus.customview_multitouch.view.MultiTouchView2;
import com.guohao.guokeui.hencoder.plus.customview_multitouch.view.MultiTouchView3;

public class PageFragment extends Fragment {
    @LayoutRes
    int sampleLayoutRes;
    @LayoutRes int practiceLayoutRes;

    public static PageFragment newInstance(@LayoutRes int sampleLayoutRes, @LayoutRes int practiceLayoutRes) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("sampleLayoutRes", sampleLayoutRes);
        args.putInt("practiceLayoutRes", practiceLayoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ViewStub sampleStub = (ViewStub) view.findViewById(R.id.sampleStub);
        sampleStub.setVisibility(View.GONE);
        ViewStub practiceStub = (ViewStub) view.findViewById(R.id.practiceStub);
        practiceStub.setVisibility(View.GONE);

        RelativeLayout sampleLayout = view.findViewById(R.id.sampleStub_layout);
        RelativeLayout practiseLayout = view.findViewById(R.id.practiceStub_layout);


        // 加入新的 view
        if(sampleLayoutRes == 0){
            View view1 = new MultiTouchView1(getContext(),null);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            view1.setLayoutParams(layoutParams1);
            sampleLayout.addView(view1);

            View view2 = new MultiTouchView1Compare(getContext(),null);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            view1.setLayoutParams(layoutParams2);
            practiseLayout.addView(view2);
        }else if(sampleLayoutRes == 1){
            View view1 = new MultiTouchView2(getContext(),null);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            view1.setLayoutParams(layoutParams1);
            sampleLayout.addView(view1);
            practiseLayout.setVisibility(View.GONE);
        }else if(sampleLayoutRes == 2){
            View view1 = new MultiTouchView3(getContext(),null);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            view1.setLayoutParams(layoutParams1);
            sampleLayout.addView(view1);
            practiseLayout.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            sampleLayoutRes = args.getInt("sampleLayoutRes");
            practiceLayoutRes = args.getInt("practiceLayoutRes");
        }
    }
}
