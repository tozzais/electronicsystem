package com.gaocheng.baselibrary.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Created by 32672 on 2018/12/26.
 */

public class HomeRecyclerView extends RecyclerView {

    private View header;

    public HomeRecyclerView(Context context) {
        super(context);
    }

    public HomeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(e.getAction()==MotionEvent.ACTION_DOWN)mFirstY= (int) e.getY();
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int getTouchPointY() {
        return mFirstY;
    }

    public View getHeader() {
        return header;
    }

    public void setHeader(View header) {
        this.header = header;
        //第一种得到高度
//        header.post(new Runnable() {
//            @Override
//            public void run() {
//                marginTop = header.getHeight();
//                Log.e("mFirstY","marginTop"+marginTop);
//            }
//        });
//第二种得到高度
        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                marginTop = header.getHeight();
//                Log.e("mFirstY","marginTop"+marginTop);
            }
        });
    }

    int mFirstY, mCurrentY, marginTop;
    boolean direction;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_MOVE:
                if (header == null){
                    break;
                }
//                Log.e("mFirstY","mFirstY"+mFirstY);
                mCurrentY = (int) motionEvent.getY();
                RelativeLayout
                        .LayoutParams top = (RelativeLayout.LayoutParams) header.getLayoutParams();
                if (mCurrentY - mFirstY > 0) {
                    direction = false; //向下滑动
                } else {
                    direction = true; //向上滑动
                }
                if (direction) {
                    if (top.topMargin > -marginTop) {
                        top.topMargin += mCurrentY - mFirstY;
                        if (top.topMargin < -marginTop)
                            top.topMargin = -marginTop;
                        requestLayout();
                    }
                } else {
                    if (top.topMargin < 0) {
                        top.topMargin += mCurrentY - mFirstY;
                        if (top.topMargin > 0) top.topMargin = 0;
                        requestLayout();
                    }
                }
                break;


        }
        return super.onTouchEvent(motionEvent);
    }
}
