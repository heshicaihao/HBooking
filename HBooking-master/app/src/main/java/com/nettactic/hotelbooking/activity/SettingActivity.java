package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.net.MyURL;
import com.nettactic.hotelbooking.update.UpdateManager;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

/**
 * 更多信息界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private LinearLayout mCompanyIntroductionLl;
    private LinearLayout mBrandIntroductionLl;
    private LinearLayout mWWCooperationLl;
    private LinearLayout mContactUsLl;
    private LinearLayout mVersionInfoLl;
    private TextView mVersionInfoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mCompanyIntroductionLl = (LinearLayout) findViewById(R.id.company_introduction_ll);
        mBrandIntroductionLl = (LinearLayout) findViewById(R.id.brand_introduction_ll);
        mWWCooperationLl = (LinearLayout) findViewById(R.id.win_win_cooperation_ll);
        mContactUsLl = (LinearLayout) findViewById(R.id.contact_us_ll);
        mVersionInfoLl = (LinearLayout) findViewById(R.id.version_info_ll);
        mVersionInfoValue = (TextView) findViewById(R.id.version_info_value);

        startAlphaAnimation(mFloatingActionButton);
        mTitle.setText(MyApplication.getContext().getString(R.string.setting));
        mVersionInfoValue.setText("V"
                + AndroidUtils.getAppVersionName(getApplicationContext()));
        mFloatingActionButton.setOnClickListener(this);
        mCompanyIntroductionLl.setOnClickListener(this);
        mBrandIntroductionLl.setOnClickListener(this);
        mWWCooperationLl.setOnClickListener(this);
        mContactUsLl.setOnClickListener(this);
        mVersionInfoLl.setOnClickListener(this);

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.company_introduction_ll:
                startOtherWeb(this, this.getString(R.string.company_introduction),
                        MyURL.CONTACT_US_URL);

                break;
            case R.id.brand_introduction_ll:
                startOtherWeb(this, this.getString(R.string.brand_introduction),
                        MyURL.CONTACT_US_URL);

                break;
            case R.id.win_win_cooperation_ll:
                startOtherWeb(this, this.getString(R.string.win_win_cooperation),
                        MyURL.CONTACT_US_URL);

                break;
            case R.id.contact_us_ll:
                startOtherWeb(this, this.getString(R.string.contact_us),
                        MyURL.CONTACT_US_URL);

                break;
            case R.id.version_info_ll:
                onClickUpdate();
                break;
            case R.id.floating_btn:
                mFloatingActionButton.setVisibility(View.GONE);
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
            mFloatingActionButton.setVisibility(View.GONE);
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    /**
     * 检查更新
     */
    private void onClickUpdate() {
        new UpdateManager(this, false);
    }

}
