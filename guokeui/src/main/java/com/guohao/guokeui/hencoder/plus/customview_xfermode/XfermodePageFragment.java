package com.guohao.guokeui.hencoder.plus.customview_xfermode;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import com.guohao.guokeui.hencoder.plus.PageFragment;
import com.guohao.guokeui.hencoder.plus.customview_xfermode.view.XfermodeView;

public class XfermodePageFragment extends PageFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        XfermodeView view1 = new XfermodeView(getContext(),null);
        RelativeLayout.LayoutParams layoutParams1 =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view1.setLayoutParams(layoutParams1);

        // 遍历枚举，匹配设置
        for(PorterDuff.Mode mode : PorterDuff.Mode.values()){
            if(mode.ordinal() == sampleLayoutRes){
                view1.setXfermode(mode);
            }
        }

        sampleLayout.addView(view1);

        practiseLayout.setVisibility(View.GONE);

        return view;
    }

}
