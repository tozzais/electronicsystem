package com.gaocheng.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.gaocheng.baselibrary.util.ScreenUtil;

/**
 * Created by 32672 on 2018/12/26.
 */

public class SupportPopupWindow extends PopupWindow {

    public SupportPopupWindow(Context context) {
        super(context);
    }

    public SupportPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public SupportPopupWindow(View contentView) {
        super(contentView);
    }

    public SupportPopupWindow(int width, int height) {
        super(width, height);
    }

    public SupportPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    /**
     *  在android7.0上，如果不主动约束PopuWindow的大小，比如，设置布局大小为 MATCH_PARENT,那么PopuWindow会变得尽可能大，
     *  以至于 view下方无空间完全显示PopuWindow，而且view又无法向上滚动，此时PopuWindow会主动上移位置，直到可以显示完全。
     *　解决办法：主动约束PopuWindow的内容大小，重写showAsDropDown方法：
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAsDropDown(anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        //7.0一下正常
        if (Build.VERSION.SDK_INT < 24 || getHeight() == ViewGroup.LayoutParams.WRAP_CONTENT) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            if (getContentView().getContext() instanceof Activity) {
                Activity activity = (Activity) getContentView().getContext();
                int screenHeight;
                // 获取屏幕可用高度：真实高度-虚拟按键的高度
                screenHeight = ScreenUtil.getContentHeight(activity);
                int[] location = new int[2];
                // 获取控件在屏幕的位置
                anchor.getLocationOnScreen(location);
                // pop最大高度
                int maxHeight = screenHeight - location[1] - anchor.getHeight();
                // pop 有具体的高度值，但是小于anchor下边缘与屏幕底部的距离， 正常显示
                if (getHeight() > 0 && getHeight() < maxHeight) {
                    super.showAsDropDown(anchor, xoff, yoff, gravity);
                } else {
                    //设置为最大可用高度
                    setHeight(maxHeight);
                    super.showAsDropDown(anchor, xoff, yoff, gravity);
                }

            }
        }


    }
}
