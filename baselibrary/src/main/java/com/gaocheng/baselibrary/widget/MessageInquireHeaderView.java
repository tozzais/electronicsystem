package com.gaocheng.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.TownBean;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.MessageListener;
import com.gaocheng.baselibrary.util.KeyboardUtil;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.gaocheng.baselibrary.util.popup.ScreenUtil;

import java.util.List;

/**
 * Created by 32672 on 2019/1/8.
 */

public class MessageInquireHeaderView extends LinearLayout  implements View.OnClickListener,TextView.OnEditorActionListener {

    private Context mContext;
    private Activity mActivity;


    private LinearLayout ll_street;
    private TextView tv_street;

    private ImageView iv_search;
    private EditText et_search;

    private MessageListener messageListener;




    public MessageInquireHeaderView(Context context) {
        super(context);
        initView(context);
    }



    public MessageInquireHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MessageInquireHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MessageInquireHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.header_message_inquire,null);
        this.mContext = context;
        this.mActivity = (Activity) context;
        ll_street = view.findViewById(R.id.ll_street);
        tv_street = view.findViewById(R.id.tv_street);
        iv_search = view.findViewById(R.id.iv_search);
        et_search = view.findViewById(R.id.et_search);
        addView(view);
        initData();
        initListener();
    }

    private void initListener() {
        iv_search.setOnClickListener(this);
        ll_street.setOnClickListener(this);
        et_search.setOnEditorActionListener(this);



    }

    private void initData() {

    }


    private HomeRecyclerView scollRecyclerView;

    public HomeRecyclerView getScollRecyclerView() {
        return scollRecyclerView;
    }

    public void setScollRecyclerView(HomeRecyclerView scollRecyclerView) {
        this.scollRecyclerView = scollRecyclerView;
        this.scollRecyclerView.setHeader(this);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_street){
            KeyboardUtil.hideKeyBoard(mContext, et_search);
            new RxHttp<BaseListResult<TownBean>>().send(ApiManager.getService().getTownList(),
                    new Response<BaseListResult<TownBean>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<TownBean> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                List<TownBean> data = baseListResult.data;
                                ScreenUtil.showTownList(mActivity, mActivity.getWindow(), ll_street, data, position ->{
                                    tv_street.setText(data.get(position).TownName);
                                    tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                                    if (messageListener != null){
                                        messageListener.onStreetClickListener(data.get(position));
                                    }
                                });
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });
        }else if (id == R.id.iv_search){

        }

    }




    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 当按了搜索之后关闭软键盘
            ((InputMethodManager) et_search.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    mActivity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            if (messageListener != null){
                messageListener.onSearchListener();
            }
            return true;
        }
        return false;
    }

    public EditText getEt_search() {
        return et_search;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }


    public void clear(){
        tv_street.setText("所属街镇");
        tv_street.setTextColor(getResources().getColor(R.color.grayText));
    }

}
