package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelInfoChildActivity extends BaseActivity implements View.OnClickListener {

    private String child_id;
    private int mType;

    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info_child);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initData() {
        getIntentData();
        String json = AssetsUtils.getJson(this, "child_hotel_info.txt");
        resolveHotelInfo(json);

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

    private void resolveHotelInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONArray data = result.optJSONArray("hotel_info");
                setEListAdapter(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setEListAdapter(JSONArray data) {
        setMyELVAdapter(this,data,mListView, mType);
        int groupCount = mListView.getCount();
        if (mType!=4){
            for (int i = 0; i < groupCount; i++) {
                mListView.expandGroup(i);
            }
        }

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mListView = (ExpandableListView) findViewById(R.id.e_listview);

        startAlphaAnimation(mFloatingActionButton);
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.floating_btn:
                mFloatingActionButton.setVisibility(View.GONE);
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:
                break;
        }
    }

    /**
     * 从Intent获取数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        child_id = intent.getStringExtra("child_id");
        String typeStr = intent.getStringExtra("type");
        mType = Integer.parseInt(typeStr);

        mTitle.setText(title);
    }

}
