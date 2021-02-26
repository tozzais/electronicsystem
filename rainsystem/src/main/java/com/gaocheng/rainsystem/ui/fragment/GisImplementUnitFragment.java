package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.gaocheng.baselibrary.adapter.listview.PopConstructionAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.UnitsBean;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.bean.eventbus.UpdateGisId;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GisImplementUnitFragment extends BaseFragment {


    @BindView(R2.id.listview)
    ListView listview;

    @Override
    public int setLayout() {
        return R.layout.fragment_gis_implement_unit;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        id = getArguments().getString("id");
    }

    private String id;
    @Override
    public void loadData() {
        getData(id);


    }

    private void getData(String id){
        if (id == null){
            return;
        }
        Log.e("GIS","第三个界面加载了"+id);
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseListResult<UnitsBean>>().send(ApiManager.getService().getUnitList(requestId),
                new Response<BaseListResult<UnitsBean>>(mActivity, false) {
                    @Override
                    public void onNext(BaseListResult<UnitsBean> rainTransDetailBaseResult) {
                        super.onNext(rainTransDetailBaseResult);
                        showContent();
                        if ("200".equals(rainTransDetailBaseResult.ret)) {
                            List<UnitsBean> data = rainTransDetailBaseResult.data;
                            if (data == null || data.size() == 0){
                                showError("暂无实施单位");
                            }else{
                                PopConstructionAdapter adapter = new PopConstructionAdapter(data,mActivity);
                                listview.setAdapter(adapter);
                            }
                        } else {
                            tsg(rainTransDetailBaseResult.msg);
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
        if (o instanceof UpdateGisId){
            id = ((UpdateGisId)o).id;
            getData(id);

        }

    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
