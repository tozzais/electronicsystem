package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.RemediationSituationBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.adapter.recycleview.StaticticalRemediationSituationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 * 整治情况
 */

public class StatisticaRemediationSituationFragment extends BaseListFragment<RemediationSituationBean> {


//    protected StaticticalRemediationSituationAdapter mAdapter;
    @BindView(R2.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R2.id.rv_list)
    RecyclerView mRecyclerView;


    @Override
    public int setLayout() {
        return R.layout.river_fragment_recycleview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initAdapter();
    }

    @Override
    public void loadData() {
        getData();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        swipeLayout.setOnRefreshListener(() -> getData());
        mAdapter = new StaticticalRemediationSituationAdapter();
//        mAdapter.setOnLoadMoreListener(()->{});
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    private void getData() {
        if (!isLoad) {
            showProress();
        }
        new RxHttp<BaseListResult<RemediationSituationBean>>().send(ApiManager.getService().getRemediationSituationList(),
                new Response<BaseListResult<RemediationSituationBean>>(mActivity, false) {
                    @Override
                    public void onNext(BaseListResult<RemediationSituationBean> beanBaseListResult) {
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


    private void setData(List<RemediationSituationBean> data) {
        mAdapter.setNewData(data);
//        mAdapter.addData(data);
        mAdapter.loadMoreEnd(false);
    }


    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
