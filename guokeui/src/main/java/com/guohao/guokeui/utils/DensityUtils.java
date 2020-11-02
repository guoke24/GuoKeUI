package com.guohao.guokeui.utils;

import android.content.Context;

public class DensityUtils {

    public static float dp2px(Context context,float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

}
