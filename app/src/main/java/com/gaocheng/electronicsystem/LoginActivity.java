package com.gaocheng.electronicsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gaocheng.baselibrary.base.BaseActivity;
import com.gaocheng.baselibrary.global.RouthConstant;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.iv_pass_visiable)
    ImageView ivPassVisiable;

    @Autowired
    String route;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected int getToolbarLayout() {
        return -1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void loadData() {
        ARouter.getInstance().inject(this);
    }

    @OnClick({ R.id.tv_login
            ,R.id.iv_phone_clean, R.id.iv_pass_visiable})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_login){
            login();
        }else if (viewId == R.id.iv_phone_clean){
            etPhone.setText("");
        }
    }

    private void login() {
        String phone = etPhone.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            tsg("请输入用户账号");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            tsg("请输入密码");
            return;
        }
        if (phone.toLowerCase().equals("admin") && pass.equals("123456")){
            ARouter.getInstance().build(RouthConstant.RIVER).navigation();
            mActivity.finish();
        }else {
             tsg("账户或密码错误");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            etPhone.setText(data.getStringExtra("phone"));
            etPass.setText(data.getStringExtra("pass"));
            etPhone.setSelection(etPhone.getText().toString().trim().length());
            etPass.setSelection(etPass.getText().toString().trim().length());

        }
    }

}
