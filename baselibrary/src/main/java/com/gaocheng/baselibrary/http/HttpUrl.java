package com.gaocheng.baselibrary.http;

/**
 * Created by jumpbox on 16/5/2.
 */
public interface HttpUrl {

    //    String BASE = "http://192.168.1.212";
    String BASE = "http://gcgc.8800.org";

    String SERVER_URL = BASE+":8003/GCAPI/";
    String IMAGE_URL = "http://gcgc.8800.org:8003/";
//    String SERVER_URL = "http://192.168.252.12:8003/GCAPI/";////演示地址

    String binddata = "HouseData/GetBindingDataList";//绑定
    String town = "HouseData/GetTownData";//城镇列表
    String basic_inform_list = "HouseData/HouseDataList";//小区基本信息列表
    String rain_trans_list = "HouseData/TransformDataList";//雨污改造列表
    String gis_search = "HouseData/GISQuery";//gis数据查询
    String gis_house_data = "HouseData/GetGISHouseData";//小区信息详情
    String rain_trans_detail = "HouseData/GetGISData";//雨污改造详情
    String unit_data = "HouseData/GetUnitData";//建设单位
    String count_transform = "HouseData/TransformCount";//改造信息统计
    String count_build = "HouseData/GetBuildCount";//建设主体统计
    String count_house = "HouseData/GetHouseCount";//美丽家园统计
    String count_history = "HouseData/YearCountDataList";//历年改造统计
    String basic_inform_list_for_search = "HouseData/SelectHouseDataList";//小区基本信息列表（模糊查询）
    String rain_trans_list_for_search = "HouseData/SelectTransformDataList";//雨污改造信息列表（模糊查询）
    String message_search = "HouseData/GetMessage";//消息查询
    String onsite_access = "HouseData/AddAccesssData";//现场评估
    String inform_update = "HouseData/UpdateReformData";//信息更新
    String inform_update_loaddata = "HouseData/ResidentialQuartersLoadUpdate";//信息更新 数据加载
    String residentialquartersloaddata = "HouseData/ResidentialQuartersLoadData";//雨污现场评价数据加载接口

    String river_basic_data_list="RiverData/RiverBaseDataList";//河道基本信息列表
    String river_basic_data_list_MH="RiverData/MHRiverBaseDataList";//河道基本信息列表（模糊查询）
    String river_regulation_data_list="RiverData/RiverGovernDataList";//河道整治信息列表
    String river_regulation_data_list_MH="RiverData/MhRiverGovernDataList";//河道整治信息列表（模糊查询）
    String river_gis_data ="RiverData/GisBaseDataList";//gis数据查询
    String river_gis_basic_data = "RiverData/GISRiverBaseDataList";//Gis基本信息
    String river_gis_regulation_data = "RiverData/GISRiverGovernDataList";//Gis整治信息
    String river_gis_unit_data = "RiverData/GetGISComoyData";//Gis建设单位
    String river_manage_level = "RiverData/GetLevelCount";//统计-管理等级
    String river_remediation_situation = "RiverData/GetReformCount";//统计-整治情况
    String river_history_remediation = "RiverData/RiverYearCountDataList";//统计-历年整治
    String river_remediation_plan = "RiverData/RiverRegulationPlanDataList";//统计-整治计划
    String river_message = "RiverData/RiverRegulationMessageDataList";//消息接口
    String river_infrom_update = "RiverData/RiverRenovationDataUpdate";//信息更新
    String river_infrom_update_loaddata = "RiverData/RiverRenovationUpdateLoadDataList";//信息更新 加载数据
    String river_onsite_evaluation = "RiverData/RiverEvaluateDataUpdate";//现场评估
    String river_onsite_evaluation_loaddata = "RiverData/RiverEvaluateLoadDataList";//现场评估 数据加载

    String version = "APPVersionUpgrade/VersionUpgradData";//现场评估 数据加载

}
