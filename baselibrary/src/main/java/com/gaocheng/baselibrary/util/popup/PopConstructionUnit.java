package com.gaocheng.baselibrary.util.popup;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.adapter.listview.PopConstructionAdapter;
import com.gaocheng.baselibrary.bean.net.UnitsBean;
import com.gaocheng.baselibrary.widget.SupportPopupWindow;

import java.util.List;


/**
 * Created by jumpbox on 16/9/28.
 */
public class PopConstructionUnit {

    private static SupportPopupWindow popupWindow;


    public static void show(final Context context, Window window, View parent, List<UnitsBean> list, final onSelectListener listener) {
        View messageView = View.inflate(context, R.layout.popup_construction_unit, null);
        ListView listview = (ListView) messageView.findViewById(R.id.listview);
        RelativeLayout rootview = (RelativeLayout)messageView.findViewById(R.id.rootview);
        PopConstructionAdapter adapter = new PopConstructionAdapter(list,context);
        listview.setAdapter(adapter);
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
        popupWindow = PopupUtils.showViewAtCenter(context,messageView,window,parent);
    }




    public interface onSelectListener{
        void commit(int position);
    }








}
