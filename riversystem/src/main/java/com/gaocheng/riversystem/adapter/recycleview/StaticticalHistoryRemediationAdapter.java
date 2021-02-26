package com.gaocheng.riversystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.adapter.gridview.HistoryTransAdapter;
import com.gaocheng.baselibrary.adapter.recycleview.HistoryRecycleviewAdapter;
import com.gaocheng.baselibrary.bean.net.RainHistorySecondBean;
import com.gaocheng.baselibrary.bean.net.RiverHistoryBean;
import com.gaocheng.baselibrary.bean.virtual.HistoryBean;
import com.gaocheng.riversystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class StaticticalHistoryRemediationAdapter extends BaseQuickAdapter<RiverHistoryBean, BaseViewHolder> {



    public StaticticalHistoryRemediationAdapter() {
        super(R.layout.item_statictical_history_remediation, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RiverHistoryBean item) {

        RecyclerView barchart = helper.getView(R.id.barchart);
        LinearLayout ll_root = helper.getView(R.id.ll_root);

        helper.setText(R.id.tv_town_name,item.TownName);
        helper.setText(R.id.tv_community_name,item.HostalCount);
        GridView gridview = helper.getView(R.id.gridview);
        gridview.setAdapter(new HistoryTransAdapter(item.yearList,mContext));

        ll_root.setOnClickListener(view->{
            onclick(helper,item,barchart);
        });
        getData(barchart,item);
        int h = 0;
        if (item.isOpen){
            h = (int) mContext.getResources().getDimension(R.dimen.statical_history);
        }else{
            h = 0;
        }
        ViewGroup.LayoutParams mParams = barchart.getLayoutParams();
        mParams.height = h;
        barchart.setLayoutParams(mParams);


    }

    private void onclick(BaseViewHolder helper, RiverHistoryBean item, View ll_more ){
        ValueAnimator animator = null;
        int h = (int) mContext.getResources().getDimension(R.dimen.statical_history);
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

    private void getData(RecyclerView recyclerView, final RiverHistoryBean item) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        List<HistoryBean> mList = new ArrayList<>();
        int max = 0;
        for (RainHistorySecondBean bean:item.yearList){
            int i = Integer.parseInt(bean.count);
            if (i>max){
                max = i;
            }
        }
        for (RainHistorySecondBean bean:item.yearList){
            mList.add(new HistoryBean(max,bean.Year,Integer.parseInt(bean.count)));
        }
        recyclerView.setAdapter(new HistoryRecycleviewAdapter(mContext,mList));

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                rv.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }





}
