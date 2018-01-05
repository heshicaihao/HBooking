package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.PointsListAdapter;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.bean.PointsBean;
import com.nettactic.hotelbooking.bean.UserBean;
import com.nettactic.hotelbooking.common.UserController;
import com.nettactic.hotelbooking.constants.MyConstants;
import com.nettactic.hotelbooking.listener.OnRefreshListener;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.FileUtil;
import com.nettactic.hotelbooking.utils.JSONUtil;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.nettactic.hotelbooking.widget.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 我的积分
 */
public class MyPointsActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener {

    private TextView mTitle;
    private ImageButton mFloatingActionButton;
    private TextView mPointsValue;
    private RefreshListView mListView;
    private PointsListAdapter mAdapter;
    private RelativeLayout mViewNull;
    private LinearLayout mContentLl;
    private Button mNullLeftBt;
    private Button mGotoHomeBt;
    private ImageView mPromptImage;
    private TextView mPromptInfo;
    private boolean mIsNoNull = false;
    private int mPage = 1;
    private int mPageCout = 5;
    private String pager_total = "1";
    private List<PointsBean> orderList = new ArrayList<PointsBean>();
    private UserBean mUser;
    private String mGoodsListStr = null;
    private LinearLayout mMoreLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);
        StatusBarUtils.myStatusBar(this);
        mUser = UserController.getInstance(this).getUserInfo();
//        mIsNoNull = SharedpreferncesUtil.getOrder(this);
        mIsNoNull = true;
        initView();
        initData();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);
        mPointsValue = (TextView) findViewById(R.id.points_value);
        mListView = (RefreshListView) findViewById(R.id.listview);
        mViewNull = (RelativeLayout) findViewById(R.id.in_view_null);
        mNullLeftBt = (Button) findViewById(R.id.null_left_bt);
        mGotoHomeBt = (Button) findViewById(R.id.goto_home_bt);
        mPromptImage = (ImageView) findViewById(R.id.prompt_image);
        mPromptInfo = (TextView) findViewById(R.id.prompt_info);
        mContentLl = (LinearLayout) findViewById(R.id.content_ll);
        mMoreLl = (LinearLayout)findViewById(R.id.more_ll);

        mPromptImage.setImageResource(R.mipmap.order_null);
        mPromptInfo.setText(this.getString(R.string.no_order_info));
        if (mUser.isIs_login() && mIsNoNull) {
            mViewNull.setVisibility(View.GONE);
            mContentLl.setVisibility(View.VISIBLE);
        } else {
            mViewNull.setVisibility(View.VISIBLE);
            mContentLl.setVisibility(View.GONE);
        }
        mAdapter = new PointsListAdapter(MyPointsActivity.this, getApplicationContext());
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);

        mTitle.setText(this.getResources().getString(R.string.my_consumption_points));
        mFloatingActionButton.setOnClickListener(this);
        mMoreLl.setOnClickListener(this);
    }

    private void initData() {
        if (!AndroidUtils.isNetworkAvailable(this)) {
            ToastUtils.show(R.string.no_net);
        }
        if (mUser.isIs_login() && mIsNoNull) {
            String order_listfilepath = FileUtil.getFilePath(
                    MyConstants.ORDERLIST, MyConstants.POINTS_LIST,
                    MyConstants.TXT);
            boolean fileIsExists = FileUtil.fileIsExists(order_listfilepath);
            if (!fileIsExists) {
                if (AndroidUtils.isNetworkAvailable(this)) {
                    initData(true);
                } else {
                    ToastUtils.show(R.string.no_net);
                }
            } else {
                mGoodsListStr = FileUtil.readFile(MyConstants.ORDERLIST,
                        MyConstants.POINTS_LIST, MyConstants.TXT);
                try {
                    JSONArray arr = new JSONArray(mGoodsListStr);
                    List<PointsBean> list = JSONUtil.getPointsList(arr);
                    if (list != null && list.size() > 0) {
                        if (orderList.isEmpty()) {
                            orderList.addAll(list);
                        }
                        mAdapter.setData(orderList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDownPullRefresh() {
        mPage = 1;
        initData(true);
    }

    @Override
    public void onLoadingMore() {
        getMoreData();
    }

    private void initData(boolean flag) {

        if (mUser.isIs_login()) {
            getOrderInfo(flag);
        }
    }

    private void getOrderInfo(final boolean flag) {
        String json = AssetsUtils.getJson(this, "points_list.txt");
        resolveOrderListChecked(flag, json);
    }

    private void resolveOrderListChecked(boolean flag, String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                pager_total = result.optString("pager_total");
                String available_integral = result.optString("available_integral");
                mPointsValue.setText(available_integral);
                JSONArray arr = result.getJSONArray("integral_info");
                if (mPage == 1) {
                    FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
                            MyConstants.POINTS_LIST, MyConstants.TXT);
                }
                List<PointsBean> list = JSONUtil.getPointsList(arr);
                if (list != null && list.size() > 0) {
                    if (flag) {
                        orderList.clear();
                    }
                    mPage++;

                    orderList.addAll(list);
                    mAdapter.setData(orderList);
                    mAdapter.notifyDataSetChanged();
                    mListView.hideHeaderView();
                    mListView.hideFooterView();
                } else {
                    mPage = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_btn:
                startMainActivity(this, MeActivity.class);
                this.finish();

                break;
            case R.id.more_ll:
                getMoreData();
                break;

            default:
                break;
        }

    }


    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startMainActivity(this, MainActivity.class);
            this.finish();
        }
        return false;
    }

    /**
     * 获取更多数据
     */
    private void getMoreData() {
        if (mPage > Integer.parseInt(pager_total)) {
            mListView.hideFooterView();
            if (AndroidUtils.isNoFastClick()) {
                ToastUtils.show(R.string.no_more);
            }
        } else {
            initData(false);
        }
    }
}
