package com.gaocheng.baselibrary.util;

import java.text.DecimalFormat;

/**
 * Created by 32672 on 2019/1/9.
 */

public class StringUtil {
    /**
     * 格式化
     * @param s
     * @return
     */
    public static String getString2(String s){

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Double.parseDouble(s))+"";
    }


    public static String getString2(int number,int count){

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(1.0*number/count*100)+"";
    }

    public static String getPercent(int number,int count){

        return Math.round(1.0*number/count*100)+"%";
    }


    /**
     * 得到统一的坐标样式
     * @param x
     * @param y
     * @return
     */
    public static String getCoordinate(String x,String y){

        return "X:"+x+"  Y:"+y;
    }


    /**
     * 得到混接情况
     * @param mix
     * @return
     */
    public static String getMixing(String mix){
        if ("2001".equals(mix)){
            return "(有混接)";
        }else if ("2002".equals(mix)){
            return "(无混接)";
        }else if ("2003".equals(mix)){
            return "(未调查)";
        }else {
            return  mix;
        }

    }
}
