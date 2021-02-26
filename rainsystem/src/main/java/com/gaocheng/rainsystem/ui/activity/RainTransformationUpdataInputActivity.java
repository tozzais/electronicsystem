package com.gaocheng.rainsystem.ui.activity;

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
import com.gaocheng.baselibrary.bean.eventbus.UpdataTransList;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.RainInfromUpdataLoadData;
import com.gaocheng.baselibrary.bean.request.RequestEiId;
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
import com.gaocheng.baselibrary.util.popup.PopBottomBeautifulHome;
import com.gaocheng.baselibrary.util.popup.PopBottomEffect;
import com.gaocheng.baselibrary.util.popup.PopBottomReformSitution;
import com.gaocheng.baselibrary.util.popup.TimeUtils;
import com.gaocheng.baselibrary.widget.ListScrollView;
import com.gaocheng.baselibrary.widget.decoration.SpaceItemDecoration;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.ui.fragment.GisBasicInformationFragment;
import com.gaocheng.rainsystem.ui.fragment.GisImplementUnitFragment;
import com.gaocheng.rainsystem.ui.fragment.GisTransformationFragment;
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
 * Created by 32672 on 2018/12/25.
 */

public class RainTransformationUpdataInputActivity extends BaseImageSeleteActivity
        implements OnSeleteImageListener{


    @BindView(R2.id.tv_beautiful_home)
    TextView tvBeautifulHome;
    @BindView(R2.id.tv_trans_situation)
    TextView tvTransSituation;
    @BindView(R2.id.tv_trans_result)
    TextView tvTransResult;
    @BindView(R2.id.tv_trans_time)
    TextView tvTransTime;
    @BindView(R2.id.tv_explain)
    EditText tvExplain;
    @BindView(R2.id.recyclerview_image)
    RecyclerView recyclerviewImage;
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_community_title)
    TextView tv_community_title;
    @BindView(R2.id.iv_advance)
    ImageView ivAdvance;
    @BindView(R2.id.vp_view)
    ViewPager vpView;
    @BindView(R2.id.scrollView)
    ListScrollView scrollView;

    private List<Fragment> fragmentList;

    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, RainTransformationUpdataInputActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_raintrans_updata_input;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBackTitle("小区雨污改造信息更新");
    }

    private RainInfromUpdataLoadData date;

    @Override
    public void loadData() {
        fragmentList = new ArrayList<>();
        id = getIntent().getStringExtra("id");
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        GisBasicInformationFragment inform = new GisBasicInformationFragment();
        GisTransformationFragment trans = new GisTransformationFragment();
        GisImplementUnitFragment unit = new GisImplementUnitFragment();
        inform.setArguments(bundle);
        trans.setArguments(bundle);
        unit.setArguments(bundle);
        fragmentList.add(inform);
        fragmentList.add(trans);
        fragmentList.add(unit);
        GisPagerNoTitleAdapter gisPagerAdapter = new GisPagerNoTitleAdapter(getSupportFragmentManager(), mActivity, fragmentList);
        vpView.setAdapter(gisPagerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerviewImage.setLayoutManager(gridLayoutManager);
        recyclerviewImage.addItemDecoration(new SpaceItemDecoration(0));
        getData();

    }

    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof UpdataCommunityName) {
            this.name = ((UpdataCommunityName) o).name;
            setBottomTitle(vpView.getCurrentItem());
        }
    }

    private String name = "";

    @Override
    public void initListener() {
        vpView.addOnPageChangeListener(new ViewPagerPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setBottomTitle(position);
            }
        });
        scrollView.setListView(vpView);
        tvExplain.addTextChangedListener(new TextChangeWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (date != null && !s.toString().equals(date.Note)) {
                    isCanLoad = true;
                }
            }
        });
    }

    public void setBottomTitle(int position) {
        if (position == 0) {
            ivBack.setVisibility(View.INVISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            tv_community_title.setText(name + "(基本信息)");
        } else if (position == 1) {
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            tv_community_title.setText(name + "(整治信息)");
        } else if (position == 2) {
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.INVISIBLE);
            tv_community_title.setText(name + "(实施单位)");
        }
    }


    @OnClick({R2.id.ll_beautiful_home, R2.id.ll_trans_situation, R2.id.ll_trans_result, R2.id.ll_trans_time
            , R2.id.iv_back, R2.id.iv_advance})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_beautiful_home) {
            PopBottomBeautifulHome.getInstance().showSelectDialog(mContext, date.BeautifulHome, s -> {
                isCanLoad = true;
                tvBeautifulHome.setText(s.Name);
                beautiful_home_upload = s.ID;
            });
        } else if (id == R.id.ll_trans_situation) {
            PopBottomReformSitution.getInstance().showSelectDialog(mContext, date.ReformSituation, s -> {
                isCanLoad = true;
                tvTransSituation.setText(s.Name);
                trans_situation_upload = s.ID;

            });
        } else if (id == R.id.ll_trans_result) {
            PopBottomEffect.getInstance().showSelectDialog(mContext, date.ReformEffect, s -> {
                isCanLoad = true;
                tvTransResult.setText(s.Name);
                trans_result_upload = s.ID;

            });
        } else if (id == R.id.ll_trans_time) {
            TimeUtils.getDataPick(mContext,tvTransTime.getText().toString(), parentView, new TimeUtils.OnFinishListener() {
                @Override
                public void onFinish(String year, String month, String day) {
                    isCanLoad = true;
                    tvTransTime.setText(year + "-" + month + "-" + day);
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
        //点击最后一个图片
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

    private String beautiful_home_upload = "", trans_situation_upload = "", trans_result_upload = "";

    private void commit() {
        if (!isCanLoad) {
            tsg("至少改变一个状态在上传哦");
            return;
        }
        String trans_time = tvTransTime.getText().toString();
        String explain = tvExplain.getText().toString();

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
                                start(trans_time, explain);
                            }
                        });
                    }
                });
            }
        }).start();
    }
    private void start(String trans_time, String explain) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < mImagesUpload.size(); i++) {
            String path = mImagesUpload.get(i);
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(part);
        }
        mImagesUpload.clear();

        new RxHttp<BaseResult>().send(ApiManager.getService().getInFormUpdataForRain(
                PartMapUtils.getTextRequestBody(id),
                PartMapUtils.getTextRequestBody(beautiful_home_upload),
                PartMapUtils.getTextRequestBody(trans_situation_upload),
                PartMapUtils.getTextRequestBody(trans_result_upload),
                PartMapUtils.getTextRequestBody(trans_time),
                PartMapUtils.getTextRequestBody(explain),
                parts
                ),
                new Response<BaseResult>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult result) {
                        super.onNext(result);
                        if ("200".equals(result.ret)) {
                            BitmapUtils.getInstance().deleteCacheImage();
                            tsg("雨污改造信息更新成功");
                            EventBus.getDefault().post(new UpdataTransList());
                            finish();
                        }
                    }
                });
    }
    private void getData() {
        showProress();
        RequestEiId requestId = new RequestEiId(id);
        new RxHttp<BaseResult<RainInfromUpdataLoadData>>().send(ApiManager.getService().getInformUpdataData(requestId),
                new Response<BaseResult<RainInfromUpdataLoadData>>(mActivity, false) {
                    @Override
                    public void onNext(BaseResult<RainInfromUpdataLoadData> basicInformDetail) {
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
    public void setDate() {
        for (RainInfromUpdataLoadData.BeautifulHomeBean bean : date.BeautifulHome) {
            if ("0".equals(bean.IsCheck)) {
                tvBeautifulHome.setText(bean.Name);
                beautiful_home_upload = bean.ID;
                break;
            }
        }
        for (RainInfromUpdataLoadData.ReformSituationBean bean : date.ReformSituation) {
            if ("0".equals(bean.IsCheck)) {
                tvTransSituation.setText(bean.Name);
                trans_situation_upload = bean.ID;
                break;
            }
        }
        for (RainInfromUpdataLoadData.ReformEffectBean bean : date.ReformEffect) {
            if ("0".equals(bean.IsCheck)) {
                tvTransResult.setText(bean.Name);
                trans_result_upload = bean.ID;
                break;
            }
        }

        tvTransTime.setText(date.Reform);
        tvExplain.setText(date.Note);

        for (RainInfromUpdataLoadData.ImageListBean bean : date.imageList) {
            mImagesForNet.add(bean.EAP_Link);
        }
        setAdapter();
    }
}
