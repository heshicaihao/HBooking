package com.nettactic.hotelbooking;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nettactic.hotelbooking.activity.MyOrderActivity;
import com.nettactic.hotelbooking.activity.QueryRoomActivity;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.constants.MyConstants;
import com.nettactic.hotelbooking.net.AsyncCallBack;
import com.nettactic.hotelbooking.net.NetHelper;
import com.nettactic.hotelbooking.update.UpdateManager;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.FileUtil;
import com.nettactic.hotelbooking.utils.LogUtils;
import com.nettactic.hotelbooking.utils.ScreenUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static Boolean mIsExit = false;
    public String TAG = getClass().getName();
    private ImageButton mFloatingActionButton;
    private TextView mBookingHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_btn:
                startMainActivity(this, MyOrderActivity.class);
                this.finish();

                break;

            case R.id.booking_hotel:
                startActivity(this, QueryRoomActivity.class);

                break;

            default:
                break;

        }

    }

    private void gotoMyBooking() {
        Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
        this.startActivity(intent);
        AndroidUtils.enterAnimMain(this);
        this.finish();
    }

    private void initData() {
        ScreenUtils.getScreenInfo(this);
        String area_dataPath = FileUtil.getFilePath(MyConstants.AREA_DATA_DIR,
                MyConstants.AREA_DATA, MyConstants.TXT);
        if (!FileUtil.fileIsExists(area_dataPath)) {
            getAreaData();
        }
        onClickUpdate();
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    private void initView() {
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mBookingHotel = (TextView) findViewById(R.id.booking_hotel);

        mFloatingActionButton.setOnClickListener(this);
        mBookingHotel.setOnClickListener(this);

    }

    /**
     * 自动检查更新App
     */
    private void onClickUpdate() {
        new UpdateManager(this, true);
    }

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (mIsExit == false) {
            mIsExit = true; // 准备退出
            String show = this.getResources().getString(R.string.tip_double_click_exit)
                    + this.getResources().getString(R.string.app_name);
            ToastUtils.show(show);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            System.exit(0);
        }
    }

    /**
     * 获取地址信息
     */
    private void getAreaData() {
        NetHelper.getAreaInfo(new AsyncCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(String result) {
                resolveAreaData(result);
            }
        });

    }

    /**
     * 解析地址信息
     *
     * @param json
     */
    private void resolveAreaData(String json) {
        try {
            LogUtils.logd(TAG, "resolveAreaDatajson:" + json);
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONArray areadata = obj.optJSONArray("result");
                FileUtil.saveFile(areadata.toString(),
                        MyConstants.AREA_DATA_DIR, MyConstants.AREA_DATA,
                        MyConstants.TXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
