package com.gaocheng.baselibrary.iterface;


import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;

import java.util.List;

/**
 * Created by 32672 on 2019/1/10.
 * 基本信息 头部 监听
 */

public  interface BindDataListener {
    void onBindData(List<BasicDataBindBean> mList);

}
