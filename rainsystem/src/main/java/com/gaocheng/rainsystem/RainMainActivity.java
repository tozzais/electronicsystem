package com.gaocheng.rainsystem;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gaocheng.baselibrary.base.BaseActivity;
import com.gaocheng.baselibrary.bean.eventbus.UpdataTransList;
import com.gaocheng.baselibrary.bean.net.VersionBean;
import com.gaocheng.baselibrary.download.DownLoadUtils;
import com.gaocheng.baselibrary.download.DownloadApk;
import com.gaocheng.baselibrary.global.GlobalParam;
import com.gaocheng.baselibrary.global.RouthConstant;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.rainsystem.ui.fragment.BasicMessageFragment;
import com.gaocheng.rainsystem.ui.fragment.GisFragment;
import com.gaocheng.rainsystem.ui.fragment.MessageInquireFragment;
import com.gaocheng.rainsystem.ui.fragment.StatisticalFragment;
import com.gaocheng.rainsystem.ui.fragment.TransInformationFragment;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RouthConstant.RAIN)
public class RainMainActivity extends BaseActivity {
    @BindView(R2.id.fl_container)
    FrameLayout flContainer;
    @BindView(R2.id.iv_sell)
    ImageView ivSell;
    @BindView(R2.id.tv_sell)
    TextView tvSell;
    @BindView(R2.id.iv_secondCart)
    ImageView ivSecondCart;
    @BindView(R2.id.tv_secondCart)
    TextView tvSecondCart;
    @BindView(R2.id.iv_newCart)
    ImageView ivNewCart;
    @BindView(R2.id.tv_newCart)
    TextView tvNewCart;
    @BindView(R2.id.iv_mine)
    ImageView ivMine;
    @BindView(R2.id.tv_mine)
    TextView tvMine;
    @BindView(R2.id.iv_message_query)
    ImageView ivMessageQuery;
    @BindView(R2.id.tv_message_query)
    TextView tvMessageQuery;

    public static final int BASIC_INFORM = 0, TRANS_INFORM = 1, GIS = 2,
            STATISTICAL = 3, MESSAGE_INQUIRE = 4;
    private int mPosition;//当前选中的底部菜单
    private FragmentManager fragmentManager;
    private BasicMessageFragment basicInformationFragment;
    private TransInformationFragment rainTransFragment;
    private GisFragment gisFragment;
    private MessageInquireFragment messageInquireFragment;
    private StatisticalFragment statisticalFragment;
    private static final String TAG_BASIC = "tag_basic", TAG_TRANS = "tag_trans", TAG_GIS = "tag_gis", TAG_STATIS = "tag_statis", TAG_MESSAGE = "tag_message";

    @Override
    public int getLayoutId() {
        return R.layout.rian_activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null){
            basicInformationFragment = (BasicMessageFragment) fragmentManager.findFragmentByTag(TAG_BASIC);
            rainTransFragment = (TransInformationFragment) fragmentManager.findFragmentByTag(TAG_TRANS);
            gisFragment = (GisFragment) fragmentManager.findFragmentByTag(TAG_GIS);
            statisticalFragment = (StatisticalFragment) fragmentManager.findFragmentByTag(TAG_STATIS);
            messageInquireFragment = (MessageInquireFragment) fragmentManager.findFragmentByTag(TAG_MESSAGE);
        }

