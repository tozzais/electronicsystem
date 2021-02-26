package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.eventbus.UpdataTransList;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RainTransBean;
import com.gaocheng.baselibrary.bean.net.RainTransListBean;
import com.gaocheng.baselibrary.bean.request.RequestBasicInformSearchBean;
import com.gaocheng.baselibrary.bean.request.RequestRainTransBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.OnRainTransformClickListener;
import com.gaocheng.baselibrary.iterface.RainTransListener;
import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.baselibrary.widget.RainTransHeaderView;
import com.gaocheng.baselibrary.widget.SwipeItemLayout;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.RainMainActivity;
import com.gaocheng.rainsystem.adapter.recycleview.RainTransfromationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */


public class TransInformationFragment extends BaseListFragment<RainTransBean>
        implements OnRainTransformClickListener, RainTransListener {

    public static final int DEFAULT = 1, SEARCH = 2;
    private int loadType = DEFAULT;


    @BindView(R2.id.header)
    RainTransHeaderView header;
    @BindView(R2.id.my_list)
    HomeRecyclerView mRecyclerView;


    @Override
    public int setLayout() {
        return R.layout.fragment_rain_transformation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initAdapter();
    }


    @Override
    public void loadData() {
        header.clear();
        town = "";
        mixing = "";
        trans_situation = "";
        loadType = DEFAULT;
        if (loadType == DEFAULT) {
            getData();
        } else if (loadType == SEARCH) {
            getDataForSearch();
        }
    }



    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new RainTransfromationAdapter(this);
        mAdapter.setOnLoadMoreListener(() -> {
            PageIndex++;
            if (loadType == DEFAULT) {
                getData();
            } else if (loadType == SEARCH) {
                getDataForSearch();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private String town = "", mixing = "", trans_situation = "";

    private void getData() {
        loadType = DEFAULT;
        RequestRainTransBean requestBasicInformBean = new RequestRainTransBean();
        requestBasicInformBean.PageIndex = PageIndex + "";
        requestBasicInformBean.PageSize = PageSize + "";
        requestBasicInformBean.Town = town;
        requestBasicInformBean.HunJie = mixing;
        requestBasicInformBean.Des = trans_situation;
        new RxHttp<BaseResult<RainTransListBean>>().send(ApiManager.getService().getRainTransList(requestBasicInformBean),
                new Response<BaseResult<RainTransListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RainTransListBean> beanBaseListResult) {
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
        loadType = SEARCH;
        String searchText = header.getSearchText();
        loadType = SEARCH;
        RequestBasicInformSearchBean requestBasicInformBean = new RequestBasicInformSearchBean();
        requestBasicInformBean.PageIndex = PageIndex + "";
        requestBasicInformBean.PageSize = PageSize + "";
        requestBasicInformBean.data = searchText;
        new RxHttp<BaseResult<RainTransListBean>>().send(ApiManager.getService().getRainTransList(requestBasicInformBean),
                new Response<BaseResult<RainTransListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RainTransListBean> beanBaseListResult) {
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

    public void onNextResult(BaseResult<RainTransListBean> result) {
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

    private void setData(boolean isRefresh, RainTransListBean rainTransListBean) {

        List<RainTransBean> data = rainTransListBean.transInfo;
        final int size = data == null ? 0 : data.size();
        if (isRefresh && size >0) {
            mAdapter.setNewData(data);
        }else if (isRefresh && !isLoad && size==0){
            showError(getResources().getString(R.string.data_empey));
            return;
        } else if (isRefresh && isLoad && size == 0){
            tsg("暂无数据");
            int selete = header.getSelete();
            if (selete == RainTransHeaderView.search){
            }else if (selete == RainTransHeaderView.street){
                town = "";
            }else if (selete == RainTransHeaderView.mix){
                mixing = "";
            }else if (selete == RainTransHeaderView.type){
                trans_situation = "";
            }
            header.clearAdvanceStatus();
            return;
        }  else {
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
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(mActivity));
        header.setBasicInfromListener(this);
    }


    @Override
    public void onItemClick(String id) {//基本信息里面每个item点击了
        if (mActivity instanceof RainMainActivity) {
            RainMainActivity mainActivity = (RainMainActivity) this.mActivity;
            mainActivity.show(RainMainActivity.TRANS_INFORM, id);
        }
    }


    @Override
    public void onStreetClickListener(String townBean) {
        PageIndex = 1;
        this.town = townBean;
        getData();
    }

    @Override
    public void onMixingSelectListener(String mixing) {
        PageIndex = 1;
        this.mixing = mixing;
        getData();

    }

    @Override
    public void onTransSituationSelectListener(String transsituation) {
        PageIndex = 1;
        this.trans_situation = transsituation;
        getData();
    }

    @Override
    public void onSearchListener() {
        PageIndex = 1;
        getDataForSearch();

    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdataTransList){
            PageIndex = 1;
            if (loadType == DEFAULT) {
                getData();
            } else if (loadType == SEARCH) {
                getDataForSearch();
            }
        }
    }
}
