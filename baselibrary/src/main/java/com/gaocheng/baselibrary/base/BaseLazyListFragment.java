package com.gaocheng.baselibrary.base;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
    setUserVisibleHint: false
    setUserVisibleHint: true
    loadData:
    加载数据: true:true:false

        情况1：第一次加载 进去  没有初始化 但是setUserVisibleHint 已经为可见了。直接请求是不行的，必须要有一个字段判断
 */

public abstract class BaseLazyListFragment<T> extends BaseFragment {

    protected View emptyView;

    protected List<T> mDatas;
    protected BaseQuickAdapter mAdapter;
    protected int PageSize = 10;
    protected int PageIndex = 1;
    protected boolean isLoad = false; //是否加载完成
    protected boolean isInit = false; //是否初始化

    @Override
    public void loadData() {
        isInit = true;
        isCanLoadData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        le( "setUserVisibleHint: "+isVisibleToUser);
        isCanLoadData();
    }

    private void isCanLoadData() {
//        le( "isCanLoadData: "+isInit+":"+isLoad);
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }

    protected void le(String s) {
        Log.e("BaseLazyListFragment",s);
    }


}
