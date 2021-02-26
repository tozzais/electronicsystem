package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.MessageBeanForRiver;
import com.gaocheng.baselibrary.bean.net.MessageItemForRiver;
import com.gaocheng.baselibrary.bean.net.TownBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverMessageBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.MessageListener;
import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.baselibrary.widget.MessageInquireHeaderView;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.adapter.recycleview.MessageInquireAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 * 消息查询
 */
public class MessageInquireFragment extends BaseListFragment implements MessageListener {



    @BindView(R2.id.header)
    MessageInquireHeaderView header;
    @BindView(R2.id.my_list)
    HomeRecyclerView mRecyclerView;



    @Override
    public int setLayout() {
        return R.layout.river_fragment_messageinquire;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        initAdapter();

    }

    private void initAdapter() {
        mAdapter = new MessageInquireAdapter();
        mAdapter.setOnLoadMoreListener(()->{
            PageIndex++;
            getData();
        });
        mRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public void loadData() {
        getData();
    }

    private String KeyValue = "",ValueType = "";
    private void getData() {
        RequestRiverMessageBean requestBasicInformBean = new RequestRiverMessageBean();
        requestBasicInformBean.PageIndex = PageIndex + "";
        requestBasicInformBean.PageSize = PageSize + "";
        requestBasicInformBean.KeyValue = KeyValue;
        requestBasicInformBean.ValueType = ValueType;
        new RxHttp<BaseResult<MessageBeanForRiver>>().send(ApiManager.getService().getMessageListForRiver(requestBasicInformBean),
                new Response<BaseResult<MessageBeanForRiver>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<MessageBeanForRiver> beanBaseListResult) {
                        super.onNext(beanBaseListResult);
                        showContent();
                        onNextResult(beanBaseListResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleted();
                        onErrorResult(e);
                    }
                });

    }

    public void onNextResult(BaseResult<MessageBeanForRiver> result) {
        if ("200".equals(result.ret)) {
            setData(PageIndex == 1, result.data);
        } else {
            tsg(result.msg);
        }
    }

    private void setData(boolean isRefresh, MessageBeanForRiver basicInfromListBean) {
        List<MessageItemForRiver> data = basicInfromListBean.RegulationMessageList;
        final int size = data == null ? 0 : data.size();
        if (isRefresh && size > 0) {
            mAdapter.setNewData(data);
        } else if (isRefresh&& !isLoad && size == 0) {
            showError(getResources().getString(R.string.data_empey));
            return;
        } else if (isRefresh && isLoad && size == 0){
            tsg("暂无数据");
            header.clear();

            return;
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PageSize) {
            mAdapter.loadMoreEnd(isRefresh);
        } else {
            mAdapter.loadMoreComplete();
        }
        isLoad = true;
    }

    public void onErrorResult(Throwable e) {
        if (PageIndex != 1) {
            mAdapter.loadMoreFail();
        } else if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            showError(getResources().getString(R.string.error_net));
        } else if (e != null && e instanceof SocketTimeoutException) {
            showError(getResources().getString(R.string.error_timeout));
        } else if (e != null && e instanceof JsonSyntaxException) {
            showError(getResources().getString(R.string.error_syntax));
        } else if (e != null && e instanceof HttpException) {
            showError(getResources().getString(R.string.error_http));
        }

    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);

    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


    @Override
    public void initListener() {
        header.setScollRecyclerView(mRecyclerView);
        header.setMessageListener(this);
    }


    @Override
    public void onStreetClickListener(TownBean townBean) {
        ValueType = "0";
        PageIndex = 1;
        KeyValue = townBean.TownID;
        getData();
    }

    @Override
    public void onSearchListener() {
        ValueType = "1";
        PageIndex = 1;
        KeyValue = header.getEt_search().getText().toString();
        getData();

    }
}
