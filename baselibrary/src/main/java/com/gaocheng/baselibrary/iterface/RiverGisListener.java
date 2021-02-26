package com.gaocheng.baselibrary.iterface;


/**
 * Created by 32672 on 2019/1/10.
 * 基本信息 头部 监听
 */

public  interface RiverGisListener {
    void onStreetClickListener(String id);
    void onWaterLeverListener(String lever);
    void onManageLevelListener(String type);
    void onSituationListener(String type);
    void onSearchListener();
}
