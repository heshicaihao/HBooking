package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSeeOrderTv;

    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mSeeOrderTv = (TextView) findViewById(R.id.see_order);

        mSeeOrderTv.setOnClickListener(this);

    }

    private void initData() {

    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_order:
                gotoOrderDetailsActivity();
                PaySuccessActivity.this.finish();

                break;
            default:
                break;
        }
    }

    /**
     * 跳转到 OrderDetailsActivity
     */
    private void gotoOrderDetailsActivity() {
        Intent intent = new Intent(PaySuccessActivity.this, OrderDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        PaySuccessActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(PaySuccessActivity.this);
    }

}
