package com.gaocheng.electronicsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gaocheng.baselibrary.global.RouthConstant;
import com.gaocheng.rainsystem.RainMainActivity;
import com.gaocheng.riversystem.RiverMainActivity;

public class MainApp extends AppCompatActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (GlobalParam.getSystemType()){
//            // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//            ARouter.getInstance().build(RouthConstant.RAIN).navigation();
//        }else {
            ARouter.getInstance().build(RouthConstant.RIVER).navigation();
//        }
        finish();
    }

    public void clickRain(View view) {
        startActivity(new Intent(this, RainMainActivity.class));


    }
    public void clickRiver(View view) {
        startActivity(new Intent(this, RiverMainActivity.class));

    }


}
