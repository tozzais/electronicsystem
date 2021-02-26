package com.gaocheng.riversystem.adapter.recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.RiverBasicInfoBean;
import com.gaocheng.baselibrary.global.ImageUtil;
import com.gaocheng.baselibrary.iterface.OnBasicInformationClickListener;
import com.gaocheng.baselibrary.ui.BigImageActivity;
import com.gaocheng.riversystem.R;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：河道基本信息适配器
 */
public class BasicInformationAdapter extends BaseQuickAdapter<RiverBasicInfoBean, BaseViewHolder> {

    private OnBasicInformationClickListener onBasicInformationClickListener;

    public BasicInformationAdapter(OnBasicInformationClickListener onBasicInformationClickListener) {
        super(R.layout.river_item_basic_information, null);
        this.onBasicInformationClickListener = onBasicInformationClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RiverBasicInfoBean item) {

        LinearLayout llMore = helper.getView(R.id.ll_more);
        ImageView view = helper.getView(R.id.iv_look_more);
        ImageView image = helper.getView(R.id.image);
        LinearLayout ll_root = helper.getView(R.id.ll_root);

        helper.setText(R.id.tv_town_name, item.getTownName());
        helper.setText(R.id.tv_river_name, item.ManagerGrade+":" + item.getRiverName()+"("+item.getRiverID()+")");
        helper.setText(R.id.tv_control, "纳入水面积控制:" + item.getInRivCtl());
        helper.setText(R.id.tv_length, "长度(m):" + item.getLength());

        helper.setText(R.id.tv_flow_town, "流经街镇：" + item.getPassTown());
        helper.setText(R.id.tv_cross, "是否跨镇：" + item.MultTown);//********
        helper.setText(R.id.tv_shrink, "汇入河道：" + item.getInflow());
        helper.setText(R.id.tv_pump_name, "泵闸名称："+item.getInPump());
        helper.setText(R.id.tv_area,"面积(㎡)："+item.getSize()+"");
        helper.setText(R.id.tv_remarks, "备注:" + item.getNote());

        if (item.picList != null && item.picList.size()>0){
            image.setVisibility(View.VISIBLE);
            ImageUtil.loadNet(mContext,image,item.picList.get(0).EAP_Link);
        }else{
            image.setVisibility(View.INVISIBLE);
        }
        image.setOnClickListener(v -> {
            BigImageActivity.launch(mContext,item.picList.get(0).EAP_Link);
        });

        llMore.setVisibility(View.GONE);
        view.setOnClickListener(v -> llMore.setVisibility(llMore.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
        ll_root.setOnClickListener(v -> onBasicInformationClickListener.onItemClick(item.getRiverID()));
    }
}
