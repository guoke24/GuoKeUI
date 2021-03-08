package com.guohao.guokeui.hencoder.plus.customview_xfermode

import android.graphics.PorterDuff
import com.guohao.guokeui.hencoder.plus.ControlSrcollViewPagerActivity2
import com.guohao.guokeui.hencoder.plus.PageModel

class XfermodeActivity3 : ControlSrcollViewPagerActivity2<XfermodePageFragment>() {

    init {
        // 先画圆，是目标
        pageModels.add(
            PageModel(
                PorterDuff.Mode.DST.ordinal,
                "DST",
                0
            )
        )
        //后画方，是源，为何？
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SRC.ordinal,
                "SRC",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.DST_OVER.ordinal,
                "DST_OVER",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SRC_OVER.ordinal,
                "SRC_OVER",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.DST_IN.ordinal,
                "DST_IN",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SRC_IN.ordinal,
                "SRC_IN",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.DST_OUT.ordinal,
                "DST_OUT",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SRC_OUT.ordinal,
                "SRC_OUT",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.DST_ATOP.ordinal,
                "DST_ATOP",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SRC_ATOP.ordinal,
                "SRC_ATOP",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.ADD.ordinal,
                "ADD",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.SCREEN.ordinal,
                "SCREEN",
                0
            )
        )

        // OVERLAY、DARKEN、LIGHTEN 效果一样？
        pageModels.add(
            PageModel(
                PorterDuff.Mode.OVERLAY.ordinal,
                "OVERLAY",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.DARKEN.ordinal,
                "DARKEN",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.LIGHTEN.ordinal,
                "LIGHTEN",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.CLEAR.ordinal,
                "CLEAR",
                0
            )
        )

        pageModels.add(
            PageModel(
                PorterDuff.Mode.MULTIPLY.ordinal,
                "MULTIPLY",
                0
            )
        )
        pageModels.add(
            PageModel(
                PorterDuff.Mode.XOR.ordinal,
                "XOR",
                0
            )
        )
    }

    override fun getClassT(): Class<XfermodePageFragment> {
        return XfermodePageFragment::class.java
    }
}