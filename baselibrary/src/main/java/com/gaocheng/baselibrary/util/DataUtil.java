package com.gaocheng.baselibrary.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32672 on 2018/12/26.
 */

public class DataUtil {
    public static List<String> getData(int length) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add("");
        }
        return list;
    }

    public static List<String> getBeautifulHomeData() {
        List<String> list = new ArrayList<>();
        list.add("完成实施");
        list.add("计划实施");
        list.add("正在实施");
        list.add("无计划");
        return list;
    }

    public static List<String> getTranSituationData() {
        List<String> list = new ArrayList<>();
        list.add("无需改造");
        list.add("完成改造");
        list.add("计划改造");
        list.add("正在改造");
        list.add("未计划改造");
        return list;
    }

    public static List<String> getTranResultData() {
        List<String> list = new ArrayList<>();
        list.add("合格");
        list.add("不达标");
        return list;
    }
}
