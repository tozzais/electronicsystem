package com.gaocheng.riversystem.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaocheng.baselibrary.adapter.recycleview.RecycleViewdapterForUpload;
import com.gaocheng.baselibrary.adapter.viewpager.GisPagerNoTitleAdapter;
import com.gaocheng.baselibrary.base.BaseImageSeleteActivity;
import com.gaocheng.baselibrary.bean.eventbus.UpdataCommunityName;
import com.gaocheng.baselibrary.bean.eventbus.UpdataRiverRegulationList;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RiverRegulationLoadData;
import com.gaocheng.baselibrary.bean.request.RequestRiverID;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.OnSeleteImageListener;
import com.gaocheng.baselibrary.iterface.TextChangeWatcher;
import com.gaocheng.baselibrary.iterface.ViewPagerPageChangeListener;
import com.gaocheng.baselibrary.util.BitmapUtils;
import com.gaocheng.baselibrary.util.LoadingUtils;
import com.gaocheng.baselibrary.util.PartMapUtils;
import com.gaocheng.baselibrary.util.popup.PopBottomRegulationCase;
import com.gaocheng.baselibrary.util.popup.PopBottomRegulationEffect;
import com.gaocheng.baselibrary.util.popup.TimeUtils;
import com.gaocheng.baselibrary.widget.decoration.SpaceItemDecoration;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.ui.fragment.GisBasicInformationFragment;
import com.gaocheng.riversystem.ui.fragment.GisImplementUnitFragment;
import com.gaocheng.riversystem.ui.fragment.GisRegulationFragment;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 河道整治更新录入
 * Created by 32672 on 2018/12/25.
 */

public class RegulationUpdateActivity extends BaseImageSeleteActivity implements OnSeleteImageListener {


