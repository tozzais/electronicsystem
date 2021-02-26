package com.gaocheng.rainsystem.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.gaocheng.baselibrary.adapter.viewpager.GisPagerNoTitleAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.BasicInformDetail;
import com.gaocheng.baselibrary.bean.net.RainGisItemBean;
import com.gaocheng.baselibrary.bean.request.RequestRainGisList;
import com.gaocheng.baselibrary.global.Constant;
import com.gaocheng.baselibrary.http.ApiManager;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.http.Response;
import com.gaocheng.baselibrary.http.RxHttp;
import com.gaocheng.baselibrary.iterface.GisListener;
import com.gaocheng.baselibrary.util.AppUtil;
import com.gaocheng.baselibrary.util.TablayoutWidthUtil;
import com.gaocheng.baselibrary.widget.GisHeaderView;
import com.gaocheng.rainsystem.R;
import com.gaocheng.rainsystem.R2;
import com.gaocheng.rainsystem.RainMainActivity;
import com.gaocheng.rainsystem.bean.eventbus.UpdateGisId;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GisFragment extends BaseFragment implements
        GisListener {


    @BindView(R2.id.ll_legend)
    LinearLayout llLegend;
    @BindView(R2.id.iv_legend)
    ImageView ivLegend;
    private String id;

    private boolean isInit = false;

    @BindView(R2.id.map)
    MapView mMapView;
    @BindView(R2.id.tabs)
    TabLayout tabs;

    @BindView(R2.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R2.id.vp_view)
    ViewPager vpView;
    @BindView(R2.id.tv_title)
    TextView tv_title;

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.iv_advance)
    ImageView ivAdvance;

    @BindView(R2.id.gis_header_view)
    GisHeaderView gis_header_view;

    @Override
    public int setLayout() {
        return R.layout.fragment_gis;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void loadData() {
        showContent();
        if (id != null) {
            bottomShow();
        } else {
            bottomHide();
        }
        isInit = true;

        initMap();
        setData();

        if (id == null) {
            getData();
        }
        mMapView.setBackgroundGrid(new BackgroundGrid(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.white),
                0, 10
        ));

    }

    private String ID = "", Year = "", endYear = "", HouserType = "", Des = "", Da = "";

    private void getData() {
        type = GIS;
        if (bottomIsShow) {
            bottomHide();
        }
        String searchString = gis_header_view.getSearchString();
        Da = searchString;
        RequestRainGisList requestBasicInformBean = new RequestRainGisList();
        requestBasicInformBean.ID = ID + "";
        requestBasicInformBean.BeginYear = Year + "";
        requestBasicInformBean.EndYear = endYear + "";
        requestBasicInformBean.HouserType = HouserType;
        requestBasicInformBean.Des = Des;
        requestBasicInformBean.Da = Da;
        new RxHttp<BaseResult<List<RainGisItemBean>>>().send(ApiManager.getService().getRainGisList(requestBasicInformBean),
                new Response<BaseResult<List<RainGisItemBean>>>(mActivity, true) {
                    @Override
                    public void onNext(BaseResult<List<RainGisItemBean>> beanBaseListResult) {
                        super.onNext(beanBaseListResult);
                        if ("200".equals(beanBaseListResult.ret)) {
                            if (beanBaseListResult.data == null || beanBaseListResult.data.size() == 0) {
                                tsg("无数据");
                                return;
                            }
                            cleanAll();
                            zoomToScale(300000);
                            List<Point> list = new ArrayList<>();
                            for (RainGisItemBean bean : beanBaseListResult.data) {
                                if (bean.PosX != null && !bean.PosX.equals("")) {
                                    try{
                                        int x = Integer.parseInt(bean.PosX);
                                        int y = Integer.parseInt(bean.PosY);
                                        addPoint(x, y, bean.NDes, bean.ID);
                                        list.add(new Point(x, y));
                                    }catch (Exception e){

                                    }
                                }
                            }
                            Multipoint multipoint = new Multipoint(list);
                            mMapView.setViewpointGeometryAsync(multipoint, 50);
                        } else {
                            tsg(beanBaseListResult.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleted();
                    }
                });


    }


    @Override
    public void initListener() {
        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ivBack.setVisibility(View.INVISIBLE);
                        tv_title.setText(String.format("%s(小区信息)", getTitle()));
                        break;
                    case 1:
                        ivBack.setVisibility(View.VISIBLE);
                        ivAdvance.setVisibility(View.VISIBLE);
                        tv_title.setText(String.format("%s(改造信息)", getTitle()));
                        break;
                    case 2:
                        ivAdvance.setVisibility(View.INVISIBLE);
                        tv_title.setText(String.format("%s(实施单位)", getTitle()));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        gis_header_view.setOnBackClickListener(view -> back());
        gis_header_view.setGisListener(this);

        mMapView.setOnTouchListener(new MapViewTouchListener(getContext(), mMapView));
    }

    private String getTitle(){
        if (basicInformDetail == null || basicInformDetail.Name == null ) return "";
        return basicInformDetail.Name;
    }

    private static final int BASIC_INFORM = 0, TRANS_INFORM = 1, GIS = 2;
    private int type = BASIC_INFORM;

    private void back() {
        switch (type) {
            case BASIC_INFORM:
            case GIS:
                ((RainMainActivity) mActivity).selectFragment(RainMainActivity.BASIC_INFORM);
                break;
            case TRANS_INFORM:
                ((RainMainActivity) mActivity).selectFragment(RainMainActivity.TRANS_INFORM);
                break;
        }
    }


    public void setFromAndId(int type, String id) {
        this.type = type;
        this.id = id;
        if (isInit) {
            bottomShow();
        }
        EventBus.getDefault().post(new UpdateGisId(id));

    }

    private List<Fragment> fragmentList;
    private List<String> list_Title;

    private void setData() {
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        GisBasicInformationFragment inform = new GisBasicInformationFragment();
        GisTransformationFragment trans = new GisTransformationFragment();
        GisImplementUnitFragment unit = new GisImplementUnitFragment();
        inform.setArguments(bundle);
        trans.setArguments(bundle);
        unit.setArguments(bundle);
        fragmentList.add(inform);
        fragmentList.add(trans);
        fragmentList.add(unit);
        list_Title.add("基本信息");
        list_Title.add("改造信息");
        list_Title.add("实施单位");
        GisPagerNoTitleAdapter gisPagerAdapter = new GisPagerNoTitleAdapter(getChildFragmentManager(), mActivity, fragmentList);
        vpView.setAdapter(gisPagerAdapter);
        tabs.setupWithViewPager(vpView);//此方法就是让tablayout和ViewPager联动
        tabs.post(() -> TablayoutWidthUtil.setIndicator(tabs, 20, 20));
        vpView.setOffscreenPageLimit(2);


    }


    @Override
    protected int getTitleLayout() {
        return -1;
    }


    private boolean bottomIsShow = false;

    public void bottomHide() { //影藏
        bottomIsShow = false;
        ll_bottom.animate()
                .translationY(ll_bottom.getHeight())//视图translationY属性动态移动到指定值
                .setInterpolator(new AccelerateInterpolator(2))
                .start();

    }

    public void bottomShow() { //显示
        if (id == null) {
            return;
        }
        bottomIsShow = true;
        ll_bottom.setVisibility(View.VISIBLE);
        ll_bottom.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        if (type == BASIC_INFORM) {
            vpView.setCurrentItem(0);
        } else if (type == TRANS_INFORM) {
            vpView.setCurrentItem(1);

        }
//        if (basicInformDetail != null){
//            float translationY = ll_bottom.getTranslationY()/2;
//            int x = Integer.parseInt(basicInformDetail.PosX);
//            int y = Integer.parseInt(basicInformDetail.PosY);
//            Log.e("number",x+":"+y+":"+translationY);
//            showPointAtCenter(x, (int) (y+translationY));
//        }
    }


    @OnClick({R2.id.iv_back, R2.id.iv_advance, R2.id.tv_navigation, R2.id.iv_legend})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            vpView.setCurrentItem(vpView.getCurrentItem() - 1);
        } else if (id == R.id.iv_advance) {
            vpView.setCurrentItem(vpView.getCurrentItem() + 1);
        } else if (id == R.id.tv_navigation) {
            if (AppUtil.baiduMapIsavailable(mActivity)) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/navi?query=" + basicInformDetail.Name + "&src=andr.baidu.openAPIdemo"));
                startActivity(i1);
            } else {
                tsg("请先安装百度地图！");
            }
//            Point point = new Point(Integer.parseInt(basicInformDetail.PosX),Integer.parseInt(basicInformDetail.PosY));
        }else if (id == R.id.iv_legend) {
            llLegend.setVisibility(llLegend.getVisibility() == View.GONE?View.VISIBLE:View.GONE);
        }

    }

