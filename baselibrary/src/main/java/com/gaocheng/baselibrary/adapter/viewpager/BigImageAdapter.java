package com.gaocheng.baselibrary.adapter.viewpager;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.widget.scaleimage.PhotoView;
import com.gaocheng.baselibrary.widget.scaleimage.PhotoViewAttacher;


/**
 * Created by Administrator on 2017/2/6.
 */

public class BigImageAdapter extends PagerAdapter {

    private String[] mList;
    private Context mContext;

    public BigImageAdapter(String[] mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_big_image,null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        ImageUtil.loadNet(mContext,photoView,mList[position]);
//        photoView.setImageResource(mList.get(position));
        photoView.setClickListener(new PhotoViewAttacher.onClickListener() {
            @Override
            public void onClick() {
                ((Activity)mContext).finish();
            }
        });
//        String path = mList.get(position);
//        if(path.contains("avatar.png")){
//            Glide.with(mContext).load(path).into(photoView);
//        }else{
//            GlobalParam.loadVeriImage(path,photoView);
//        }
        container.addView(view);
        return view;
    }

}



