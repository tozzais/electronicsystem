package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * 河道整治信息实体
 * Created by i on 2019/1/22.
 */
public class RiverRegulationInfoBean {
    /**
     "RiverID": "MHw450",
     "RiverName": "叶家后宅河",
     "TownName": "浦江镇",
     "length": "472.925",
     "Detail": "综合整治",
     "Des": "正在整治",
     "Count": "",
     "RTime": "",
     "Project": "",
     "SPot": null,
     "EPot": null,
     "RateType": null,
     "Content": null,
     "StartTime": "",
     "FinTime": "",
     "Describe": null,
     "Note": null,
     "AdviseNote": null
     */
    private String RiverID;//河道编号
    private String RiverName;//河道名称
    private String TownName;//街镇名称
    private String length;//长度
    private String Detail;//整治内容
    private String Des;//整治情况
    private String Count;//整治次数
    private String RTime;//完成时间
    private String Project;//整治项目
    private String SPot;//起始位置
    private String EPot;//终止位置
    private String RateType;//整治计划
    private String Content;//现场评价
    private String StartTime;//开工时间
    private String FinTime;//计划完成时间
    private String Describe;//现场情况描述
    private String Note;//备注
    private String AdviseNote;//建议

    public String ManagerGrade;//建议
    public String Rate; //改造进度 9003迟滞  正在改造才有迟滞 (优先)
    public String Res; //改造效果 8002未达标  改造完成才有达不达标

    public List<ImageBean> picList;

    public List<ImageBean> getImgList() {
        return picList;
    }

    public void setImgList(List<ImageBean> imgList) {
        this.picList = imgList;
    }

    public String getRiverID() {
        return RiverID;
    }

    public void setRiverID(String riverID) {
        RiverID = riverID;
    }

    public String getRiverName() {
        return RiverName;
    }

    public void setRiverName(String riverName) {
        RiverName = riverName;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getRTime() {
        return RTime;
    }

    public void setRTime(String RTime) {
        this.RTime = RTime;
    }

    public String getProject() {
        return Project;
    }

    public void setProject(String project) {
        Project = project;
    }

    public String getSPot() {
        return SPot;
    }

    public void setSPot(String SPot) {
        this.SPot = SPot;
    }

    public String getEPot() {
        return EPot;
    }

    public void setEPot(String EPot) {
        this.EPot = EPot;
    }

    public String getRateType() {
        return RateType;
    }

    public void setRateType(String rateType) {
        RateType = rateType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getFinTime() {
        return FinTime;
    }

    public void setFinTime(String finTime) {
        FinTime = finTime;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getAdviseNote() {
        return AdviseNote;
    }

    public void setAdviseNote(String adviseNote) {
        AdviseNote = adviseNote;
    }
}
