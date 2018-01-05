package com.nettactic.hotelbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heshicaihao on 2017/4/6.
 */
public class TypeTwoEListViewAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private JSONArray data;
    private ExpandableListView eListView;

    public TypeTwoEListViewAdapter(Activity activity, JSONArray data, ExpandableListView eListView) {
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
            JSONArray room_priceinfo = parent.optJSONArray("content");
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
            room_priceinfo = parent.optJSONArray("content");
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
            room_priceinfo = parent.optJSONArray("content");
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
            view = inflater.inflate(R.layout.item_e_listview_parent_type_two, null);
        }
        view.setTag(R.layout.item_e_listview_parent_type_two, parentPos);
        view.setTag(R.layout.item_e_listview_child_type_two, -1);
        TextView parent_name_tv = (TextView) view.findViewById(R.id.parent_name_tv);

        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            String title = parent.optString("title");
            parent_name_tv.setText(title);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_e_listview_child_type_two, null);
        }
        view.setTag(R.layout.item_e_listview_parent_type_two, parentPos);
        view.setTag(R.layout.item_e_listview_child_type_two, childPos);

        TextView child_content = (TextView) view.findViewById(R.id.child_content);
        RelativeLayout line_rl = (RelativeLayout) view.findViewById(R.id.line_rl);

        try {
            JSONObject parent = (JSONObject) data.get(parentPos);
            JSONArray content = parent.optJSONArray("content");
            if (content.length() != 0) {
                JSONObject child = (JSONObject) content.get(childPos);
                String title = child.optString("title");
                child_content.setText(title);
            }
            LogUtils.logd("content.length():",content.length()+"");
            LogUtils.logd("childPos:",childPos+"");
            if (content.length()-1 == childPos) {
                line_rl.setVisibility(View.VISIBLE);
            } else {
                line_rl.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void startActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterActvityAnim(this.activity);

    }

}