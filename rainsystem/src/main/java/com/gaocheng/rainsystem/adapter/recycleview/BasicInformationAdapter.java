package com.gaocheng.rainsystem.adapter.recycleview;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.BasicInformationBean;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.iterface.OnBasicInformationClickListener;
import com.gaocheng.baselibrary.ui.BigImageActivity;
import com.gaocheng.baselibrary.util.StringUtil;
import com.gaocheng.rainsystem.R;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class BasicInformationAdapter extends BaseQuickAdapter<BasicInformationBean, BaseViewHolder> {


    private OnBasicInformationClickListener onBasicInformationClickListener;

    public BasicInformationAdapter(OnBasicInformationClickListener onBasicInformationClickListener) {
        super(R.layout.item_basic_information, null);
        this.onBasicInformationClickListener = onBasicInformationClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BasicInformationBean item) {

        LinearLayout llMore = helper.getView(R.id.ll_more);

        ImageView view = helper.getView(R.id.iv_look_more);
        ImageView image = helper.getView(R.id.image);
        LinearLayout ll_root = helper.getView(R.id.ll_root);


        helper.setText(R.id.tv_town_name,item.getTown());
        helper.setText(R.id.tv_community_name, "小区:"+item.getName());
        helper.setText(R.id.tv_community_address, "地址:"+item.getAddress());
        helper.setText(R.id.tv_overview, "概况:"+item.getHouseType()+","+item.getBuiltDate()+"建,"+ item.getBuiltSize()+"㎡");
        if (item.getMixing() != null){
            helper.setText(R.id.tv_community_mixing, StringUtil.getMixing(item.getMixing()));
            helper.getView(R.id.tv_community_mixing).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.tv_community_mixing).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_sewage_estination, "污水去向:"+item.getDrainGo());
        helper.setText(R.id.tv_rainfall, "雨水去向:"+item.getDrainReturn());
        helper.setText(R.id.tv_coordinate, "坐标:"+StringUtil.getCoordinate(item.getPosX(),item.getPosY()));
        helper.setText(R.id.tv_remarks, "备注:"+item.getNote());

        if (item.imgList != null && item.imgList.size()>0){
            image.setVisibility(View.VISIBLE);
            ImageUtil.loadNet(mContext,image,item.imgList.get(0).EAP_Link);
        }else{
            image.setVisibility(View.INVISIBLE);

        }
        image.setOnClickListener(v -> {
            BigImageActivity.launch(mContext,item.imgList.get(0).EAP_Link);
        });

        llMore.setVisibility(View.GONE);
        view.setOnClickListener(v -> {
            llMore.setVisibility(llMore.getVisibility() == View.GONE ?View.VISIBLE:View.GONE);
//            onclick(helper,item,llMore);
        });



        ll_root.setOnClickListener(v -> onBasicInformationClickListener.onItemClick(item.getID()));
    }

    private void onclick(BaseViewHolder helper, BasicInformationBean item, ViewGroup ll_more ){
        ValueAnimator animator = null;
//        View childAt = ll_more.getChildAt(1);
        int h = (int) mContext.getResources().getDimension(R.dimen.rian_basic_message);
//        int h = childAt.getMeasuredHeight()+ll_more.getPaddingTop()+ll_more.getPaddingBottom();
        Log.e("TAGTAGTAG",h+"");
        if (item.isOpen == true) {
            animator = ValueAnimator.ofInt(h, 0);// 从某个值变化到某个值
        } else {
            animator = ValueAnimator.ofInt(0, h);// 从某个值变化到某个值
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int n = helper.getLayoutPosition();
                    RecyclerView recycleview = getRecyclerView();
                    LinearLayoutManager manager = (LinearLayoutManager) recycleview.getLayoutManager();
                    int firstItem = manager.findFirstVisibleItemPosition();
                    int lastItem = manager.findLastVisibleItemPosition();
                    if (n <= firstItem) {
                        recycleview.smoothScrollToPosition(n);
                    } else if (n <= lastItem) {
                        int top = recycleview.getChildAt(n - firstItem).getTop();
                        recycleview.smoothScrollBy(0, top);
                    } else {
                        recycleview.smoothScrollBy(0, h);
                    }
                }
            },200);

        }
        item.isOpen = !item.isOpen;
        animator.addUpdateListener(animation -> {
            Integer height = (Integer) animation.getAnimatedValue();
            ViewGroup.LayoutParams mParams = ll_more.getLayoutParams();
            mParams.height = height;
            ll_more.setLayoutParams(mParams);
        });
        animator.setDuration(200);
        animator.start();
    }


}
