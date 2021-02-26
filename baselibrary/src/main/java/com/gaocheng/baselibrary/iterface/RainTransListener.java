package com.gaocheng.baselibrary.iterface;


/**
 * Created by 32672 on 2019/1/10.
 * 基本信息 头部 监听
 */

public  interface RainTransListener {
    void onStreetClickListener(String townBean);
    void onMixingSelectListener(String mixing);
    void onTransSituationSelectListener(String transsituation);
    void onSearchListener();
}
