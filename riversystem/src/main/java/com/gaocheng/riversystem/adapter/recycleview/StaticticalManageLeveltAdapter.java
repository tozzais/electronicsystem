package com.gaocheng.riversystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.ManageLevelBean;
import com.gaocheng.baselibrary.widget.chart.SingCakeBean;
import com.gaocheng.baselibrary.widget.chart.SingleCakeView;
import com.gaocheng.baselibrary.widget.mp.formatter.ValueFormatter;
import com.gaocheng.riversystem.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
public class StaticticalManageLeveltAdapter extends BaseQuickAdapter<ManageLevelBean, BaseViewHolder> {

    public StaticticalManageLeveltAdapter() {
        super(R.layout.item_statictical_manage_level, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManageLevelBean item) {

        SingleCakeView barchart = helper.getView(R.id.cakeView);
        BarChart chart = helper.getView(R.id.chart);
        LinearLayout ll_root = helper.getView(R.id.ll_root);
        RelativeLayout ll_more = helper.getView(R.id.ll_more);
        ll_root.setOnClickListener(v -> {
                  onclick(helper,item,ll_more);
                }
        );
        helper.setText(R.id.tv_town_name, item.TownName);
        helper.setText(R.id.tv_total, item.WaterCount + "");
        helper.setText(R.id.tv_river_total, item.RiverCount + "");
        helper.setText(R.id.tv_room_count, item.CityCount + "");
        helper.setText(R.id.tv_home_count, item.AreaCount + "");
        helper.setText(R.id.tv_water_count, item.TownCount + "");
        helper.setText(R.id.tv_water_coun2, item.VillageCount + "");

        getData(barchart, item);

        setChart(chart);
        setData(chart, item);

        int h = 0;
        if (item.isOpen){
            h = (int) mContext.getResources().getDimension(R.dimen.river_statical_manage_level);
        }else{
            h = 0;
        }
        ViewGroup.LayoutParams mParams = ll_more.getLayoutParams();
        mParams.height = h;
        ll_more.setLayoutParams(mParams);


    }
    private void onclick(BaseViewHolder helper, ManageLevelBean item,RelativeLayout ll_more ){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.river_statical_manage_level);
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

    private void getData(SingleCakeView barchart, ManageLevelBean item) {
        List<SingCakeBean> mList = new ArrayList<>();
        SingCakeBean mes = new SingCakeBean();
        mes.percent = item.CityCount;
        mes.content = "市管河道";
        mes.color = mContext.getResources().getColor(R.color.chart_green);
        mList.add(mes);

        SingCakeBean mes1 = new SingCakeBean();
        mes1.percent = item.AreaCount;
        mes1.content = "区管河道";
        mes1.color = mContext.getResources().getColor(R.color.chart_yellow);
        mList.add(mes1);

        SingCakeBean mes2 = new SingCakeBean();
        mes2.percent = item.TownCount;
        mes2.content = "镇管河道";
        mes2.color = mContext.getResources().getColor(R.color.chart_red);
        mList.add(mes2);

        SingCakeBean mes3 = new SingCakeBean();
        mes3.percent = item.VillageCount;
        mes3.content = "村管河道";
        mes3.color = mContext.getResources().getColor(R.color.chart_gray);
        mList.add(mes3);

        barchart.setCakeData(mList);

    }

    private String[] s = new String[]{"河道", "湖泊", "其他湖泊"};

    private void setData(BarChart chart, ManageLevelBean item) {
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, item.RivCount));
        values.add(new BarEntry(1, item.LakeCount));
        values.add(new BarEntry(2, item.OtherLakeHCount));
        BarDataSet set1;
        set1 = new BarDataSet(values, "");
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value,
                                            com.github.mikephil.charting.data.Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                return n + "";
            }
        });
        set1.setValueTextSize(10f);

        set1.setColors(mContext.getResources().getColor(R.color.chart_blue),
                mContext.getResources().getColor(R.color.chart_green),
                mContext.getResources().getColor(R.color.chart_red));
        set1.setDrawValues(true);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.5f);
        chart.setData(data);
//            chart.setFitBars(true);
//        }

        chart.getData().setHighlightEnabled(false);
        chart.invalidate();


    }

    // 初始化柱状图数据（可以根据自己需要插入数据）
    private void setChart(BarChart chart) {
        chart.setDoubleTapToZoomEnabled(false);
        chart.getDescription().setEnabled(false); //设置描述
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);  //设置按比例放缩柱状图
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);////设置X轴标签显示位置
        xAxis.setDrawGridLines(false); //不绘制格网线
        xAxis.setLabelCount(s.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(s));
        //y轴设置
        YAxis leftAxis = chart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0f);//设置Y轴最小值
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(true);
        leftAxis.setLabelCount(6, true);
//        leftAxis.setAxisMaxValue(300);
        chart.getAxisRight().setEnabled(false);//禁用右侧y轴
        chart.animateY(1500);
        Legend l = chart.getLegend();
        l.setEnabled(false);

    }


}
