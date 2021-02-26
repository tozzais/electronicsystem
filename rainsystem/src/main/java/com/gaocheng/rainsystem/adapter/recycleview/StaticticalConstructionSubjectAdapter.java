package com.gaocheng.rainsystem.adapter.recycleview;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.ConstructSubjectBean;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.util.BarChartManager;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class StaticticalConstructionSubjectAdapter extends BaseQuickAdapter<ConstructSubjectBean, BaseViewHolder> {

    protected Typeface tfLight;

    public StaticticalConstructionSubjectAdapter() {
        super(R.layout.item_statictical_construction_subject, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ConstructSubjectBean item) {

        BarChart barchart = helper.getView(R.id.barchart);
        LinearLayout ll_root = helper.getView(R.id.ll_root);
        ll_root.setOnClickListener(v -> {
            onclick(helper,item,barchart);
        });
        helper.setText(R.id.tv_town_name, item.BaseName);
        helper.setText(R.id.tv_total, (item.EstateCount+item.StreetTownCount+item.WaterCount) + "");
        helper.setText(R.id.tv_room_count, item.EstateCount + "");
        helper.setText(R.id.tv_home_count, item.StreetTownCount + "");
        helper.setText(R.id.tv_water_count, item.WaterCount + "");

//        setChart(barchart);
        barChart(barchart, item);

        int h = 0;
        if (item.isOpen){
            h = (int) mContext.getResources().getDimension(R.dimen.rain_statical_subject);
        }else{
            h = 0;
        }
        ViewGroup.LayoutParams mParams = barchart.getLayoutParams();
        mParams.height = h;
        barchart.setLayoutParams(mParams);

    }
    private void onclick(BaseViewHolder helper, ConstructSubjectBean item, View ll_more ){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.rain_statical_subject);
        if (item.isOpen == true) {
            animator = ValueAnimator.ofInt(h, 0);// 从某个值变化到某个值
        } else {
            animator = ValueAnimator.ofInt(0, h);// 从某个值变化到某个值
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int n = helper.getLayoutPosition();
                    RecyclerView recycleview = getRecyclerView();
                    LinearLayoutManager manager = (LinearLayoutManager) recycleview.getLayoutManager();
                    int firstItem = manager.findFirstVisibleItemPosition();
                    int lastItem = manager.findLastVisibleItemPosition();
                    if (n <= firstItem) {
                        recycleview.smoothScrollToPosition(n);
                    } else if (n <= lastItem) {
                        int top = recycleview.getChildAt(n - firstItem).getTop();
                        recycleview.smoothScrollBy(0, top);
                    } else {
                        recycleview.smoothScrollBy(0, h);
                    }
                }
            },200);

        }
        item.isOpen = !item.isOpen;
        animator.addUpdateListener(animation -> {
            Integer height = (Integer) animation.getAnimatedValue();
            ViewGroup.LayoutParams mParams = ll_more.getLayoutParams();
            mParams.height = height;
            ll_more.setLayoutParams(mParams);
        });
        animator.setDuration(200);
        animator.start();
    }


    private void barChart(BarChart barChart, ConstructSubjectBean item) {
//        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        BarChartManager barChartManager2 = new BarChartManager(barChart);

        //设置x轴的数据
        ArrayList<String> xValues0 = new ArrayList<>();
        xValues0.add("区房管局");
        xValues0.add("街镇美丽家园");
        xValues0.add("区水务局");


        //设置y轴的数据()
        List<List<Integer>> yValues = new ArrayList<>();
//        List<Integer> yValue0 = new ArrayList<>();
//        yValue0.add(0);
//        yValue0.add(0);
//        yValue0.add(0);
//        yValues.add(yValue0);
        List<Integer> yValue1 = new ArrayList<>();
        yValue1.add(item.EstateZzCount);
        yValue1.add(item.StreetTownZzCount);
        yValue1.add(item.WaterZzCount);
        yValues.add(yValue1);
        List<Integer> yValue2 = new ArrayList<>();
        yValue2.add(item.EstateWcCount);
        yValue2.add(item.StreetTownWcCount);
        yValue2.add(item.WaterWcCount);
        yValues.add(yValue2);

        //颜色集合
        List<Integer> colors = new ArrayList<>();
//        colors.add(mContext.getResources().getColor(R.color.chart_gray));
        colors.add(mContext.getResources().getColor(R.color.chart_yellow_light));
        colors.add(mContext.getResources().getColor(R.color.chart_green));

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("正在改造");
        names.add("完成改造");

        //创建多条柱状的图表
//        barChartManager1.showBarChart(xValues, yValues.get(0), names.get(1), colors.get(3));
        barChartManager2.showBarChart(xValues0, yValues, names, colors);
    }


}
