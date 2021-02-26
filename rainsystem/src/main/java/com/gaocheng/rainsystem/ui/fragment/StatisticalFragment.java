package com.gaocheng.rainsystem.ui.fragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gaocheng.baselibrary.adapter.viewpager.GisPagerAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;

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
        return R.layout.fragment_statistics;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {

        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        fragmentList.add(new StatisticalTransInformationFragment());
        fragmentList.add(new StatisticaConstructionSubjectFragment());
        fragmentList.add(new StatisticaBeautifulHomesFragment());
        fragmentList.add(new StatisticaHistoryTransformationFragment());
        list_Title.add("改造信息");
        list_Title.add("建设主体");
        list_Title.add("美丽家园");
        list_Title.add("历年改造");
        GisPagerAdapter gisPagerAdapter = new GisPagerAdapter(getChildFragmentManager(), mActivity, fragmentList, list_Title);
        vpView.setAdapter(gisPagerAdapter);
        tabs.setupWithViewPager(vpView);//此方法就是让tablayout和ViewPager联动

    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }


}
