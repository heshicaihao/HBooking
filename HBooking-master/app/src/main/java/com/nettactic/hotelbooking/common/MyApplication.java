package com.nettactic.hotelbooking.common;

import android.app.Application;
import android.content.Context;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.constants.MyConstants;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by heshicaihao on 2017/1/21.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath(MyConstants.FONTS_PATH).setFontAttrId(R.attr.fontPath).build());

//        initData();

    }

    /**
     * 获得当前app运行的Context
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

}
