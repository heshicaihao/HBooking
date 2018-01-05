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
import com.nettactic.hotelbooking.activity.HotelInfoChildActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heshicaihao on 2017/4/6.
 */
public class HotelInfoListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Activity activity;
    private JSONArray data;

    public HotelInfoListViewAdapter(Activity activity, Context context,JSONArray data) {
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
            convertView = mInflater.inflate(R.layout.item_hotel_info_list, null);
            holder = new ViewHolder();
            holder.item_ll = (LinearLayout) convertView
                    .findViewById(R.id.item_ll);
            holder.name_tv = (TextView) convertView
                    .findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject object = data.getJSONObject(position);
            final String title = object.optString("title");
            final String child_id = object.optString("id");
            final String type = object.optString("type");
            holder.name_tv.setText(title);
            holder.item_ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    gotoGoodsDetails(title,child_id,type);
                }

            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;

    }

    private void gotoGoodsDetails(String title,String child_id,String type) {
        Intent intent = new Intent(activity, HotelInfoChildActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("child_id", child_id);
        intent.putExtra("type", type);
        activity.startActivity(intent);
        AndroidUtils.enterActvityAnim(activity);
    }

    public static class ViewHolder {
        public LinearLayout item_ll;
        public TextView name_tv;

    }

}