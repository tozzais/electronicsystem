package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * 雨污 现场评估数据加载
 */

public class RainOnsiteAssessmentLoadData {


    public String CommunityID;
    public String ID;
    public String SiteEvaluation; //现场评价
    public String Describe;   //现场情况描述
    public String Proposal;  //建议

    public List<ImageListBean> imageList;




    public static class ImageListBean {


        public String ID;
        public Object EI_ID;
        public String EAP_Link;


    }
}
