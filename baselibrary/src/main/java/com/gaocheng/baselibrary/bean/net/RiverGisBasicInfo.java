package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * 河道Gis基本信息
 * Created by i on 2019/1/23.
 */

public class RiverGisBasicInfo {
    /**
     "RiverID": "MHw450",
     "RiverName": "叶家后宅河",
     "TownName": "浦江镇",
     "InRivCtl": "是",
     "length": "472.92",
     "PassTown": "1012",
     "MultTown": null,
     "Inflow": null,
     "InPump": null,
     "Size": null,
     "Note": "",
     "picList": null
     */
    public String RiverID;
    public String RiverName;
    public String TownName;
    public String InRivCtl;
    public String length;
    public String PassTown;
    public String MultTown;
    public String InPump;
    public String Inflow;
    public String Size;
    public String Note;
    public String ManagerGrade;

    public List<ImageBean> picList;
}
