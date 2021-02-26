package com.gaocheng.baselibrary.util;

/**
 * Created by jumpbox on 2017/2/28.
 */

public class AMapLocationUtils {
//    public static AMapLocationUtils getInstance() {
//        return new AMapLocationUtils();
//    }
//
//
//    //声明AMapLocationClientOption对象
//    public AMapLocationClientOption mLocationOption = null;
//    //声明AMapLocationClient类对象
//    public AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    public AMapLocationListener mLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            //回收资源
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//            mLocationClient = null;
//            mLocationOption  = null;
//
//            if (mOnResultListener != null){
//                mOnResultListener.onResult(aMapLocation);
//            }
//
//        }
//    };
//
//    //回调到页面监听器
//    OnLocationResultListener mOnResultListener;
//
//
//    public AMapLocationUtils() {
//        //初始化定位
//        mLocationClient = new AMapLocationClient(BaseApplication.mContext);
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        setOnceLocation();
//    }
//
//    /**
//     * 设置单次定位
//     */
//    private void setOnceLocation() {
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
//        //设置超时时间
//        mLocationOption.setHttpTimeOut(10000);
//        //缓存
//        mLocationOption.setLocationCacheEnable(false);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//    }
//
//
//    public AMapLocationUtils setLocationResultListener(OnLocationResultListener resultListener) {
//        mOnResultListener = resultListener;
//        return this;
//    }
//
//    public void start() {
//        if (mLocationListener == null) {
//            throw new NullPointerException("mLocationListener not null");
//        }
//        //启动定位
//        mLocationClient.startLocation();
//    }
//
//
//    public interface OnLocationResultListener{
//        void onResult(AMapLocation aMapLocation);
//    }
}
