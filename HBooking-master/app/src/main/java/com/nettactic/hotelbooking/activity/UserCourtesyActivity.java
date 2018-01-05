package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 会员礼遇
 */
public class UserCourtesyActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_courtesy);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mListView = (ExpandableListView) findViewById(R.id.e_listview);

        startAlphaAnimation(mFloatingActionButton);
        mTitle.setText(MyApplication.getContext().getString(R.string.user_courtesy));
        mFloatingActionButton.setOnClickListener(this);

    }

    private void initData() {
        String json = AssetsUtils.getJson(this, "user_courtesy.txt");
        resolveHotelInfo(json);

    }

    private void resolveHotelInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONArray data = result.optJSONArray("price_info");
                setEListAdapter(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setEListAdapter(JSONArray data) {
        setMyELVAdapter(this,data,mListView,3);
        int groupCount = mListView.getCount();
        for (int i=0; i<groupCount; i++) {

            mListView.expandGroup(i);

        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mFloatingActionButton.setVisibility(View.GONE);
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

}
