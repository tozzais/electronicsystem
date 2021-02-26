package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.BasicInformationBean;
import com.gaocheng.baselibrary.bean.net.BasicInfromListBean;
import com.gaocheng.baselibrary.bean.request.RequestBasicInformBean;
import com.gaocheng.baselibrary.bean.request.RequestBasicInformSearchBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.BasicInfromListener;
import com.gaocheng.baselibrary.iterface.OnBasicInformationClickListener;
import com.gaocheng.baselibrary.widget.BasicMessageHeaderView;
import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.RainMainActivity;
import com.gaocheng.rainsystem.adapter.recycleview.BasicInformationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */


public class BasicMessageFragment extends BaseListFragment<BasicInformationBean>
        implements OnBasicInformationClickListener, BasicInfromListener {

    public static final int DEFAULT = 1, SEARCH = 2;

    private int loadType = DEFAULT;


    @BindView(R2.id.header)
    BasicMessageHeaderView header;
    @BindView(R2.id.my_list)
    HomeRecyclerView mRecyclerView;


    @Override
    public int setLayout() {
        return R.layout.fragment_basicmessage;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initAdapter();
    }

    @Override
    public void loadData() {

        town = "";
        startDate = "";
        roomType = "";
        header.clear();
        loadType = DEFAULT;

        if (loadType == DEFAULT) {
            getData();
        } else if (loadType == SEARCH) {
            getDataForSearch();
        }

    }


    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new BasicInformationAdapter(this);
        mAdapter.setOnLoadMoreListener(() -> {
            PageIndex++;
            if (loadType == DEFAULT) {
                getData();
            } else if (loadType == SEARCH) {
                getDataForSearch();
            }
        });
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    private String town = "", startDate = "", EndDate = "", roomType = "";

    private void getData() {
        loadType = DEFAULT;
        RequestBasicInformBean requestBasicInformBean = new RequestBasicInformBean();
        requestBasicInformBean.PageIndex = PageIndex + "";
        requestBasicInformBean.PageSize = PageSize + "";
        requestBasicInformBean.Town = town;
        requestBasicInformBean.StartDate = startDate;
        requestBasicInformBean.EndDate = EndDate;
        requestBasicInformBean.RoomType = roomType;
        new RxHttp<BaseResult<BasicInfromListBean>>().send(ApiManager.getService().getBasicInformList(requestBasicInformBean),
                new Response<BaseResult<BasicInfromListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<BasicInfromListBean> beanBaseListResult) {
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

    private void getDataForSearch() {
        String searchText = header.getSearchText();
        loadType = SEARCH;
        RequestBasicInformSearchBean requestBasicInformBean = new RequestBasicInformSearchBean();
        requestBasicInformBean.PageIndex = PageIndex + "";
        requestBasicInformBean.PageSize = PageSize + "";
        requestBasicInformBean.data = searchText;
        new RxHttp<BaseResult<BasicInfromListBean>>().send(ApiManager.getService().getBasicInformList(requestBasicInformBean),
                new Response<BaseResult<BasicInfromListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<BasicInfromListBean> beanBaseListResult) {
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

    public void onNextResult(BaseResult<BasicInfromListBean> result) {
        if ("200".equals(result.ret)) {
            setData(PageIndex == 1, result.data);
        } else {
            tsg(result.msg);
        }
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

    private void setData(boolean isRefresh, BasicInfromListBean basicInfromListBean) {
        List<BasicInformationBean> data = basicInfromListBean.rsInfo;
        final int size = data == null ? 0 : data.size();
        if (isRefresh && size > 0) {
            mAdapter.setNewData(data);
        } else if (isRefresh && !isLoad && size == 0) {
            showError(getResources().getString(R.string.data_empey));
            return;
        } else if (isRefresh && isLoad && size == 0){
            tsg("暂无数据");
            int selete = header.getSelete();
            if (selete == BasicMessageHeaderView.search){

            }else if (selete == BasicMessageHeaderView.street){
                town = "";
            }else if (selete == BasicMessageHeaderView.year){
                startDate = "";
            }else if (selete == BasicMessageHeaderView.type){
                roomType = "";
            }
            header.clearAdvanceStatus();
            return;
        } else{
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

    @Override
    protected int getTitleLayout() {
        return -1;
    }

    @Override
    public void initListener() {
        mRecyclerView.setHeader(header);

        header.setBasicInfromListener(this);

    }

    @Override
    public void onItemClick(String id) {//基本信息里面每个item点击了
        if (mActivity instanceof RainMainActivity) {
            RainMainActivity mainActivity = (RainMainActivity) this.mActivity;
            mainActivity.show(RainMainActivity.BASIC_INFORM, id);
        }
    }

    @Override
    public void onStreetClickListener(String townBean) {
        PageIndex = 1;
        town = townBean;
        getData();
    }

    @Override
    public void onYearSelectListener(String year,String endyear) {
        PageIndex = 1;
        startDate = year;
        EndDate = endyear;
        getData();

    }

    @Override
    public void onTypeListener(String type) {
        PageIndex = 1;
        roomType = type;
        getData();

    }

    @Override
    public void onSearchListener() {
        PageIndex = 1;
        getDataForSearch();
    }
}
