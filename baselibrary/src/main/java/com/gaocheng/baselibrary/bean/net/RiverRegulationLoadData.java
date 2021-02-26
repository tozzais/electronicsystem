package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * 河道整治 更新 数据加载实体
 * Created by i on 2019/3/11.
 */

public class RiverRegulationLoadData {
    public List<RenovationEffectBean> RenovationEffectList;
    public List<RenovationSituationListBean> RenovationSituationList;
    public RiverRenovationDetailBean RiverRenovationDetail;
    public class RiverRenovationDetailBean{
        public String RiverID;             //用到
        public String RiverName;
        public String TownName;
        public String length;
        public String ReformID;
        public String Detail;
        public String Des;
        public String Count;
        public String RTime;              //用到
        public String Project;
        public String SPot;
        public String EPot;
        public String RateType;
        public String Content;
        public String StartTime;
        public String ID;
        public String FinTime;
        public String Describe;
        public String Note;                //用到
        public String AdviseNote;
        public List<ImageListBean> imgList;   //用到
    }

    public static class RenovationEffectBean{
        public String Code;              //用到
        public String Name;
        public String IsCheck;
    }
    public static class RenovationSituationListBean{
        public String Code;             //用到
        public String Name;
        public String IsCheck;
    }

    public static class ImageListBean {
        public String ID;
        public Object EI_ID;
        public String EAP_Link;
    }
}