//    private String townName = "";

    //用来显示 小区的名字 和 得到要绘制 点的坐标和状态
    BasicInformDetail basicInformDetail;
    //地图是否加载
    private boolean isLoad = false;
    //是点击地图还是重其他界面接入地图的
//    private boolean isClickMap = false;


    public void setTownName(BasicInformDetail name) {
        this.basicInformDetail = name;
        if (type == BASIC_INFORM) {
            tv_title.setText(basicInformDetail.Name + "(小区信息)");
        } else if (type == TRANS_INFORM) {
            ivBack.setVisibility(View.VISIBLE);
            tv_title.setText(basicInformDetail.Name + "(改造信息)");
        } else if (type == GIS) {
            ivBack.setVisibility(View.VISIBLE);
            tv_title.setText(basicInformDetail.Name + "(改造信息)");
            vpView.setCurrentItem(1);
        }
        if (isLoad) {
            showPoint();
        }

    }

    private void showPoint() {
        if (basicInformDetail != null) {
            int x = Integer.parseInt(basicInformDetail.PosX);
            int y = Integer.parseInt(basicInformDetail.PosY);
            if (type != GIS) {
                //从前面进去的
                cleanAll();
                addPic(x, y, basicInformDetail.Des, basicInformDetail.ID);
                addPoint(x, y, basicInformDetail.Des, basicInformDetail.ID);
            } else if (type == GIS) {
                cleanPrevious();
                addPic(x, y, basicInformDetail.Des, basicInformDetail.ID);
            }
            new Handler().postDelayed(() -> {
//                zoomToScale(scale);
                showPointAtCenter(x, y, scale);
            }, 100);

        }
    }

    @Override
    public void onStreetClickListener(String townBean) {
        ID = townBean;
        getData();

    }

    @Override
    public void onYearSelectListener(String year, String endYear) {
        this.Year = year;
        this.endYear = endYear;
        getData();

    }

    @Override
    public void onTypeListener(String type) {
        HouserType = type;
        getData();
    }

    @Override
    public void onTranSituationListener(String type) {
        Des = type;
        getData();
    }

    @Override
    public void onSearchListener() {

        getData();
    }

    //选中点的缩放比例
    private int scale = 5000;

    private int pointSize = 10;
    //GraphicsOverlay变量 图像覆盖
    GraphicsOverlay mGraphicsOverlay;
    Graphic picGraphic;
    private FeatureLayer mFeatureLayer;
    //用来存
    private Map<Graphic, String> graphicMap = new HashMap<>();


    /* 地图 相关的 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.dispose();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.pause();
        }
    }


    private void initMap() {
        ArcGISMap map = new ArcGISMap();
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(Constant.url_basemap);
        Basemap basemap = new Basemap(tiledLayer);
        map.setBasemap(basemap);

        mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        mMapView.setMap(map);
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4043726575,none,ZZ0RJAY3FL77P2ELJ156");
        mMapView.setAttributionTextVisible(false);
        map.addDoneLoadingListener(() -> {

            isLoad = true;
            showPoint();
            addTitleLayer(Constant.url_city_area);
            addTitleLayer(Constant.url_community_area);
            addTitleLayer(Constant.url_town_city_area);
        });
        map.addLoadStatusChangedListener(loadStatusChangedEvent -> {
            LoadStatus newLoadStatus = loadStatusChangedEvent.getNewLoadStatus();
            if (newLoadStatus == LoadStatus.FAILED_TO_LOAD && !NetworkUtil.isNetworkAvailable(mActivity)) {
                //加载失败
                showError("无网络");
            }
        });

    }

    private void addTitleLayer(String url) {
        //缩小 后无法全部加载
        ArcGISTiledLayer layer = new ArcGISTiledLayer(url);
        mMapView.getMap().getOperationalLayers().add(layer);
        layer.addDoneLoadingListener(() -> {
            layer.setMinScale(mMapView.getMap().getMinScale());
            layer.setMaxScale(mMapView.getMap().getMaxScale());
        });
    }

    /**
     * 地图平移到指定中心点 （54坐标系）
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    private void showPointAtCenter(int x, int y, double scale) {
        Point point = new Point(x, y);
//        android.graphics.Point point1 = mMapView.locationToScreen(point);
//        android.graphics.Point point2 = new android.graphics.Point(point1.x,point1.y+ll_bottom.getHeight()/2);
//        Point point3 = mMapView.screenToLocation(point2);

        ListenableFuture<Boolean> bool = mMapView.setViewpointCenterAsync(point, scale);
        bool.addDoneListener(() -> {
            zoomToScale(scale);
            android.graphics.Point point1 = mMapView.locationToScreen(point);
            android.graphics.Point point2 = new android.graphics.Point(point1.x, point1.y + ll_bottom.getHeight() / 3);
            Point point3 = mMapView.screenToLocation(point2);
            mMapView.setViewpointCenterAsync(point3, scale);
        });


    }

    /**
     * 地图缩放到指定规模 我们项目中 是 2000~1000000
     * 可以使用 mMapView.getMapScale() 获取当前比例
     *
     * @param scale 值越大 地图显示的越大
     */
    private void zoomToScale(double scale) {
        mMapView.setViewpointScaleAsync(scale);

    }


    /**
     * 添加一个点
     *
     * @param x   x 坐标
     * @param y   y 坐标
     * @param des 点的颜色
     */
    private void addPoint(int x, int y, String des, String id) {

        int color;
        if ("201".equals(des)) {
            //完成改造
            color = getResources().getColor(R.color.chart_green);
        } else if ("202".equals(des)) {
            //无需改造
            color = getResources().getColor(R.color.chart_blue);
        } else if ("203".equals(des)) {
            //正在改造
            color = getResources().getColor(R.color.chart_yellow_light);
        } else if ("204".equals(des)) {
            //计划改造
            color = getResources().getColor(R.color.chart_red);
        } else if ("205".equals(des)) {
            //未计划
            color = getResources().getColor(R.color.chart_gray);
        } else {
            color = getResources().getColor(R.color.chart_red);
        }
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, color, pointSize);
        Point point = new Point(x, y);
        Graphic graphic = new Graphic(point, pointSymbol);
        graphicMap.put(graphic, id + "");
