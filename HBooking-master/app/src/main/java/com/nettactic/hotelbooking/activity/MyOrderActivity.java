package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.fragment.CanceledFragment;
import com.nettactic.hotelbooking.fragment.CheckedFragment;
import com.nettactic.hotelbooking.fragment.NoCheckeFragment;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final int VIEW_NOCHECKE = 0;
    private static final int VIEW_CHECKED = 1;
    private static final int VIEW_CANCELED = 2;
    private int mSign = VIEW_NOCHECKE;    private int deText = R.color.order_text_color;
    private int peText = R.color.black;

    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private TextView mNoChecke;
    private TextView mChecked;
    private TextView mCanceled;
    private NoCheckeFragment mNoCheckeFragment;
    private CheckedFragment mCheckedFragment;
    private CanceledFragment mCanceledFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mNoChecke = (TextView) findViewById(R.id.no_checke);
        mChecked = (TextView) findViewById(R.id.checked);
        mCanceled = (TextView) findViewById(R.id.canceled);

        mTitle.setText(this.getResources().getString(R.string.my_booking));
        mNoChecke.setOnClickListener(this);
        mChecked.setOnClickListener(this);
        mCanceled.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);

    }

    private void initData() {
        changButUI(mSign);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.no_checke:
                mSign = VIEW_NOCHECKE;
                changButUI(mSign);

                break;
            case R.id.checked:
                mSign = VIEW_CHECKED;
                changButUI(mSign);

                break;
            case R.id.canceled:
                mSign = VIEW_CANCELED;
                changButUI(mSign);

                break;
            case R.id.floating_btn:
                startMainActivity(this, MyPointsActivity.class);
                this.finish();

                break;

            default:
                break;
        }

    }

    private void changButUI(int mSign) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        mNoChecke.setTextColor(getResources().getColor(deText));
        mChecked.setTextColor(getResources().getColor(deText));
        mCanceled.setTextColor(getResources().getColor(deText));

        switch (mSign) {
            case VIEW_NOCHECKE:
                if (mNoCheckeFragment == null) {
                    mNoCheckeFragment = new NoCheckeFragment();
                }
                transaction.replace(R.id.fragment_content, mNoCheckeFragment);
                mNoChecke.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_CHECKED:
                if (mCheckedFragment == null) {
                    mCheckedFragment = new CheckedFragment();
                }
                transaction.replace(R.id.fragment_content, mCheckedFragment);
                mChecked.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_CANCELED:
                if (mCanceledFragment == null) {
                    mCanceledFragment = new CanceledFragment();
                }
                transaction.replace(R.id.fragment_content, mCanceledFragment);
                mCanceled.setTextColor(getResources().getColor(peText));

                break;
            default:
                break;

        }
        transaction.commit();
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startMainActivity(this, MainActivity.class);
            this.finish();
        }
        return false;
    }


}
