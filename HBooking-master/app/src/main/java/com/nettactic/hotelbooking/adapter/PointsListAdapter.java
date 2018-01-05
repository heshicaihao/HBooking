package com.nettactic.hotelbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.bean.PointsBean;
import com.nettactic.hotelbooking.utils.StringUtils;

import java.util.List;

/**
 * Created by heshicaihao on 2017/4/19.
 */

public class PointsListAdapter extends BaseAdapter {
    public String TAG = getClass().getName();

    private Context mContext;
    private Activity mActivity;
    private List<PointsBean> mData;
    private LayoutInflater mInflater;
    private PointsHolder mHolder;

    public PointsListAdapter(Activity mActivity, Context context) {
        this.mContext = context;
        this.mActivity = mActivity;
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
    public void setData(List<PointsBean> mData) {
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

            convertView = mInflater.inflate(R.layout.item_points_list, null);
            mHolder = new PointsHolder();
            getItemView(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (PointsHolder) convertView.getTag();
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
        mHolder.points_tv = (TextView) convertView.findViewById(R.id.points_tv);
        mHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);

    }

    /**
     * 给UI加载数据
     *
     * @param position
     */
    private void setData2UI(int position) {
        PointsBean object = mData.get(position);

        String time_inStr = object.getDate();
        long time_in = Long.parseLong(time_inStr);
        String time_inOK = StringUtils.longToDate02(time_in);
        mHolder.time_tv.setText(time_inOK);

        String points = object.getIntegral();
        int pointsInt = Integer.parseInt(points);
        String pointsStr = null;
        if (pointsInt > 0) {
            pointsStr = "+" + points + " 分";
        } else if (pointsInt == 0){
            pointsStr =  points + " 分";
        }else{
            pointsStr =  points + " 分";
        }

        mHolder.points_tv.setText(pointsStr);
    }


}