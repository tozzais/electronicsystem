package com.gaocheng.baselibrary.util.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.widget.SupportPopupWindow;


public class PopupUtils {

    public static void backgroundAlpha(Window window, float alpha) {
        LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }

    public static SupportPopupWindow getPopupNoAnim(Context context, View contentView, Window window) {
        final SupportPopupWindow popup = new SupportPopupWindow(context);
        popup.setHeight(LayoutParams.MATCH_PARENT);
        popup.setWidth(LayoutParams.MATCH_PARENT);
        popup.setContentView(contentView);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
//        popup.setTouchable(true);
        popup.setOutsideTouchable(false);
        backgroundAlpha(window, 0.5f);
        popup.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    popup.dismiss();
                    return false;
                }
                return false;
            }
        });

        return popup;
    }
    public static SupportPopupWindow getPopupAnim(Context context, View contentView, Window window) {
        final SupportPopupWindow popup = new SupportPopupWindow(context);
        popup.setHeight(LayoutParams.MATCH_PARENT);
        popup.setWidth(LayoutParams.MATCH_PARENT);
        popup.setContentView(contentView);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
        popup.setTouchable(true);
        popup.setOutsideTouchable(false);
//        backgroundAlpha(window, 0.5f);
        popup.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popup.dismiss();
                    return false;
                }
                return false;
            }
        });

        return popup;
    }


    public static SupportPopupWindow showViewAtCenter(Context context, View view, final Window window, View parent) {
        SupportPopupWindow popup = getPopupNoAnim(context, view, window);

        popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(window, 1f);

            }
        });
        return popup;
    }






    public static PopupWindow showViewAtBottom(Context context, View view, final Window window, View parent) {
        PopupWindow popup = getPopup(context, view, window);
        popup.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(window, 1f);
            }
        });
        return popup;
    }

    public static PopupWindow getPopup(Context context, View contentView, Window window) {
        PopupWindow popup = new PopupWindow(context);
        popup.setAnimationStyle(R.style.PopupAnimation);
        popup.setHeight(LayoutParams.WRAP_CONTENT);
        popup.setWidth(LayoutParams.MATCH_PARENT);
        popup.setContentView(contentView);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
//        backgroundAlpha(window, 0.5f);
        return popup;
    }




}
