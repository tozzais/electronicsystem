package com.gaocheng.rainsystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.BeautifulHomeBean;
import com.gaocheng.baselibrary.widget.chart.SingCakeBean;
import com.gaocheng.baselibrary.widget.chart.SingleCakeView;
import com.gaocheng.rainsystem.R;

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
public class StaticticalBeautifulHomesAdapter extends BaseQuickAdapter<BeautifulHomeBean, BaseViewHolder> {


    public StaticticalBeautifulHomesAdapter() {
        super(R.layout.item_statictical_beautiful_home, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BeautifulHomeBean item) {

        SingleCakeView barchart = helper.getView(R.id.barchart);
        LinearLayout ll_root = helper.getView(R.id.ll_root);

        helper.setText(R.id.tv_town_name,item.BaseName);
        helper.setText(R.id.tv_total,item.MljyZCount+"");
        helper.setText(R.id.tv_no_trans,item.MljyWgzCount+"");
        helper.setText(R.id.tv_plan_trans,item.MljyJhCount+"");
        helper.setText(R.id.tv_transing,item.MljyZzCount+"");
        helper.setText(R.id.tv_finish_trans,item.MljyWCCount+"");

        ll_root.setOnClickListener(v -> {
            onclick(helper,item,barchart);
        });
        getData(barchart,item);
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
    private void onclick(BaseViewHolder helper, BeautifulHomeBean item, View ll_more ){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.rain_statical_home);
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

    //4 10
    private void getData(SingleCakeView chart,BeautifulHomeBean item) {
        List<SingCakeBean> mList = new ArrayList<>();
        SingCakeBean mes = new SingCakeBean();
        mes.percent = item.MljyWgzCount;
        mes.content = "未改造";
        mes.color = mContext.getResources().getColor(R.color.chart_gray);
        mList.add(mes);

        SingCakeBean mes1 = new SingCakeBean();
        mes1.percent = item.MljyJhCount;
        mes1.content = "计划改造";
        mes1.color = mContext.getResources().getColor(R.color.chart_red);
        mList.add(mes1);

        SingCakeBean mes2 = new SingCakeBean();
        mes2.percent = item.MljyZzCount;
        mes2.content = "正在改造";
        mes2.color = mContext.getResources().getColor(R.color.chart_yellow);
        mList.add(mes2);

        SingCakeBean mes3 = new SingCakeBean();
        mes3.percent = item.MljyWCCount;
        mes3.content = "完成改造";
        mes3.color = mContext.getResources().getColor(R.color.chart_green);
        mList.add(mes3);

        chart.setCakeData(mList);
    }

}
