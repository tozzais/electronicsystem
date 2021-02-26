package com.gaocheng.baselibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.widget.LinearLayout;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.R2;
import com.gaocheng.baselibrary.adapter.viewpager.BigImageAdapter;
import com.gaocheng.baselibrary.base.BaseActivity;
import com.gaocheng.baselibrary.util.VpUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/18.
 */

public class BigImageActivity extends BaseActivity {

    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.ll_points)
    LinearLayout mLinearLayout;

    public static  void launch(Context form, String[] data, int position){
        Intent intent = new Intent(form,BigImageActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("position",position);
        form.startActivity(intent);
    }

    public static  void launch(Context form, String data){
        Intent intent = new Intent(form,BigImageActivity.class);
        String[] array = new String[]{data};
        intent.putExtra("data",array);
        intent.putExtra("position",0);
        form.startActivity(intent);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_big_image;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {
//        List<Integer> list = new ArrayList<>();
//        for(Integer i:bannerImages){
//            list.add(i);
//        }
        String[] data = getIntent().getStringArrayExtra("data");
        int position = getIntent().getIntExtra("position",0);

        VpUtil.addPoint1(mActivity,mLinearLayout,data.length);
        viewpager.setAdapter(new BigImageAdapter(data,mActivity));
//        viewpager.setOnPageChangeListener(new BannerPagerListener() {
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position;
                mLinearLayout.getChildAt(newPosition).setEnabled(true);
                mLinearLayout.getChildAt(pointIndex).setEnabled(false);
                // 更新标志位
                pointIndex = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLinearLayout.getChildAt(0).setEnabled(true);
        viewpager.setCurrentItem(position);

    }

    private int pointIndex = 0;
//    private int[] bannerImages = { R.mipmap.cart_detail,R.mipmap.cart_detail,R.mipmap.cart_detail};

    @Override
    protected int getToolbarLayout() {
        return -1;
    }
}
