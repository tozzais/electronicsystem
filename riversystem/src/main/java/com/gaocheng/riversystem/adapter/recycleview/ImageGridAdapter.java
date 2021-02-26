package com.gaocheng.riversystem.adapter.recycleview;

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
import com.gaocheng.riversystem.R;

import java.util.List;

/**
 * 适配recycleViewer的图片适配器
 * Created by 32672 on 2019/1/7.
 */


public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder>{
    private Context mContext;
    private List<String> mDateBeen;
    private List<String> mImagesForUpload;
    private SeleteImageListener seleteImageListener;

    public ImageGridAdapter(Context context, List<String> dates,SeleteImageListener seleteImageListener) {
        mContext = context;
        mDateBeen = dates;
        this.seleteImageListener = seleteImageListener;
    }
    public ImageGridAdapter(Context context, List<String> dates,List<String> mImagesForUpload,SeleteImageListener seleteImageListener) {
        mContext = context;
        mDateBeen = dates;
        this.mImagesForUpload = mImagesForUpload;
        this.seleteImageListener = seleteImageListener;
    }


    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.river_item_image, null);
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
                //点击加号后，调用 seleteImageListener 接口的方法selete，此方法的实现在
                //RegulationOnSiteAssessmentActivity 中，来从相册中选择图片
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
                Glide.with(mContext).load(data).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mIvIcon.setImageBitmap(resource);
                        mImagesForUpload.add(BitmapUtils.getInstance().saveBitmap(mContext,resource));
                    }
                });
            }else{
                mImagesForUpload.add(data);
                ImageUtil.load(mContext,mIvIcon,data);
            }
        }
    }
}
