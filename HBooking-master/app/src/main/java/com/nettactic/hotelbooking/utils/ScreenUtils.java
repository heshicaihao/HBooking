package com.nettactic.hotelbooking.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nettactic.hotelbooking.constants.MyConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by heshicaihao on 2017/2/9.
 */

public class ScreenUtils {

    /**
     * 获取手机屏幕 宽度
     *
     * @return width
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        return width;
    }

    /**
     * 获取手机屏幕 高度
     *
     * @return height
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;
        return height;
    }

    /**
     * 获取屏幕信息 保存到SD中
     *
     * @param activity
     */
    public static void getScreenInfo(Activity activity) {
        int screenWidth = getScreenWidth(activity);
        int screenHeight = getScreenHeight(activity);
        String screenData = getScreenData(screenWidth + "",
                screenHeight + "");
        FileUtil.saveFile(screenData, MyConstants.SCREEN,
                MyConstants.SCREEN_INFO, MyConstants.TXT);
    }

    /**
     * 拼屏幕 信息
     *
     * @param screenWidth
     * @param screenHeight
     * @return
     */
    public static String getScreenData(String screenWidth, String screenHeight) {
        JSONObject object = new JSONObject();
        try {
            object.put("screenwidth", screenWidth);
            object.put("screenheight", screenHeight);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = object.toString();
        return data;
    }


    /**
     * 获取屏幕信息 保存到SD中
     */
    public static int getMyScreenWidth() {
        int screenwidth = 0;
        String screen_infoStr = FileUtil.readFile(MyConstants.SCREEN,
                MyConstants.SCREEN_INFO, MyConstants.TXT);
        try {
            JSONObject screen_info = new JSONObject(screen_infoStr);
            String screenwidthStr = screen_info.optString("screenwidth");
            screenwidth = Integer.parseInt(screenwidthStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return screenwidth;

    }

    /**
     * 获取屏幕信息 保存到SD中
     */
    public static int getMyScreenHeight() {
        int screenheight = 0;
        String screen_infoStr = FileUtil.readFile(MyConstants.SCREEN,
                MyConstants.SCREEN_INFO, MyConstants.TXT);
        try {
            JSONObject screen_info = new JSONObject(screen_infoStr);
            String screenwidthStr = screen_info.optString("screenheight");
            screenheight = Integer.parseInt(screenwidthStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return screenheight;

    }


    /**
     * 获取手机屏幕 密度
     *
     * @return height
     */
    public static float getScreenensity(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        return density;
    }


    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     *
     * @param context
     * @return
     */
    public static int getTotalScreenHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getTotalScreenHeight(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 检查虚拟键是否存在
     *
     * @param activity
     * @return
     */
    public static boolean navigationBarExist(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0
                || (realHeight - displayHeight) > 0;
    }


    /**
     * dip转化为px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * px转化为dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }

    /**
     * @param context
     * @param view    控件
     * @param left    左
     * @param top     上
     * @param right   右
     * @param bottom  下
     */
    public static void setMargin(Context context, View view, int left, int top,
                                 int right, int bottom) {
        int leftpx = dip2px(context, left);
        int toppx = dip2px(context, top);
        int rightpx = dip2px(context, right);
        int bottompx = dip2px(context, bottom);
        RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(leftpx, toppx, rightpx, bottompx);
        view.setLayoutParams(mParams);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取标题栏的高度
     *
     * @param activity
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return titleBarHeight;
    }

}