        StatusBarUtil.setColor(RainMainActivity.this, getResources().getColor(R.color.black));
        selectFragment(BASIC_INFORM);
    }

    @Override
    public void loadData() {
        Rect visibleFrame = new Rect();
        mHeaderView.getGlobalVisibleRect(visibleFrame);
        int height = mHeaderView.getResources().getDisplayMetrics().heightPixels;
        Log.e("shuju",visibleFrame.bottom+":::"+height);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getVersion();
            }
        },1000);
    }

    //显示地图上的点
    public void show(int type, String id) {
        selectFragment(GIS);
//        gisFragment.setShowPoint(new ShowPoint());
        gisFragment.setFromAndId(type, id);
    }

    @OnClick({R2.id.ll_basic_message, R2.id.ll_secondCart, R2.id.ll_newCart, R2.id.ll_mine
            , R2.id.ll_message_query, R2.id.tv_left_title})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_basic_message) {
            selectFragment(BASIC_INFORM);
        } else if (id == R.id.ll_secondCart) {
            selectFragment(TRANS_INFORM);
        } else if (id == R.id.ll_newCart) {
            selectFragment(GIS);
        } else if (id == R.id.ll_mine) {
            selectFragment(STATISTICAL);
        } else if (id == R.id.ll_message_query) {
            selectFragment(MESSAGE_INQUIRE);
        }else if (id == R.id.tv_left_title) {
            AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
            builder.setItems(new String[]{"切换到河道管理","打开信息更新权限","打开现场评估权限"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            GlobalParam.savaSystemType(false);
//                            Class clazz= null;
//                            try {
//                                clazz = Class.forName("com.gaocheng.riversystem.RiverMainActivity");
//                                startActivity(new Intent(mActivity,clazz));
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
                            ARouter.getInstance().build(RouthConstant.RIVER).navigation();
                            finish();
                            break;
                        case 1:
                            GlobalParam.savaRainUpdatePermission(true);
                            EventBus.getDefault().post(new UpdataTransList());
                            break;
                        default:
                            GlobalParam.savaRainUpdatePermission(false);
                            EventBus.getDefault().post(new UpdataTransList());
                            break;
                    }

                }
            });
            builder.show();
        }

    }

    public void selectFragment(int position) {
        switch (position) {
            case BASIC_INFORM:
                mPosition = BASIC_INFORM;
                mHeaderView.setVisibility(View.VISIBLE);
                setNotBackTitle("小区雨污基本信息");
                tvLeftTitle.setText("切换");
                tvLeftTitle.setVisibility(View.VISIBLE);
                setTabChecked(0);
                setTabSelection(0);
                break;
            case TRANS_INFORM:
                mPosition = TRANS_INFORM;
                mHeaderView.setVisibility(View.VISIBLE);
                setNotBackTitle("小区雨污改造信息");
                tvLeftTitle.setVisibility(View.VISIBLE);
                setTabChecked(1);
                setTabSelection(1);
                break;
            case GIS:
                mPosition = GIS;
                mHeaderView.setVisibility(View.GONE);
                setNotBackTitle("GIS信息");
                setTabChecked(2);
                setTabSelection(2);
                break;
            case STATISTICAL:
                mPosition = STATISTICAL;
                mHeaderView.setVisibility(View.VISIBLE);
                tvLeftTitle.setVisibility(View.GONE);
                setNotBackTitle("小区雨污统计信息");
                setTabChecked(3);
                setTabSelection(3);
                break;
            case MESSAGE_INQUIRE:
                mPosition = MESSAGE_INQUIRE;
                mHeaderView.setVisibility(View.VISIBLE);
                tvLeftTitle.setVisibility(View.GONE);
                setNotBackTitle("小区雨污改造情况信息");
                setTabChecked(4);
                setTabSelection(4);
                break;

        }

    }

    /**
     * 设置底部菜单被选中后字体、图片的颜色
     *
     * @param pos
     */
    private void setTabChecked(int pos) {
        ivSell.setImageResource(pos == 0 ? R.mipmap.basic_information_select : R.mipmap.basic_information);
        tvSell.setTextColor(pos == 0 ? getResources().getColor(R.color.baseColor) : getResources().getColor(R.color.grayText));
        ivSecondCart.setImageResource(pos == 1 ? R.mipmap.transform_select : R.mipmap.transform);
        tvSecondCart.setTextColor(pos == 1 ? getResources().getColor(R.color.baseColor) : getResources().getColor(R.color.grayText));
        ivNewCart.setImageResource(pos == 2 ? R.mipmap.gis_select : R.mipmap.gis);
        tvNewCart.setTextColor(pos == 2 ? getResources().getColor(R.color.baseColor) : getResources().getColor(R.color.grayText));
        ivMine.setImageResource(pos == 3 ? R.mipmap.statis_select : R.mipmap.statis);
        tvMine.setTextColor(pos == 3 ? getResources().getColor(R.color.baseColor) : getResources().getColor(R.color.grayText));
        ivMessageQuery.setImageResource(pos == 4 ? R.mipmap.message_query_select : R.mipmap.message_query);
        tvMessageQuery.setTextColor(pos == 4 ? getResources().getColor(R.color.baseColor) : getResources().getColor(R.color.grayText));
    }

    private void setTabSelection(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();//开启Fragment事务
        hideFragments(transaction);//隐藏所有页面（）
        switch (position) {
            case 0:
                if (basicInformationFragment == null) {
                    basicInformationFragment = new BasicMessageFragment();
                    transaction.add(R.id.fl_container, basicInformationFragment,TAG_BASIC);
                } else {
                    transaction.show(basicInformationFragment);
                }
                break;

            case 1:
                if (rainTransFragment == null) {
                    rainTransFragment = new TransInformationFragment();
                    transaction.add(R.id.fl_container, rainTransFragment,TAG_TRANS);
                } else {
                    transaction.show(rainTransFragment);
                }
                break;
            case 2:
                if (gisFragment == null) {
                    gisFragment = new GisFragment();
                    transaction.add(R.id.fl_container, gisFragment,TAG_GIS);
                } else {
                    transaction.show(gisFragment);
                }
                break;

            case 3:
                if (statisticalFragment == null) {
                    statisticalFragment = new StatisticalFragment();
                    transaction.add(R.id.fl_container, statisticalFragment,TAG_STATIS);
                } else {
                    transaction.show(statisticalFragment);
                }
                break;
            case 4:
                if (messageInquireFragment == null) {
                    messageInquireFragment = new MessageInquireFragment();
                    transaction.add(R.id.fl_container, messageInquireFragment,TAG_MESSAGE);
                } else {
                    transaction.show(messageInquireFragment);
                }

                break;
        }
        // 提交
        transaction.commit();
    }

    /**
     * 隐藏所有的页面
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (gisFragment != null) {
            transaction.hide(gisFragment);
        }
        if (rainTransFragment != null) {
            transaction.hide(rainTransFragment);
        }
        if (basicInformationFragment != null) {
            transaction.hide(basicInformationFragment);
        }
        if (messageInquireFragment != null) {
            transaction.hide(messageInquireFragment);
        }
        if (statisticalFragment != null) {
            transaction.hide(statisticalFragment);
        }
    }

    /**
     * 当activity被回收后再次调用，恢复数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt("mPosition");
        selectFragment(mPosition);
    }

    /**
     * activity退出时（非返回键退出），保存数据（现场保护），
     * 保存的数据被onRestoreInstanceState（）调用恢复
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPosition", mPosition);
    }


    private void getVersion(){
        new RxHttp<VersionBean>().send(ApiManager.getService().getVersion(),
                new Response<VersionBean>(mActivity, false) {
                    @Override
                    public void onNext(VersionBean result) {
                        super.onNext(result);
                        versionBean = result;
                        //3.如果手机已经启动下载程序，执行downloadApk。否则跳转到设置界面
                        try {
                            PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), 0);
                            if (!pi.versionName.equals(result.VersionNumber))
                            showNormalDialog(result.APPTitleName,result.VersionDesc);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    private VersionBean versionBean;
    private void showNormalDialog(String title,String content){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mActivity);
        normalDialog.setTitle(title);
        normalDialog.setMessage(content);
        normalDialog.setPositiveButton("确定",((dialog, which) -> {
            if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
                tsg("正在下载");
                DownloadApk.downloadApk(getApplicationContext(), versionBean.DownloadAddress, versionBean.VersionDesc, versionBean.APPTitleName);
            } else {
                DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
            }
        }));
        normalDialog.setNegativeButton("取消",((dialog, which) -> {

        }));
        normalDialog.show();
    }






}
