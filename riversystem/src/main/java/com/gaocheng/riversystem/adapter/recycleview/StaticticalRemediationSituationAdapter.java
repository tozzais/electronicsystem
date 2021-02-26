package com.gaocheng.riversystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.RemediationSituationBean;
import com.gaocheng.baselibrary.util.StringUtil;
import com.gaocheng.baselibrary.widget.chart.RiverSingleCakeView;
import com.gaocheng.baselibrary.widget.chart.SingCakeBean;
import com.gaocheng.riversystem.R;

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
public class StaticticalRemediationSituationAdapter extends BaseQuickAdapter<RemediationSituationBean, BaseViewHolder> {


    public StaticticalRemediationSituationAdapter() {
        super(R.layout.item_statictical_remediation_situation, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RemediationSituationBean item) {

        RiverSingleCakeView barchart = helper.getView(R.id.cakeView);
        LinearLayout ll_root = helper.getView(R.id.ll_root);
        LinearLayout ll_more = helper.getView(R.id.ll_more);
        ll_root.setOnClickListener(v -> {
            onclick(helper,item,ll_more);
        });
        helper.setText(R.id.tv_town_name,item.TownName);
        helper.setText(R.id.tv_total,item.RiverCount+"");
        helper.setText(R.id.tv_title_transing,item.ZZCount+"");
        helper.setText(R.id.tv_title_finish,item.WCCount+"");
        helper.setText(R.id.tv_title_plan,item.JHCount+"");

        int wc_count = item.WCZhCount + item.WCSxCount + item.WCQyCount;
        int zz_count = item.ZzZhCount + item.ZzSxCount + item.ZzQyCount;


        helper.setText(R.id.tv_wc_zh,StringUtil.getPercent(item.WCZhCount,wc_count)+"");
        helper.setText(R.id.tv_wc_sx,StringUtil.getPercent(item.WCSxCount,wc_count)+"");
        helper.setText(R.id.tv_wc_qy,StringUtil.getPercent(item.WCQyCount,wc_count)+"");
        helper.setText(R.id.tv_zz_zh,StringUtil.getPercent(item.ZzZhCount,zz_count)+"");
        helper.setText(R.id.tv_zz_sx,StringUtil.getPercent(item.ZzSxCount,zz_count)+"");
        helper.setText(R.id.tv_zz_qy,StringUtil.getPercent(item.ZzQyCount,zz_count)+"");

        setChart(barchart,item);

        int h = 0;
        if (item.isOpen){
            h = (int) mContext.getResources().getDimension(R.dimen.river_statical_situation);
        }else{
            h = 0;
        }
        ViewGroup.LayoutParams mParams = ll_more.getLayoutParams();
        mParams.height = h;
        ll_more.setLayoutParams(mParams);


    }
    private void onclick(BaseViewHolder helper, RemediationSituationBean item, View ll_more ){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.river_statical_situation);
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

    public int getColor(int res){
        return mContext.getResources().getColor(res);
    }



    public void setChart(RiverSingleCakeView chart, RemediationSituationBean item){
        List<SingCakeBean> mList = new ArrayList<>();
        SingCakeBean mes = new SingCakeBean();
        mes.percent = item.WCCount;//3
        mes.content = "完成整治";
        mes.contentAndNumber = "完成整治 "+item.WCCount;
        mes.color = getColor(R.color.chart_green);
        mList.add(mes);

        SingCakeBean mes1 = new SingCakeBean();
        mes1.percent = item.ZZCount; //0
        mes1.content = "正在整治";
        mes1.contentAndNumber = "正在整治 "+item.ZZCount;
        mes1.color = getColor(R.color.chart_yellow);
        mList.add(mes1);

        SingCakeBean mes2 = new SingCakeBean();
        mes2.percent = item.JHCount; //18
        mes2.content = "计划整治";
        mes2.contentAndNumber = "计划整治 "+item.JHCount;
        mes2.color = getColor(R.color.chart_red);
        mList.add(mes2);

        SingCakeBean mes3 = new SingCakeBean();
        mes3.percent = item.NoJHCount; //8
        mes3.content = "未计划";
        mes3.contentAndNumber = "未计划"+item.NoJHCount;
        mes3.color = getColor(R.color.chart_gray);
        mList.add(mes3);

        chart.setCakeData(mList);
    }

}
