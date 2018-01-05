package com.nettactic.hotelbooking.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nettactic.hotelbooking.activity.OtherWebActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;


/**
 * Created by heshicaihao on 2017/1/21.
 */
public abstract class BaseFragment extends Fragment {
    public String TAG = getClass().getName();

    protected LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 打开H5界面
     *
     * @param context
     */
    public void startOtherWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, OtherWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void startActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());

    }






}
