package com.gaocheng.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.gaocheng.baselibrary.widget.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;


public abstract class BaseActivity<T> extends AppCompatActivity {

    private Toolbar mToolbar;
    public LinearLayout parentView;
    public TextView mTitle;
    public TextView tvLeftTitle;
    protected FrameLayout mFlContainer;
    protected Context mContext;
    protected Activity mActivity;
    protected ProgressLayout progress_layout;
    protected AppBarLayout mHeaderView;



    protected Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(GlobalParams.getTheme());
        super.onCreate(savedInstanceState);
        setContentView(getBaseLayout());

        //设置只能屏幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mContext = this;
        mActivity = this;



        progress_layout = (ProgressLayout) findViewById(R.id.progress_layout);
        mHeaderView = (AppBarLayout) findViewById(R.id.layout_header);
        mFlContainer = (FrameLayout) findViewById(R.id.content_container);
        parentView = (LinearLayout) findViewById(R.id.parent_view);


        int toolbarLayoutRes = getToolbarLayout();
        if (toolbarLayoutRes >= 0){
            //添加toolbar
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderView.addView(LayoutInflater.from(mContext).inflate(toolbarLayoutRes, mHeaderView, false));

        }else{
            mHeaderView.setVisibility(View.GONE);

        }
        //内容View
        mFlContainer.addView(LayoutInflater.from(mContext).inflate(getLayoutId(), mHeaderView, false));

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);


        initView(savedInstanceState);
        loadData();
        initListener();
    }

    protected int getBaseLayout() {
        return R.layout.activity_base;
    }

    protected int getToolbarLayout() {
        return R.layout.base_toolbar;
    }

    /**
     * 初始化 标题栏 只能内部调用
     */
    private void initToolbar(String title, boolean isCanBack) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mTitle = (TextView) findViewById(R.id.tv_title);
            tvLeftTitle = (TextView) findViewById(R.id.tv_left_title);
            mTitle.setText(title);

            if(isCanBack){
                mToolbar.setNavigationIcon(R.mipmap.back);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }else{
                mToolbar.setNavigationIcon(null);
            }
        }
    }



    public abstract int getLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void loadData();

    public  void initListener(){}

    public void  setBackTitle(String title) {
        initToolbar(title,true);
    }

    public void  setBackTitle(int title) {
        initToolbar(getResources().getString(title),true);
    }
    public void  setNotBackTitle(String title) {
        initToolbar(title,false);
    }

    public void  setNotBackTitle(int title) {
        initToolbar(getResources().getString(title),false);
    }
    public Toolbar getToolbar(){
        return mToolbar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Subscribe
    public void onEvent(T t) {

    }
    protected void tsg(String str){  //封装弹出框提示
       ToastCommom.createToastConfig().ToastShow(mContext,str);
    }
    protected void log(String str){//打印提示封装
        Log.e("----------------",str);
    }

    protected void showProress() {
        progress_layout.showLoading();
    }

    protected void showContent() {
        progress_layout.showContent();
    }

    protected void showError() {
        progress_layout.showError(R.string.loading_error, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }
    protected void showError(String message) {
        progress_layout.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }
}
