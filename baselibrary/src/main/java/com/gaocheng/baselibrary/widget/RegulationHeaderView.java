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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;
import com.gaocheng.baselibrary.global.GlobalParam;
import com.gaocheng.baselibrary.iterface.RegulationListener;
import com.gaocheng.baselibrary.util.KeyboardUtil;
import com.gaocheng.baselibrary.util.popup.ScreenUtil;

/**
 * 河道整治 头布局加载
 * Created by i on 2019/1/17.
 */

public class RegulationHeaderView extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener {

    private Context mContext;
    private Activity mActivity;

    private LinearLayout ll_header;
    private LinearLayout ll_street;
    private TextView tv_street;

    private LinearLayout ll_mixing;
    private TextView tv_mixing;
    private LinearLayout ll_type;
    private TextView tv_type;

    private EditText et_search;

    private RegulationListener regulationListener;

    public void setRegulationListener(RegulationListener regulationListener) {
        this.regulationListener = regulationListener;
    }

    public RegulationHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public RegulationHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RegulationHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RegulationHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.header_rain_trans, null);
        this.mContext = context;
        this.mActivity = (Activity) context;
        ll_header = view.findViewById(R.id.ll_header);
        ll_street = view.findViewById(R.id.ll_street);
        tv_street = view.findViewById(R.id.tv_street);
        ll_mixing = view.findViewById(R.id.ll_mixing);
        tv_mixing = view.findViewById(R.id.tv_mixing);
        ll_type = view.findViewById(R.id.ll_type);
        tv_type = view.findViewById(R.id.tv_type);
        et_search = view.findViewById(R.id.et_search);
        addView(view);
        initData();
        initListener();
    }

    private void initListener() {
        ll_street.setOnClickListener(this);
        ll_mixing.setOnClickListener(this);
        ll_type.setOnClickListener(this);
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
        KeyboardUtil.hideKeyBoard(mContext, et_search);
        if (id == R.id.ll_street){
            select=street;
            GlobalParam.getBindData(mActivity,GlobalParam.RIVER_TOWN, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_street.setText(bean.Name);
                    tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                    if (regulationListener != null){
                        regulationListener.onStreetSelectListener(bean.ID);
                    }
                });
            });
            /*new RxHttp<BaseListResult<TownBean>>().send(ApiManager.getService().getTownList(),
                    new Response<BaseListResult<TownBean>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<TownBean> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                List<TownBean> data = baseListResult.data;
                                ScreenUtil.showTownList(mActivity, mActivity.getWindow(), ll_header, data, new ScreenUtil.onSelectListener() {
                                    @Override
                                    public void commit(int position) {
                                        tv_street.setText(data.get(position).TownName);
                                        tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                                        if (regulationListener != null){
                                            regulationListener.onStreetSelectListener(data.get(position));
                                        }
                                    }
                                });
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });*/
        }else if (id == R.id.ll_mixing){//管理等级
            select = level;
            GlobalParam.getBindData(mActivity,GlobalParam.MANAGE_LEVEL,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_mixing.setText(bean.Name);
                    tv_mixing.setTextColor(getResources().getColor(R.color.baseColor));
                    if (regulationListener != null){
                        regulationListener.onLevelSelectListener(bean.ID);
                    }
                });
            });
            /*new RxHttp<BaseListResult<BasicDataBind>>().send(ApiManager.getService().getBindDate(),
                    new Response<BaseListResult<BasicDataBind>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<BasicDataBind> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                List<BasicDataBindBean> list = baseListResult.data.get(0).Rlevel;//管理等级
                                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, list,position -> {
                                    BasicDataBindBean bean = list.get(position);
                                    tv_mixing.setText(bean.Name);
                                    tv_mixing.setTextColor(getResources().getColor(R.color.baseColor));
                                    if (regulationListener != null){
                                        regulationListener.onLevelSelectListener(bean.ID);
                                    }
                                });
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });*/
        }else if (id == R.id.ll_type){//整治情况
            select = regulation;
            GlobalParam.getBindData(mActivity,GlobalParam.REGULATION_CASE,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_type.setText(bean.Name);
                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                    if (regulationListener != null){
                        regulationListener.onRegulationSelectListener(bean.ID);
                    }
                });
            });
            /*new RxHttp<BaseListResult<BasicDataBind>>().send(ApiManager.getService().getBindDate(),
                    new Response<BaseListResult<BasicDataBind>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<BasicDataBind> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                List<BasicDataBindBean> list = baseListResult.data.get(0).RRF;
                                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, list,position -> {
                                    BasicDataBindBean bean = list.get(position);
                                    tv_type.setText(bean.Name);
                                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                                    if (regulationListener != null){
                                        regulationListener.onRegulationSelectListener(bean.ID);
                                    }
                                });
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });*/
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
            if (regulationListener != null){
                regulationListener.onSearchListener();
            }
            return true;
        }
        return false;
    }

    public String getSearchText(){
        return et_search.getText().toString();
    }

    public void clear(){
        tv_type.setText("整治情况");
        tv_street.setText("所属街镇");
        tv_mixing.setText("管理等级");
        tv_type.setTextColor(getResources().getColor(R.color.grayText));
        tv_mixing.setTextColor(getResources().getColor(R.color.grayText));
        tv_street.setTextColor(getResources().getColor(R.color.grayText));
    }
    private int select = 0;
    public static int search = 1,street=2,level = 3,regulation = 4;

    public void clearAdvanceStatus(){
        if (select == search){
            et_search.setText("");
        }else if (select == street){
            tv_street.setText("所属街镇");
            tv_street.setTextColor(getResources().getColor(R.color.grayText));
        }else if (select == level){
            tv_mixing.setText("管理等级");
            tv_mixing.setTextColor(getResources().getColor(R.color.grayText));
        }else if (select == regulation){
            tv_type.setText("整治情况");
            tv_type.setTextColor(getResources().getColor(R.color.grayText));
        }
    }

    public int getSelete(){
        return select;
    }
}
