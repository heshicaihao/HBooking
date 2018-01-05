package com.nettactic.hotelbooking.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.constants.MyConstants;


/***
 * 提示工具类
 *
 * @author heshicaihao
 *
 */
public class ToastUtils {

    private static int yOffset = ScreenUtils.getMyScreenHeight() / 2;
    private static Context context = MyApplication.getContext();
    private static Toast toast = null;

    /**
     * 短 提示语
     *
     * @param resId 资源id
     */
    public static void show(int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);

        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);
        }
        toast.show();

    }


    /**
     * 短 提示语
     *
     * @param resStr 提示语
     */
    public static void show(String resStr) {
        if (toast == null) {
            toast = Toast.makeText(context, resStr,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 短 提示语
     *
     * @param resId 资源id
     */
    public static void shortShow(int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 短 提示语
     *
     * @param resStr 提示语
     */
    public static void shortShow(String resStr) {
        if (toast == null) {
            toast = Toast.makeText(context, resStr,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 长 提示语
     *
     * @param resId 资源id
     */
    public static void longShow(int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(context.getString(resId));
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 长 提示语
     *
     * @param resStr 提示语
     */
    public static void longShow(String resStr) {
        if (toast == null) {
            toast = Toast.makeText(context, resStr,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, yOffset);
            LayoutInflater mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.toast_view, null);
            TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
            setType(toast_tv);
            toast_tv.setText(resStr);
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 设置字体 样式
     *
     * @param toast_tv
     */
    private static void setType(TextView toast_tv) {
        AssetManager assets = context.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(assets, MyConstants.FONTS_PATH);
        toast_tv.setTypeface(fromAsset);
    }
}
