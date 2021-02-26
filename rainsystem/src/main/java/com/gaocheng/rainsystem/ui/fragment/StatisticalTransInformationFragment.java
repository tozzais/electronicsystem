package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.TransFormBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.adapter.recycleview.StaticticalTransInformationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */

public class StatisticalTransInformationFragment extends BaseListFragment<TransFormBean> {


//    protected StaticticalTransInformationAdapter mAdapter;
    @BindView(R2.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R2.id.rv_list)
    RecyclerView mRecyclerView;


    @Override
    public int setLayout() {
        return R.layout.fragment_recycleview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initAdapter();
    }

    @Override
    public void loadData() {
        isInit = true;

        if (getUserVisibleHint() && !isLoad){
            getData();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoad && isInit){
            getData();
        }
    }


    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        swipeLayout.setOnRefreshListener(() -> getData());
        mAdapter = new StaticticalTransInformationAdapter();
//        mAdapter.setOnLoadMoreListener(()->{});
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    private void getData() {
        if (!isLoad) {
            showProress();
        }
        new RxHttp<BaseListResult<TransFormBean>>().send(ApiManager.getService().getTransFromList(),
                new Response<BaseListResult<TransFormBean>>(mActivity, false) {
                    @Override
                    public void onNext(BaseListResult<TransFormBean> beanBaseListResult) {
                        super.onNext(beanBaseListResult);
                        swipeLayout.setRefreshing(false);
                        showContent();
                        if ("200".equals(beanBaseListResult.ret)) {
                            isLoad = true;
                            setData(beanBaseListResult.data);
                        } else {
                            tsg(beanBaseListResult.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleted();
                        String errorMsg = "";
                        swipeLayout.setRefreshing(false);
                        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
                            errorMsg = getResources().getString(R.string.error_net);
                        } else if (e != null && e instanceof SocketTimeoutException) {
                            errorMsg = getResources().getString(R.string.error_timeout);
                        } else if (e != null && e instanceof JsonSyntaxException) {
                            errorMsg = getResources().getString(R.string.error_syntax);
                        } else if (e != null && e instanceof HttpException) {
                            errorMsg = getResources().getString(R.string.error_http);
                        }
                        if (!isLoad) {
                            showError(errorMsg);
                        } else {
                            tsg(errorMsg);
                        }
                    }
                });

    }


    private void setData(List<TransFormBean> data) {
        mAdapter.setNewData(data);
//        mAdapter.addData(data);
        mAdapter.loadMoreEnd(false);
    }


    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
