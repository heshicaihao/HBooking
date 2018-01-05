package com.nettactic.hotelbooking.amap.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.amap.adapter.BusResultListAdapter;
import com.nettactic.hotelbooking.amap.overlay.RouteOverlay;
import com.nettactic.hotelbooking.amap.overlay.WalkRouteOverlay;
import com.nettactic.hotelbooking.amap.util.AMapUtil;
import com.nettactic.hotelbooking.amap.util.ToastUtil;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class AMapNavigationActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener, AMap.OnMapLoadedListener, View.OnClickListener, AMapNaviListener {

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private int mRouteType = ROUTE_TYPE_DRIVE;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint = new LatLonPoint(22.533984, 114.019262);//终点，39.997796,116.468939
    private TextView mGoToBtn;
    private TextView mRotueTimeDes;
    private TextView mRouteDetailDes;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private WalkRouteOverlay walkRouteOverlay;
    private WalkPath mWalkPath;
    private DrivePath mDrivePath;
    private AMap aMap;
    private LinearLayout mRouteDriveLl;
    private ImageView mRouteDriveIv;
    private TextView mRouteDriveTv;
    private LinearLayout mRouteWalkLl;
    private ImageView mRouteWalkIv;
    private TextView mRouteWalkTv;
    private LinearLayout mRouteBusLl;
    private ImageView mRouteBusIv;
    private TextView mRouteBusTv;
    private int deText = R.color.order_text_color;
    private int peText = R.color.black;
    private String mCurrentCityName = null;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private LinearLayout mBusResultLayout;
    private ListView mBusResultList;
    private LinearLayout mMiddleAmapView;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 开始点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    private NaviLatLng endLatlng = new NaviLatLng(22.533984, 114.019262);
    //    private NaviLatLng startLatlng = new NaviLatLng(23.533984, 115.019262);
    private NaviLatLng startLatlng = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double Latitude = amapLocation.getLatitude();//获取纬度
                    double Longitude = amapLocation.getLongitude();//获取经度
                    String Address = amapLocation.getAddress();
                    mCurrentCityName = amapLocation.getAdCode();
                    mStartPoint = new LatLonPoint(Latitude, Longitude);
                    startLatlng = new NaviLatLng(Latitude, Longitude);
                    searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                    startList.add(startLatlng);
                    calculateDriveRoute();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    ToastUtils.show("location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());

                }
            }
        }
    };
    /**
     * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
     * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
     */
    private int strategyFlag = 0;
    private RouteOverLay mRouteOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHead();
        setContentView(R.layout.activity_amap_navigation);

        initView();
        initAMap(savedInstanceState);
        initNavi();

    }

    /**
     * 导航初始化
     */
    private void initNavi() {
//        startList.add(startLatlng);
        endList.add(endLatlng);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
    }

    private void initView() {
        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.route_map);
        mGoToBtn = (TextView) findViewById(R.id.go_to_btn);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

        mRouteDriveLl = (LinearLayout) findViewById(R.id.route_drive);
        mRouteDriveIv = (ImageView) findViewById(R.id.route_drive_iv);
        mRouteDriveTv = (TextView) findViewById(R.id.route_drive_tv);

        mRouteWalkLl = (LinearLayout) findViewById(R.id.route_walk);
        mRouteWalkIv = (ImageView) findViewById(R.id.route_walk_iv);
        mRouteWalkTv = (TextView) findViewById(R.id.route_walk_tv);

        mRouteBusLl = (LinearLayout) findViewById(R.id.route_bus);
        mRouteBusIv = (ImageView) findViewById(R.id.route_bus_iv);
        mRouteBusTv = (TextView) findViewById(R.id.route_bus_tv);

        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mBusResultList = (ListView) findViewById(R.id.bus_result_list);
        mMiddleAmapView = (LinearLayout) findViewById(R.id.middle_amap_view);

        showProgressDialog();
        mapView.setVisibility(View.GONE);
        changButUI(mRouteType);
        mGoToBtn.setOnClickListener(this);
        mRouteDriveLl.setOnClickListener(this);
        mRouteWalkLl.setOnClickListener(this);
        mRouteBusLl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.route_drive:
                mRouteType = ROUTE_TYPE_DRIVE;
                changButUI(mRouteType);
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                mapView.setVisibility(View.VISIBLE);
                mMiddleAmapView.setVisibility(View.VISIBLE);
                mBusResultLayout.setVisibility(View.GONE);
                break;

            case R.id.route_walk:
                mRouteType = ROUTE_TYPE_WALK;
                changButUI(mRouteType);
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WALK_DEFAULT);
                mapView.setVisibility(View.VISIBLE);
                mMiddleAmapView.setVisibility(View.VISIBLE);
                mBusResultLayout.setVisibility(View.GONE);
                break;

            case R.id.route_bus:
                mRouteType = ROUTE_TYPE_BUS;
                changButUI(mRouteType);
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
                mapView.setVisibility(View.GONE);
                mMiddleAmapView.setVisibility(View.GONE);
                mBusResultLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.go_to_btn:
                if (mRouteType == ROUTE_TYPE_WALK) {
//                    Intent intent = new Intent(mContext,
//                            AMNWalkDetailActivity.class);
//                    intent.putExtra("walk_path", mWalkPath);
//                    intent.putExtra("walk_result",
//                            mWalkRouteResult);
//                    startActivity(intent);
                    startNavi();
                } else if (mRouteType == ROUTE_TYPE_DRIVE) {
                    startNavi();
//                    Intent intent = new Intent(mContext,
//                            AMNDriveDetailActivity.class);
//                    intent.putExtra("drive_path", mDrivePath);
//                    intent.putExtra("drive_result",
//                            mDriveRouteResult);
//                    startActivity(intent);
                }

                break;

            default:
                break;
        }

    }


    /**
     * @param savedInstanceState
     */
    private void initAMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        initLocation();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        aMap.setOnMapClickListener(AMapNavigationActivity.this);
        aMap.setOnMarkerClickListener(AMapNavigationActivity.this);
        aMap.setOnInfoWindowClickListener(AMapNavigationActivity.this);
        aMap.setInfoWindowAdapter(AMapNavigationActivity.this);
        aMap.setOnMapLoadedListener(this);

    }

    @Override
    public void onCalculateRouteSuccess() {
        cleanRouteOverlay();
        AMapNaviPath path = mAMapNavi.getNaviPath();
//        if (path != null) {
//            drawRoutes(path);
//        }
//        mStartNaviButton.setVisibility(View.VISIBLE);
    }

    private void cleanRouteOverlay() {
        if (mRouteOverlay != null) {
            mRouteOverlay.removeFromMap();
            mRouteOverlay.destroy();
        }
    }

    /**
     * 绘制路径规划结果
     *
     * @param path AMapNaviPath
     */
    private void drawRoutes(AMapNaviPath path) {
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        mRouteOverlay = new RouteOverLay(aMap, path, this);
        mRouteOverlay.addToMap();
        mRouteOverlay.zoomToSpan();
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 设置头部透明
     */
    private void setHead() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }


    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
