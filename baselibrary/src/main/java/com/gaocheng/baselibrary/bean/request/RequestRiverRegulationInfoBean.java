package com.gaocheng.baselibrary.bean.request;

/**
 * 河道政治信息请求实体
 * Created by i on 2019/1/22.
 */

public class RequestRiverRegulationInfoBean {
    /**
     "PageIndex": 1,
     "PageSize": 2,
     "TownID": "",
     "Des": "",
     "Level": ""
     */
    public String PageSize;
    public String PageIndex;
    public String TownID;//街镇编号
    public String Des;//整治情况
    public String Level;//管理等级
}
