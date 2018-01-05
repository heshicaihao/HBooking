package com.nettactic.hotelbooking.amap.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;

/**
 * 地图信息
 */
public class AMapLocationActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private MapView mapView;
    private AMap aMap;
    private ImageButton mFloatingBt;

    private LinearLayout.LayoutParams mParams;
    private RelativeLayout mContainerLayout;
    private LatLng centerPoint = new LatLng(22.533984, 114.019262);// 深航酒店 经纬度
    private UiSettings mUiSettings;//定义一个UiSettings对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHead();
        setContentView(R.layout.activity_amap_location);

        initView();
        initAMap(savedInstanceState);

    }

    private void initView() {
        mContainerLayout = (RelativeLayout) findViewById(R.id.amap_initialcenter);
        mFloatingBt = (ImageButton) findViewById(R.id.floating_btn);
        mFloatingBt.setOnClickListener(this);

    }

    /**
     * 设置地图
     *
     * @param savedInstanceState
     */
    private void initAMap(Bundle savedInstanceState) {
        AMapOptions aOptions = new AMapOptions();
        aOptions.camera(new CameraPosition(centerPoint, 18f, 0, 0));
        mapView = new MapView(this, aOptions);
        mapView.onCreate(savedInstanceState);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.addView(mapView, mParams);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        addGrowMarker();
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

    /**
     * 添加带生长效果marker
     */
    private void addGrowMarker() {
        MarkerOptions options = new MarkerOptions();
        options.position(centerPoint);
        View view = View.inflate(this, R.layout.view_amap_custom, null);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(view);
        options.icon(markerIcon);
        aMap.addMarker(options);
        aMap.setOnMarkerClickListener(this);

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

    /**
     * markerClick回调启动marker动画
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        gotoNavigation();
        return true;
    }

    private void gotoNavigation() {
        Intent intent = new Intent(AMapLocationActivity.this, AMapNavigationActivity.class);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_btn:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:
                break;
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

}
