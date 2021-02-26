package com.gaocheng.baselibrary.iterface;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.baselibrary.widget.MessageInquireHeaderView;


/**
 * Created by 32672 on 2019/1/9.
 */

public class RecycleTouchListener implements View.OnTouchListener {
    private MessageInquireHeaderView header;
    private HomeRecyclerView scollRecyclerView;


    int mFirstY, mCurrentY, marginTop;
    boolean direction;

    public RecycleTouchListener(MessageInquireHeaderView header, HomeRecyclerView scollRecyclerView) {
        this.header = header;
        this.scollRecyclerView = scollRecyclerView;
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mFirstY = scollRecyclerView.getTouchPointY();
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
                        header.requestLayout();
                    }
                } else {
                    if (top.topMargin < 0) {
                        top.topMargin += mCurrentY - mFirstY;
                        if (top.topMargin > 0) top.topMargin = 0;
                        header.requestLayout();
                    }
                }
                break;
        }




        return false;
    }
}
