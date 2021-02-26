package com.gaocheng.rainsystem.util;

import android.graphics.Color;

import com.gaocheng.baselibrary.widget.mp.formatter.ValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32672 on 2019/2/26.
 */

public class BarChartManager {
    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    //颜色集合
    List<Integer> colors = new ArrayList<>();

    public BarChartManager(BarChart barChart) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        initBarChart();
    }

    /**
     * 初始化LineChart
     */
    private void initBarChart() {
        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);

        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);
        //图表的描述
        mBarChart.getDescription().setText("");

        //折线图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);//图示 标签的形状。   正方形
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);//设置最小的区间，避免标签的迅速增多
        xAxis.setDrawGridLines(false);//设置竖状的线是否显示
        xAxis.setCenterAxisLabels(true);//设置标签居中
        // xAxis.setSpaceMax(10);
        // xAxis.setValueFormatter();

        leftAxis.setDrawGridLines(true);//设置横状的线是否显示
        leftAxis.enableGridDashedLine(6f, 3f, 0);//虚线

        leftAxis.setAxisLineWidth(1f);
        leftAxis.setEnabled(true);
        leftAxis.setGridColor(0xacb3e5fc);
        //   leftAxis.setTextColor(0xb3e5fc);//设置左边Y轴文字的颜色
        //   leftAxis.setAxisLineColor(0xb3e5fc);//设置左边Y轴的颜色

        // rightAxis.setDrawGridLines(false);//设置横状的线是否显示
        rightAxis.setEnabled(false);//隐藏右边轴和数字

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        mBarChart.setDoubleTapToZoomEnabled(false); // 设置为false以禁止通过在其上双击缩放图表。
        // mBarChart.setBorderWidth(15);//设置边界宽度
    }



    public void showBarChart(List<String> xValues, List<List<Integer>> yValuesList, List<String> labels
    , List<Integer> colors){

        mBarChart.getXAxis().setValueFormatter(new StringAxisValueFormatter(xValues));
        BarData data = new BarData();


        for (int i = 0; i < yValuesList.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yValuesList.get(i).size(); j++) {
                entries.add(new BarEntry(i,yValuesList.get(i).get(j)));
            }
            // y轴的数据集合
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colors.get(i));
            barDataSet.setValueTextColor(colors.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value,
                                                com.github.mikephil.charting.data.Entry entry,
                                                int dataSetIndex, ViewPortHandler viewPortHandler) {
                    int n = (int) value;
                    return n+"";
                }
            });

            data.addDataSet(barDataSet);
        }
//

        float groupSpace = 0.5f; //柱状图组之间的间距
        float barSpace = 0.05f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet

        // (barSpace + barWidth) * 2 + groupSpace = 1.00 -> interval per "group"
        mBarChart.getXAxis().setLabelCount(xValues.size(), false);
        data.setBarWidth(barWidth);
        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        data.groupBars(0, groupSpace, barSpace);
        mBarChart.setData(data);
        mBarChart.getData().setHighlightEnabled(false);
    }

}
