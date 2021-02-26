package com.gaocheng.baselibrary.global;

import com.gaocheng.baselibrary.http.HttpUrl;

/**
 * 全部的常量类。
 * Created by Administrator on 2017/4/15.
 */

public class Constant {


    public static String BINGDATA = "minhang_binddata";  //绑定数据

//        public static String MAP_BASE_URL = "http://192.168.252.12:6080/"; //演示地址
    public static String MAP_BASE_URL = HttpUrl.BASE+":6080/";

    public static String SYSTEMTYPE = "SystemType";  //是打开雨污 还是打开河道
    public static String RAIN_UPDATE_PERMISSION = "rain_update_Permission";  //雨污的信息更新权限
    public static String RIVER_UPDATE_PERMISSION = "river_update_Permission";  //是打开雨污 还是打开河道
    public static String url_basemap = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/ShangHaiCityMap_2019Tile/MapServer";
    public static String url_city_area = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/MH_Area/MapServer";
    public static String url_town_city_area = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/MH_Town/MapServer";
    public static String url_community_area = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/MH_Comm1yuwu/MapServer";
    public static String url_featrue = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/MH_River2016/MapServer/0";
    public static String url_river = MAP_BASE_URL + "arcgis/rest/services/ShangHaiMap/MH_River2016/MapServer/0";

}
