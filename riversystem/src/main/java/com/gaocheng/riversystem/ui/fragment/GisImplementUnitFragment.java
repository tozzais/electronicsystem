package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.gaocheng.baselibrary.adapter.listview.PopConstructionAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverGisUnitInfo;
import com.gaocheng.baselibrary.bean.net.UnitsBean;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.bean.eventbus.UpdateRiverGisId;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道GIS实施单位
 * Created by Administrator on 2017/4/19.
 */

public class GisImplementUnitFragment extends BaseFragment {

    @BindView(R2.id.listview)
    ListView listview;

    @Override
    public int setLayout() {
        return R.layout.river_fragment_gis_implement_unit;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {
        String id = getArguments().getString("id");
        getData(id);
    }

    private void getData(String id){
        if (id == null){
            return;
        }
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseResult<RiverGisUnitInfo>>().send(ApiManager.getService().getRiverGisUnitInfo(requestId),
                new Response<BaseResult<RiverGisUnitInfo>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RiverGisUnitInfo> baseResult) {
                        super.onNext(baseResult);
                        showContent();
                        if ("200".equals(baseResult.ret)) {
                            List<UnitsBean> data = baseResult.data.unitList;
                            if (data == null || data.size() == 0){
                                showError("暂无实施单位");
                            }else{
                                PopConstructionAdapter adapter = new PopConstructionAdapter(data,mActivity);
                                listview.setAdapter(adapter);
                            }
                        } else {
                            tsg(baseResult.msg);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (!NetworkUtil.isNetworkAvailable(mActivity)){
                            showError("网络错误");
                        }else if (e != null && e instanceof SocketTimeoutException){
                            showError("连接超时");
                        }else if (e != null && e instanceof SocketTimeoutException){
                            showError("连接超时");
                        }else if (e != null && e instanceof JsonSyntaxException){
                            showError("解析数据出错");
                        }else if (e != null && e instanceof HttpException){
                            showError("服务器连接错误");
                        }
                    }
                });
    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateRiverGisId){
            getData(((UpdateRiverGisId)o).id);
        }
    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
