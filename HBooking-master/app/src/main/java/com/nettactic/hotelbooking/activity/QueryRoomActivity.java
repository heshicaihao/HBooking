package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 查询客房
 */
public class QueryRoomActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener, View.OnClickListener {

    private LinearLayout mUserLL;
    private LinearLayout mUnloginLL;
    private TextView mStartDateValue;
    private TextView mEndDateValue;
    private TextView mBookingBt;
    private TextView mLoginTV;
    private TextView mRegisterTV;
    private ImageButton mFloatingActionButton;
    private CalendarPickerView mPickerView;

    //    private int cout = 0;
    private String mTimeInStr;
    private String mTimeOutStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_room);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login:
                startOutsideActivity(this, LoginActivity.class);

                break;

            case R.id.register:
                startOutsideActivity(this, RegisterActivity.class);

                break;

            case R.id.booking_bt:
                gotoRoomTypeList();

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

    private void gotoRoomTypeList() {
        Intent intent = new Intent(QueryRoomActivity.this, RoomTypeListActivity.class);
        intent.putExtra("mTimeInStr", mTimeInStr);
        intent.putExtra("mTimeOutStr", mTimeOutStr);
        this.startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
    }

    private void initView() {
        mStartDateValue = (TextView) findViewById(R.id.start_date_value);
        mEndDateValue = (TextView) findViewById(R.id.end_date_value);
        mPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        mBookingBt = (TextView) findViewById(R.id.booking_bt);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mUserLL = (LinearLayout) findViewById(R.id.user_ll);
        mUnloginLL = (LinearLayout) findViewById(R.id.unlogin_ll);

        mLoginTV = (TextView) findViewById(R.id.login);
        mRegisterTV = (TextView) findViewById(R.id.register);

        mBookingBt.setText(String.
                format(MyApplication.
                        getContext().
                        getString(R.string.booking_one_night), "1"));
        mUserLL.setVisibility(View.GONE);
        mUnloginLL.setVisibility(View.VISIBLE);

        startAlphaAnimation(mFloatingActionButton);
        mPickerView.setOnDateSelectedListener(this);
        mBookingBt.setOnClickListener(this);
        mRegisterTV.setOnClickListener(this);
        mLoginTV.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);

    }

    private void initData() {
        mPickerView.init(new Date(), getDate())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(getDates());

        SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
        Date date_in = new Date(mPickerView.getSelectedDate().getTime());
        //选择了几天
        int daycount = mPickerView.getSelectedDates().size();
        mTimeInStr = sdf.format(date_in);
        mTimeOutStr = sdf.format(mPickerView.getSelectedDates().get(daycount - 1));

        mStartDateValue.setText(mTimeInStr);
        mEndDateValue.setText(mTimeOutStr);

    }

    /**
     * 设置 初始化展示日期
     *
     * @return
     */
    private Date getDate() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.DAY_OF_YEAR, 100);
        return nextYear.getTime();
    }

    /**
     * 设置 初始化选择日期 开始日期 和 结束日期
     *
     * @return
     */
    private ArrayList<Date> getDates() {
        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        today.add(Calendar.DATE, 0);
        dates.add(today.getTime());
        today.add(Calendar.DATE, 1);
        dates.add(today.getTime());
        return dates;
    }

    @Override
    public void onDateSelected(Date date) {

//        mPickerView.setDecorators(Arrays.<CalendarCellDecorator>asList(new TimeInDecorator()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Date date_in = new Date(mPickerView.getSelectedDate().getTime());
        //选择了几天
        int daycount = mPickerView.getSelectedDates().size();
        mTimeInStr = sdf.format(date_in);
        mTimeOutStr = sdf.format(mPickerView.getSelectedDates().get(daycount - 1));

        mStartDateValue.setText(mTimeInStr);
        mEndDateValue.setText(mTimeOutStr);

        int cout = 1;
        if (daycount - 1 == 0) {
            cout = 1;
        } else {
            cout = daycount - 1;
        }
        mBookingBt.setText(String.
                format(MyApplication.
                        getContext().
                        getString(R.string.booking_one_night), "" + cout));

    }

    @Override
    public void onDateUnselected(Date date) {

    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            mFloatingActionButton.setVisibility(View.GONE);
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

}
