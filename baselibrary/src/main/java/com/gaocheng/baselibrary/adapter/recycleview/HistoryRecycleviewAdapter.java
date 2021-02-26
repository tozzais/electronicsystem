package com.gaocheng.baselibrary.adapter.recycleview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.bean.virtual.HistoryBean;

import java.util.List;

/**
 * Created by 32672 on 2019/1/22.
 */

// ① 创建Adapter
public class HistoryRecycleviewAdapter extends RecyclerView.Adapter<HistoryRecycleviewAdapter.VH>{

    private int lenght;
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView tv_time;
        public final TextView tv_number;
        public final View progress;
        public VH(View v) {
            super(v);
            progress =  v.findViewById(R.id.progress);
            tv_time =  v.findViewById(R.id.tv_time);
            tv_number =  v.findViewById(R.id.tv_number);
        }
    }

    private List<HistoryBean> mDatas;
    public HistoryRecycleviewAdapter(Context context, List<HistoryBean> data) {
        this.mDatas = data;
        this.lenght = (int) context.getResources().getDimension(R.dimen.history_max_lenght);
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(VH holder, int position) {
        HistoryBean historyBean = mDatas.get(position);
        holder.tv_time.setText(historyBean.time);
        holder.tv_number.setText(historyBean.number+"");
        ViewGroup.LayoutParams layoutParams = holder.progress.getLayoutParams();
        layoutParams.width = (int) (1.0*historyBean.number/historyBean.max*lenght);
        holder.progress.setLayoutParams(layoutParams);


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second_history, parent, false);
        return new VH(v);
    }
}
