package com.gaocheng.baselibrary.util.popup;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.OnWheelScrollListener;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.WheelView;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.adapters.NumericWheelAdapter;

import java.util.Calendar;


/**
 * Created by jumpbox on 16/4/20.
 */
public class YearUtil {
    private static WheelView year;
    private static WheelView endYear;
    private static PopupWindow timePopup;

    private static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
        }
    };

    public static View getDataPick(final Context context,String time, ViewGroup parent, final OnFinishListener listener) {
        Calendar c = Calendar.getInstance();
        int maxYear = c.get(Calendar.YEAR);
        final int minYear = maxYear - 100;

        final View view = View.inflate(context, R.layout.pop_bottom_two_wheel, null);

        year = (WheelView) view.findViewById(R.id.startyear);
        year.setViewAdapter(getAdapter(context, minYear, maxYear, null));
        year.setCyclic(false);
        year.addScrollingListener(scrollListener);
        endYear = (WheelView) view.findViewById(R.id.endyear);
        endYear.setViewAdapter(getAdapter(context, minYear, maxYear, null));
        endYear.setCyclic(false);
        endYear.addScrollingListener(scrollListener);
        view.findViewById(R.id.ll_root).setOnClickListener(v -> {
            if (timePopup != null) {
                timePopup.dismiss();
            }
            releaseResourse();
        });

        year.setVisibleItems(7);
        endYear.setVisibleItems(7);
        int startCurrent = 99,endCurrent = 100;
        try {
            startCurrent = 100 - maxYear + Integer.parseInt(time.substring(0,4));
            endCurrent = 100- maxYear + Integer.parseInt(time.substring(5,9));
        }catch (Exception e){

        }

        year.setCurrentItem(startCurrent);
        endYear.setCurrentItem(endCurrent);


        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_all = (TextView) view.findViewById(R.id.tv_all);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePopup != null) {
                    timePopup.dismiss();
                }
                if (listener != null) {
                    listener.onFinish(minYear + year.getCurrentItem() + "",
                            minYear + endYear.getCurrentItem() + "");
                }

                releaseResourse();
            }
        });
        tv_all.setOnClickListener(v -> {
            if (timePopup != null) {
                timePopup.dismiss();
            }
            if (listener != null) {
                listener.onFinish("",
                        "");
            }

            releaseResourse();
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePopup != null) {
                    timePopup.dismiss();
                }
                releaseResourse();
            }
        });


        timePopup = PopupUtils.showViewAtBottom(context, view, ((Activity) context).getWindow(), parent);


        return view;
    }


    private static void releaseResourse(){
        timePopup = null;
        year = null;
        endYear = null;
    }


    private static NumericWheelAdapter getAdapter(Context context, int minValue, int MaxValue, String format) {
        NumericWheelAdapter adapter = new NumericWheelAdapter(context, minValue, MaxValue, format);
        adapter.setTextSize(14);
        return adapter;

    }




    public interface OnFinishListener {
        void onFinish(String year, String endYear);
    }

}
