package com.gaocheng.baselibrary.iterface;


/**
 * Created by 32672 on 2019/1/10.
 * 基本信息 头部 监听
 */

public  interface BasicInfromListener {
    void onStreetClickListener(String townBean);
    void onYearSelectListener(String year,String endyear);
    void onTypeListener(String type);
    void onSearchListener();
}
