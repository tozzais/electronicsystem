package com.gaocheng.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.gaocheng.baselibrary.widget.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by jumpbox on 16/4/7.
 */
public abstract class BaseFragment<T> extends Fragment {
    protected Activity mActivity;
    public View mRootView;
    protected RelativeLayout mHeaderView;
    protected FrameLayout mContainerView;
    protected Intent intent;
    protected ProgressLayout progress_layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getBaseLayout(), container, false);

            int titleLayoutRes = getTitleLayout();
            if (titleLayoutRes > 0) {
                //添加头布局
                mHeaderView = (RelativeLayout) mRootView.findViewById(R.id.rl_header);
                mHeaderView.addView(LayoutInflater.from(mActivity).inflate(titleLayoutRes, mHeaderView, false));
            }

            //添加内容区域
            mContainerView = (FrameLayout) mRootView.findViewById(R.id.content_container);
            mContainerView.addView(LayoutInflater.from(mActivity).inflate(setLayout(), mContainerView, false));

            //加载
            progress_layout = (ProgressLayout) mRootView.findViewById(R.id.progress_layout);

            EventBus.getDefault().register(this);
            ButterKnife.bind(this, mRootView);

            initView(savedInstanceState);
        }


        return mRootView;
    }
    @Subscribe
    public void onEvent(T t) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initListener();
    }

    protected int getTitleLayout() {
        return R.layout.base_toolbar;
    }
    protected int getBaseLayout() {
        return R.layout.fragment_base;
    }

    public void initTitle() {

    }

    protected void tsg(String str){
        ToastCommom.createToastConfig().ToastShow(getContext(),str);
    }

    protected void log(String str){
        Log.e("----------------",str);
    }


    public void initEventBus(){
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered){
            EventBus.getDefault().register(this);
        }
    }

    public abstract int setLayout();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void loadData();

    public  void initListener(){}

    public  void scrollTop(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    protected void showProress() {
        progress_layout.showLoading();
    }

    protected void showContent() {
        progress_layout.showContent();
    }

    protected void showError() {
        showError(getString(R.string.loading_error));
    }

    protected void showError(int errorStr) {
        showError(getString(errorStr));
    }

    protected void showError(String errorStr) {
        progress_layout.showError(errorStr, v -> loadData());
    }

    protected void hideHeader() {
        if (null != mHeaderView) {
            mHeaderView.setVisibility(View.GONE);
        }
    }


}
