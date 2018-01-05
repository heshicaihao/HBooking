package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.bean.UserBean;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.common.UserController;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 填写订单
 */
public class FillOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final int ROOM_NUM_REQUEST_CODE = 10001;
    private static final int PERSON_INFO_REQUEST_CODE = 10002;
    private static final int OTHER_DEMANDS_REQUEST_CODE = 10003;
    private static final int VIEW_CHECK_IN_LOGIN = 0;
    private static final int VIEW_CHECK_IN_UNLOGIN = 1;
    private static final int VIEW_BOOKED = 2;
    private int mSign = VIEW_CHECK_IN_LOGIN;

    private ImageView mFloatingActionButton;
    private LinearLayout mRoomNumLL;
    private LinearLayout mCheckInLL;
    private LinearLayout mOtherDemandsLL;
    private LinearLayout mBookedLL;
    private LinearLayout mPriceDetailsLL;
    private LinearLayout mReminderDetailsLL;
    private LinearLayout mCheckInHintLl;
    private LinearLayout mCheckInContentLl;
    private LinearLayout mBookedHintLl;
    private LinearLayout mBookedContentLl;
    private TextView mTitle;
    private TextView mSubmitOrderTv;
    private TextView mRoomNumTv;
    private TextView mOtherDemandsTv;
    private TextView mCheckInNameTv;
    private TextView mCheckInSexTv;
    private TextView mCheckInPhoneTv;
    private TextView mCheckInEmailTv;
    private TextView mBookedNameTv;
    private TextView mBookedSexTv;
    private TextView mBookedPhoneTv;
    private TextView mBookedEmailTv;

    private int mRoomNum = 1;
    private String mCheckInNameStr;
    private boolean mCheckInSex = true;
    private String mCheckInPhoneStr;
    private String mCheckInEmailStr;
    private boolean mIsCheckInSava = false;
    private String mBookedNameStr;
    private boolean mBookedSex = true;
    private String mBookedPhoneStr;
    private String mBookedEmailStr;
    private boolean mIsBookedSava = false;
    private String mOtherDemandsStr = null;
    private String mOrderId;
    private boolean mIsLogin = false;

    private UserBean mUser;
    private JSONArray mCommonOccupancyArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_order);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fill_order_room_info_view:
                gotoFillRoomNum();
                break;

            case R.id.fill_order_check_in_info_view:
                if (mIsLogin) {
                    gotoFillPersonInfo(VIEW_CHECK_IN_LOGIN);
                } else {
                    gotoFillPersonInfo(VIEW_CHECK_IN_UNLOGIN);
                }

                break;

            case R.id.fill_order_other_demands_info_view:
                gotoFillOtherDemands();

                break;

            case R.id.fill_order_booked_info_view:
                gotoFillPersonInfo(VIEW_BOOKED);

                break;

            case R.id.price_details_info_view:
                gotoPriceDetailsActivity(mOrderId);

                break;

            case R.id.reminder_details_view:
                gotoReminderDetailsActivity(mOrderId);

                break;

            case R.id.submit_order:
                gotoOrderPayActivity(mOrderId);

                break;

            case R.id.back:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ROOM_NUM_REQUEST_CODE:
                dealtBackDataRoomNum(resultCode, data);
                break;
            case PERSON_INFO_REQUEST_CODE:
                dealtBackDataPersonInfo(resultCode, data);
                break;
            case OTHER_DEMANDS_REQUEST_CODE:
                dealtBackDataOtherDemands(resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageView) findViewById(R.id.back);
        mSubmitOrderTv = (TextView) findViewById(R.id.submit_order);
        mRoomNumLL = (LinearLayout) findViewById(R.id.fill_order_room_info_view);
        mCheckInLL = (LinearLayout) findViewById(R.id.fill_order_check_in_info_view);
        mOtherDemandsLL = (LinearLayout) findViewById(R.id.fill_order_other_demands_info_view);
        mBookedLL = (LinearLayout) findViewById(R.id.fill_order_booked_info_view);
        mPriceDetailsLL = (LinearLayout) findViewById(R.id.price_details_info_view);
        mReminderDetailsLL = (LinearLayout) findViewById(R.id.reminder_details_view);
        mRoomNumTv = (TextView) findViewById(R.id.room_num);
        mOtherDemandsTv = (TextView) findViewById(R.id.other_demands);
        mCheckInHintLl = (LinearLayout) findViewById(R.id.check_in_hint);
        mCheckInContentLl = (LinearLayout) findViewById(R.id.check_in_content);
        mCheckInNameTv = (TextView) findViewById(R.id.check_in_name);
        mCheckInSexTv = (TextView) findViewById(R.id.check_in_sex);
        mCheckInPhoneTv = (TextView) findViewById(R.id.check_in_phone);
        mCheckInEmailTv = (TextView) findViewById(R.id.check_in_email);

        mBookedHintLl = (LinearLayout) findViewById(R.id.booked_hint);
        mBookedContentLl = (LinearLayout) findViewById(R.id.booked_content);
        mBookedNameTv = (TextView) findViewById(R.id.booked_name);
        mBookedSexTv = (TextView) findViewById(R.id.booked_sex);
        mBookedPhoneTv = (TextView) findViewById(R.id.booked_phone);
        mBookedEmailTv = (TextView) findViewById(R.id.booked_email);


        mTitle.setText(this.getResources().getString(R.string.fill_order));
        setListener();

    }

    private void initData() {
        mUser = UserController.getInstance(this).getUserInfo();
        mIsLogin = mUser.isIs_login();
        mIsLogin = false;
        if (mIsLogin) {
            mCheckInHintLl.setVisibility(View.GONE);
            mCheckInContentLl.setVisibility(View.VISIBLE);
            getUserInfo();
        } else {
            mCheckInHintLl.setVisibility(View.VISIBLE);
            mCheckInContentLl.setVisibility(View.GONE);
        }

    }

    /**
     * 获取会员信息
     */
    private void getUserInfo() {
        String json = AssetsUtils.getJson(this, "user_details_info.txt");
        resolveUserDetailsInfo(json);
    }

    private void resolveUserDetailsInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONObject account_info = result.optJSONObject("account_info");
                mCommonOccupancyArr = result.optJSONArray("common_occupancy");
                JSONArray common_booker = result.optJSONArray("common_booker");
                if (common_booker.length() != 0 && common_booker != null) {
                    JSONObject common_booker_info = (JSONObject) common_booker.get(0);
                    setDefaultBooked(common_booker_info);
                }
                setDefaultCheckIn(account_info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setListener() {
        mFloatingActionButton.setOnClickListener(this);
        mRoomNumLL.setOnClickListener(this);
        mCheckInLL.setOnClickListener(this);
        mOtherDemandsLL.setOnClickListener(this);
        mBookedLL.setOnClickListener(this);
        mPriceDetailsLL.setOnClickListener(this);
        mReminderDetailsLL.setOnClickListener(this);
        mSubmitOrderTv.setOnClickListener(this);
    }

    /**
     * 跳转到 FillPersonInfoActivity
     */
    private void gotoFillPersonInfo(int mSign) {
        Intent intent = new Intent(this, FillPersonInfoActivity.class);
        intent.putExtra("mSign", String.valueOf(mSign));
        switch (mSign) {
            case VIEW_CHECK_IN_LOGIN:
                intent.putExtra("mCheckInNameStr", mCheckInNameStr);
                intent.putExtra("mCheckInSex", mCheckInSex + "");
                intent.putExtra("mCheckInPhoneStr", mCheckInPhoneStr);
                intent.putExtra("mCheckInEmailStr", mCheckInEmailStr);
                intent.putExtra("mIsCheckInSava", mIsCheckInSava + "");
                intent.putExtra("mDataPopupWindow", mCommonOccupancyArr.toString());
                break;

            case VIEW_CHECK_IN_UNLOGIN:
                intent.putExtra("mCheckInNameStr", mCheckInNameStr);
                intent.putExtra("mCheckInSex", mCheckInSex + "");
                intent.putExtra("mCheckInPhoneStr", mCheckInPhoneStr);
                intent.putExtra("mCheckInEmailStr", mCheckInEmailStr);
                intent.putExtra("mIsCheckInSava", mIsCheckInSava + "");
                break;

            case VIEW_BOOKED:
                intent.putExtra("mBookedNameStr", mBookedNameStr);
                intent.putExtra("mBookedSex", mBookedSex+ "");
                intent.putExtra("mBookedPhoneStr", mBookedPhoneStr);
                intent.putExtra("mBookedEmailStr", mBookedEmailStr);
                intent.putExtra("mIsBookedSava", mIsBookedSava + "");
                break;

            default:
                break;
        }
        FillOrderActivity.this.startActivityForResult(intent, PERSON_INFO_REQUEST_CODE);
        AndroidUtils.enterActvityAnim(this);

    }


    /**
     * 跳转到 FillRoomNumActivity
     */
    private void gotoFillOtherDemands() {
        mOtherDemandsStr = mOtherDemandsTv.getText().toString().trim();
        Intent intent = new Intent(this, FillOtherDemandsActivity.class);
        intent.putExtra("other_demands", mOtherDemandsStr);
        FillOrderActivity.this.startActivityForResult(intent, OTHER_DEMANDS_REQUEST_CODE);
        AndroidUtils.enterActvityAnim(this);

    }

    /**
     * 跳转到 FillRoomNumActivity
     */
    private void gotoFillRoomNum() {
        Intent intent = new Intent(this, FillRoomNumActivity.class);
        intent.putExtra("room_num", String.valueOf(mRoomNum));
        FillOrderActivity.this.startActivityForResult(intent, ROOM_NUM_REQUEST_CODE);
        AndroidUtils.enterActvityAnim(this);

    }

    /**
     * 跳转到 OrderPayActivity
     *
     * @param mOrderId
     */
    private void gotoOrderPayActivity(String mOrderId) {
        Intent intent = new Intent(FillOrderActivity.this, OrderPayActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        FillOrderActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(FillOrderActivity.this);
    }

    /**
     * 跳转到 PriceDetailsActivity
     *
     * @param mOrderId
     */
    private void gotoPriceDetailsActivity(String mOrderId) {
        Intent intent = new Intent(FillOrderActivity.this, PriceDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        FillOrderActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(FillOrderActivity.this);
    }

    /**
     * 跳转到 PriceDetailsActivity
     *
     * @param mOrderId
     */
    private void gotoReminderDetailsActivity(String mOrderId) {
        Intent intent = new Intent(FillOrderActivity.this, ReminderDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        FillOrderActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(FillOrderActivity.this);
    }

    /**
     * 处理 更改客房数量返回数据
     *
     * @param resultCode
     * @param data
     */
    private void dealtBackDataRoomNum(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String room_numStr = data.getStringExtra("room_num");
            mRoomNum = Integer.valueOf(room_numStr);
            mRoomNumTv.setText(room_numStr + "间");
        }
    }

    /**
     * 处理 其他要求 返回数据
     *
     * @param resultCode
     * @param data
     */
    private void dealtBackDataOtherDemands(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mOtherDemandsStr = data.getStringExtra("other_demands");
            if (StringUtils.isEmpty(mOtherDemandsStr)) {
                mOtherDemandsStr = MyApplication.getContext().getString(R.string.nothing);
            }
            mOtherDemandsTv.setText(mOtherDemandsStr);
        }
    }

    /**
     * 处理 填写客户信息 返回数据
     *
     * @param resultCode
     * @param intent
     */
    private void dealtBackDataPersonInfo(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String mSignStr = intent.getStringExtra("mSign");
            mSign = Integer.valueOf(mSignStr);
            switch (mSign) {
                case VIEW_CHECK_IN_LOGIN:
                    mCheckInNameStr = intent.getStringExtra("mCheckInNameStr");
                    mCheckInSex = Str2Boolean(intent.getStringExtra("mCheckInSex"));
                    mCheckInPhoneStr = intent.getStringExtra("mCheckInPhoneStr");
                    mCheckInEmailStr = intent.getStringExtra("mCheckInEmailStr");
                    mIsCheckInSava = Boolean.parseBoolean(intent.getStringExtra("mIsCheckInSava"));
                    showCheckInContent();
                    break;

                case VIEW_CHECK_IN_UNLOGIN:
                    mCheckInNameStr = intent.getStringExtra("mCheckInNameStr");
                    mCheckInSex = Str2Boolean(intent.getStringExtra("mCheckInSex"));
                    mCheckInPhoneStr = intent.getStringExtra("mCheckInPhoneStr");
                    mCheckInEmailStr = intent.getStringExtra("mCheckInEmailStr");
                    mIsCheckInSava = Boolean.parseBoolean(intent.getStringExtra("mIsCheckInSava"));
                    showCheckInContent();
                    break;

                case VIEW_BOOKED:
                    mBookedNameStr = intent.getStringExtra("mBookedNameStr");
                    mBookedSex = Str2Boolean(intent.getStringExtra("mBookedSex"));
                    mBookedPhoneStr = intent.getStringExtra("mBookedPhoneStr");
                    mBookedEmailStr = intent.getStringExtra("mBookedEmailStr");
                    mIsBookedSava = Boolean.parseBoolean(intent.getStringExtra("mIsBookedSava"));
                    showBookedContent();
                    break;

                default:

                    break;
            }
        }
    }


    /**
     * 显示入住人信息
     *
     * @param account_info
     */
    private void setDefaultCheckIn(JSONObject account_info) {
        mCheckInNameStr = account_info.optString("name");
        mCheckInSex = account_info.optBoolean("sex");
        mCheckInPhoneStr = account_info.optString("mobile");
        mCheckInEmailStr = account_info.optString("email");

        showCheckInContent();

    }

    /**
     * 设置默认 预订人信息
     *
     * @param account_info
     */
    private void setDefaultBooked(JSONObject account_info) {
        mBookedNameStr = account_info.optString("occupancy_name");
        mBookedSex = account_info.optBoolean("occupancy_sex");
        mBookedPhoneStr = account_info.optString("occupancy_mobile");
        mBookedEmailStr = account_info.optString("occupancy_mail");
    }

    /**
     * 显示预订人信息
     */
    private void showBookedContent() {
        mBookedHintLl.setVisibility(View.GONE);
        mBookedContentLl.setVisibility(View.VISIBLE);
        mBookedNameTv.setText(mBookedNameStr);
        showSex(mBookedSexTv, mBookedSex);
        mBookedPhoneTv.setText(mBookedPhoneStr);
        mBookedEmailTv.setText(mBookedEmailStr);
    }


    /**
     * 显示入住人信息
     */
    private void showCheckInContent() {
        mCheckInHintLl.setVisibility(View.GONE);
        mCheckInContentLl.setVisibility(View.VISIBLE);
        mCheckInNameTv.setText(mCheckInNameStr);
        showSex(mCheckInSexTv, mCheckInSex);
        mCheckInPhoneTv.setText(mCheckInPhoneStr);
        mCheckInEmailTv.setText(mCheckInEmailStr);
    }


    /**
     * 控件显示性别
     *
     * @param view
     * @param sex
     */
    private void showSex(TextView view, boolean sex) {
        if (sex) {
            view.setText(MyApplication.getContext().getString(R.string.man));
        } else {
            view.setText(MyApplication.getContext().getString(R.string.woman));
        }
    }

    /**
     * 将传来的值转回 boolean
     *
     * @param SexStr
     * @return
     */
    private boolean Str2Boolean(String SexStr) {
        if (StringUtils.isEmpty(SexStr)) {
            return true;
        } else {
            return Boolean.parseBoolean(SexStr);
        }
    }

}
