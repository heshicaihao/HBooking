package com.nettactic.hotelbooking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.OrderListAdapter;
import com.nettactic.hotelbooking.base.BaseFragment;
import com.nettactic.hotelbooking.bean.OrderBean;
import com.nettactic.hotelbooking.bean.UserBean;
import com.nettactic.hotelbooking.common.UserController;
import com.nettactic.hotelbooking.constants.MyConstants;
import com.nettactic.hotelbooking.listener.OnRefreshListener;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.FileUtil;
import com.nettactic.hotelbooking.utils.JSONUtil;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.nettactic.hotelbooking.widget.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NoCheckeFragment extends BaseFragment implements OnRefreshListener, View.OnClickListener {

    private View mView;
    private RefreshListView mListView;
    private RelativeLayout mViewNull;
    private LinearLayout mContentLl;
    private Button mNullLeftBt;
    private Button mGotoHomeBt;
    private ImageView mPromptImage;
    private TextView mPromptInfo;
    private boolean mIsNoNull = false;
    private OrderListAdapter mAdapter;
    private int mPage = 0;
    private int mPageCout = 5;
    private String pager_total = "1";
    private List<OrderBean> orderList = new ArrayList<OrderBean>();
    private UserBean mUser;
    private String mGoodsListStr = null;
    private LinearLayout more_ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_my_order, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        mUser = UserController.getInstance(getContext()).getUserInfo();
//        mIsNoNull = SharedpreferncesUtil.getOrder(getContext());
        mIsNoNull = true;
        initView(mView);
        initData();
        return mView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_ll:
                getMoreData();
                break;

            default:
                break;

        }
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

    private void initView(View mView) {
        mListView = (RefreshListView) mView.findViewById(R.id.listview);
        mViewNull = (RelativeLayout) mView.findViewById(R.id.in_view_null);
        mNullLeftBt = (Button) mView.findViewById(R.id.null_left_bt);
        mGotoHomeBt = (Button) mView.findViewById(R.id.goto_home_bt);
        mPromptImage = (ImageView) mView.findViewById(R.id.prompt_image);
        mPromptInfo = (TextView) mView.findViewById(R.id.prompt_info);
        mContentLl = (LinearLayout) mView.findViewById(R.id.content_ll);
        more_ll = (LinearLayout) mView.findViewById(R.id.more_ll);

        mPromptImage.setImageResource(R.mipmap.order_null);
        mPromptInfo.setText(this.getString(R.string.no_order_info));
        if (mUser.isIs_login() && mIsNoNull) {
            mViewNull.setVisibility(View.GONE);
            mContentLl.setVisibility(View.VISIBLE);
        } else {
            mViewNull.setVisibility(View.VISIBLE);
            mContentLl.setVisibility(View.GONE);
        }
        setOrderAdapter();
        mListView.setOnRefreshListener(this);
        more_ll.setOnClickListener(this);
    }

    private void setOrderAdapter() {
        if (mAdapter == null) {
            mAdapter = new OrderListAdapter(getActivity(), getContext(),0);
        }
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        if (!AndroidUtils.isNetworkAvailable(getContext())) {
            ToastUtils.show(R.string.no_net);
        }
        if (mUser.isIs_login() && mIsNoNull) {
            String order_listfilepath = FileUtil.getFilePath(
                    MyConstants.ORDERLIST, MyConstants.ORDER_LIST_NO_CHECKE,
                    MyConstants.TXT);
            boolean fileIsExists = FileUtil.fileIsExists(order_listfilepath);
            if (!fileIsExists) {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    initData(true);
                } else {
                    ToastUtils.show(R.string.no_net);
                }
            } else {
                mGoodsListStr = FileUtil.readFile(MyConstants.ORDERLIST,
                        MyConstants.ORDER_LIST_NO_CHECKE, MyConstants.TXT);
                try {
                    JSONArray arr = new JSONArray(mGoodsListStr);
                    List<OrderBean> list = JSONUtil.getOrderList(arr);
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
        String json = AssetsUtils.getJson(getContext(), "order_list_no_checke.txt");
        resolveOrderListChecked(flag, json);
    }

    private void resolveOrderListChecked(boolean flag, String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                pager_total = result.optString("pager_total");
                JSONArray arr = result.getJSONArray("orderData");
                if (mPage == 1) {
                    FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
                            MyConstants.ORDER_LIST_NO_CHECKE, MyConstants.TXT);
                }
                List<OrderBean> list = JSONUtil.getOrderList(arr);
                if (list != null && list.size() > 0) {
//                    if (flag) {
//                        orderList.clear();
//                    }
                    orderList.clear();
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


}
