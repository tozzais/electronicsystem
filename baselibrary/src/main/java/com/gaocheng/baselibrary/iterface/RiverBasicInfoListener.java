package com.gaocheng.baselibrary.iterface;

/**
 * 河道基本信息头部监听
 * Created by i on 2019/1/22.
 */

public interface RiverBasicInfoListener {
    void onStreetClickListener(String townBean);//所属街镇
    void onRiverTypeListener(String type);//水体分类
    void onLevelListener(String level);//管理等级
    void onSearchListener();
}
