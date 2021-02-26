package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * Created by 32672 on 2019/2/25.
 */

public class RainInfromUpdataLoadData {


    public String ID;
    public String Reform;
    public String Note;
    public List<BeautifulHomeBean> BeautifulHome;
    public List<ReformSituationBean> ReformSituation;
    public List<ReformEffectBean> ReformEffect;
    public List<ImageListBean> imageList;



    public static class BeautifulHomeBean {
      

        public String ID;
        public String Name;
        public String IsCheck;


    }

    public static class ReformSituationBean {


        public String ID;
        public String Name;
        public String IsCheck;


    }

    public static class ReformEffectBean {


        public String ID;
        public String Name;
        public String IsCheck;


    }

    public static class ImageListBean {


        public String ID;
        public Object EI_ID;
        public String EAP_Link;


    }
}
