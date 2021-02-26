package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverGisRegulationInfo;
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

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道GIS整治情况
 * Created by Administrator on 2017/4/19.
 */

public class GisRegulationFragment extends BaseFragment {

    @BindView(R2.id.tv_project)
    TextView tvProject;
    @BindView(R2.id.tv_times)
    TextView tvTimes;
    @BindView(R2.id.tv_start_point)
    TextView tvStartPoint;
    @BindView(R2.id.tv_schedule)
    TextView tvSchedule;
    @BindView(R2.id.tv_plan)
    TextView tvPlan;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tv_work_time)
    TextView tvWorkTime;
    @BindView(R2.id.tv_work_end)
    TextView tvWorkEnd;
    @BindView(R2.id.tv_case)
    TextView tvCase;
    @BindView(R2.id.tv_effect)
    TextView tvEffect;
    @BindView(R2.id.tv_evaluation)
    TextView tvEvaluation;
    @BindView(R2.id.tv_finish_time)
    TextView tvFinishTime;
    @BindView(R2.id.tv_site_desc)
    TextView tvSiteDesc;
    @BindView(R2.id.tv_note)
    TextView tvNote;
    @BindView(R2.id.gridview)
    MyGridView gridview;



    @Override
    public int setLayout() {
        return R.layout.river_fragment_gis_regulation_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {
        String id = getArguments().getString("id");
        getData(id);


    }

    private void getData(String id) {
        if (id == null) {
            return;
        }
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseResult<RiverGisRegulationInfo>>().send(ApiManager.getService().getRiverGisRegulationInfo(requestId),
                new Response<BaseResult<RiverGisRegulationInfo>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RiverGisRegulationInfo> basicInformDetail) {
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

    private void setDate(RiverGisRegulationInfo data) {
        tvProject.setText("整治项目:" + data.Project);
        tvTimes.setText("整治次数:" + data.Count);
        tvStartPoint.setText("起始位置:" + data.SPot);
        tvSchedule.setText("整治进度:" + data.Rate);
        tvPlan.setText("整治计划:" + data.RateType);
        tvContent.setText("整治内容:" + data.Detail);
        tvWorkTime.setText("开工时间:" + data.StartTime);
        tvWorkEnd.setText("完成时间:" + data.FinTime);
        tvEffect.setText("整治情况:" + data.Des);
        tvEvaluation.setText("整治效果:" + data.Res);
        tvCase.setText("现场评价:" + data.Content);
        tvFinishTime.setText("完成整治时间:" + data.RTime);
        tvSiteDesc.setText("现场情况描述:" + data.Describe);
        tvNote.setText("备注:" + data.Note);

        gridview.setAdapter(new ImageAdapter(data.picList, mActivity));
    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateRiverGisId) {
            getData(((UpdateRiverGisId) o).id);
        }
    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }
}
