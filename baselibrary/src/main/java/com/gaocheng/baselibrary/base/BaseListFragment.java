package com.gaocheng.baselibrary.base;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public abstract class BaseListFragment<T> extends BaseFragment {

    protected View emptyView;

    protected   List<T>   mDatas;
    protected BaseQuickAdapter mAdapter;
    protected int PageSize = 10;
    protected int PageIndex = 1;
    protected boolean isLoad = false; //是否加载完成
    protected boolean isInit = false; //是否初始化

    @Override
    public void loadData() {
        isLoad = true;
    }
}
