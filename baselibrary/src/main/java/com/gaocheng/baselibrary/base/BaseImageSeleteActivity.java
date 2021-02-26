package com.gaocheng.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gaocheng.baselibrary.adapter.recycleview.RecycleViewdapterForUpload;
import com.gaocheng.baselibrary.util.BitmapUtils;
import com.gaocheng.baselibrary.util.LoadingUtils;
import com.move.ximageSelector.config.XImgSelConfig;
import com.move.ximageSelector.imageView.XSelectAct;
import com.move.ximageSelector.utils.XImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 32672 on 2018/12/25.
 * 信息更新和现场评估的基类
 */

public  abstract  class BaseImageSeleteActivity extends BaseActivity {
    protected String id;

    protected boolean isCanLoad = false;

    protected RecycleViewdapterForUpload recyclerViewGridAdapter;;


    //所有的地址 = 服务器地址（服务器地址转化为本地地址 ）+本地地址
    protected ArrayList<String> mImagesTotal = new ArrayList<>();
    //本地地址
    protected ArrayList<String> mImagesForLocal = new ArrayList<>();
    //服务器地址
    protected ArrayList<String> mImagesForNet = new ArrayList<>();


    //上传图片需要的地址 （压缩后的地址）
    protected List<String> mImagesUpload = new ArrayList<>();

    protected int total = 5; //文件的总数


    public void deletePath(String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("确定要删除吗？")
                .setMessage("删除之后上传可能导致服务器图片丢失哦").setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isCanLoad = true;
                        mImagesTotal.remove(path);
                        mImagesForNet.remove(path);
                        mImagesForLocal.remove(path);
                        setAdapter();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.create().show();

    }

    protected  void setAdapter(){
        mImagesTotal.clear();
        mImagesTotal.addAll(mImagesForNet);
        mImagesTotal.addAll(mImagesForLocal);
    };

    protected void seleteImage() {
        XImageLoader imageLoader = new XImageLoader() {
            @Override
            public void load(Context context, String localPath, ImageView iv) {
                Glide.with(context).load(localPath).into(iv);
            }
        };
        XSelectAct.open(this, new XImgSelConfig.Builder(imageLoader)
                .btnConfirmText("完成")
                .backTitle("返回")
                .isPreview(false)
                .maxNum(total - mImagesForNet.size())
                .cropSize(1, 1, 500, 500)
                .isCamera(true)
                .selectImage(mImagesForLocal)
                .btnConfirmText("完成")
                .isCrop(true)
                .build(), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            isCanLoad = true;
            mImagesForLocal = data.getStringArrayListExtra("data");
//            for (String s:mImagesForLocal){
//                Log.e("正在处理图片", s);
//            }
            setAdapter();
        }
    }




    protected void compressPic(onCompressListener listener){

        LoadingUtils.show(mContext, "正在处理图片");
        mImagesForLocal.addAll(recyclerViewGridAdapter.getmImageForLoad());
        new Thread(()->{
            for (int i = 0; i < mImagesForLocal.size(); i++) {
                String localPath = mImagesForLocal.get(i);
                int degree = BitmapUtils.getInstance().readPictureDegree(localPath);
                Bitmap bitmap = BitmapUtils.getInstance().rotaingImageView(degree, BitmapFactory.decodeFile(localPath));
                BitmapUtils.getInstance().CompactPic((Activity) mContext, BitmapUtils.getInstance().saveBitmap(mContext, bitmap), path -> {
                    mImagesUpload.add(path);
                    Log.e("正在处理图片", path);
                    if (mImagesUpload.size() == mImagesForLocal.size()) {
                        LoadingUtils.show(mContext, "上传中..");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFinish();
//                                start(trans_time, explain);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public interface onCompressListener{
        void onFinish();
    }



}
