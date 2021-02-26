package com.gaocheng.riversystem.adapter.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gaocheng.baselibrary.base.BaseAdapter;
import com.gaocheng.baselibrary.bean.net.ImageBean;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.ui.BigImageActivity;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * convertView：缓存布局
 * ViewHolder：缓存控件的实例
 *
 * 图片适配器，用于装载图片数据，然后适配给控件
 * Created by 32672 on 2019/1/18.
 */

public class ImageAdapter extends BaseAdapter<ImageBean> {

    /**
     * 构造方法
     * @param mList （参数类型）图片数据列表，
     * @param context
     */
    public ImageAdapter(List<ImageBean> mList, Context context) {
        super(mList, context);
    }

    /**
     * 此方法在每个子项被滚动到屏幕内，就会被调用
     * @param position
     * @param convertView 用于将之前加载好的布局进行缓存，提高运行速度
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {//如果没有缓存
            convertView = View.inflate(context, R.layout.river_item_image, null);
            holder = new ViewHolder(convertView);//将控件的实例存放在ViewHolder中
            convertView.setTag(holder);//将ViewHolder存储在view中
        } else {//如果有缓存，就重用缓存中的布局
            holder = (ViewHolder) convertView.getTag();//convertView不为空时，将其中存放的ViewHolder取出
        }
        ImageUtil.loadNet(context, holder.itemListIvIcon, mList.get(position).EAP_Link);//根据图片路径，加载图片
        holder.itemListIvIcon.setOnClickListener(v -> {
            String[] array = new String[mList.size()];
            for (int i = 0;i<mList.size();i++){
                array[i] = mList.get(i).EAP_Link;
            }
            BigImageActivity.launch(context,array,position);
        });
        return convertView;//返回当前视图
    }

    /**
     * 告诉ListView有多少个子项（0-5）
     * @return
     */
    @Override
    public int getCount() {
        return mList == null?0:mList.size()>5?5:mList.size();
    }

    /**
     * 内部类，对控件的实例进行缓存
     * 声明布局中的控件
     */
    static class ViewHolder {
        @BindView(R2.id.item_list_iv_icon)
        ImageView itemListIvIcon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
