package com.nettactic.hotelbooking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.RoomTypeListEListViewAdapter;
import com.nettactic.hotelbooking.amap.activity.AMapLocationActivity;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.AssetsUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * 房型列表
 */
public class RoomTypeListActivity extends BaseActivity implements View.OnClickListener {

    private View mHeader;
    private LinearLayout mBackLL;
    private LinearLayout mHotelInfoLL;
    private TextView mKeyValueTV;
    private TextView mHotelPhoneTV;
    private ImageView mShareIV;
    private ImageView mHotelAddressIV;
    private ExpandableListView mListView;
    private RoomTypeListEListViewAdapter mAdapter;
    private View mMenuView;
    private RoomTypeListActivity.MyPopupWindow mPopupWindow;
    private Tencent mTencent;

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.qq_ll:
                    shareToQQ();
                    ToastUtils.show("qq_ll");
                    break;

                case R.id.qzone_ll:
                    shareToQzone();
                    ToastUtils.show("qzone_ll");
                    break;

                case R.id.wechat_ll:
                    ToastUtils.show("wechat_ll");
                    break;

                case R.id.wxcircle_ll:
                    ToastUtils.show("wxcircle_ll");
                    break;

                default:
                    break;
            }
        }
    };
    private ArrayList<String> list = new ArrayList<String>();
    private String SHARE_TO_QZONE_TYPE_IMAGE_TEXT = "SHARE_TO_QZONE_TYPE_IMAGE_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type_list);
        StatusBarUtils.myStatusBar(this);
        mTencent = Tencent.createInstance(APP_ID, MyApplication.getContext());
        initView();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_iv:
                mPopupWindow = new RoomTypeListActivity.MyPopupWindow(RoomTypeListActivity.this,
                        itemsOnClick);
                mPopupWindow.showAtLocation(RoomTypeListActivity.this
                        .findViewById(R.id.e_listview), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.hotel_phone_tv:
                String call = MyApplication.getContext().getString(R.string.single_hotel_phone);
                telCall(call);

                break;

            case R.id.hotel_info_ll:
                startActivity(this, HotelInfoActivity.class);

                break;

            case R.id.hotel_address_img:
                startActivity(this, AMapLocationActivity.class);

                break;

            case R.id.back_ll:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:

                break;

        }
    }

    private void initView() {
        mKeyValueTV = (TextView) findViewById(R.id.key_value);
        mBackLL = (LinearLayout) findViewById(R.id.back_ll);
        mListView = (ExpandableListView) findViewById(R.id.e_listview);
        mHeader = this.getLayoutInflater().inflate(
                R.layout.header_room_type_list, null);
        mShareIV = (ImageView) mHeader.findViewById(R.id.share_iv);
        mHotelAddressIV = (ImageView) mHeader.findViewById(R.id.hotel_address_img);
        mHotelPhoneTV = (TextView) mHeader.findViewById(R.id.hotel_phone_tv);
        mHotelInfoLL = (LinearLayout) mHeader.findViewById(R.id.hotel_info_ll);

        mListView.addHeaderView(mHeader);
        mListView.setGroupIndicator(null);

        mShareIV.setVisibility(View.VISIBLE);
        mBackLL.setOnClickListener(this);
        mShareIV.setOnClickListener(this);
        mHotelAddressIV.setOnClickListener(this);
        mHotelPhoneTV.setOnClickListener(this);
        mHotelInfoLL.setOnClickListener(this);

    }

    private void initData() {
        getIntentData();

        String json = AssetsUtils.getJson(this, "room_type_list.txt");
        resolveHotelInfo(json);

    }

    /**
     * 从Intent获取数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        String mTimeInStr = intent.getStringExtra("mTimeInStr");
        String mTimeOutStr = intent.getStringExtra("mTimeOutStr");
        String key_valueStr = mTimeInStr.substring(0, mTimeInStr.length() - 1) + " 至 " + mTimeOutStr.substring(0, mTimeOutStr.length() - 1);
        mKeyValueTV.setText(key_valueStr);
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
                JSONArray result = obj.optJSONArray("result");
                JSONObject result01 = (JSONObject) result.get(0);
                JSONArray hotel_room = result01.optJSONArray("hotel_room");

                setEListAdapter(hotel_room);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载适配器加载数据
     *
     * @param hotel_room
     */
    private void setEListAdapter(JSONArray hotel_room) {
        mAdapter = new RoomTypeListEListViewAdapter(RoomTypeListActivity.this, hotel_room, mListView);
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

    private void shareToQQ() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, Integer.parseInt("222222"));
        mTencent.shareToQQ(RoomTypeListActivity.this, params, new BaseUiListener());
    }

    private void shareToQzone() {
        final Bundle params = new Bundle();
        list.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        list.add("http://pic41.nipic.com/20140518/18521768_133448822000_2.jpg");
        list.add("http://pic28.nipic.com/20130424/11588775_115415688157_2.jpg");
        params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        mTencent.shareToQzone(RoomTypeListActivity.this, params, new BaseUiListener());
    }

    /**
     * 弹出层 选材质 选型号
     *
     * @author heshicaihao
     */
    class MyPopupWindow extends PopupWindow {

        public FillPersonInfoActivity.PopupListAdapter mPopupAdapter;
        public Activity context;

        public MyPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
            super(context);
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(
                    R.layout.view_popup_window_share, null);
            initView(mMenuView, itemsOnClick);
        }

        /**
         * 找ID
         *
         * @param view
         * @param itemsOnClick
         */
        private void initView(View view, View.OnClickListener itemsOnClick) {
            LinearLayout qq_ll = (LinearLayout) view.findViewById(R.id.qq_ll);
            LinearLayout qzone_ll = (LinearLayout) view.findViewById(R.id.qzone_ll);
            LinearLayout wechat_ll = (LinearLayout) view.findViewById(R.id.wechat_ll);
            LinearLayout wxcircle_ll = (LinearLayout) view.findViewById(R.id.wxcircle_ll);

            qq_ll.setOnClickListener(itemsOnClick);
            qzone_ll.setOnClickListener(itemsOnClick);
            wechat_ll.setOnClickListener(itemsOnClick);
            wxcircle_ll.setOnClickListener(itemsOnClick);
            setPopupWindow();
            setMenuTouch();

        }


        /**
         * 设置Touch PopupWindow
         */
        private void setMenuTouch() {
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            mMenuView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = mMenuView.findViewById(R.id.pop_layout)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }

        /**
         * 设置PopupWindow
         */
        private void setPopupWindow() {
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            // 因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {

        }
    }

}
