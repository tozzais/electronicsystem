package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.eventbus.UpdataCommunityName;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.BasicInformDetail;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.util.StringUtil;
import com.gaocheng.baselibrary.widget.MyGridView;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.adapter.recycleview.gridview.ImageAdapter;
import com.gaocheng.rainsystem.bean.eventbus.UpdateGisId;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GisBasicInformationFragment extends BaseFragment {


    @BindView(R2.id.tv_address)
    TextView tvAddress;
    @BindView(R2.id.tv_drainreturn)
    TextView tvDrainreturn;
    @BindView(R2.id.tv_explain)
    TextView tvExplain;
    @BindView(R2.id.tv_draingo)
    TextView tvDraingo;
    @BindView(R2.id.tv_note)
    TextView tvNote;
    @BindView(R2.id.gridview)
    MyGridView gridview;
    @BindView(R2.id.tv_house_type)
    TextView tvHouseType;
    @BindView(R2.id.tv_build_time)
    TextView tvBuildTime;
    @BindView(R2.id.tv_house_area)
    TextView tvHouseArea;
    @BindView(R2.id.tv_coordinate)
    TextView tvCoordinate;

    //是否初始化

    @Override
    public int setLayout() {
        return R.layout.fragment_gis_basic_inform;
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

    private void getData(String id) {

        Log.e("GIS", "第一个界面请求数据了" + id);
        if (id == null) {
            return;
        }
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseResult<BasicInformDetail>>().send(ApiManager.getService().getBasicInformDetail(requestId),
                new Response<BaseResult<BasicInformDetail>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<BasicInformDetail> basicInformDetail) {
                        super.onNext(basicInformDetail);
                        showContent();
                        if ("200".equals(basicInformDetail.ret)) {
                            setDate(basicInformDetail.data);
                        } else {
                            tsg(basicInformDetail.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
                            showError("网络错误");
                        } else if (e != null && e instanceof SocketTimeoutException) {
                            showError("连接超时");
                        } else if (e != null && e instanceof SocketTimeoutException) {
                            showError("连接超时");
                        } else if (e != null && e instanceof JsonSyntaxException) {
                            showError("解析数据出错");
                        } else if (e != null && e instanceof HttpException) {
                            showError("服务器连接错误");
                        }
                    }
                });

    }

    private void setDate(BasicInformDetail data) {
        Fragment parentFragment = getParentFragment();

        tvHouseType.setText("房屋类型:" + data.HouseType);
        tvBuildTime.setText("建设时间:" + data.BuiltDate);
        tvHouseArea.setText("房屋面积:" + data.BuiltSize);
        tvCoordinate.setText("坐标:"+StringUtil.getCoordinate(data.PosX,data.PosY));
        tvAddress.setText("小区地址:" + data.Address);
        tvDrainreturn.setText("雨水去向:" + data.DrainReturn);
        tvExplain.setText(data.HouseType + "," + data.BuiltDate + "建," + data.BuiltSize + "㎡");
        tvDraingo.setText("污水去向:" + data.DrainGo);
        tvNote.setText("改造备注:" + data.Note);

        gridview.setAdapter(new ImageAdapter(data.imgList, mActivity));

        if (parentFragment instanceof GisFragment) {
            ((GisFragment) parentFragment).setTownName(data);
        }
        EventBus.getDefault().post(new UpdataCommunityName(data.Name));
    }


    @Override
    protected int getTitleLayout() {
        return -1;
    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateGisId) {
            id = ((UpdateGisId) o).id;
            getData(id);

        }
    }
}
