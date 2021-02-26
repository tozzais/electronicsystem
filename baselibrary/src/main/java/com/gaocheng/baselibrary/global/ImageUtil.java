package com.gaocheng.baselibrary.global;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gaocheng.baselibrary.R;

/**
 * 目前为测试，当路径为空时才向控件imageView中加载图片
 * Created by 32672 on 2019/1/7.
 */

public class ImageUtil {
    /**
     *
     * @param mContext 上下位
     * @param imageView 加载图片的控件
     * @param path 绝对路径
     */
    public static  void load(Context mContext,ImageView imageView, String path){
        Glide.with(mContext).load(path).error(R.mipmap.add_image).into(imageView);
    }

    public static  void loadNet(Context mContext,ImageView imageView, String path){
        Glide.with(mContext).load(path).error(R.mipmap.ic_default_image).into(imageView);

//        Glide.with(mContext).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                imageView.setImageBitmap(resource);
//            }
//        });
    }
}
