package com.nettactic.hotelbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.activity.FillOrderActivity;
import com.nettactic.hotelbooking.activity.RoomDetailsActivity;
import com.nettactic.hotelbooking.activity.RoomRateDetailsActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heshicaihao on 2017/4/6.
 */
public class RoomTypeListEListViewAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private JSONArray data;
    private ExpandableListView eListView;

    public RoomTypeListEListViewAdapter(Activity activity, JSONArray data, ExpandableListView eListView) {
        this.activity = activity;
        this.data = data;
        this.eListView = eListView;

    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        JSONObject child = null;
        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            JSONArray room_priceinfo = parent.optJSONArray("room_priceinfo");
            child = (JSONObject) room_priceinfo.get(childPos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return child;
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return data.length();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        JSONArray room_priceinfo = null;
        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            room_priceinfo = parent.optJSONArray("room_priceinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return room_priceinfo.length();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        JSONArray room_priceinfo = null;
        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            room_priceinfo = parent.optJSONArray("room_priceinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return room_priceinfo;
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(final int parentPos, boolean b, View view, final ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_e_listview_parent_room_type, null);
        }
        view.setTag(R.layout.item_e_listview_parent_room_type, parentPos);
        view.setTag(R.layout.item_e_listview_child_room_type, -1);
        TextView parent_room_name = (TextView) view.findViewById(R.id.parent_room_name);
        TextView parent_room_baseprice = (TextView) view.findViewById(R.id.parent_room_baseprice);
        TextView parent_room_info = (TextView) view.findViewById(R.id.parent_room_info);
        TextView parent_room_introduce = (TextView) view.findViewById(R.id.parent_room_introduce);
        TextView parent_choice_bt = (TextView) view.findViewById(R.id.parent_choice_bt);
        LinearLayout parent_top_ll = (LinearLayout) view.findViewById(R.id.parent_top_ll);
        LinearLayout null_ll = (LinearLayout) view.findViewById(R.id.parent_room_introduce_ll);

        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            String room_name = parent.optString("room_name");
            parent_room_name.setText(room_name);

            String room_baseprice = parent.optString("room_baseprice");
            parent_room_baseprice.setText(room_baseprice);

            String room_info = parent.optString("room_info");
            parent_room_info.setText(room_info);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        parent_room_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOutsideActivity(activity, RoomDetailsActivity.class);
            }
        });
        parent_room_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        parent_top_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        null_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_e_listview_child_room_type, null);
        }
        view.setTag(R.layout.item_e_listview_parent_room_type, parentPos);
        view.setTag(R.layout.item_e_listview_child_room_type, childPos);

        TextView child_room_name = (TextView) view.findViewById(R.id.child_room_name);
        TextView child_room_baseprice = (TextView) view.findViewById(R.id.child_room_baseprice);
        TextView child_room_info = (TextView) view.findViewById(R.id.child_room_info);
        TextView child_booking_bt = (TextView) view.findViewById(R.id.child_booking_bt);
        TextView room_rate_details = (TextView) view.findViewById(R.id.child_room_rate_details);

        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            JSONArray room_priceinfo = parent.optJSONArray("room_priceinfo");
            JSONObject child = (JSONObject) room_priceinfo.get(childPos);

            String price_name = child.optString("price_name");
            child_room_name.setText(price_name);

            String price_money = child.optString("price_money");
            child_room_baseprice.setText(price_money);

            String price_info = child.optString("price_info");
            child_room_info.setText(price_info);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        child_booking_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(activity, FillOrderActivity.class);
            }
        });
        room_rate_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOutsideActivity(activity, RoomRateDetailsActivity.class);
            }
        });
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void startOutsideActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterOutsideActvityAnim(this.activity);

    }

    public void startActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterActvityAnim(this.activity);

    }

}