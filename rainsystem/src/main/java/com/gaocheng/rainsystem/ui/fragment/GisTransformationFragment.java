package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RainTransDetail;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.widget.MyGridView;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.adapter.recycleview.gridview.ImageAdapter;
import com.gaocheng.rainsystem.bean.eventbus.UpdateGisId;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GisTransformationFragment extends BaseFragment {


    @BindView(R2.id.tv_draingo)
    TextView tvDraingo;
    @BindView(R2.id.tv_drainreturn)
    TextView tvDrainreturn;
    @BindView(R2.id.tv_starttime)
    TextView tvStarttime;
    @BindView(R2.id.tv_fintime)
    TextView tvFintime;
    @BindView(R2.id.tv_gzcontent)
    TextView tvGzcontent;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tv_note)
    TextView tvNote;
    @BindView(R2.id.tv_case)
    TextView tvCase;
    @BindView(R2.id.tv_advisenote)
    TextView tvAdvisenote;
    @BindView(R2.id.gridview)
    MyGridView gridview;
    @BindView(R2.id.tv_trans_sutiation)
    TextView tvTransSutiation;
    @BindView(R2.id.tv_trans_subject)
    TextView tvTransSubject;

    @Override
    public int setLayout() {
        return R.layout.fragment_gis_trans_inform;
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
        if (id == null) {
            return;
        }
        Log.e("GIS", "第二个界面加载了" + id);
        showProress();
        RequestId requestId = new RequestId(id);
        new RxHttp<BaseResult<RainTransDetail>>().send(ApiManager.getService().getRainTransDetail(requestId),
                new Response<BaseResult<RainTransDetail>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RainTransDetail> basicInformDetail) {
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

    private void setDate(RainTransDetail data) {
        tvDraingo.setText("污水去向:" + data.DrainGo);
        tvDrainreturn.setText("雨(污)水去向:" + data.DrainReturn);
        tvStarttime.setText("开工时间:" + data.StartTime);
        tvFintime.setText("计划完成时间:" + data.FinTime);
        tvTransSutiation.setText("改造情况:"+data.Des);
        tvTransSubject.setText("改造主体:"+data.Entity);
        tvGzcontent.setText("改造内容:" + data.GZContent);
        tvContent.setText("评价:" + data.Content);
        tvNote.setText("改造备注:" + data.Note);
        tvCase.setText("现场情况:" + data.Case);
        tvAdvisenote.setText("意见建议:" + data.AdviseNote);

        gridview.setAdapter(new ImageAdapter(data.imgList, mActivity));


    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdateGisId) {
            this.id = ((UpdateGisId) o).id;
            getData(this.id);

        }
    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