//        mGoToBtn.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    mDrivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    RouteOverlay.DrivingRouteOverlay drivingRouteOverlay = new RouteOverlay.DrivingRouteOverlay(
                            mContext, aMap, mDrivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
//                    mGoToBtn.setVisibility(View.VISIBLE);
                    int dis = (int) mDrivePath.getDistance();
                    int dur = (int) mDrivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");


                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    mWalkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    if (walkRouteOverlay != null) {
                        walkRouteOverlay.removeFromMap();
                    }
                    walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, mWalkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    int dis = (int) mWalkPath.getDistance();
                    int dur = (int) mWalkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mGoToBtn.setOnClickListener(this);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            mapView.setVisibility(View.VISIBLE);
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapLoaded() {
//        searchRouteResult(ROUTE_TYPE_WALK);
        calculateDriveRoute();
    }

    /**
     * 开始导航
     */
    private void startNavi() {
        Intent gpsintent = new Intent(getApplicationContext(), AMapTTSRouteNaviActivity.class);
        gpsintent.putExtra("gps", false); // gps 为true为真实导航，为false为模拟导航
        startActivity(gpsintent);
    }

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute() {
        try {
            strategyFlag = mAMapNavi.strategyConvert(true, false, false, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mRouteType == ROUTE_TYPE_DRIVE){
            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
        }else if (mRouteType == ROUTE_TYPE_WALK){
            mAMapNavi.calculateWalkRoute(startLatlng,endLatlng);
        }

    }

    /**
     * 设置UI
     *
     * @param mSign
     */
    private void changButUI(int mSign) {
        mRouteBusTv.setTextColor(getResources().getColor(deText));
        mRouteBusIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_bus_normal));

        mRouteDriveTv.setTextColor(getResources().getColor(deText));
        mRouteDriveIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_drive_normal));

        mRouteWalkTv.setTextColor(getResources().getColor(deText));
        mRouteWalkIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_walk_normal));

        switch (mSign) {
            case ROUTE_TYPE_BUS:
                mRouteBusTv.setTextColor(getResources().getColor(peText));
                mRouteBusIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_bus_pressed));

                break;

            case ROUTE_TYPE_DRIVE:
                mRouteDriveTv.setTextColor(getResources().getColor(peText));
                mRouteDriveIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_drive_pressed));

                break;

            case ROUTE_TYPE_WALK:
                mRouteWalkTv.setTextColor(getResources().getColor(peText));
                mRouteWalkIv.setImageDrawable(getResources().getDrawable(R.mipmap.route_walk_pressed));

                break;

            default:
                break;

        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }
}
