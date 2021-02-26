package com.gaocheng.baselibrary.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.gaocheng.baselibrary.R;


/**
 * Created by Administrator on 2017/4/26.
 */

public class VpUtil {


    public static void addPoint1(Context context, LinearLayout ll_points, int size){
        for (int i = 0; i < size; i++) {
            // 设置圆圈点
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 15;
            view.setBackgroundResource(R.drawable.shape_point_selete);
            view.setLayoutParams(params);
            view.setEnabled(false);
            ll_points.addView(view);
        }
    }
}
