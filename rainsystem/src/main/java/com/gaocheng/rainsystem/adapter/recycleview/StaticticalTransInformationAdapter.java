package com.gaocheng.rainsystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.TransFormBean;
import com.gaocheng.baselibrary.widget.chart.BaseMessage;
import com.gaocheng.baselibrary.widget.chart.DoubleCakeView;
import com.gaocheng.baselibrary.widget.chart.SecondMessage;
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
public class StaticticalTransInformationAdapter extends BaseQuickAdapter<TransFormBean, BaseViewHolder> {


    public StaticticalTransInformationAdapter() {
        super(R.layout.item_statictical_trans_information, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TransFormBean item) {
        Log.e("item",helper.getAdapterPosition()+"="+item.isOpen);

        DoubleCakeView barchart = helper.getView(R.id.cakeView);
        LinearLayout ll_root = helper.getView(R.id.ll_root);
        LinearLayout ll_more = helper.getView(R.id.ll_more);
        ll_root.setOnClickListener(v -> {
            onclick(helper,item,ll_more);
        });
        helper.setText(R.id.tv_town_name,item.BaseName);
        helper.setText(R.id.tv_total,item.Count+"");
        helper.setText(R.id.tv_title_transing,item.HjZzCount+"");
        helper.setText(R.id.tv_title_finish,item.NoHjFinCount+"");
        helper.setText(R.id.tv_title_plan,item.HjJhCount+"");
        helper.setText(R.id.tv_title_no,item.NoHjCount+"");
        helper.setText(R.id.tv_no_mixing,item.NoHjCounts+"");
        helper.setText(R.id.tv_no_mixing_no,item.NoHjCount+"");
        helper.setText(R.id.tv_no_mixing_finish,item.NoHjFinCount+"");
        helper.setText(R.id.tv_hava_mixing,item.HjCount+"");
        helper.setText(R.id.tv_hava_mixing_traning,item.HjZzCount+"");
        helper.setText(R.id.tv_hava_mixing_plan,item.HjJhCount+"");
        helper.setText(R.id.tv_hava_mixing_no,item.HjFinWjhCount+"");

        setChart(barchart,item);
        int h = 0;
        if (item.isOpen){
            h = (int) mContext.getResources().getDimension(R.dimen.rain_statical_trans);
        }else{
            h = 0;
        }
        ViewGroup.LayoutParams mParams = ll_more.getLayoutParams();
        mParams.height = h;
        ll_more.setLayoutParams(mParams);
    }
    private void onclick(BaseViewHolder helper, TransFormBean item, ViewGroup viewGroup){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.rain_statical_trans);
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
            ViewGroup.LayoutParams mParams = viewGroup.getLayoutParams();
            mParams.height = height;
            viewGroup.setLayoutParams(mParams);
        });
        animator.setDuration(200);
        animator.start();
    }



    public int getColor(int res){
        return mContext.getResources().getColor(res);
    }



    public void setChart(DoubleCakeView chart, TransFormBean item){
        List<BaseMessage> mList = new ArrayList<>();
        BaseMessage mes = new BaseMessage();
        mes.percent = item.HjCount;
        mes.content = "存在混接";
        mes.color = getColor(R.color.chart_yellow);
        SecondMessage s = new SecondMessage(item.HjZzCount, "正在改造", getColor(R.color.chart_yellow_light));
        SecondMessage s1 = new SecondMessage(item.HjJhCount, "计划改造", getColor(R.color.chart_red));
        SecondMessage s2 = new SecondMessage(item.HjFinWjhCount, "未计划", getColor(R.color.chart_gray));
        List<SecondMessage> list = new ArrayList<>();
        list.add(s);
        list.add(s1);
        list.add(s2);
        mes.secondpercentTotal = s.percent+s1.percent+s2.percent;
        mes.secondMessageList = list;
        mList.add(mes);

        BaseMessage message = new BaseMessage();
        message.percent = item.NoHjCounts;
        message.content = "不存在混接";
        message.color = getColor(R.color.no_mixing);

        SecondMessage s4 = new SecondMessage(item.NoHjFinCount, "完成改造", getColor(R.color.chart_green));
        SecondMessage s5 = new SecondMessage(item.NoHjCount, "无需改造", getColor(R.color.chart_blue));
        List<SecondMessage> list1 = new ArrayList<>();
        list1.add(s4);
        list1.add(s5);
        message.secondMessageList = list1;
        message.secondpercentTotal = s4.percent+s5.percent;
        mList.add(message);

        BaseMessage mes0 = new BaseMessage();
        mes0.percent =item.WdcCount;
        mes0.content = "未调查";
        mes0.color = getColor(R.color.chart_gray_deep);

        SecondMessage s6 = new SecondMessage(item.WdcCount, "未调查", getColor(R.color.chart_gray_deep));
        List<SecondMessage> list3 = new ArrayList<>();
        list3.add(s6);
        mes0.secondMessageList = list3;
        mes0.secondpercentTotal = s6.percent;

        mList.add(mes0);
        chart.setCakeData(mList);
    }

}
