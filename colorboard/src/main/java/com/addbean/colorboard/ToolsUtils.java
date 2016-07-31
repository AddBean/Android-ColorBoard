package com.addbean.colorboard;

import android.content.Context;

/**
 * Created by AddBean on 2016/7/31.
 */
public class ToolsUtils {
    public static int dp2Px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int px = (int)((float)dp * scale + 0.5F);
        return px;
    }
}
