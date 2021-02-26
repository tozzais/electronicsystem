package com.gaocheng.rainsystem.adapter.recycleview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gaocheng.baselibrary.bean.net.MessageItemForRain;
import com.gaocheng.rainsystem.R;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class MessageInquireAdapter extends BaseQuickAdapter<MessageItemForRain, BaseViewHolder> {

    public MessageInquireAdapter() {
        super( R.layout.item_message_inquire, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageItemForRain item) {
        helper.setText(R.id.tv_title,item.TownName);
        helper.setText(R.id.tv_community_name,"小区:"+item.HouseName);
        helper.setText(R.id.tv_content,"内容:"+item.Mes);
        helper.setText(R.id.tv_time,"时间:"+item.Time);

    }
}
