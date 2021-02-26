package com.gaocheng.baselibrary.adapter.recycleview;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.iterface.OnSeleteImageListener;
import com.gaocheng.baselibrary.ui.BigImageActivity;
import com.gaocheng.baselibrary.util.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32672 on 2019/1/7.
 * 信息更新 和 现场评估 上传图片的通用适配器
 *
 */


public class RecycleViewdapterForUpload extends RecyclerView.Adapter<RecycleViewdapterForUpload.GridViewHolder>{
    private Context mContext;
    private List<String> mImages; //全部的图片
    private List<String> mImageForLoad;
    private OnSeleteImageListener seleteImageListener;

    public List<String> getmImageForLoad() {
        return mImageForLoad;
    }

    public RecycleViewdapterForUpload(Context context, List<String> mImages
            , OnSeleteImageListener seleteImageListener) {
        this.mContext = context;
        this.mImages = mImages;
        this.seleteImageListener = seleteImageListener;
        this.mImageForLoad = new ArrayList<>();

    }


    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_add_image_base, null);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {


        if (mImages.size() == holder.getAdapterPosition()){
            //最后一个
            holder.mIvIcon.setImageResource(R.mipmap.add_image);
            holder.delete.setVisibility(View.GONE);
        }else{
            holder.delete.setVisibility(View.VISIBLE);
            String dateBean = mImages.get(position);
            holder.setData(dateBean);
        }

        holder.mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == mImages.size()){
                    seleteImageListener.selete(holder.mIvIcon,position);
                }else {
                    String[] array = new String[mImages.size()];
                    for (int i = 0;i<mImages.size();i++){
                        array[i] = mImages.get(i);
                    }
                    BigImageActivity.launch(mContext,array,position);
                }
            }
        });
        holder.mIvIcon.setOnLongClickListener(v -> {
            if (mImages.size()!= holder.getAdapterPosition() ){
                seleteImageListener.delete(mImages.get(position));
            }
            return false;
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImages.size()!= holder.getAdapterPosition() ){
                    seleteImageListener.delete(mImages.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size()+1>5?5:mImages.size()+1;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mIvIcon;
        private final ImageView delete;

        public GridViewHolder(View itemView) {
            super(itemView);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_item);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

        public void setData(String data) {
            //给imageView设置图片
            if (data.contains("http://")){
                Glide.with(mContext).load(data).asBitmap().error(com.gaocheng.baselibrary.R.mipmap.error_img).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mIvIcon.setImageBitmap(resource);
                        mImageForLoad.add(BitmapUtils.getInstance().saveBitmap(mContext,resource));
                    }
                });
            }else{
//                int degree = BitmapUtils.getInstance().readPictureDegree(data);
//                Bitmap bitmap = BitmapUtils.getInstance().rotaingImageView(degree, BitmapFactory.decodeFile(data));
//                mImageForLoad.add(BitmapUtils.getInstance().saveBitmap(mContext,bitmap));
                ImageUtil.loadNet(mContext,mIvIcon,data);
            }
        }
    }
}
