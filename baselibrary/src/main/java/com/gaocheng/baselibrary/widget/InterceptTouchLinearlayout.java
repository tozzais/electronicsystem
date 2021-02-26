package com.gaocheng.baselibrary.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by 32672 on 2019/1/30.
 * 拦截事件传递 是子view 无法响应
 */

public class InterceptTouchLinearlayout extends LinearLayout {
    public InterceptTouchLinearlayout(Context context) {
        super(context);
    }

    public InterceptTouchLinearlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchLinearlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InterceptTouchLinearlayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
