package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverBasicInfoBean;
import com.gaocheng.baselibrary.bean.net.RiverBasicInfoListBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverBasicInfoBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverBasicInfoSearchBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.OnBasicInformationClickListener;
import com.gaocheng.baselibrary.iterface.RiverBasicInfoListener;
import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.baselibrary.widget.RiverMessageHeaderView;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.RiverMainActivity;
import com.gaocheng.riversystem.adapter.recycleview.BasicInformationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道基本信息Fragment，
 * Created by Administrator on 2017/4/19.
 */


public class BasicInfoFragment extends BaseListFragment<RiverBasicInfoBean>
        implements OnBasicInformationClickListener, RiverBasicInfoListener {

    public static final int DEFAULT = 1, SEARCH = 2;

    private int loadType = DEFAULT;

    private String townName = "";//街镇名称
    private String town = "", riverType = "", level = "";//所属街镇、水体分类、管理等级

    @BindView(R2.id.header_view)
    RiverMessageHeaderView header;
    @BindView(R2.id.my_list)
    HomeRecyclerView mRecyclerView;

    /**
     * 加载布局
     * @return
     */
    @Override
    public int setLayout() {
        return R.layout.river_fragment_basicmessage;
    }

    /**
     * 为布局添加适配器
     * @param savedInstanceState
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new BasicInformationAdapter(this);//初始化适配器
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

    /**
     * 网络请求，加载数据
     */
    @Override
    public void loadData() {
        town = "";
        townName="";
        riverType = "";
        level = "";
        loadType = DEFAULT;
        header.clear();

        if (loadType == DEFAULT) {
            getData();
        } else if (loadType == SEARCH) {
            getDataForSearch();
        }
    }

    /**
     * 默认数据加载
     */
    private void getData() {
        loadType = DEFAULT;
        RequestRiverBasicInfoBean requestRiverBasicInfoBean = new RequestRiverBasicInfoBean();
        requestRiverBasicInfoBean.PageIndex = PageIndex + "";
        requestRiverBasicInfoBean.PageSize = PageSize + "";
        requestRiverBasicInfoBean.TownID=town ;
        requestRiverBasicInfoBean.Town = townName;
        requestRiverBasicInfoBean.Level=level;
        requestRiverBasicInfoBean.RivType=riverType;
        new RxHttp<BaseResult<RiverBasicInfoListBean>>().send(ApiManager.getService().getRiverBasicInfoList(requestRiverBasicInfoBean),
                new Response<BaseResult<RiverBasicInfoListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RiverBasicInfoListBean> beanBaseListResult) {
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

    /**
     * 选择条件查询数据，数据加载
     */
    private void getDataForSearch() {
        String searchText = header.getSearchText();
        loadType = SEARCH;
        RequestRiverBasicInfoSearchBean basicInfoBean = new RequestRiverBasicInfoSearchBean();
        basicInfoBean.PageIndex = PageIndex + "";
        basicInfoBean.PageSize = PageSize + "";
        basicInfoBean.data = searchText;
        new RxHttp<BaseResult<RiverBasicInfoListBean>>().send(ApiManager.getService().getRiverBasicInfoList(basicInfoBean),
                new Response<BaseResult<RiverBasicInfoListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RiverBasicInfoListBean> beanBaseListResult) {
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

    public void onNextResult(BaseResult<RiverBasicInfoListBean> result) {
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

    private void setData(boolean isRefresh, RiverBasicInfoListBean basicInfoListBean) {
        List<RiverBasicInfoBean> data = basicInfoListBean.RiverDataList;
        final int size = data == null ? 0 : data.size();
        if (isRefresh && size > 0) {
            mAdapter.setNewData(data);
        } else if (isRefresh && !isLoad && size == 0) {
            showError(getResources().getString(R.string.data_empey));
            return;
        } else if (isRefresh && isLoad && size == 0) {
            tsg("暂无数据");
            int select = header.getSelete();
            if (select == RiverMessageHeaderView.search) {

            } else if (select == RiverMessageHeaderView.street) {
                town = "";
            } else if (select == RiverMessageHeaderView.level) {
                level = "";
            } else if (select == RiverMessageHeaderView.type) {
                riverType = "";
            }
            header.clearAdvanceStatus();
            return;
        }
        else {
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

    /**
     * 头部监听
     */
    @Override
    public void initListener() {
        mRecyclerView.setHeader(header);
        header.setBasicInfromListener(this);
    }

    /**
     * 基本信息监听
     * @param id
     */
    @Override
    public void onItemClick(String id) {//基本信息里面每个item点击了
        if (mActivity instanceof RiverMainActivity) {
            RiverMainActivity mainActivity = (RiverMainActivity) this.mActivity;
            mainActivity.show(RiverMainActivity.BASIC_INFO, id);
        }
    }

    @Override
    public void onStreetClickListener(String townBean) {
        PageIndex = 1;//从第一页开始（刷新）
        town = townBean;
        getData();
    }

    @Override
    public void onRiverTypeListener(String year) {
        PageIndex = 1;
        riverType = year;//水体分类
        getData();
    }

    @Override
    public void onLevelListener(String type) {
        PageIndex = 1;
        level = type;//管理等级
        getData();
    }

    @Override
    public void onSearchListener() {
        PageIndex = 1;
        getDataForSearch();
    }
}