//        SimpleRenderer pointRenderer = new SimpleRenderer(pointSymbol);
//        mGraphicsOverlay.setRenderer(pointRenderer);
        mGraphicsOverlay.getGraphics().add(graphic);
    }

    /**
     * 添加一个图片
     *
     * @param x
     * @param y
     * @param status
     */
    private void addPic(int x, int y, String status, String id) {
        Point point = new Point(x, y);
        //通过APP资源创建picture marker symbol
        BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_red);
        if ("201".equals(status)) {
            //完成改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_green);
        } else if ("202".equals(status)) {
            //无需改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_blue);
        } else if ("203".equals(status)) {
            //正在改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_yellow);
        } else if ("204".equals(status)) {
            //计划改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_red);
        } else if ("205".equals(status)) {
            //未计划
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_grey);
        }
        final PictureMarkerSymbol pinStarBlueSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
        //可以设置图片大小，如果没有设置，图片将自动设置为其自身默认的像素大小
        //它的外观会因不同分辨率的设备而异。
        pinStarBlueSymbol.setHeight(30);
        pinStarBlueSymbol.setWidth(20);
        //Optionally set the offset, to align the base of the symbol aligns with the point geometry
        //可以设置图片的偏移，使图片符号的基点与几何点对齐
        pinStarBlueSymbol.setOffsetY(
                pointSize); //符号使用的图像周围有一个透明的缓冲区，因此偏移量不仅仅是高度的1/2
        pinStarBlueSymbol.loadAsync();
        pinStarBlueSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                //一旦符号被装载，在GraphicOverlay（图像覆盖）中添加一个新的Graphic（图像）
                //通过与初始化视点相同的位置的创建一个新的Graphic
                picGraphic = new Graphic(point, pinStarBlueSymbol);
                graphicMap.put(picGraphic, id + "");
                mGraphicsOverlay.getGraphics().add(picGraphic);
            }
        });
    }

    class MapViewTouchListener extends DefaultMapViewOnTouchListener {

        public MapViewTouchListener(Context context, MapView mapView) {
            super(context, mapView);
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            android.graphics.Point screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
            final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic = mMapView.identifyGraphicsOverlayAsync(mGraphicsOverlay, screenPoint, 10.0, false, 2);

            identifyGraphic.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                        // get the list of graphics returned by identify graphic overlay
                        List<Graphic> graphic = grOverlayResult.getGraphics();
                        // get size of list in results
                        int identifyResultSize = graphic.size();
                        if (!graphic.isEmpty()) {
                            //说明点中图标
                            Graphic graphic1 = graphic.get(0);
//                            graphic1.setSelected(true);
                            id = graphicMap.get(graphic1);
                            Log.e("TAG", bottomIsShow + "");
                            if (!bottomIsShow) {
                                bottomShow();
                            }
                            EventBus.getDefault().post(new UpdateGisId(id));
                        } else {
                            if (bottomIsShow) {
                                bottomHide();
                            }
                            gis_header_view.hideCondition();
                        }
                    } catch (InterruptedException | ExecutionException ie) {
                        ie.printStackTrace();
                    }

                }
            });

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            bottomHide();
            return super.onScale(detector);
        }
    }

    /**
     * 清除所有自己绘制的点
     */
    private void cleanAll() {
        for (Graphic graphic : graphicMap.keySet()) {
            mGraphicsOverlay.getGraphics().remove(graphic);
        }
    }

    /**
     * 清除之前绘制的图片
     */
    private void cleanPrevious() {
        mGraphicsOverlay.getGraphics().remove(picGraphic);
    }


}
