package com.nettactic.hotelbooking.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.net.MyURL;
import com.nettactic.hotelbooking.utils.ScreenUtils;
import com.nettactic.hotelbooking.utils.StringUtils;

/**
 * 我的界面
 */
public class MeActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton mFloatingActionButton;
    private ImageButton mSettingButton;
    private RelativeLayout mTitleContainer;
    private RelativeLayout mBottomContainer;
    private RelativeLayout mBottomRl;
    private ImageView mTopPic;
    private TextView mMyOrder;
    private TextView mMyPoints;
    private TextView mReviseData;
    private TextView mRevisePassword;
    private LinearLayout mUserCourtesyLl;
    private LinearLayout mKnowClubLl;
    private TextView mCardNumber;
    private TextView mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_me);
        initView();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.my_order:
                mSettingButton.setVisibility(View.GONE);
                startMainActivity(this, MyOrderActivity.class);
                this.finish();

                break;

            case R.id.my_consumption_points:
                mSettingButton.setVisibility(View.GONE);
                startMainActivity(this, MyPointsActivity.class);
                this.finish();

                break;

            case R.id.revise_my_data:
                mFloatingActionButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                startActivity(this, ReviseMyDataActivity.class);
                break;

            case R.id.revise_password:
                mFloatingActionButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                startActivity(this, RevisePasswordActivity.class);
                break;

            case R.id.user_courtesy_ll:
                mFloatingActionButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                startActivity(this, UserCourtesyActivity.class);
                break;

            case R.id.know_club_ll:
                mFloatingActionButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                startOtherWeb(this, this.getString(R.string.know_club),
                        MyURL.CONTACT_US_URL);
                break;

            case R.id.logout_btn:

                break;

            case R.id.floating_btn:
                mSettingButton.setVisibility(View.GONE);
                startMainActivity(this, MainActivity.class);
                this.finish();

                break;

            case R.id.setting_btn:
                mFloatingActionButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                startActivity(this, SettingActivity.class);

                break;

            default:
                break;
        }

    }

    private void initView() {
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mSettingButton = (ImageButton) findViewById(R.id.setting_btn);
        mTitleContainer = (RelativeLayout) findViewById(R.id.title_container);
        mBottomRl = (RelativeLayout) findViewById(R.id.bottom_rl);
        mTopPic = (ImageView) findViewById(R.id.me_top_pic);
        mBottomContainer = (RelativeLayout) findViewById(R.id.bottom_container);

        mMyOrder = (TextView) findViewById(R.id.my_order);
        mMyPoints = (TextView) findViewById(R.id.my_consumption_points);
        mReviseData = (TextView) findViewById(R.id.revise_my_data);
        mRevisePassword = (TextView) findViewById(R.id.revise_password);
        mUserCourtesyLl = (LinearLayout) findViewById(R.id.user_courtesy_ll);
        mKnowClubLl = (LinearLayout) findViewById(R.id.know_club_ll);
        mLogoutBtn = (TextView) findViewById(R.id.logout_btn);

        mCardNumber = (TextView) findViewById(R.id.card_number);

        mSettingButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setVisibility(View.VISIBLE);
        mLogoutBtn.setVisibility(View.VISIBLE);
        mFloatingActionButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
        mMyOrder.setOnClickListener(this);
        mMyPoints.setOnClickListener(this);
        mReviseData.setOnClickListener(this);
        mRevisePassword.setOnClickListener(this);
        mUserCourtesyLl.setOnClickListener(this);
        mKnowClubLl.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFloatingActionButton.setVisibility(View.VISIBLE);
        mSettingButton.setVisibility(View.VISIBLE);

    }

    private void initData() {
        setViewSize();
        String for_card_number = this.getString(R.string.test_card_number);
        String card_number = StringUtils.getFourSpace(for_card_number);
        mCardNumber.setText(card_number);

    }

    private void setViewSize() {
        float sW = ScreenUtils.getScreenWidth(this);
        setView02(mTitleContainer, sW, (198 + 348) / (float) 720 * sW, 0, 0);
        setView(mBottomRl, sW - 2 * 87 / (float) 720 * sW, 348 / (float) 545 * (sW - 2 * 87 / (float) 720 * sW), 87 / (float) 720 * sW, 198 / (float) 720 * sW);
        setView(mBottomContainer, sW - 2 * 87 / (float) 720 * sW, 348 / (float) 545 * (sW - 2 * 87 / (float) 720 * sW), 87 / (float) 720 * sW, 198 / (float) 720 * sW);
        setView(mTopPic, sW, 450 / (float) 720 * sW, 0, 0);

    }


    /**
     * 设置控件 的位置（用于其父布局为RelativeLayout 的View）
     *
     * @param view
     * @param viewHeight
     * @param viewWidth
     * @param y
     * @param x
     */
    private void setView(View view, float viewWidth, float viewHeight, float x, float y) {
        float mInitX = 0;
        float mInitY = 0;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                view.getLayoutParams());
        params.width = (int) Math.ceil(viewWidth);
        params.height = (int) Math.ceil(viewHeight);
        params.leftMargin = (int) (mInitX + x);
        params.topMargin = (int) (mInitY + y);
        view.setLayoutParams(params);
    }

    /**
     * 设置控件 的位置（用于其父布局为LinearLayout 的View）
     *
     * @param view
     * @param viewHeight
     * @param viewWidth
     * @param y
     * @param x
     */
    private void setView02(View view, float viewWidth, float viewHeight, float x, float y) {
        float mInitX = 0;
        float mInitY = 0;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                view.getLayoutParams());
        params.width = (int) Math.ceil(viewWidth);
        params.height = (int) Math.ceil(viewHeight);
        params.leftMargin = (int) (mInitX + x);
        params.topMargin = (int) (mInitY + y);
        view.setLayoutParams(params);
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mSettingButton.setVisibility(View.GONE);
            startMainActivity(this, MainActivity.class);
            this.finish();
        }
        return false;
    }

}
