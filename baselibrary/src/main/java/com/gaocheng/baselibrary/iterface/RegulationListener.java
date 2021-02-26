package com.gaocheng.baselibrary.iterface;


/**
 * 河道整治筛选接口
 * Created by i on 2019/1/17.
 */

public interface RegulationListener {
    void onStreetSelectListener(String townBean);
    void onLevelSelectListener(String level);
    void onRegulationSelectListener(String regulation);
    void onSearchListener();
}
