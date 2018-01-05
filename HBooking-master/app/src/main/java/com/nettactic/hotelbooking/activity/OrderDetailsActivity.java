package com.nettactic.hotelbooking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.amap.activity.AMapLocationActivity;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.dialog.OrderCustomDialog;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {


    private ImageButton mFloatingActionButton;
    private LinearLayout mPriceDetailsView;
    private LinearLayout mReminderDetailsView;
    private TextView mTitle;
    private TextView mOrderIdValue;
    private TextView mGotoBuyBtn;
    private TextView mCancelBtn;
    private TextView mStatus;
    private TextView mHotelNameTv;
    private TextView mHotelPhoneTv;
    private TextView mHotelAddressTv;
    private ImageView mHotelAddressIv;
    private TextView mTimeTv;
    private TextView mMuchDayTv;
    private TextView mRoomTypeTv;
    private TextView mRoomNumTv;
    private TextView mOtherDemandsTv;
    private TextView mReminderTv;
    private TextView mCheckInNameTv;
    private TextView mCheckInSexTv;
    private TextView mCheckInPhoneTv;
    private TextView mCheckInEmailTv;
    private TextView mTimeAndNumTv;
    private TextView mRoomChargeTv;
    private TextView mTaxationChargeTv;
    private TextView mTotalChargeTv;

    private String mOrderId;
    private String mCallNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mPriceDetailsView = (LinearLayout) findViewById(R.id.price_details_info_view);
        mReminderDetailsView = (LinearLayout) findViewById(R.id.reminder_details_view);

        mTitle = (TextView) findViewById(R.id.title);
        mOrderIdValue = (TextView) findViewById(R.id.order_id_value);
        mStatus = (TextView) findViewById(R.id.status);
        mGotoBuyBtn = (TextView) findViewById(R.id.goto_buy_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);

        mHotelNameTv = (TextView) findViewById(R.id.hotel_name);
        mHotelPhoneTv = (TextView) findViewById(R.id.hotel_phone_tv);
        mHotelAddressIv = (ImageView) findViewById(R.id.hotel_address_img);
        mHotelAddressTv = (TextView) findViewById(R.id.hotel_address_tv);

        mTimeTv = (TextView) findViewById(R.id.time_in_and_time_out);
        mMuchDayTv = (TextView) findViewById(R.id.much_time);
        mRoomTypeTv = (TextView) findViewById(R.id.room_type);
        mRoomNumTv = (TextView) findViewById(R.id.room_num);
        mOtherDemandsTv = (TextView) findViewById(R.id.other_demands);
        mReminderTv = (TextView) findViewById(R.id.reminder);

        mCheckInNameTv = (TextView) findViewById(R.id.check_in_name);
        mCheckInSexTv = (TextView) findViewById(R.id.check_in_sex);
        mCheckInPhoneTv = (TextView) findViewById(R.id.check_in_phone);
        mCheckInEmailTv = (TextView) findViewById(R.id.check_in_email);

        mTimeAndNumTv = (TextView) findViewById(R.id.time_and_num);
        mRoomChargeTv = (TextView) findViewById(R.id.room_charge);
        mTaxationChargeTv = (TextView) findViewById(R.id.taxation_charge);
        mTotalChargeTv = (TextView) findViewById(R.id.total_charge);

        startAlphaAnimation(mFloatingActionButton);
        mFloatingActionButton.setOnClickListener(this);
        mGotoBuyBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mPriceDetailsView.setOnClickListener(this);
        mReminderDetailsView.setOnClickListener(this);
        mHotelPhoneTv.setOnClickListener(this);
        mHotelAddressIv.setOnClickListener(this);

    }

    private void initData() {
        getIntentData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.hotel_address_img:
                startActivity(this, AMapLocationActivity.class);

                break;

            case R.id.hotel_phone_tv:
                telCall(mCallNum);

                break;

            case R.id.reminder_details_view:
                gotoReminderDetailsActivity(mOrderId);

                break;

            case R.id.price_details_info_view:
                gotoPriceDetailsActivity(mOrderId);

                break;

            case R.id.goto_buy_btn:
                gotoOrderPayActivity(mOrderId);

                break;
            case R.id.cancel_btn:
                showDialog();

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
            this.finish();
            mFloatingActionButton.setVisibility(View.GONE);
            AndroidUtils.exitActvityAnim(this);

        }

        return false;

    }

    /**
     * 从Intent获取数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("mOrderId");
        String json = AssetsUtils.getJson(this, "order_details.txt");
        resolveOrderDetails(json);


    }

    /**
     * 解析
     *
     * @param json
     */
    private void resolveOrderDetails(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONObject orderData = result.optJSONObject("orderData");
                setTitleView(orderData);
                setHotelInfoView(orderData);
                setCheckInView(orderData);
                setPriceView(orderData);
                setOtherView(orderData);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到 OrderPayActivity
     *
     * @param mOrderId
     */
    private void gotoOrderPayActivity(String mOrderId) {
        Intent intent = new Intent(OrderDetailsActivity.this, OrderPayActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        OrderDetailsActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(OrderDetailsActivity.this);
    }

    /**
     * 跳转到 PriceDetailsActivity
     *
     * @param mOrderId
     */
    private void gotoPriceDetailsActivity(String mOrderId) {
        Intent intent = new Intent(OrderDetailsActivity.this, PriceDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        OrderDetailsActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(OrderDetailsActivity.this);
    }

    /**
     * 跳转到 PriceDetailsActivity
     *
     * @param mOrderId
     */
    private void gotoReminderDetailsActivity(String mOrderId) {
        Intent intent = new Intent(OrderDetailsActivity.this, ReminderDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        OrderDetailsActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(OrderDetailsActivity.this);
    }

    /**
     * 显示对话框
     */
    private void showDialog() {
        OrderCustomDialog.Builder builder = new OrderCustomDialog.Builder(OrderDetailsActivity.this);
        builder.setMessage(this.getString(R.string.confirm_cancel_order));
        builder.setPositiveButton(this.getString(R.string.go_back),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (AndroidUtils.isNoFastClick()) {
                            dialog.dismiss();

                        }
                    }
                });

        builder.setNegativeButton(this.getString(R.string.ok),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (AndroidUtils.isNoFastClick()) {
                            dialog.dismiss();
//                            netCancelOrder(position);
                        }
                    }
                });

        builder.create().show();
    }


    private void setTitleView(JSONObject result) {
        mOrderId = result.optString("order_id");
        mOrderIdValue.setText(mOrderId);
        String order_status = result.optString("order_status");
        String pay_status = result.optString("pay_status");
        String occupancy_status = result.optString("occupancy_status");
        if ("3".equals(order_status)) {
            mTitle.setText(this.getResources().getString(R.string.canceled_order));
            mStatus.setText(this.getResources().getString(R.string.cancelled));
            mCancelBtn.setVisibility(View.GONE);
        } else {
            if ("0".equals(occupancy_status)) {
                mTitle.setText(this.getResources().getString(R.string.checked_order));
                mStatus.setText(this.getResources().getString(R.string.checked));
                mCancelBtn.setVisibility(View.GONE);
            } else if ("1".equals(occupancy_status)) {
                mTitle.setText(this.getResources().getString(R.string.no_checke_order));
                mStatus.setText(this.getResources().getString(R.string.no_checke));
                mCancelBtn.setVisibility(View.VISIBLE);
            }
            if ("0".equals(pay_status)) {
                mGotoBuyBtn.setVisibility(View.VISIBLE);
                mCancelBtn.setVisibility(View.VISIBLE);
            } else if ("1".equals(pay_status)) {
                mGotoBuyBtn.setVisibility(View.GONE);
                mCancelBtn.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置酒店信息 数值
     *
     * @param result
     */
    private void setHotelInfoView(JSONObject result) {
        JSONObject hotel_info = result.optJSONObject("hotel_info");
        String hotel_name = hotel_info.optString("hotel_name");
        mHotelNameTv.setText(hotel_name);
        String hotel_adr = hotel_info.optString("hotel_adr");
        mHotelAddressTv.setText(hotel_adr);
        mCallNum = hotel_info.optString("hotel_tel");
        mHotelPhoneTv.setText(mCallNum);
        String hotel_locate_pic = hotel_info.optString("hotel_locate_pic");
        Glide.with(this).load(hotel_locate_pic).into(mHotelAddressIv);
    }

    /**
     * 设置其他 数值
     *
     * @param result
     */
    private void setOtherView(JSONObject result) {
        String arrival_date = result.optString("arrival_date");
        String departure_date = result.optString("departure_date");
        long time_in = Long.parseLong(arrival_date);
        String time_inOK = StringUtils.longToDate(time_in);
        long time_out = Long.parseLong(departure_date);
        String time_outOK = StringUtils.longToDate(time_out);
        String time_in_and_time_outStr = time_inOK + " 至 " + time_outOK;
        mTimeTv.setText(time_in_and_time_outStr);
        String room_name = result.optString("room_name");
        mRoomTypeTv.setText(room_name);
        String room_num = result.optString("room_num");
        mRoomNumTv.setText(room_num + "间");
        String room_remark = result.optString("room_remark");
        mOtherDemandsTv.setText(room_remark);
        String room_tip = result.optString("room_tip");
        mReminderTv.setText(room_tip);
        JSONObject price_info = result.optJSONObject("price_info");
        String days = price_info.optString("days");
        mMuchDayTv.setText("共" + days + "晚");

    }

    /**
     * 设置入住信息 数值
     *
     * @param result
     */
    private void setCheckInView(JSONObject result) {
        JSONObject occupancy_info = result.optJSONObject("occupancy_info");
        String name = occupancy_info.optString("name");
        mCheckInNameTv.setText(name);
        String sex = occupancy_info.optString("sex");
        boolean mCheckInSex = Str2Boolean(sex);
        showSex(mCheckInSexTv, mCheckInSex);
        String mobile = occupancy_info.optString("mobile");
        mCheckInPhoneTv.setText(mobile);
        String mail = occupancy_info.optString("mail");
        mCheckInEmailTv.setText(mail);

    }


    /**
     * 设置价格信息 数值
     *
     * @param result
     */
    private void setPriceView(JSONObject result) {
        JSONObject price_info = result.optJSONObject("price_info");
        String days = price_info.optString("days");
        String rooms = price_info.optString("rooms");
        String time_and_numStr = days + "晚，" + rooms + "间";
        mTimeAndNumTv.setText(time_and_numStr);
        String rooms_total = price_info.optString("rooms_total");
        mRoomChargeTv.setText("¥" + rooms_total);
        String service_amount = price_info.optString("service_amount");
        mTaxationChargeTv.setText("¥" + service_amount);
        String total = price_info.optString("total");
        mTotalChargeTv.setText("¥" + total);

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

}
