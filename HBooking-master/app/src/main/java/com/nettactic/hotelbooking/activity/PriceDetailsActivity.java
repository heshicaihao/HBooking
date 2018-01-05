package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.PriceDetailsListViewAdapter;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.LogUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 价格明细
 */
public class PriceDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mCancelBtn;
    private TextView mCompleteBtn;
    private ListView mListView;
    private View mHeader;
    private View mFooter;
    private TextView mTaxationChargeTv;
    private TextView mTotalPriceTv;

    private PriceDetailsListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_details);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mCompleteBtn = (TextView) findViewById(R.id.complete_btn);
        mListView = (ListView) findViewById(R.id.listview);
        mHeader = this.getLayoutInflater().inflate(
                R.layout.header_price_details_list, null);
        mFooter = this.getLayoutInflater().inflate(
                R.layout.footer_price_details_list, null);

        mTaxationChargeTv = (TextView) mFooter.findViewById(R.id.taxation_charge);
        mTotalPriceTv = (TextView) mFooter.findViewById(R.id.total_price);
        mListView.addHeaderView(mHeader);
        mListView.addFooterView(mFooter);
        mTitle.setText(this.getResources().getString(R.string.see_price_details));
        mCancelBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
    }

    private void initData() {
        String json = AssetsUtils.getJson(this, "price_details_list.txt");
        resolveHotelInfo(json);
    }


    private void resolveHotelInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                String taxation_chargeStr = result.optString("taxation_charge");
                String total_priceStr = result.optString("total_price");
                mTaxationChargeTv.setText("¥" + StringUtils.format2point(taxation_chargeStr) + "元");
                mTotalPriceTv.setText("¥" + StringUtils.format2point(total_priceStr) + "元");
                JSONArray price_details_list = result.optJSONArray("price_details_list");
                setListAdapter(price_details_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListAdapter(JSONArray price_details_list) {
        mAdapter = new PriceDetailsListViewAdapter(PriceDetailsActivity.this, this, price_details_list);
        mListView.setAdapter(mAdapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.complete_btn:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            case R.id.cancel_btn:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:
                break;
        }
    }

}
