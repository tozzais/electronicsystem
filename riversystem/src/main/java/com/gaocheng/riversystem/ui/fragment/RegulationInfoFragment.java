package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gaocheng.baselibrary.base.BaseListFragment;
import com.gaocheng.baselibrary.bean.eventbus.UpdataRiverRegulationList;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverRegulationInfoBean;
import com.gaocheng.baselibrary.bean.net.RiverRegulationInfoListBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverRegulationInfoBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverRegulationInfoSearchBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.OnRegulationClickListener;
import com.gaocheng.baselibrary.iterface.RegulationListener;
import com.gaocheng.baselibrary.widget.HomeRecyclerView;
import com.gaocheng.baselibrary.widget.RegulationHeaderView;
import com.gaocheng.baselibrary.widget.SwipeItemLayout;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.RiverMainActivity;
import com.gaocheng.riversystem.adapter.recycleview.RegulationAdapter;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道整治Fragment
 * Created by Administrator on 2017/4/19.
 */

public class RegulationInfoFragment extends BaseListFragment<RiverRegulationInfoBean>
        implements OnRegulationClickListener, RegulationListener {

    public static final int DEFAULT = 1, SEARCH = 2;
    private int loadType = DEFAULT;

    private String town = "", //所属街镇
            level = "",//管理等级
            des = "";//整治情况

    @BindView(R2.id.header)
    RegulationHeaderView header;
    @BindView(R2.id.my_list)
    HomeRecyclerView mRecyclerView;


    @Override
    public int setLayout() {
        return R.layout.fragment_regulation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new RegulationAdapter(this);
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

    @Override
    public void loadData() {
        header.clear();
        town = "";
        level = "";
        des = "";
        loadType = DEFAULT;
        if (loadType == DEFAULT) {
            getData();
        } else if (loadType == SEARCH) {
            getDataForSearch();
        }
    }

    private void getData() {
        loadType = DEFAULT;
        RequestRiverRegulationInfoBean infoBean = new RequestRiverRegulationInfoBean();
        infoBean.PageIndex = PageIndex + "";
        infoBean.PageSize = PageSize + "";
        infoBean.TownID = town;
        infoBean.Level = level;
        infoBean.Des = des;
        new RxHttp<BaseResult<RiverRegulationInfoListBean>>().send(ApiManager.getService().getRiverRegulationInfoList(infoBean),
                new Response<BaseResult<RiverRegulationInfoListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RiverRegulationInfoListBean> infoListBeanBaseResult) {
                        super.onNext(infoListBeanBaseResult);
                        showContent();
                        onNextResult(infoListBeanBaseResult);
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
        RequestRiverRegulationInfoSearchBean infoSearchBean = new RequestRiverRegulationInfoSearchBean();
        infoSearchBean.PageIndex = PageIndex + "";
        infoSearchBean.PageSize = PageSize + "";
        infoSearchBean.Data = searchText;
        new RxHttp<BaseResult<RiverRegulationInfoListBean>>().send(ApiManager.getService().getRiverRegulationInfoList(infoSearchBean),
                new Response<BaseResult<RiverRegulationInfoListBean>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<RiverRegulationInfoListBean> infoListBeanBaseResult) {
                        super.onNext(infoListBeanBaseResult);
                        showContent();
                        onNextResult(infoListBeanBaseResult);
                    }
                    @Override
                    public void onError(Throwable e) {
                        onCompleted();
                        onErrorResult(e);
                    }
                });
    }

    public void onNextResult(BaseResult<RiverRegulationInfoListBean> result) {
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

    private void setData(boolean isRefresh, RiverRegulationInfoListBean regulationInfoListBean) {

        List<RiverRegulationInfoBean> data = regulationInfoListBean.GovernDataList;
        final int size = data == null ? 0 : data.size();
        if (isRefresh && size >0) {
            mAdapter.setNewData(data);
        }else if (isRefresh && !isLoad && size==0){
            showError(getResources().getString(R.string.data_empey));
            return;
        }  else if (isRefresh && isLoad && size == 0) {
            tsg("暂无数据");
            int select = header.getSelete();
            if (select == RegulationHeaderView.search) {

            } else if (select == RegulationHeaderView.street) {//所属街镇
                town = "";
            } else if (select == RegulationHeaderView.level) {//管理等级
                level = "";
            } else if (select == RegulationHeaderView.regulation) {//整治情况
                des = "";
            }
            header.clearAdvanceStatus();
            return;
        }else {
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
        header.setRegulationListener(this);//头部加载数据
    }

    @Override
    public void onItemClick(String id) {//整治信息里面每个item点击了
        if (mActivity instanceof RiverMainActivity) {
            RiverMainActivity mainActivity = (RiverMainActivity) this.mActivity;
            mainActivity.show(RiverMainActivity.REGULATION_INFO, id);
        }
    }

    /**
     * 所属街镇筛选
     * @param townBean
     */
    @Override
    public void onStreetSelectListener(String townBean) {
        PageIndex = 1;
        this.town = townBean;
        getData();
    }


    /**
     * 管理等级筛选
     * @param level
     */
    @Override
    public void onLevelSelectListener(String level) {
        PageIndex = 1;
        this.level = level;
        getData();
    }

    /**
     *整治情况筛选
     * @param regulation
     */
    @Override
    public void onRegulationSelectListener(String regulation) {
        PageIndex = 1;
        this.des = regulation;
        getData();
    }

    /**
     *模糊搜索
     */
    @Override
    public void onSearchListener() {
        PageIndex = 1;
        getDataForSearch();
    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdataRiverRegulationList){
            PageIndex = 1;
            if (loadType == DEFAULT) {
                getData();
            } else if (loadType == SEARCH) {
                getDataForSearch();
            }
        }
    }
}
