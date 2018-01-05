package com.nettactic.hotelbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.activity.OrderDetailsActivity;
import com.nettactic.hotelbooking.bean.OrderBean;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StringUtils;

import java.util.List;

/**
 * Created by heshicaihao on 2017/4/19.
 */

public class OrderListAdapter extends BaseAdapter {
    public String TAG = getClass().getName();

    private Context mContext;
    private Activity mActivity;
    private List<OrderBean> mData;
    private LayoutInflater mInflater;
    private OrderHolder mHolder;
    private int mSign;

    public OrderListAdapter(Activity mActivity, Context context,int sign) {
        this.mContext = context;
        this.mActivity = mActivity;
        this.mSign = sign;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int cout = 0;
        if (mData == null) {
            cout = 0;
        } else {
            cout = mData.size();
        }
        return cout;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    public void setData(List<OrderBean> mData) {
        this.mData = mData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_order_list, null);
            mHolder = new OrderHolder();
            getItemView(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (OrderHolder) convertView.getTag();
        }
        setData2UI(position);

        return convertView;
    }

    /**
     * 找到Item 的 控件
     *
     * @param convertView
     */
    private void getItemView(View convertView) {
        mHolder.time_in_and_time_out = (TextView) convertView.findViewById(R.id.time_in_and_time_out);
        mHolder.status = (TextView) convertView.findViewById(R.id.status);
        mHolder.room_name = (TextView) convertView.findViewById(R.id.room_name);
        mHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
        mHolder.item_order_list_ll = (LinearLayout) convertView.findViewById(R.id.item_order_list_ll);

    }

    /**
     * 给UI加载数据
     *
     * @param position
     */
    private void setData2UI(final int position) {
        OrderBean object = mData.get(position);

        String time_inStr = object.getTime_in();
        long time_in = Long.parseLong(time_inStr);
        String time_inOK = StringUtils.longToDate(time_in);
        String time_outStr = object.getTime_out();
        long time_out = Long.parseLong(time_outStr);
        String time_outOK = StringUtils.longToDate(time_out);
        String time_in_and_time_out = time_inOK + " 至 " + time_outOK;
        mHolder.time_in_and_time_out.setText(time_in_and_time_out);

        String pay_status = object.getPay_status();
        String order_status = object.getOrder_status();
        if ("3".equals(order_status)) {
            mHolder.status.setText(mContext
                    .getString(R.string.canceled));
            mHolder.status.setTextColor(mContext.getResources().getColor(R.color.order_text_color_b));
        } else {
            if ("0".equals(pay_status)) {
                mHolder.status.setText(mContext
                        .getString(R.string.no_pay));
                mHolder.status.setTextColor(mContext.getResources().getColor(R.color.text_color_heavy));
            } else if ("1".equals(pay_status)) {
                mHolder.status.setText(mContext
                        .getString(R.string.paymented));
                mHolder.status.setTextColor(mContext.getResources().getColor(R.color.order_text_color_c));
            }
        }
        String room_name = object.getRoom_name();
        mHolder.room_name.setText(room_name);
        String user_name = object.getUser_name();
        mHolder.user_name.setText(user_name);

        mHolder.item_order_list_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderDetailsActivity(position);
            }
        });
    }

    /**
     * 跳转到 OrderDetailsActivity
     *
     * @param position
     */
    private void gotoOrderDetailsActivity(final int position) {
        OrderBean object = mData.get(position);
        String mOrderId = object.getOrder_id();
        Intent intent = new Intent(mContext, OrderDetailsActivity.class);
        intent.putExtra("mOrderId", mOrderId);
        mActivity.startActivity(intent);
        AndroidUtils.enterActvityAnim(mActivity);
    }

}