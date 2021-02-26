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
import android.text.TextUtils;
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
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.OnSeleteImageListener;
import com.gaocheng.baselibrary.iterface.TextChangeWatcher;
import com.gaocheng.baselibrary.iterface.ViewPagerPageChangeListener;
import com.gaocheng.baselibrary.util.BitmapUtils;
import com.gaocheng.baselibrary.util.DataUtil;
import com.gaocheng.baselibrary.util.LoadingUtils;
import com.gaocheng.baselibrary.util.PartMapUtils;
import com.gaocheng.baselibrary.util.popup.PopBottomUtil;
import com.gaocheng.baselibrary.widget.decoration.SpaceItemDecoration;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.ui.fragment.GisBasicInformationFragment;
import com.gaocheng.riversystem.ui.fragment.GisImplementUnitFragment;
import com.gaocheng.riversystem.ui.fragment.GisRegulationFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 河道整治现场评估activity
 * Created by 32672 on 2018/12/25.
 */

public class RegulationOnSiteAssessmentActivity extends BaseImageSeleteActivity
        implements OnSeleteImageListener {


    @BindView(R2.id.tv_onsite_assessment)
    TextView tv_onsite_assessment;
    @BindView(R2.id.tv_explain)
    EditText tvExplain;
    @BindView(R2.id.tv_suggest)
    EditText tvSuggest;
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

    private List<Fragment> fragmentList;

    public static void launch(Context context,String id) {
        Intent intent = new Intent(context, RegulationOnSiteAssessmentActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.river_activity_regulation_onsite_assessment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setBackTitle("河道整治现场评估");
    }



    @Override
    public void loadData() {
        fragmentList = new ArrayList<>();
        Bundle bundle = new Bundle();
        id = getIntent().getStringExtra("id");
        bundle.putString("id",id);
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
                if (!TextUtils.isEmpty(s.toString())){
                    isCanLoad = true;
                }

            }
        });
        tvSuggest.addTextChangedListener(new TextChangeWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    isCanLoad = true;
                }
            }
        });

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
            tv_community_title.setText(name+"(基本信息)");
        }else if (position == 1){
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            tv_community_title.setText(name+"(整治信息)");
        }else if (position == 2){
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.INVISIBLE);
            tv_community_title.setText(name+"(实施单位)");
        }
    }

    @OnClick({R2.id.ll_onsite_assessment,R2.id.iv_back, R2.id.iv_advance})
    public void onClick(View view) {

            int id = view.getId();
            if (id == R.id.ll_onsite_assessment){
                PopBottomUtil.getInstance().showSelectDialog(mContext, DataUtil.getBeautifulHomeData(), new PopBottomUtil.onSelectCityFinishListener() {
                    @Override
                    public void onFinish(String s) {
                        tv_onsite_assessment.setText(s);
                    }
                });
            }else if (id == R.id.iv_back) {
                vpView.setCurrentItem(vpView.getCurrentItem() - 1);
            }else if (id == R.id.iv_advance) {
                vpView.setCurrentItem(vpView.getCurrentItem() + 1);
            }
    }

    @Override
    public void selete(ImageView imageView, int position) {
        seleteImage();
    }

    @Override
    public void delete(String path) {
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
        String explain = tvExplain.getText().toString();
        String suggest = tvSuggest.getText().toString();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < mImagesUpload.size(); i++) {
            String path = mImagesUpload.get(i);
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(part);
        }
        mImagesUpload.clear();
        new RxHttp<BaseResult>().send(ApiManager.getService().getOnsiteEvaluationForRiver(
                PartMapUtils.getTextRequestBody(id),
                PartMapUtils.getTextRequestBody(explain),
                PartMapUtils.getTextRequestBody(suggest),
                parts
                ),
                new Response<BaseResult>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult result) {
                        super.onNext(result);
                        if ("200".equals(result.ret)) {
                            tsg("评价成功");
                            EventBus.getDefault().post(new UpdataRiverRegulationList());
                            finish();
                        }
                    }
                });
    }
}
