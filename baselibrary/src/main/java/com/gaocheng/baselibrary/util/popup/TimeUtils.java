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
public class TimeUtils {
    private static WheelView year;
    private static WheelView month;
    private static WheelView day;
    private static PopupWindow timePopup;

    private static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem();//
            int n_month = month.getCurrentItem() + 1;//
            day.setViewAdapter(getAdapter(wheel.getContext(), 1, getDay(n_year, n_month), "%02d"));
        }
    };

    public static View getDataPick(final Context context,String time, ViewGroup parent, final OnFinishListener listener) {
        Calendar c = Calendar.getInstance();
//        final int curYear = c.get(Calendar.YEAR);
        int maxYear = c.get(Calendar.YEAR);
        final int curYear = maxYear - 5;
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        final View view = View.inflate(context, R.layout.pop_bottom_three_wheel, null);

        year = (WheelView) view.findViewById(R.id.year);
        year.setViewAdapter(getAdapter(context, curYear, maxYear, null));
        year.setCyclic(false);
        year.addScrollingListener(scrollListener);
        month = (WheelView) view.findViewById(R.id.month);
        month.setViewAdapter(getAdapter(context, 1, 12, null));
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        day.setViewAdapter(getAdapter(context, 1, getDay(curYear, curMonth), "%02d"));
        day.setCyclic(false);

        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        int yearItem = 4,
                monthItem = curMonth-1,
                dayItem = curDate-1;
        try {
            yearItem = 5 - maxYear + Integer.parseInt(time.substring(0,4));
            monthItem = Integer.parseInt(time.substring(5,7))-1;
            dayItem = Integer.parseInt(time.substring(8,10))-1;
        }catch (Exception e){

        }
        year.setCurrentItem(yearItem);
        month.setCurrentItem(monthItem);
        day.setCurrentItem(dayItem);
//        if (!"建设年份".equals(time)){
//            startCurrent = 100 - ;
//            endCurrent = 100- maxYear + Integer.parseInt(time.substring(5,9));
//        }


        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePopup != null) {
                    timePopup.dismiss();
                }
                if (listener != null) {
                    int monthInt = month.getCurrentItem() + 1;
                    int dayInt = day.getCurrentItem() + 1;
                    listener.onFinish(curYear + year.getCurrentItem() + "", monthInt < 10 ? "0" + monthInt : monthInt + "",
                            dayInt < 10 ? "0" + dayInt : dayInt + "");
                }

                releaseResourse();
            }
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
        month = null;
        day = null;
    }


    private static NumericWheelAdapter getAdapter(Context context, int minValue, int MaxValue, String format) {
        NumericWheelAdapter adapter = new NumericWheelAdapter(context, minValue, MaxValue, format);
        adapter.setTextSize(14);
        return adapter;

    }


    /**
     * @param year
     * @param month
     * @return
     */
    public static int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    public interface OnFinishListener {
        public void onFinish(String year, String month, String day);
    }
    public interface OnTimeFinishListener {
        public void onFinish(String year, String month, String day, String hour, String minutes);
    }
}
