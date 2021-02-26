package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.eventbus.UpdataCommunityName;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverGisBasicInfo;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.widget.MyGridView;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.adapter.gridview.ImageAdapter;
import com.gaocheng.riversystem.bean.eventbus.UpdateRiverGisId;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道GIS基本信息
 * Created by Administrator on 2017/4/19.
 */

public class GisBasicInformationFragment extends BaseFragment {
    @BindView(R2.id.tv_flow_town)
    TextView tvFlowTown;
    @BindView(R2.id.tv_shrink_river)
    TextView tvShrinkRiver;
    @BindView(R2.id.tv_belong_town)
    TextView tvBelongTown;
    @BindView(R2.id.tv_control)
    TextView tvControl;
    @BindView(R2.id.tv_pump_name)
    TextView tvPumpName;
    @BindView(R2.id.tv_area)
    TextView tvArea;
    @BindView(R2.id.tv_note)
    TextView tvNote;
    @BindView(R2.id.tv_over_town)
    TextView tvOverTown;
    @BindView(R2.id.tv_length)
    TextView tvLength;
    @BindView(R2.id.gridview)
    MyGridView gridview;




    private String id;

    //是否初始化
    @Override
    public int setLayout() {
        return R.layout.river_fragment_gis_basic_inform;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        id = getArguments().getString("id");
    }

    @Override
    public void loadData() {
        getData(id);
    }

    private void getData(String id) {
        Log.e("shunxu", "第一个界面请求数据了" + id);
        if (id == null) {
            return;
        }
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseResult<RiverGisBasicInfo>>().send(ApiManager.getService().getRiverGisBasicInfo(requestId),
                new Response<BaseResult<RiverGisBasicInfo>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RiverGisBasicInfo> basicInformDetail) {
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

    private void setDate(RiverGisBasicInfo data) {

        String title = data.ManagerGrade +":"+data.RiverName;
        EventBus.getDefault().post(new UpdataCommunityName(title));

        tvFlowTown.setText("流经街镇:" + data.PassTown);
        tvShrinkRiver.setText("汇入河道:" + data.Inflow);
        tvBelongTown.setText("所属街镇:" + data.TownName);
        tvControl.setText("纳入水面积控制:" + data.InRivCtl);
        tvPumpName.setText("泵闸名称:" + data.InPump);
        tvArea.setText("面积(㎡):" + data.Size + "");
        tvNote.setText("备注:" + data.Note);
        tvOverTown.setText("是否跨镇：" + data.MultTown);
        tvLength.setText("长度：" + data.length);

        gridview.setAdapter(new ImageAdapter(data.picList, mActivity));

        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof GisFragment) {
            ((GisFragment) parentFragment).setRiverInfo(data);
        }
    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateRiverGisId) {
            getData(((UpdateRiverGisId) o).id);
        }
    }

}
