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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.bean.net.BasicDataBindBean;
import com.gaocheng.baselibrary.global.GlobalParam;
import com.gaocheng.baselibrary.iterface.RiverGisListener;
import com.gaocheng.baselibrary.util.KeyboardUtil;
import com.gaocheng.baselibrary.util.popup.ScreenUtil;

/**
 * Created by 32672 on 2018/12/27.
 */

public class RiverGisHeaderView extends RelativeLayout implements View.OnClickListener,TextView.OnEditorActionListener {
    private Context mContext;
    private Activity mActivity;

    private LinearLayout ll_condition;
    private LinearLayout ll_search;

    private LinearLayout ll_street;
    private LinearLayout ll_year;
    private LinearLayout ll_type;
    private LinearLayout ll_tran_situation;

    private TextView tv_street;
    private TextView tv_year;
    private TextView tv_type;
    private TextView tv_tran_situation;

    private ImageView iv_search;
    private EditText et_search;

    private RelativeLayout rl_hide;
    private LinearLayout ll_back;
    private LinearLayout ll_show;



    private boolean isHide = true;
    private RiverGisListener gisListener;

    public void setGisListener(RiverGisListener gisListener) {
        this.gisListener = gisListener;
    }

    public RiverGisHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public String getSearchString() {
        return et_search.getText().toString().trim();
    }

    public RiverGisHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RiverGisHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RiverGisHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.header_gis_river,null);
        this.mContext = context;
        this.mActivity = (Activity) context;
        ll_condition = view.findViewById(R.id.ll_condition);
        ll_tran_situation = view.findViewById(R.id.ll_tran_situation);
        ll_search = view.findViewById(R.id.ll_search);
        ll_street = view.findViewById(R.id.ll_street);
        ll_year = view.findViewById(R.id.ll_year);
        ll_type = view.findViewById(R.id.ll_type);
        tv_street = view.findViewById(R.id.tv_street);
        tv_year = view.findViewById(R.id.tv_year);
        tv_type = view.findViewById(R.id.tv_type);
        tv_tran_situation = view.findViewById(R.id.tv_tran_situation);
        iv_search = view.findViewById(R.id.iv_search);
        et_search = view.findViewById(R.id.et_search);
        rl_hide = view.findViewById(R.id.rl_hide);
        ll_back = view.findViewById(R.id.ll_back);
        ll_show = view.findViewById(R.id.ll_show);
        addView(view);
        initData();
        initListener();
    }

    private void initListener() {
        iv_search.setOnClickListener(this);
        ll_street.setOnClickListener(this);
        ll_year.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        rl_hide.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_show.setOnClickListener(this);
        ll_tran_situation.setOnClickListener(this);
        et_search.setOnEditorActionListener(this);
    }

    private void initData() {

    }

    //显示搜索条件
    public void showCondition(){
        isHide = true;
        rl_hide.setVisibility(GONE);
        ll_search.setVisibility(VISIBLE);
        ll_condition.setVisibility(VISIBLE);
    }
    //影藏搜索条件
    public void hideCondition(){
        KeyboardUtil.hideKeyBoard(mContext,et_search);
        isHide = false;
        rl_hide.setVisibility(VISIBLE);
        ll_search.setVisibility(GONE);
        ll_condition.setVisibility(GONE);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_search){
            hideCondition();
        }else if (id == R.id.ll_show){
            showCondition();
        }else if (id == R.id.ll_back){
            if (onBackClickListener != null){
                onBackClickListener.onClick(ll_back);
            }
        }else if (id == R.id.ll_street){
            KeyboardUtil.hideKeyBoard(mContext,et_search);
            GlobalParam.getBindData(mActivity,GlobalParam.RIVER_TOWN, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_street, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_street.setText(bean.Name);
                    tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                    if (gisListener != null){
                        gisListener.onStreetClickListener(position == 0?"":bean.Name);
                    }
                });
            });
        }else if (id == R.id.ll_year){
            KeyboardUtil.hideKeyBoard(mContext,et_search);
            GlobalParam.getBindData(mActivity,GlobalParam.RIVER_TYPE,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_type, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_year.setText(bean.Name);
                    tv_year.setTextColor(getResources().getColor(R.color.baseColor));
                    if (gisListener != null){
                        gisListener.onWaterLeverListener(position == 0?"":bean.Name);
                    }
                });
            });

        }else if (id == R.id.ll_type){
            KeyboardUtil.hideKeyBoard(mContext,et_search);
            GlobalParam.getBindData(mActivity,GlobalParam.MANAGE_LEVEL,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_type, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_type.setText(bean.Name);
                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                    if (gisListener != null){
                        gisListener.onManageLevelListener(position == 0?"":bean.Name);
                    }
                });
            });

        }else if (id == R.id.ll_tran_situation){
            KeyboardUtil.hideKeyBoard(mContext,et_search);
            GlobalParam.getBindData(mActivity,GlobalParam.REGULATION_CASE,dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_type, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_tran_situation.setText(bean.Name);
                    tv_tran_situation.setTextColor(getResources().getColor(R.color.baseColor));
                    if (gisListener != null){
                        gisListener.onSituationListener(position == 0?"":bean.Name);
                    }
                });
            });
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

            if (gisListener != null){
                gisListener.onSearchListener();
            }
            return true;
        }
        return false;
    }



    public void setOnBackClickListener(RiverGisHeaderView.onBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    private onBackClickListener onBackClickListener;

    public interface onBackClickListener{
        void onClick(View view);
    }



}
