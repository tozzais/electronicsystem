package com.gaocheng.baselibrary.global;

import android.content.Context;

import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.BasicDataBind;
import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.BindDataListener;
import com.gaocheng.baselibrary.util.SharedPreferencesUtil;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.gaocheng.baselibrary.BaseApplication.mContext;

/**
 * Created by jumpbox on 16/4/19.
 */
public class GlobalParam {

    public static final int
            RAIN_TOWN = 1,//雨污的所属街镇
            HOUSE_TYPE = 2,//房屋类型
            MIXING = 3,//混接情况
            TRANSFORMATION_SITUATION = 4,//改造情况
            RIVER_TOWN = 5,//河道的所属街镇
            RIVER_TYPE = 6,//水体分类
            MANAGE_LEVEL = 7,//管理等级
            REGULATION_CASE = 8,//整治情况
            GIS_RIVER_TOWN = 9,//河道的所属街镇
            GIS_RIVER_TYPE = 10,//水体分类
            GIS_MANAGE_LEVEL = 11,//管理等级
            GIS_REGULATION_CASE = 12;//整治情况

    private static void savaBindData(BasicDataBind s) {
        Gson gson = new Gson();
        SharedPreferencesUtil.saveStringData(mContext, Constant.BINGDATA, gson.toJson(s));
    }

    /**
     *
     * @param mActivity
     * @param listener
     * @param type
     */
    public static void getBindData(Context mActivity,int type, BindDataListener listener) {
        String s = SharedPreferencesUtil.getStringData(mContext, Constant.BINGDATA, "");
        if ("".equals(s)){
            new RxHttp<BaseListResult<BasicDataBind>>().send(ApiManager.getService().getBindDate(),
                    new Response<BaseListResult<BasicDataBind>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<BasicDataBind> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                BasicDataBind data = baseListResult.data.get(0);
                                savaBindData(data);
                                bind(data,listener,type);
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });
        }else {
            Gson gson = new Gson();
            BasicDataBind basicDataBind = gson.fromJson(s, BasicDataBind.class);
            bind(basicDataBind,listener,type);
        }
    }

    public static void bind(BasicDataBind basicDataBind, BindDataListener listener,int type) {
        /***************************雨污********************************************/
        if (type == RAIN_TOWN){
            //小区 所属城镇
            List<BasicDataBindBean> data = new ArrayList<>();
            BasicDataBindBean bean = new BasicDataBindBean("","闵行区");
            data.add(bean);
            data.addAll(basicDataBind.Town);
            listener.onBindData(data);
        }else if (type == 1){
            //年份
        }else if (type == HOUSE_TYPE){
            //房屋类型
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.ROOMTYPE);
            listener.onBindData(data);
        }else if (type == MIXING){
            //混接情况
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.HJ);
            listener.onBindData(data);
        }else if (type == TRANSFORMATION_SITUATION){
            //改造情况
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.EIDES);
            listener.onBindData(data);
        /***************************河道********************************************/
        }else if (type == RIVER_TOWN){
            //河道 所属街镇
            List<BasicDataBindBean> data = new ArrayList<>();
            BasicDataBindBean bean = new BasicDataBindBean("","闵行区");
            data.add(bean);
            data.addAll(basicDataBind.Town);
            listener.onBindData(data);
        }else if (type == RIVER_TYPE){
            //水体分类
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.RivType);
            listener.onBindData(data);
        }else if (type == MANAGE_LEVEL){
            //管理等级
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.Rlevel);
            listener.onBindData(data);
        }else if (type == REGULATION_CASE){
            //整治情况
            List<BasicDataBindBean> data = new ArrayList<>();
            data.add(new BasicDataBindBean("","全部"));
            data.addAll(basicDataBind.RRF);
            listener.onBindData(data);
        }else if (type == GIS_RIVER_TOWN){
            //河道 GIS 所属街镇
            List<BasicDataBindBean> data = new ArrayList<>();
            data.addAll(basicDataBind.Town);
            listener.onBindData(data);
        }else if (type == GIS_RIVER_TYPE){
            // GIS 水体分类
            List<BasicDataBindBean> data = new ArrayList<>();
            data.addAll(basicDataBind.RivType);
            listener.onBindData(data);
        }else if (type == GIS_MANAGE_LEVEL){
            // GIS 管理等级
            List<BasicDataBindBean> data = new ArrayList<>();
            data.addAll(basicDataBind.Rlevel);
            listener.onBindData(data);
        }else if (type == GIS_REGULATION_CASE){
            // GIS 整治情况
            List<BasicDataBindBean> data = new ArrayList<>();
            data.addAll(basicDataBind.RRF);
            listener.onBindData(data);
        }
    }

    public static void savaSystemType(boolean isRain) {
        SharedPreferencesUtil.saveBooleanData(mContext, Constant.SYSTEMTYPE, isRain);
    }
    public static boolean getSystemType() {
        return SharedPreferencesUtil.getBooleanData(mContext, Constant.SYSTEMTYPE,true);
    }
    public static void savaRainUpdatePermission(boolean isRain) {
        SharedPreferencesUtil.saveBooleanData(mContext, Constant.RAIN_UPDATE_PERMISSION, isRain);
    }
    public static boolean getRainUpdatePermission() {
        return SharedPreferencesUtil.getBooleanData(mContext, Constant.RAIN_UPDATE_PERMISSION,true);
    }
    public static void savaRiverUpdatePermission(boolean isRain) {
        SharedPreferencesUtil.saveBooleanData(mContext, Constant.RIVER_UPDATE_PERMISSION, isRain);
    }
    public static boolean getRiverUpdatePermission() {
        return SharedPreferencesUtil.getBooleanData(mContext, Constant.RIVER_UPDATE_PERMISSION,true);
    }
}
