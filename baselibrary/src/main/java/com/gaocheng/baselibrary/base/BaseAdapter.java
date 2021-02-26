package com.gaocheng.baselibrary.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    public List<T> mList;
    public Context context;

    public BaseAdapter(List<T> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }




    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);
}
