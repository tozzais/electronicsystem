package com.gaocheng.riversystem.adapter.recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.RiverRegulationInfoBean;
import com.gaocheng.baselibrary.global.GlobalParam;
import com.gaocheng.baselibrary.iterface.OnRegulationClickListener;
import com.gaocheng.baselibrary.widget.MyGridView;
import com.gaocheng.baselibrary.widget.SwipeItemLayout;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.adapter.gridview.ImageAdapter;
import com.gaocheng.riversystem.ui.activity.RegulationOnSiteAssessmentActivity;
import com.gaocheng.riversystem.ui.activity.RegulationUpdateActivity;

/**
 * 配置 河道整治 适配器
 * Created by i on 2019/1/17.
 */

public class RegulationAdapter  extends BaseQuickAdapter<RiverRegulationInfoBean, BaseViewHolder> {

    private OnRegulationClickListener onRegulationClickListener;

    public RegulationAdapter(OnRegulationClickListener onRegulationClickListener) {
        super(R.layout.item_regulation_info, null);
        this.onRegulationClickListener = onRegulationClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RiverRegulationInfoBean item) {
        SwipeItemLayout scroll_item = helper.getView(R.id.scroll_item);

        LinearLayout llMore = helper.getView(R.id.ll_more);
        ImageView view = helper.getView(R.id.iv_look_more);
        TextView update = helper.getView(R.id.delete);
        LinearLayout ll_root = helper.getView(R.id.main);
        MyGridView gridview = helper.getView(R.id.gridview);

        helper.setText(R.id.tv_town_name, item.getTownName());
        helper.setText(R.id.tv_channel, item.ManagerGrade+":"+item.getRiverName()+"("+item.getRiverID()+")");
        helper.setText(R.id.tv_finish_time1, "整治完成时间："+item.getRTime());
        helper.setText(R.id.tv_content, "整治内容:"+item.getDetail()+"(长度："+item.getLength()+"m)");
        helper.setText(R.id.tv_condition, "整治情况:"+item.getDes());
        helper.setText(R.id.tv_times, "整治次数:"+item.getCount());

        helper.setText(R.id.tv_project, "整治项目:"+item.getProject());
        helper.setText(R.id.tv_start_point, "起始位置:"+item.getSPot());
        helper.setText(R.id.tv_plan, "整治计划:"+item.getRateType());
        helper.setText(R.id.tv_evaluation, "现场评价:"+item.getContent());
        helper.setText(R.id.tv_start_time, "开工时间:"+item.getStartTime());
        helper.setText(R.id.tv_finish_time, "完成时间:"+item.getFinTime());
        helper.setText(R.id.tv_note, "整治信息备注:"+item.getNote());
        helper.setText(R.id.tv_case, "现场情况描述:"+item.getDescribe());
        helper.setText(R.id.tv_advice, "建议:"+item.getAdviseNote());

        gridview.setAdapter(new ImageAdapter(item.picList,mContext));

        llMore.setVisibility(View.GONE);
        if (GlobalParam.getRiverUpdatePermission()){
            update.setText("信息更新");
        }else {
            update.setText("现场评估");
        }

        //下拉按钮监听
        view.setOnClickListener(v -> llMore.setVisibility(llMore.getVisibility()==View.GONE?View.VISIBLE:View.GONE));

        //设置item监听
        ll_root.setOnClickListener(v ->{
            onRegulationClickListener.onItemClick(item.getRiverID());
        } );
        //跳转 信息更新 和 现场评估 页面监听
        update.setOnClickListener(v -> {
            if (GlobalParam.getRiverUpdatePermission()){
                RegulationUpdateActivity.launch(mContext,item.getRiverID());
            }else{
                RegulationOnSiteAssessmentActivity.launch(mContext,item.getRiverID());
            }
        });
    }
}
