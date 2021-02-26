package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * Created by 32672 on 2019/1/9.
 */

public class RainTransBean {

    
    public String ID;  //小区编号
    public String EAID; //现场评价编号
    public String ErID; //改造ID  信息更新
    public String Town;
    public String Name;
    public String Mixing;
    public String NDes;
    public String BuilderEntity;
    public String HouDes;
    public Object DrainGo;
    public Object DrainReturn;
    public String StartTime;
    public String FinTime;
    public String GZContent;
    public String Content;
    public Object Note;
    public String Case;
    public Object PosX;
    public Object PosY;
    public String AdviseNote;
    public String Rate; //改造进度 9003迟滞  正在改造才有迟滞 (优先)
    public String Res; //改造效果 8002未达标  改造完成才有达不达标
    public List<UnitsBean> units;
    public List<ImageBean> imgList;







}
