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
import com.gaocheng.baselibrary.iterface.RainTransListener;
import com.gaocheng.baselibrary.util.KeyboardUtil;
import com.gaocheng.baselibrary.util.popup.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32672 on 2019/1/8.
 */

public class RainTransHeaderView extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener {

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

    private RainTransListener basicInfromListener;



    public void setBasicInfromListener(RainTransListener basicInfromListener) {
        this.basicInfromListener = basicInfromListener;
    }

    public RainTransHeaderView(Context context) {
        super(context);
        initView(context);
    }


    public RainTransHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RainTransHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RainTransHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            selete = street;
            GlobalParam.getBindData(mActivity,GlobalParam.RAIN_TOWN, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_street.setText(bean.Name);
                    tv_street.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfromListener != null){
                        basicInfromListener.onStreetClickListener(bean.ID);
                    }
                });
            });
        }else if (id == R.id.ll_mixing){//混接情况
            selete = mix;
            GlobalParam.getBindData(mActivity,GlobalParam.MIXING, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_mixing.setText(bean.Name);
                    tv_mixing.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfromListener != null){
                        basicInfromListener.onMixingSelectListener(bean.ID);
                    }
                });
            });

        }else if (id == R.id.ll_type){   //改造情况
            selete = type;
            GlobalParam.getBindData(mActivity,GlobalParam.TRANSFORMATION_SITUATION, dataBind ->{
                ScreenUtil.showBindList(mActivity, mActivity.getWindow(), ll_header, dataBind,position -> {
                    BasicDataBindBean bean = dataBind.get(position);
                    tv_type.setText(bean.Name);
                    tv_type.setTextColor(getResources().getColor(R.color.baseColor));
                    if (basicInfromListener != null){
                        basicInfromListener.onTransSituationSelectListener(bean.ID);
                    }
                });
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
            if (basicInfromListener != null){
                selete = search;
                basicInfromListener.onSearchListener();
            }
            return true;
        }
        return false;
    }

    public String getSearchText(){
        return et_search.getText().toString();
    }



    public List<String> getMixing(){
        List<String> mix = new ArrayList<>();
        mix.add("是");
        mix.add("否");
        return mix;

    }

    public List<String> getSituation(){
        List<String> type = new ArrayList<>();
        type.add("无需改造");
        type.add("完成改造");
        type.add("计划改造");
        type.add("正在改造");
        type.add("未计划");
        return type;

    }

    public void clear(){
        tv_type.setText("改造情况");
        tv_street.setText("所属街镇");
        tv_mixing.setText("混接情况");
        tv_type.setTextColor(getResources().getColor(R.color.grayText));
        tv_mixing.setTextColor(getResources().getColor(R.color.grayText));
        tv_street.setTextColor(getResources().getColor(R.color.grayText));
    }

    private int selete = 0;
    public static int search = 1,street=2,mix = 3,type = 4;
    public void clearAdvanceStatus(){
        if (selete == search){
            et_search.setText("");
        }else if (selete == street){
            tv_street.setText("所属街镇");
            tv_street.setTextColor(getResources().getColor(R.color.grayText));
        }else if (selete == mix){
            tv_mixing.setText("混接情况");
            tv_mixing.setTextColor(getResources().getColor(R.color.grayText));
        }else if (selete == type){
            tv_type.setText("改造情况");
            tv_type.setTextColor(getResources().getColor(R.color.grayText));
        }
    }

    public int getSelete(){
        return selete;
    }
}
