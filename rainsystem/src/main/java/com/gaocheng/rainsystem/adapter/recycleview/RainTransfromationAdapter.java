package com.gaocheng.rainsystem.adapter.recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.RainTransBean;
import com.gaocheng.baselibrary.bean.net.UnitsBean;
import com.gaocheng.baselibrary.global.GlobalParam;
import com.gaocheng.baselibrary.iterface.OnRainTransformClickListener;
import com.gaocheng.baselibrary.util.StringUtil;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.gaocheng.baselibrary.util.popup.PopConstructionUnit;
import com.gaocheng.baselibrary.widget.MyGridView;
import com.gaocheng.baselibrary.widget.SwipeItemLayout;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.RainMainActivity;
import com.gaocheng.rainsystem.adapter.recycleview.gridview.ImageAdapter;
import com.gaocheng.rainsystem.ui.activity.RainTransformationOnsiteAssessmentActivity;
import com.gaocheng.rainsystem.ui.activity.RainTransformationUpdataInputActivity;

import java.util.List;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class RainTransfromationAdapter extends BaseQuickAdapter<RainTransBean, BaseViewHolder> {


    private OnRainTransformClickListener onBasicInformationClickListener;

    public RainTransfromationAdapter(OnRainTransformClickListener onBasicInformationClickListener) {
        super(R.layout.item_rain_transformation, null);
        this.onBasicInformationClickListener = onBasicInformationClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RainTransBean item) {
        SwipeItemLayout scroll_item = helper.getView(R.id.scroll_item);

        LinearLayout llMore = helper.getView(R.id.ll_more);
        ImageView view = helper.getView(R.id.iv_look_more);
        TextView tv_look_pic = helper.getView(R.id.tv_look_pic);
        TextView update = helper.getView(R.id.delete);
        LinearLayout ll_root = helper.getView(R.id.ll_root);
        MyGridView gridview = helper.getView(R.id.gridview);

        TextView tv_town_name = helper.getView(R.id.tv_town_name);
        TextView tv_community_name = helper.getView(R.id.tv_community_name);
        TextView tv_community_mixing = helper.getView(R.id.tv_community_mixing);
        TextView tv_community_address = helper.getView(R.id.tv_community_address);
        TextView tv_overview = helper.getView(R.id.tv_overview);
        TextView tv_beautiful_home = helper.getView(R.id.tv_beautiful_home);

        tv_town_name.setText(item.Town);
        tv_community_name.setText("小区:"+item.Name);
        tv_community_mixing.setText(StringUtil.getMixing(item.Mixing));
        tv_community_address.setText("改造情况:"+item.NDes);
        tv_overview.setText( "建设主体:"+item.BuilderEntity);
        tv_beautiful_home.setText( "美丽家园:"+item.HouDes);

        helper.setText(R.id.tv_draingo, "污水去向:"+item.DrainGo);
        helper.setText(R.id.tv_drainreturn, "雨水去向:"+item.DrainReturn);
        helper.setText(R.id.tv_starttime, "开工时间:"+item.StartTime);
        helper.setText(R.id.tv_fintime, "计划完工时间:"+item.FinTime);
        helper.setText(R.id.tv_gzcontent, "改造内容:"+item.GZContent);
        helper.setText(R.id.tv_content, "评价:"+item.Content);
        helper.setText(R.id.tv_note, "改造备注:"+item.Note);
        helper.setText(R.id.tv_case, "现场情况:"+item.Case);
        helper.setText(R.id.tv_advisenote, "意见建议:"+item.AdviseNote);

        gridview.setAdapter(new ImageAdapter(item.imgList,mContext));

        llMore.setVisibility(View.GONE);
        final int position = helper.getAdapterPosition();
        if (GlobalParam.getRainUpdatePermission()){
            update.setText("信息更新");
        }else {
            update.setText("现场评估");
        }

        if("9003".equals(item.Rate)){
            //迟滞
            setType(2,tv_town_name,tv_community_name,tv_community_mixing,tv_community_address,tv_overview,tv_beautiful_home,ll_root);
        }else if("8002".equals(item.Res)){
            //不达标
            setType(1,tv_town_name,tv_community_name,tv_community_mixing,tv_community_address,tv_overview,tv_beautiful_home,ll_root);
        }else{
            setType(0,tv_town_name,tv_community_name,tv_community_mixing,tv_community_address,tv_overview,tv_beautiful_home,ll_root);
        }



        view.setOnClickListener(v -> llMore.setVisibility(llMore.getVisibility()==View.GONE?View.VISIBLE:View.GONE));
        tv_look_pic.setOnClickListener(v -> {
            List<UnitsBean> units = item.units;
            if (units == null || units.size() == 0){
                ToastCommom.createToastConfig().ToastShow(mContext,"暂无施工单位");
                return;
            }
            RainMainActivity mainActivity = (RainMainActivity) mContext;
            PopConstructionUnit.show(mContext, mainActivity.getWindow(), mainActivity.parentView
                    , units, position1 -> {});
        });
        ll_root.setOnClickListener(v ->{
                    onBasicInformationClickListener.onItemClick(item.ID);
        } );
        update.setOnClickListener(v -> {
            if (GlobalParam.getRainUpdatePermission()){
                RainTransformationUpdataInputActivity.launch(mContext,item.ID);
            }else{
                RainTransformationOnsiteAssessmentActivity.launch(mContext,item.ID);
            }
        });
    }

    private void setType(int type, TextView tv_town_name, TextView tv_community_name, TextView tv_community_mixing, TextView tv_community_address, TextView tv_overview, TextView tv_beautiful_home, LinearLayout ll_root){
        int textColor = mContext.getResources().getColor(R.color.title_textcolor);
        int bgColor = mContext.getResources().getColor(R.color.white);
        int mixColor = mContext.getResources().getColor(R.color.yellow);
        if (type == 0){ //正常
            textColor = mContext.getResources().getColor(R.color.title_textcolor);
            bgColor = mContext.getResources().getColor(R.color.white);
            mixColor = mContext.getResources().getColor(R.color.yellow);
        }else if(type == 1){//红色不达标
            textColor = mContext.getResources().getColor(R.color.white);
            bgColor = mContext.getResources().getColor(R.color.red_trans);
            mixColor = mContext.getResources().getColor(R.color.white);
        }else if(type == 2){//黄色迟滞
            textColor = mContext.getResources().getColor(R.color.title_textcolor);
            bgColor = mContext.getResources().getColor(R.color.yellow_trans);
            mixColor = mContext.getResources().getColor(R.color.title_textcolor);
        }
        tv_town_name.setTextColor(textColor);
        tv_community_name.setTextColor(textColor);
        tv_community_mixing.setTextColor(mixColor);
        tv_community_address.setTextColor(textColor);
        tv_overview.setTextColor(textColor);
        tv_beautiful_home.setTextColor(textColor);
        ll_root.setBackgroundColor(bgColor);


    }


}
