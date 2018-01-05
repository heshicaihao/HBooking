package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

public class OrderPayActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private LinearLayout mAliPayLl;
    private LinearLayout mWechatPayLl;
    private LinearLayout mReceptionPayLl;
    private TextView mSeeOrderTv;

    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.see_order:
                gotoOrderDetailsActivity();
                OrderPayActivity.this.finish();
                break;

            case R.id.ali_pay_ll:

                break;

            case R.id.wechat_pay_ll:

                break;

            case R.id.reception_pay_ll:
                startActivity(this, PaySuccessActivity.class);
                OrderPayActivity.this.finish();
                break;

            default:
                break;
        }
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mSeeOrderTv = (TextView) findViewById(R.id.see_order);
        mAliPayLl = (LinearLayout) findViewById(R.id.ali_pay_ll);
        mWechatPayLl = (LinearLayout) findViewById(R.id.wechat_pay_ll);
        mReceptionPayLl = (LinearLayout) findViewById(R.id.reception_pay_ll);

        mTitle.setText(this.getResources().getString(R.string.order_pay));
        mSeeOrderTv.setOnClickListener(this);
        mAliPayLl.setOnClickListener(this);
        mWechatPayLl.setOnClickListener(this);
        mReceptionPayLl.setOnClickListener(this);

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

    /**
     * 跳转到 OrderDetailsActivity
     */
    private void gotoOrderDetailsActivity() {
        Intent intent = new Intent(OrderPayActivity.this, OrderDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        OrderPayActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(OrderPayActivity.this);
    }

}
