package com.gaocheng.riversystem.bean.virtual;

/**
 * Created by 32672 on 2018/12/25.
 */

public class BasicInformation {
    public boolean isOpen;
    public String town_name;
    public String community_name;
    public String address;
    public String overview;
    public String coordinate; //31.1397700000,121.3622200000
    public String remarks;

    public BasicInformation(boolean isopen, String town_name, String community_name, String address, String overview, String coordinate, String remarks) {
        this.isOpen = isopen;
        this.town_name = town_name;
        this.community_name = community_name;
        this.address = address;
        this.overview = overview;
        this.coordinate = coordinate;
        this.remarks = remarks;
    }
}
