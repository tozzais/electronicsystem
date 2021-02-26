package com.gaocheng.baselibrary.adapter.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.R2;
import com.gaocheng.baselibrary.base.BaseAdapter;
import com.gaocheng.baselibrary.bean.net.RainHistorySecondBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 32672 on 2019/1/18.
 */

public class HistoryTransAdapter extends BaseAdapter<RainHistorySecondBean> {




    public HistoryTransAdapter(List<RainHistorySecondBean> mList, Context context) {
        super(mList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_statictical_history_second_transformation, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }

        RainHistorySecondBean rainHistorySecondBean = mList.get(position);
        hodler.tvTitle.setText(rainHistorySecondBean.count);
        hodler.tvContent.setText(rainHistorySecondBean.Year+"年");
        return convertView;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size() > 4 ? 4 : mList.size();
    }


    static class ViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
