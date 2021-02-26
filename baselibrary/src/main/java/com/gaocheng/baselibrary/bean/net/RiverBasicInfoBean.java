package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * 河道基本信息实体
 * Created by i on 2019/1/21.
 */

public class RiverBasicInfoBean {
    /**
     "RiverID": "MHw450",
     "RiverName": "叶家后宅河",
     "TownName": "浦江镇",
     "InRivCtl": "是",
     "length": "472.925",
     "PassTown": "",
     "Inflow": null,
     "InPump": null,
     "Size": null,
     "Note": ""
     */
    private String RiverID;
    private String RiverName;
    private String TownName;
    private String InRivCtl;
    private String length;
    private String PassTown;
    private String Inflow;
    private String InPump;
    private String Size;
    private String Note;
    public String ManagerGrade;
    public String MultTown;
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

    public String getInRivCtl() {
        return InRivCtl;
    }

    public void setInRivCtl(String inRivCtl) {
        InRivCtl = inRivCtl;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPassTown() {
        return PassTown;
    }

    public void setPassTown(String passTown) {
        PassTown = passTown;
    }

    public String getInflow() {
        return Inflow;
    }

    public void setInflow(String inflow) {
        Inflow = inflow;
    }

    public String getInPump() {
        return InPump;
    }

    public void setInPump(String inPump) {
        InPump = inPump;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }


}
