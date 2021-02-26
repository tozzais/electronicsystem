package com.gaocheng.rainsystem.adapter.recycleview;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.iterface.SeleteImageListener;
import com.gaocheng.baselibrary.util.BitmapUtils;
import com.gaocheng.rainsystem.R;

import java.util.List;

/**
 * Created by 32672 on 2019/1/7.
 */


public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder>{
    private Context mContext;
    private List<String> mDateBeen;
    private List<String> mImagesForUpload;
    private SeleteImageListener seleteImageListener;

    public ImageGridAdapter(Context context, List<String> dates, List<String> mImagesForUpload,SeleteImageListener seleteImageListener) {
        mContext = context;
        mDateBeen = dates;
        this.seleteImageListener = seleteImageListener;
        this.mImagesForUpload = mImagesForUpload;
    }


    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_add_image, null);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {

        if (mDateBeen.size() == holder.getAdapterPosition()){
            //最后一个
            holder.mIvIcon.setImageResource(R.mipmap.add_image);

        }else{
            String dateBean = mDateBeen.get(position);
            holder.setData(dateBean);
        }

        holder.mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleteImageListener.selete(holder.mIvIcon,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDateBeen.size()+1>5?5:mDateBeen.size()+1;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mIvIcon;

        public GridViewHolder(View itemView) {
            super(itemView);
            mIvIcon = (ImageView) itemView.findViewById(R.id.item_list_iv_icon);
        }

        public void setData(String data) {
            //给imageView设置图片
            if (data.contains("http://")){
                Glide.with(mContext).load(data).asBitmap().error(com.gaocheng.baselibrary.R.mipmap.error_img).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mIvIcon.setImageBitmap(resource);
                        mImagesForUpload.add(BitmapUtils.getInstance().saveBitmap(mContext,resource));
                    }
                });
            }else{
                mImagesForUpload.add(data);
                ImageUtil.loadNet(mContext,mIvIcon,data);
            }
        }
    }
}
