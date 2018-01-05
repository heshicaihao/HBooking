package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.TypeOneEListViewAdapter;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 客房详情
 */
public class RoomDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mClose;
    private TextView mCloseBtn;
    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (ImageView) findViewById(R.id.close);
        mCloseBtn = (TextView) findViewById(R.id.close_btn);
        mListView = (ExpandableListView) findViewById(R.id.e_listview);

        mListView.setGroupIndicator(null);
        mTitle.setText(MyApplication.getContext().getString(R.string.room_details));
        mClose.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);

    }

    private void initData() {

        String json = AssetsUtils.getJson(this, "room_details_info.txt");
        resolveHotelInfo(json);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.close_btn:
                this.finish();
                AndroidUtils.exitOutsideActvityAnim(this);

                break;

            case R.id.close:
                this.finish();
                AndroidUtils.exitOutsideActvityAnim(this);

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitOutsideActvityAnim(this);

        }

        return false;

    }


    /**
     * 解析酒店信息
     *
     * @param json
     */
    private void resolveHotelInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONArray price_info = result.optJSONArray("room_info");

                setEListAdapter(price_info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载适配器加载数据
     *
     * @param data
     */
    private void setEListAdapter(JSONArray data) {
        setMyELVAdapter(this,data,mListView,1);
        int groupCount = mListView.getCount();

        for (int i=0; i<groupCount; i++) {

            mListView.expandGroup(i);

        };
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }


}
