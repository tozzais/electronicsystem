package com.gaocheng.baselibrary.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.R2;
import com.gaocheng.baselibrary.base.BaseAdapter;
import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基础数据绑定 适配器
 * Created by Administrator on 2017/4/19.
 *  房屋类型 混接情况 改造情况 水体分类 管理等级 整治情况
 */

public class BaseBindAdapter extends BaseAdapter<BasicDataBindBean> {


    public BaseBindAdapter(List mList, Context context) {
        super(mList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_screen_list, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        hodler.tvTitle.setText(mList.get(position).Name);
        return convertView;
    }

    static class ViewHolder {

        @BindView(R2.id.tv_title)
        TextView tvTitle;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
