package com.guohao.guokeui.hencoder.plus.customview_xfermode;

import android.graphics.PorterDuff;

import com.guohao.guokeui.R;
import com.guohao.guokeui.hencoder.plus.ControlSrcollViewPagerActivity2;
import com.guohao.guokeui.hencoder.plus.PageModel;

public class XfermodeActivity2 extends ControlSrcollViewPagerActivity2<XfermodePageFragment> {

    {
        pageModels.add(new PageModel(
                PorterDuff.Mode.DST_IN.ordinal(),
                "DST_IN1",
                0));

    }

    @Override
    protected Class<XfermodePageFragment> getClassT() {
        return XfermodePageFragment.class;
    }
}
