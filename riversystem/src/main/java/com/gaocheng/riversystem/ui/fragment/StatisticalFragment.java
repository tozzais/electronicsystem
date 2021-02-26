package com.gaocheng.riversystem.ui.fragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gaocheng.baselibrary.adapter.viewpager.GisPagerAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class StatisticalFragment extends BaseFragment {

    private List<Fragment> fragmentList;
    private List<String> list_Title;

    @BindView(R2.id.tabs)
    TabLayout tabs;
    @BindView(R2.id.vp_view)
    ViewPager vpView;

    @Override
    public int setLayout() {
        return R.layout.river_fragment_statistics;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {

        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        fragmentList.add(new StatisticaRemediationSituationFragment());
        fragmentList.add(new StatisticaManageLevelFragment());
        fragmentList.add(new StatisticaRemediationPlanFragment());
        fragmentList.add(new StatisticaHistoryRemediationFragment());
        list_Title.add("整治情况");
        list_Title.add("管理等级");
        list_Title.add("整治计划");
        list_Title.add("历年整治");
        GisPagerAdapter gisPagerAdapter = new GisPagerAdapter(getChildFragmentManager(), mActivity, fragmentList, list_Title);
        vpView.setAdapter(gisPagerAdapter);
        tabs.setupWithViewPager(vpView);//此方法就是让tablayout和ViewPager联动
        vpView.setOffscreenPageLimit(3);

    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
