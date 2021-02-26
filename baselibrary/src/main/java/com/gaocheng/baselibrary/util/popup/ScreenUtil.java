package com.gaocheng.baselibrary.util.popup;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.adapter.listview.BaseBindAdapter;
import com.gaocheng.baselibrary.adapter.listview.ScreenListAdapter;
import com.gaocheng.baselibrary.adapter.listview.TownListAdapter;
import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;
import com.gaocheng.baselibrary.bean.net.TownBean;
import com.gaocheng.baselibrary.widget.SupportPopupWindow;

import java.util.List;


/**
 * Created by jumpbox on 16/9/28.
 */
public class ScreenUtil {

    private static SupportPopupWindow popupWindow;


    public static void showList(final Context context, Window window, View parent, List<String> list, final onSelectListener listener) {
        View messageView = View.inflate(context, R.layout.popup_list, null);
        ListView listview = (ListView) messageView.findViewById(R.id.listview);
        LinearLayout rootview = (LinearLayout)messageView.findViewById(R.id.rootview);
        ScreenListAdapter adapter = new ScreenListAdapter(list,context);
        listview.setAdapter(adapter);
        popupWindow = PopupUtils.getPopupAnim(context,messageView,window);
        popupWindow.showAsDropDown(parent);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.commit(i);
                popupWindow.dismiss();
//                ToastCommom.createToastConfig().ToastShow(context,""+i);
            }
        });
        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }


    public static void showTownList(final Context context, Window window, View parent, List<TownBean> list, final onSelectListener listener) {
        View messageView = View.inflate(context, R.layout.popup_list, null);
        ListView listview = (ListView) messageView.findViewById(R.id.listview);
        LinearLayout rootview = (LinearLayout)messageView.findViewById(R.id.rootview);
        TownListAdapter adapter = new TownListAdapter(list,context);
        listview.setAdapter(adapter);
        popupWindow = PopupUtils.getPopupAnim(context,messageView,window);
        popupWindow.showAsDropDown(parent);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.commit(i);
                popupWindow.dismiss();
//                ToastCommom.createToastConfig().ToastShow(context,""+i);
            }
        });
        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public static void showBindList(final Context context, Window window, View parent, List<BasicDataBindBean> list, final onSelectListener listener) {
        View messageView = View.inflate(context, R.layout.popup_list, null);
        ListView listview = messageView.findViewById(R.id.listview);
        LinearLayout rootview = messageView.findViewById(R.id.rootview);
        BaseBindAdapter adapter = new BaseBindAdapter(list,context);
        listview.setAdapter(adapter);
        popupWindow = PopupUtils.getPopupAnim(context,messageView,window);
        popupWindow.showAsDropDown(parent);
        popupWindow.setClippingEnabled(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.commit(i);
                popupWindow.dismiss();
            }
        });
        rootview.setOnClickListener(v -> popupWindow.dismiss());
    }




    public interface onSelectListener{
        void commit(int position);
    }








}
