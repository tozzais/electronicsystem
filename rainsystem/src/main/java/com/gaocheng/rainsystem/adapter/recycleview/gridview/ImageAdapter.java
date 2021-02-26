package com.gaocheng.rainsystem.adapter.recycleview.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gaocheng.baselibrary.base.BaseAdapter;
import com.gaocheng.baselibrary.bean.net.ImageBean;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.ui.BigImageActivity;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 32672 on 2019/1/18.
 */

public class ImageAdapter extends BaseAdapter<ImageBean> {


    public ImageAdapter(List<ImageBean> mList, Context context) {
        super(mList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_image, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        ImageUtil.loadNet(context, hodler.itemListIvIcon, mList.get(position).EAP_Link);
        hodler.itemListIvIcon.setOnClickListener(v -> {
            String[] array = new String[mList.size()];
            for (int i = 0;i<mList.size();i++){
                array[i] = mList.get(i).EAP_Link;
            }
            BigImageActivity.launch(context,array,position);
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.size()>5?5:mList.size();
    }


    static class ViewHolder {
        @BindView(R2.id.item_list_iv_icon)
        ImageView itemListIvIcon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
