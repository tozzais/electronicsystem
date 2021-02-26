package com.gaocheng.baselibrary.bean.net;

import java.util.List;

/**
 * Created by 32672 on 2019/1/9.
 */

public class BasicInformationBean {

    /**
     * Index : 1
     * ID : 1
     * Town : 1011
     * Name : 浦江综合（陈行）
     * Mixing : 否
     * Address : 陈行、塘口、中河等
     * HouseType : null
     * BuiltDate : 1952-05-01
     * BuiltSize : 9596.9500000000007
     * DrainGo : null
     * DrainReturn : null
     * PosX : null
     * PosY : null
     * Note : null
     */

    private int Index;
    private String ID;
    private String Town;
    private String Name;
    private String Mixing;
    private String Address;
    private String HouseType;
    private String BuiltDate;
    private String BuiltSize;
    private String DrainGo;
    private String DrainReturn;
    private String PosX;
    private String PosY;
    private String Note;
    public boolean isOpen;

    public List<ImageBean> imgList;

    public int getIndex() {
        return Index;
    }

    public void setIndex(int Index) {
        this.Index = Index;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String Town) {
        this.Town = Town;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMixing() {
        return Mixing;
    }

    public void setMixing(String Mixing) {
        this.Mixing = Mixing;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getHouseType() {
        return HouseType;
    }

    public void setHouseType(String HouseType) {
        this.HouseType = HouseType;
    }

    public String getBuiltDate() {
        return BuiltDate;
    }

    public void setBuiltDate(String BuiltDate) {
        this.BuiltDate = BuiltDate;
    }

    public String getBuiltSize() {
        return BuiltSize;
    }

    public void setBuiltSize(String BuiltSize) {
        this.BuiltSize = BuiltSize;
    }

    public String getDrainGo() {
        return DrainGo;
    }

    public void setDrainGo(String DrainGo) {
        this.DrainGo = DrainGo;
    }

    public String getDrainReturn() {
        return DrainReturn;
    }

    public void setDrainReturn(String DrainReturn) {
        this.DrainReturn = DrainReturn;
    }

    public String getPosX() {
        return PosX;
    }

    public void setPosX(String PosX) {
        this.PosX = PosX;
    }

    public String getPosY() {
        return PosY;
    }

    public void setPosY(String PosY) {
        this.PosY = PosY;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }
}
