package com.gaocheng.baselibrary.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.R2;
import com.gaocheng.baselibrary.base.BaseAdapter;
import com.gaocheng.baselibrary.bean.net.UnitsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 筛选条件的排序的适配器
 * 实施单位  适配器
 * Created by Administrator on 2017/4/19.
 */

public class PopConstructionAdapter extends BaseAdapter<UnitsBean> {




    public PopConstructionAdapter(List mList, Context context) {
        super(mList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_construction_pop, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        UnitsBean bean = mList.get(position);
        hodler.tvName.setText(bean.Type+":"+bean.Name);
        hodler.tvContent.setText(bean.Man+" "+bean.Tel + "   备注:"+bean.Note);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_content)
        TextView tvContent;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
