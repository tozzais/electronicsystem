package com.gaocheng.riversystem.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
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
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.gaocheng.baselibrary.adapter.viewpager.GisPagerNoTitleAdapter;
import com.gaocheng.baselibrary.base.BaseFragment;
import com.gaocheng.baselibrary.bean.net.RiverGisBasicInfo;
import com.gaocheng.baselibrary.global.Constant;
import com.gaocheng.baselibrary.http.NetworkUtil;
import com.gaocheng.baselibrary.iterface.RiverGisListener;
import com.gaocheng.baselibrary.iterface.ViewPagerPageChangeListener;
import com.gaocheng.baselibrary.util.TablayoutWidthUtil;
import com.gaocheng.baselibrary.widget.RiverGisHeaderView;
import com.gaocheng.riversystem.R;
import com.gaocheng.riversystem.R2;
import com.gaocheng.riversystem.RiverMainActivity;
import com.gaocheng.riversystem.bean.eventbus.UpdateRiverGisId;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GisFragment extends BaseFragment implements
        RiverGisListener {

    @BindView(R2.id.ll_legend)
    LinearLayout llLegend;
    @BindView(R2.id.iv_legend)
    ImageView ivLegend;
    //河道的编号
    private String riverId;
    //界面是否初始化
    private boolean isInit = false;
    /**
     * 是否应该缩放地图
     * 列表页进入的 不缩放
     * 搜索 和 点击地图 缩放
     */

    private boolean isZoomMap = true;

    //GraphicsOverlay变量 图像覆盖
    private GraphicsOverlay mGraphicsOverlay;
    private Graphic picGraphic, solidGrahpic;
    private Map<Graphic, String> graphicMap = new HashMap<>();
    private ServiceFeatureTable table;
    private FeatureLayer featureLayer;

    @BindView(R2.id.mapview)
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
    RiverGisHeaderView gis_header_view;

    @Override
    public int setLayout() {
        return R.layout.river_fragment_gis;
    }

    @Override
    public void initView(Bundle savedInstanceState) {


    }


    GisBasicInformationFragment inform;
    GisRegulationFragment trans;
    GisImplementUnitFragment unit;

    @Override
    public void loadData() {
        showContent();
        initMap();
        if (riverId != null) {
            bottomShow();
            EventBus.getDefault().post(new UpdateRiverGisId(riverId));
        } else {
            bottomHide();
        }
        isInit = true;
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("id", riverId);
        inform = new GisBasicInformationFragment();
        trans = new GisRegulationFragment();
        unit = new GisImplementUnitFragment();
        inform.setArguments(bundle);
        trans.setArguments(bundle);
        unit.setArguments(bundle);
        fragmentList.add(inform);
        fragmentList.add(trans);
        fragmentList.add(unit);
        list_Title.add("基本信息");
        list_Title.add("整治信息");
        list_Title.add("实施单位");
        GisPagerNoTitleAdapter gisPagerAdapter = new GisPagerNoTitleAdapter(getChildFragmentManager(), mActivity, fragmentList);
        vpView.setAdapter(gisPagerAdapter);
        tabs.setupWithViewPager(vpView);//此方法就是让tablayout和ViewPager联动
        tabs.post(() -> TablayoutWidthUtil.setIndicator(tabs, 20, 20));
        vpView.setOffscreenPageLimit(2);

        mMapView.setBackgroundGrid(new BackgroundGrid(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.white),
                0, 10
        ));
    }


    @Override
    public void initListener() {
        vpView.addOnPageChangeListener(new ViewPagerPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setBottomTitle(position);
            }

        });
        gis_header_view.setOnBackClickListener(view -> back());
        gis_header_view.setGisListener(this);
    }

    private static final int BASIC_INFORM = 0, TRANS_INFORM = 1, GIS = 2;
    private int type = BASIC_INFORM;


    private void back() {
        switch (type) {
            case BASIC_INFORM:
            case GIS:
                ((RiverMainActivity) mActivity).selectFragment(RiverMainActivity.BASIC_INFO);
                break;
            case TRANS_INFORM:
                ((RiverMainActivity) mActivity).selectFragment(RiverMainActivity.REGULATION_INFO);
                break;
        }
    }


    public void setFromAndId(int type, String id) {
        if (gis_header_view != null)
            gis_header_view.hideCondition();
        isZoomMap = false;
        this.type = type;
        this.riverId = id;
        if (isInit) {
            bottomShow();
            EventBus.getDefault().post(new UpdateRiverGisId(id));
        }

    }

    private RiverGisBasicInfo info;

    public void setRiverInfo(RiverGisBasicInfo info) {
        this.info = info;
        if (type == BASIC_INFORM) {
            vpView.setCurrentItem(0);
            setBottomTitle(0);
        } else if (type == TRANS_INFORM || type == GIS) {
            vpView.setCurrentItem(1);
            setBottomTitle(1);
        }
        if (riverId != null) {
            search(riverId, 1);
        }
    }

    public void setBottomTitle(int position) {
        if (position == 0) {
            ivBack.setVisibility(View.INVISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            if (tv_title != null)
            tv_title.setText(String.format("%s(基本信息)", getTitle()));
        } else if (position == 1) {
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.VISIBLE);
            if (tv_title != null)
            tv_title.setText(String.format("%s(整治信息)", getTitle()));
        } else if (position == 2) {
            ivBack.setVisibility(View.VISIBLE);
            ivAdvance.setVisibility(View.INVISIBLE);
            if (tv_title != null)
            tv_title.setText(String.format("%s(实施单位)", getTitle()));
        }
    }
    private String getTitle(){
        if (info == null || info.ManagerGrade == null || info.RiverName == null) return "";
       return info.ManagerGrade + ":" + info.RiverName;
    }


    private List<Fragment> fragmentList;
    private List<String> list_Title;


    @Override
    protected int getTitleLayout() {
        return -1;
    }


    public void bottomHide() { //隐藏
        ll_bottom.animate()
                .translationY(ll_bottom.getHeight())//视图translationY属性动态移动到指定值
                .setInterpolator(new AccelerateInterpolator(2))
                .start();
    }

    public void bottomShow() { //显示
        if (riverId == null) {
            return;
        }
        ll_bottom.setVisibility(View.VISIBLE);
        ll_bottom.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
    }


    @OnClick({R2.id.iv_back, R2.id.iv_advance, R2.id.iv_legend})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            vpView.setCurrentItem(vpView.getCurrentItem() - 1);
        } else if (id == R.id.iv_advance) {
            vpView.setCurrentItem(vpView.getCurrentItem() + 1);
        } else if (id == R.id.iv_legend) {
            llLegend.setVisibility(llLegend.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        }
    }


    private String RTown = "", RRivType = "", RLevel = "", Des = "";

    @Override
    public void onStreetClickListener(String name) {
        //所属街道
        RTown = name;
        query();
    }

    @Override
    public void onWaterLeverListener(String name) {
        //水体等级
        RRivType = name;
        query();

    }

    @Override
    public void onManageLevelListener(String name) {
        //管理等级
        RLevel = name;
        query();
    }

    @Override
    public void onSituationListener(String name) {
        //整治情况
        Des = name;
        query();
    }

    private void query() {
        String s = "";
        if (!"".equals(RTown)) {
            s += ("RTown = '" + RTown + "'");
        }
        if (!"".equals(RRivType)) {
            if (!s.equals("")) {
                s += (" AND RRivType = '" + RRivType + "'");
            } else {
                s += ("RRivType = '" + RRivType + "'");
            }
        }
        if (!"".equals(RLevel)) {
            if (!s.equals("")) {
                s += (" AND RLevel = '" + RLevel + "'");
            } else {
                s += ("RLevel = '" + RLevel + "'");
            }
        }
        if (!"".equals(Des)) {
            if (!s.equals("")) {
                s += (" AND Des = '" + Des + "'");
            } else {
                s += ("Des = '" + Des + "'");
            }
        }
//        Log.e("query", s);
        isZoomMap = true;
        searchForAll(s);

    }

    @Override
    public void onSearchListener() {
        type = GIS;
        String s = gis_header_view.getSearchString();
        searchForAll("rname LIKE '%" + s + "%'");
    }


    private void initMap() {
        ArcGISMap map = new ArcGISMap();
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(Constant.url_basemap);
        Basemap basemap = new Basemap(tiledLayer);
        map.setBasemap(basemap);
        mMapView.setMap(map);

        mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        addFeatrueLayer();
        loadGeodatabase();

        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4043726575,none,ZZ0RJAY3FL77P2ELJ156");
        mMapView.setAttributionTextVisible(false);
        map.addDoneLoadingListener(() -> {
            addTitleLayer(Constant.url_city_area);
            addTitleLayer(Constant.url_town_city_area);
        });
        mMapView.setOnTouchListener(new MapTouchListener(getContext(), mMapView));

        map.addLoadStatusChangedListener(loadStatusChangedEvent -> {
            LoadStatus newLoadStatus = loadStatusChangedEvent.getNewLoadStatus();
            if (newLoadStatus == LoadStatus.FAILED_TO_LOAD && !NetworkUtil.isNetworkAvailable(mActivity)) {
                //加载失败
                showError("无网络");
            }
        });
    }
    private void loadGeodatabase() {

        String path =
                Environment.getExternalStorageDirectory() + "/river/map/river.geodatabase";
        final Geodatabase geodatabase = new Geodatabase(path);
        geodatabase.loadAsync();
        geodatabase.addDoneLoadingListener(() -> {
            if (geodatabase.getLoadStatus() == LoadStatus.LOADED) {
                // access the geodatabase's feature table Trailheads
                GeodatabaseFeatureTable geodatabaseFeatureTable = geodatabase.getGeodatabaseFeatureTables().get(0);
                int size = geodatabase.getGeodatabaseFeatureTables().size();
                Log.e("TAG","size="+size);
                geodatabaseFeatureTable.loadAsync();
                FeatureLayer featureLayer = new FeatureLayer(geodatabaseFeatureTable);
                mMapView.getMap().getOperationalLayers().add(featureLayer);
            } else {
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

    private void addFeatrueLayer() {
        table = new ServiceFeatureTable(Constant.url_featrue);
        table.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE);
        featureLayer = new FeatureLayer(table);
        mMapView.getMap().getOperationalLayers().add(featureLayer);
        table.loadAsync();
        table.addDoneLoadingListener(() -> {
            addFeature("RLevel = '市管' ");
//            addFeature("FID >= 0");
        });
    }

    int lever = 1;

    private void addFeature(String whereClause) {
        long l3 = System.currentTimeMillis();
        QueryParameters params = new QueryParameters();
        params.setWhereClause(whereClause);
        List<String> out = new ArrayList<>();
        out.add("*");
        final ListenableFuture<FeatureQueryResult> future = table
                .populateFromServiceAsync(params, false, out);
        future.addDoneListener(() -> {
            zoomMapAndShowRiver(future);
            long l4 = System.currentTimeMillis();
//            Log.e("addFeature" +lever, "l4 = " + l4 + "时差 = " + (l4 - l3));
            if (lever == 1) {
                lever = 2;
                addFeature("RLevel = '区管'");
            } else if (lever == 2) {
                lever = 3;
                addFeature("RLevel = '镇管'");
            } else if (lever == 3) {
                lever = 4;
                addFeature("RLevel = '村级'");
            }

        });
    }


    /**
     * @param feature
     * @param style   SOLID 实线 DASH 虚线
     * @param type    1绘制图片
     */
    private void addGraphic(Feature feature, SimpleLineSymbol.Style style, int type) {

        Map<String, Object> attr = feature.getAttributes();
        String des = (String) attr.get("Des");
        Float posx = (Float) attr.get("posx");
        Float posy = (Float) attr.get("posy");
        String key = posx + ":" + posy;

        SimpleLineSymbol lineSymbol;
        switch (des) {
            case "完成整治":
                lineSymbol = new SimpleLineSymbol(style, Color.parseColor("#00FF00"), 1);
                break;
            case "计划整治":
                lineSymbol = new SimpleLineSymbol(style, Color.parseColor("#FF0000"), 1);
                break;
            case "正在整治":
                lineSymbol = new SimpleLineSymbol(style, Color.parseColor("#EEEE00"), 1);
                break;
            default:
                lineSymbol = new SimpleLineSymbol(style, Color.parseColor("#BEBEBE"), 1);
                break;

        }

        Geometry geometry = feature.getGeometry();
        if (style == SimpleLineSymbol.Style.SOLID) {
            //实线
            if (solidGrahpic != null) {
                mGraphicsOverlay.getGraphics().remove(solidGrahpic);
            }
            solidGrahpic = new Graphic(geometry, lineSymbol);
            mGraphicsOverlay.getGraphics().add(solidGrahpic);
        } else {
            //虚线
            Graphic graphic = new Graphic(geometry, lineSymbol);
            graphicMap.put(graphic, key);
            mGraphicsOverlay.getGraphics().add(graphic);
        }
        if (type == 1) {
            addPic(posx, posy, des, key);
        }
    }

    private void addPic(Float x, Float y, String status, String id) {
        Point point = new Point(x, y);
        //通过APP资源创建picture marker symbol
        BitmapDrawable pinStarBlueDrawable;
        if ("完成整治".equals(status)) {
            //完成改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_green);
        } else if ("正在整治".equals(status)) {
            //正在改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_yellow);
        } else if ("计划整治".equals(status)) {
            //计划改造
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_red);
        } else {
            //未计划
            pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.mipmap.icon_grey);
        }
        final PictureMarkerSymbol pinStarBlueSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
        pinStarBlueSymbol.setHeight(30);
        pinStarBlueSymbol.setWidth(20);
        pinStarBlueSymbol.setOffsetY(
                10); //符号使用的图像周围有一个透明的缓冲区，因此偏移量不仅仅是高度的1/2
        pinStarBlueSymbol.loadAsync();
        pinStarBlueSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (picGraphic != null) {
                    mGraphicsOverlay.getGraphics().remove(picGraphic);
                }
                picGraphic = new Graphic(point, pinStarBlueSymbol);
                graphicMap.put(picGraphic, id + "");
                mGraphicsOverlay.getGraphics().add(picGraphic);
            }
        });
        showPointAtCenter(x, y, 5000);
    }


    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.dispose();
        }
        super.onDestroy();
    }

    /**
     * @param rid  河道的id
     * @param type 是否绘制 图片 0 不绘制 1 绘制
     */
    private void search(final String rid, int type) {

        featureLayer.clearSelection();
        QueryParameters query = new QueryParameters();
        query.setWhereClause("rid = '" + rid + "'");
        List<String> out = new ArrayList<>();
        out.add("*");
        final ListenableFuture<FeatureQueryResult> future = table.populateFromServiceAsync(query, false, out);
        future.addDoneListener(() -> {
            try {
                FeatureQueryResult result = future.get();
                Iterator<Feature> resultIterator = result.iterator();
                if (resultIterator.hasNext()) {
                    Feature feature = resultIterator.next();
                    Envelope envelope = feature.getGeometry().getExtent();
                    mMapView.setViewpointGeometryAsync(envelope, 50);
                    addGraphic(feature, SimpleLineSymbol.Style.SOLID, type);
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void searchForAll(final String queryString) {
        bottomHide();
        cleanAll();
        featureLayer.clearSelection();
        QueryParameters query = new QueryParameters();
        query.setWhereClause(queryString);
        final ListenableFuture<FeatureQueryResult> future = table.queryFeaturesAsync(query);
        future.addDoneListener(() -> zoomMapAndShowRiver(future));
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
        if (picGraphic != null) {
            mGraphicsOverlay.getGraphics().remove(picGraphic);
        }
        if (solidGrahpic != null) {
            mGraphicsOverlay.getGraphics().remove(solidGrahpic);
        }
    }


    /**
     * 地图平移到指定中心点 （54坐标系）
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    private void showPointAtCenter(Float x, Float y, double scale) {
        Point point = new Point(x, y);
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

    private class MapTouchListener extends DefaultMapViewOnTouchListener {

        public MapTouchListener(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isZoomMap = false;
            android.graphics.Point screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
            final Point clickPoint = mMapView.screenToLocation(screenPoint);
            QueryParameters query = new QueryParameters();
            query.setGeometry(clickPoint);
            List<String> out = new ArrayList<>();
            out.add("*");
            final ListenableFuture<FeatureQueryResult> future = table
                    .populateFromServiceAsync(query, false, out);
            future.addDoneListener(() -> {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature feature;
                    int i = 0;
                    while (iterator.hasNext()) {
                        i++;
                        feature = iterator.next();
                        if (iterator.hasNext()) { //
                            continue;
                        }
                        Map<String, Object> attr = feature.getAttributes();
                        Set<String> keys = attr.keySet();
                        for (String key : keys) {
//                            Log.e("rivervalue", attr.get(key) + "===");
                            if (key.equals("rid")) {
                                String value = (String) attr.get(key);
                                setFromAndId(GIS, value);
                                break;
                            }
                        }
                    }
                    if (i == 0) {
                        gis_header_view.hideCondition();
                        bottomHide();
                    }
                } catch (Exception e1) {
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
     * 缩放地图 并且绘制 查询结果内所有的河道
     *
     * @param future
     */
    private void zoomMapAndShowRiver(ListenableFuture<FeatureQueryResult> future) {

        try {
            FeatureQueryResult result = future.get();
            Iterator<Feature> resultIterator = result.iterator();
            double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
            while (resultIterator.hasNext()) {
                Feature feature = resultIterator.next();
                Envelope envelope = feature.getGeometry().getExtent();
                if (x1 > envelope.getXMin()) {
                    x1 = envelope.getXMin();
                }
                if (x2 < envelope.getXMax()) {
                    x2 = envelope.getXMax();
                }
                if (y1 > envelope.getYMin()) {
                    y1 = envelope.getYMin();
                }
                if (y2 < envelope.getYMax()) {
                    y2 = envelope.getYMax();
                }
                addGraphic(feature, SimpleLineSymbol.Style.DASH, 0);
            }
            if (x1 != 0 && isZoomMap) {
                Envelope envelope = new Envelope(x1, y1, x2, y2, mMapView.getSpatialReference());
                mMapView.setViewpointGeometryAsync(envelope, 10);
            }
        } catch (Exception e) {
        }

    }

}
