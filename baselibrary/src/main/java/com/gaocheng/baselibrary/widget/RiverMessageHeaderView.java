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
import com.gaocheng.baselibrary.iterface.RiverBasicInfoListener;
import com.gaocheng.baselibrary.util.KeyboardUtil;
import com.gaocheng.baselibrary.util.popup.ScreenUtil;

/**
 * Created by i on 2019/1/16.
 */

public class RiverMessageHeaderView extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener {
    private Context mContext;
    private Activity mActivity;

    private LinearLayout ll_header;
    private LinearLayout ll_street;
    private TextView tv_street;

    private LinearLayout ll_year;
    private TextView tv_year;
    private LinearLayout ll_type;
    private TextView tv_type;

    private EditText et_search;

    private RiverBasicInfoListener basicInfoListener;

    public RiverBasicInfoListener getbasicInfoListener() {
        return basicInfoListener;
    }

    public void setBasicInfromListener(RiverBasicInfoListener basicInfoListener) {
        this.basicInfoListener = basicInfoListener;
    }

    public RiverMessageHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public RiverMessageHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RiverMessageHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RiverMessageHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.header_basic_message, null);
        this.mContext = context;
        this.mActivity = (Activity) context;
        ll_header = view.findViewById(R.id.ll_header);
        ll_street = view.findViewById(R.id.ll_street);
        tv_street = view.findViewById(R.id.tv_street);
        ll_year = view.findViewById(R.id.ll_year);
        tv_year = view.findViewById(R.id.tv_year);
        ll_type = view.findViewById(R.id.ll_type);
        tv_type = view.findViewById(R.id.tv_type);
        et_search = view.findViewById(R.id.et_search);
        addView(view);
        initData();
        initListener();
    }

    private void initListener() {
        ll_year.setOnClickListener(this);
        ll_type.setOnClickListener(this);
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
        KeyboardUtil.hideKeyBoard(mContext, et_search);
        if (id == R.id.ll_street){//所属街镇
            select=street;
            GlobalParam.getBindData(mActivity,GlobalParam.RIVER_TOWN, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_street.setText(bean.Name);
                    tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfoListener != null){
                        basicInfoListener.onStreetClickListener(bean.ID);
                    }
                });
            });

        }else if (id == R.id.ll_year){//水体分类
            select = type;
            GlobalParam.getBindData(mActivity,GlobalParam.RIVER_TYPE,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_year.setText(bean.Name);
                    tv_year.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfoListener != null){
                        basicInfoListener.onRiverTypeListener(bean.ID);
                    }
                });
            });
            /*new RxHttp<BaseListResult<BasicDataBind>>().send(ApiManager.getService().getBindDate(),
                    new Response<BaseListResult<BasicDataBind>>(mActivity, true) {
                        @Override
                        public void onNext(BaseListResult<BasicDataBind> baseListResult) {
                            super.onNext(baseListResult);
                            if ("200".equals(baseListResult.ret)) {
                                List<BasicDataBindBean> list = baseListResult.data.get(0).RivType;//管理等级
                                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, list,position -> {
                                    BasicDataBindBean bean = list.get(position);
                                    tv_year.setText(bean.Name);
                                    tv_year.setTextColor(getResources().getColor(R.color.baseColor));
                                    if (basicInfoListener != null){
                                        basicInfoListener.onRiverTypeListener(bean.ID);
                                    }
                                });
                            }else {
                                ToastCommom.createToastConfig().ToastShow(mContext,baseListResult.msg);
                            }
                        }
                    });*/
        }else if (id == R.id.ll_type){ //管理等级
            select = level;
            GlobalParam.getBindData(mActivity,GlobalParam.MANAGE_LEVEL,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_type.setText(bean.Name);
                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfoListener != null){
                        basicInfoListener.onLevelListener(bean.ID);
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
                                    tv_type.setText(bean.Name);
                                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                                    if (basicInfoListener != null){
                                        basicInfoListener.onLevelListener(bean.ID);
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
            if (basicInfoListener != null){
                select=search;
                basicInfoListener.onSearchListener();
            }
            return true;
        }
        return false;
    }

    public String getSearchText(){
        return et_search.getText().toString();
    }

    public void clear(){
        tv_type.setText("管理等级");
        tv_street.setText("所属街镇");
        tv_year.setText("水体分类");
        tv_type.setTextColor(getResources().getColor(R.color.grayText));
        tv_year.setTextColor(getResources().getColor(R.color.grayText));
        tv_street.setTextColor(getResources().getColor(R.color.grayText));
    }

    private int select = 0;
    public static int search = 1,street=2,level = 3,type = 4;

    public void clearAdvanceStatus(){
        if (select == search){
            et_search.setText("");
        }else if (select == street){
            tv_street.setText("所属街镇");
            tv_street.setTextColor(getResources().getColor(R.color.grayText));
        }else if (select == level){
            tv_type.setText("管理等级");
            tv_type.setTextColor(getResources().getColor(R.color.grayText));
        }else if (select == type){
            tv_year.setText("水体分类");
            tv_year.setTextColor(getResources().getColor(R.color.grayText));
        }
    }

    public int getSelete(){
        return select;
    }

}