    @BindView(R2.id.tv_regulation_case)
    TextView tvRegulationCase;
    @BindView(R2.id.tv_regulation_effect)
    TextView tvRegulationEffect;
    @BindView(R2.id.tv_regulation_finish_time)
    TextView tvRegulationFinishTime;
    @BindView(R2.id.tv_explain)
    EditText tvExplain;
    @BindView(R2.id.recyclerview_image)
    RecyclerView recyclerviewImage;
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_community_title)
    TextView tvChannelTitle;
    @BindView(R2.id.iv_advance)
    ImageView ivAdvance;
    @BindView(R2.id.vp_view)
    ViewPager vpView;

    private List<Fragment> fragmentList;

    private RiverRegulationLoadData date;

    private String regulation_case="",regulation_effect="";

    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, RegulationUpdateActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.river_activity_regulation_update;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBackTitle("河道整治信息更新");
    }

    @Override
    public void loadData() {
        fragmentList = new ArrayList<>();
        id = getIntent().getStringExtra("id");
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        GisBasicInformationFragment inform = new GisBasicInformationFragment();
        GisRegulationFragment trans = new GisRegulationFragment();
        GisImplementUnitFragment unit = new GisImplementUnitFragment();
        inform.setArguments(bundle);
        trans.setArguments(bundle);
        unit.setArguments(bundle);
        fragmentList.add(inform);
        fragmentList.add(trans);
        fragmentList.add(unit);
        GisPagerNoTitleAdapter gisPagerAdapter = new GisPagerNoTitleAdapter(getSupportFragmentManager(), mActivity, fragmentList);
        vpView.setAdapter(gisPagerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, total);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerviewImage.setLayoutManager(gridLayoutManager);
        recyclerviewImage.addItemDecoration(new SpaceItemDecoration(0));
        setAdapter();
        getData();
    }

    private String name = "";
    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdataCommunityName){
            this.name = ((UpdataCommunityName)o).name;
            setBottomTitle(vpView.getCurrentItem());
        }
    }
    public void setBottomTitle(int position){
        if (position == 0){
            ivBack.setVisibility(View.INVISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            tvChannelTitle.setText(name+"(基本信息)");
        }else if (position == 1){
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            tvChannelTitle.setText(name+"(整治信息)");
        }else if (position == 2){
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.INVISIBLE);
            tvChannelTitle.setText(name+"(实施单位)");
        }
    }
    @Override
    public void initListener() {
        vpView.addOnPageChangeListener(new ViewPagerPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setBottomTitle(position);
            }
        });
        tvExplain.addTextChangedListener(new TextChangeWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (date != null && (!s.toString().equals(date.RiverRenovationDetail.Note))){
                    isCanLoad = true;
                }
            }
        });
    }
    @OnClick({R2.id.ll_regulation_case, R2.id.ll_regulation_effect, R2.id.ll_regulation_finish_time
            , R2.id.iv_back, R2.id.iv_advance})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_regulation_case) {
            PopBottomRegulationCase.getInstance().showSelectDialog(mContext, date.RenovationSituationList, s ->  {
                isCanLoad = true;
                tvRegulationCase.setText(s.Name);
                    regulation_case=s.Code;
            });
        } else if (id == R.id.ll_regulation_effect) {
            PopBottomRegulationEffect.getInstance().showSelectDialog(mContext, date.RenovationEffectList, s -> {
                isCanLoad = true;
                tvRegulationEffect.setText(s.Name);
                    regulation_effect=s.Code;

            });
        } else if (id == R.id.ll_regulation_finish_time) {
            TimeUtils.getDataPick(mContext,tvRegulationFinishTime.getText().toString(), parentView, new TimeUtils.OnFinishListener() {
                @Override
                public void onFinish(String year, String month, String day) {
                    isCanLoad = true;
                    tvRegulationFinishTime.setText(year + "-" + month + "-" + day);
                }
            });
        } else if (id == R.id.iv_back) {
            vpView.setCurrentItem(vpView.getCurrentItem() - 1);
        } else if (id == R.id.iv_advance) {
            vpView.setCurrentItem(vpView.getCurrentItem() + 1);
        }
    }

    @Override
    public void selete(ImageView imageView, int position) {
        seleteImage();
    }

    @Override
    public void delete(String path) {
        //点击叉号 删除图片
        deletePath(path);
    }

    @Override
    protected void setAdapter() {
        super.setAdapter();
        recyclerViewGridAdapter = new RecycleViewdapterForUpload(this, mImagesTotal, this);
        recyclerviewImage.setAdapter(recyclerViewGridAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_commit) {
            commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void commit() {
        if (!isCanLoad) {
            tsg("至少改变一个状态在上传哦");
            return;
        }
        LoadingUtils.show(mContext, "正在处理图片");
        mImagesForLocal.addAll(recyclerViewGridAdapter.getmImageForLoad());
        new Thread(()->{
            for (int i = 0; i < mImagesForLocal.size(); i++) {
                String localPath = mImagesForLocal.get(i);
                int degree = BitmapUtils.getInstance().readPictureDegree(localPath);
                Bitmap bitmap = BitmapUtils.getInstance().rotaingImageView(degree, BitmapFactory.decodeFile(localPath));
                BitmapUtils.getInstance().CompactPic((Activity) mContext, BitmapUtils.getInstance().saveBitmap(mContext, bitmap), path -> {
                    mImagesUpload.add(path);
                    Log.e("正在处理图片", path);
                    if (mImagesUpload.size() == mImagesForLocal.size()) {
                        LoadingUtils.show(mContext, "上传中..");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                start();
                            }
                        });
                    }
                });
            }
        }).start();

    }
    private void start() {
        String finishTime = tvRegulationFinishTime.getText().toString();
        String explain = tvExplain.getText().toString();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < mImagesUpload.size(); i++) {
            String path = mImagesUpload.get(i);
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(part);
        }
        mImagesUpload.clear();
        new RxHttp<BaseResult>().send(ApiManager.getService().getInfoUpLoadForRiver(
                PartMapUtils.getTextRequestBody(id),
                PartMapUtils.getTextRequestBody(regulation_case),
                PartMapUtils.getTextRequestBody(regulation_effect),
                PartMapUtils.getTextRequestBody(finishTime),
                PartMapUtils.getTextRequestBody(explain),
                parts),
                new Response<BaseResult>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult result) {
                        super.onNext(result);
                        if ("200".equals(result.ret)) {
                            tsg("河道整治信息更新成功");
                            EventBus.getDefault().post(new UpdataRiverRegulationList());
                            finish();
                        }
                    }
                });
    }

    /**
     * 从接口获取到数据，并将数据传输到页面
     */
    private void getData(){
        showProress();
        RequestRiverID requestId = new RequestRiverID(id);
        new RxHttp<BaseResult<RiverRegulationLoadData>>().send(ApiManager.getService().getRiverRegulationUpdataData(requestId),
                new Response<BaseResult<RiverRegulationLoadData>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RiverRegulationLoadData> basicInformDetail) {
                        super.onNext(basicInformDetail);
                        showContent();
                        if ("200".equals(basicInformDetail.ret)) {
                            date = basicInformDetail.data;
                            setDate();
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
    /**
     * 将数据设置到页面
     */
    public void setDate() {
        //整治情况
        for (RiverRegulationLoadData.RenovationSituationListBean bean:date.RenovationSituationList){
            if ("0".equals(bean.IsCheck)){
                tvRegulationCase.setText(bean.Name);
                regulation_case = bean.Code;
                break;
            }
        }
        //整治效果
        for (RiverRegulationLoadData.RenovationEffectBean bean:date.RenovationEffectList){
            if ("0".equals(bean.IsCheck)){
                tvRegulationEffect.setText(bean.Name);
                regulation_effect = bean.Code;
                break;
            }
        }
        //整治完成时间
        tvRegulationFinishTime.setText(date.RiverRenovationDetail.RTime);
        //备注
        tvExplain.setText(date.RiverRenovationDetail.Note);
        //加载图片（将多个图片地址存储到mImage中）
        for (RiverRegulationLoadData.ImageListBean bean:date.RiverRenovationDetail.imgList){
            mImagesForNet.add(bean.EAP_Link);
        }
        setAdapter();
    }
}
