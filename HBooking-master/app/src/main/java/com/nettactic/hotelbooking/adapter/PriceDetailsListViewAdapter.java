package com.nettactic.hotelbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heshicaihao on 2017/4/26.
 */
public class PriceDetailsListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Activity activity;
    private JSONArray data;

    public PriceDetailsListViewAdapter(Activity activity, Context context, JSONArray data) {
        this.activity = activity;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_price_details_list, null);
            holder = new ViewHolder();
            holder.time_in_and_time_out = (TextView) convertView
                    .findViewById(R.id.time_in_and_time_out);
            holder.price = (TextView) convertView
                    .findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject object = data.getJSONObject(position);
            String time_inStr = object.optString("time_in");
            long time_in = Long.parseLong(time_inStr);
            String time_inOK = StringUtils.longToDate(time_in);
            String time_outStr = object.optString("time_out");
            long time_out = Long.parseLong(time_outStr);
            String time_outOK = StringUtils.longToDate(time_out);
            String time_in_and_time_out = time_inOK + " 至 " + time_outOK;
            holder.time_in_and_time_out.setText(time_in_and_time_out);

            String priceStr = object.optString("price");
            holder.price.setText("¥"+StringUtils.format2point(priceStr)+"元/晚");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;

    }

    public static class ViewHolder {
        public TextView time_in_and_time_out;
        public TextView price;

    }

}