package com.gaocheng.baselibrary.util;

import android.content.Context;

import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.TownBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;

import java.util.List;

/**
 * Created by 32672 on 2019/1/17.
 */

public class TownDataUtil {
    private static volatile TownDataUtil singleton;

    private TownDataUtil() {}

    public static TownDataUtil getInstance() {
        if (singleton == null) {
            synchronized (TownDataUtil.class) {
                if (singleton == null) {
                    singleton = new TownDataUtil();
                }
            }
        }
        return singleton;
    }

    public interface DataListener{
        void onsuccess(List<TownBean> list);
    }

    public  void getData(Context context,DataListener listener){
        new RxHttp<BaseListResult<TownBean>>().send(ApiManager.getService().getTownList(),
                new Response<BaseListResult<TownBean>>(context, true) {
                    @Override
                    public void onNext(BaseListResult<TownBean> baseListResult) {
                        super.onNext(baseListResult);
                        if ("200".equals(baseListResult.ret)) {
                            List<TownBean> data = baseListResult.data;
                            listener.onsuccess(data);
                        }else {
                            ToastCommom.createToastConfig().ToastShow(context,baseListResult.msg);
                        }
                    }
                });
    }
}
