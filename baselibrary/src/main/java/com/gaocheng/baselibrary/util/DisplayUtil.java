package com.gaocheng.baselibrary.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by 32672 on 2018/12/26.
 */

public class DisplayUtil {
    /**
     * 把dp或sp转成px （像素）
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        // mdpi 1dp=1px
        // hdpi 1dp=1.5px
        // xhdpi 720*1280 1dp=2px
        // xxhdpi 1080*1920 1dp=3px
        // xxxhdpi 1440*2560 1dp=4px
        //获得资源
        Resources resources = context.getResources();
        // 1个dp或sp等于多少个像素点  算是密度 也可以算是dp与像素的比率
        float density = resources.getDisplayMetrics().scaledDensity;
        // 6.5-->6
        // 6.5+0.5=7
        float px = density * dp + 0.5F;
        return (int) px;
    }
}
