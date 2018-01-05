package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.HotelInfoListViewAdapter;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.LogUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 酒店信息
 */
public class HotelInfoActivity extends BaseActivity implements View.OnClickListener {

    private View mHeader;
    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private ListView mListView;
    private TextView mIntroduce;
    private TextView mMoreBtn;
    private HotelInfoListViewAdapter mAdapter;
    private String mShortIntroduceStr;
    private String mLongIntroduceStr;
    private int mCout = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mListView = (ListView) findViewById(R.id.listview);
        mHeader = this.getLayoutInflater().inflate(
                R.layout.header_hotel_info_list, null);
        mIntroduce = (TextView) mHeader.findViewById(R.id.introduce);
        mMoreBtn = (TextView) mHeader.findViewById(R.id.more_btn);

        startAlphaAnimation(mFloatingActionButton);
        mTitle.setText(MyApplication.getContext().getString(R.string.hotel_info));
        mListView.addHeaderView(mHeader);
        mFloatingActionButton.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);
    }

    private void initData() {
        String json = AssetsUtils.getJson(this, "hotel_info.txt");
        resolveHotelInfo(json);
    }

    private void resolveHotelInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                LogUtils.logd("result:",result.toString());
                mShortIntroduceStr = result.optString("hotel_intro_short");
                LogUtils.logd("mShortIntroduceStr:",mShortIntroduceStr);
                mLongIntroduceStr = result.optString("hotel_intro_long");
                LogUtils.logd("mLongIntroduceStr:",mLongIntroduceStr);
                mIntroduce.setText(mShortIntroduceStr);
                mMoreBtn.setText(MyApplication.getContext().getString(R.string.see_more));
                JSONArray hotel_info = result.optJSONArray("hotel_info");
                setListAdapter(hotel_info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListAdapter(JSONArray hotel_room) {
        mAdapter = new HotelInfoListViewAdapter(HotelInfoActivity.this,this, hotel_room);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.more_btn:
                mCout++;
                if (mCout % 2 == 1) {
                    mIntroduce.setText(mLongIntroduceStr);
                    mMoreBtn.setText(MyApplication.getContext().getString(R.string.show_less));
                } else {
                    mIntroduce.setText(mShortIntroduceStr);
                    mMoreBtn.setText(MyApplication.getContext().getString(R.string.see_more));
                }

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


}
