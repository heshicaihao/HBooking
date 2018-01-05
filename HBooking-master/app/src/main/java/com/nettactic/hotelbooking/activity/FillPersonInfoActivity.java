package com.nettactic.hotelbooking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.PopupHolder;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.utils.StringUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.nettactic.hotelbooking.widget.ChoiceSexView;
import com.nettactic.hotelbooking.widget.MyListView;
import com.nettactic.hotelbooking.widget.SwitchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FillPersonInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int VIEW_CHECK_IN_LOGIN = 0;
    private static final int VIEW_CHECK_IN_UNLOGIN = 1;
    private static final int VIEW_BOOKED = 2;
    private int mSign = VIEW_CHECK_IN_LOGIN;
    private View mMenuView;
    private RelativeLayout mUnloginLineRl;
    private RelativeLayout mUserInfoRl;
    private RelativeLayout mOftenPersonRl;
    private LinearLayout mPhonePromptLl;
    private LinearLayout mSaveAutoFillLl;
    private TextView mTitle;
    private TextView mCancelBtn;
    private TextView mCompleteBtn;
    private TextView mEmailHeadTv;
    private TextView mSaveAutoFillTv;
    private TextView mChoiceOftenPersonTv;
    private TextView mOKBtn;
    private EditText mPhoneEt;
    private EditText mEmailEt;
    private EditText mNameEt;
    private ImageView mNameDeleteIv;
    private ImageView mPhoneDeleteIv;
    private ImageView mEmailDeleteIv;
    private ChoiceSexView mChoiceSexView;
    private SwitchView mSwitchView;
    private MyListView mPopupListview;
    private MyPopupWindow mPopupWindow;

    private String mCheckInNameStr;
    private boolean mCheckInSex = true;
    private String mCheckInPhoneStr;
    private String mCheckInEmailStr;
    private boolean mIsCheckInSava = false;
    private String mBookedNameStr;
    private boolean mBookedSex = true;
    private String mBookedPhoneStr;
    private String mBookedEmailStr;
    private boolean mIsBookedSava = false;
    private JSONArray mDataPopupWindow = null;
    private JSONObject mDataIntent;
    private int mFirstPosition = 0;

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ok_btn:
                    mPopupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_delete:
                mNameEt.setText("");
                mNameDeleteIv.setVisibility(View.GONE);
                break;

            case R.id.phone_delete:
                mPhoneEt.setText("");
                mPhoneDeleteIv.setVisibility(View.GONE);
                break;

            case R.id.email_delete:
                mEmailEt.setText("");
                mEmailDeleteIv.setVisibility(View.GONE);
                break;

            case R.id.fill_person_info_often_person_view:
                mPopupWindow = new MyPopupWindow(FillPersonInfoActivity.this,
                        itemsOnClick);
                mPopupWindow.showAtLocation(FillPersonInfoActivity.this
                        .findViewById(R.id.complete_btn), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.complete_btn:
                if (!proofInputInfo()) {
                    return;
                }
                goback(mSign);
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

    /**
     * 校对用户输入是否合规
     */
    private boolean proofInputInfo() {
        String name = mNameEt.getText().toString();
        String phone = mPhoneEt.getText().toString();
        String email = mEmailEt.getText().toString();
        if (StringUtils.isEmpty(name)) {
            ToastUtils.show(R.string.please_input_name_null);
            return false;
        }
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.show(R.string.please_input_phone_null);
            return false;
        }
        if (StringUtils.isEmpty(email)) {
            ToastUtils.show(R.string.please_input_email_null);
            return false;
        }
        if (!AndroidUtils.isPhoneNumberValid(phone)) {
            ToastUtils.show(R.string.please_input_phone_again);
            mPhoneEt.setText("");
            return false;
        }
        if (!AndroidUtils.isEmailValid(email)) {
            mEmailEt.setText("");
            ToastUtils.show(R.string.please_input_email_again);
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mCompleteBtn = (TextView) findViewById(R.id.complete_btn);
        mUnloginLineRl = (RelativeLayout) findViewById(R.id.fill_person_info_unlogin_line_view);
        mUserInfoRl = (RelativeLayout) findViewById(R.id.fill_person_info_user_info_view);
        mOftenPersonRl = (RelativeLayout) findViewById(R.id.fill_person_info_often_person_view);
        mPhonePromptLl = (LinearLayout) findViewById(R.id.phone_prompt_ll);
        mEmailHeadTv = (TextView) findViewById(R.id.email_head);
        mSaveAutoFillLl = (LinearLayout) findViewById(R.id.save_and_next_auto_fill_ll);
        mSaveAutoFillTv = (TextView) findViewById(R.id.save_and_next_auto_fill);
        mChoiceOftenPersonTv = (TextView) findViewById(R.id.choice_often_person);
        mNameDeleteIv = (ImageView) findViewById(R.id.name_delete);
        mPhoneDeleteIv = (ImageView) findViewById(R.id.phone_delete);
        mEmailDeleteIv = (ImageView) findViewById(R.id.email_delete);


        mNameEt = (EditText) findViewById(R.id.name);
        mChoiceSexView = (ChoiceSexView) findViewById(R.id.choice_sex_view);
        mPhoneEt = (EditText) findViewById(R.id.phone);
        mEmailEt = (EditText) findViewById(R.id.email);
        mSwitchView = (SwitchView) findViewById(R.id.switch_view);

        mTitle.setText(this.getResources().getString(R.string.check_in_person_info));
        setTextChangedListener();
        mCancelBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
        mNameDeleteIv.setOnClickListener(this);
        mPhoneDeleteIv.setOnClickListener(this);
        mEmailDeleteIv.setOnClickListener(this);


    }

    private void initData() {
        getIntentData();
    }

    /**
     * 从Intent获取数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        String mSignStr = intent.getStringExtra("mSign");
        mSign = Integer.valueOf(mSignStr);
        showUI(intent, mSign);

    }

    /**
     * 动态改变UI
     *
     * @param mSign
     */
    private void showUI(Intent intent, int mSign) {
        switch (mSign) {
            case VIEW_CHECK_IN_LOGIN:
                showCheckInLoginUI();
                setCheckInDefaultValue(intent);
                String data = intent.getStringExtra("mDataPopupWindow");
                mDataPopupWindow = String2JSONArray(data);
                if (mDataPopupWindow.length() != 0 && mDataPopupWindow != null) {
                    mOftenPersonRl.setOnClickListener(this);
                    String choice_often_personStr = MyApplication.getContext().getString(R.string.choice_often_person);
                    String text = choice_often_personStr + "(" + mDataPopupWindow.length() + ")";
                    mChoiceOftenPersonTv.setText(text);
                } else {
                    mOftenPersonRl.setVisibility(View.GONE);
                }

                break;

            case VIEW_CHECK_IN_UNLOGIN:
                showCheckInUnLoginUI();
                setCheckInDefaultValue(intent);
                break;

            case VIEW_BOOKED:
                showBookedUI();
                setBookedDefaultValue(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 将String 转化为JSONArray
     *
     * @param mDataPopupWindowStr
     * @return
     */
    private JSONArray String2JSONArray(String mDataPopupWindowStr) {
        JSONArray data = null;
        try {
            data = new JSONArray(mDataPopupWindowStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 将传来的值转回 boolean
     *
     * @param SexStr
     * @return
     */
    private boolean Str2Boolean(String SexStr) {
        if (StringUtils.isEmpty(SexStr)) {
            mChoiceSexView.setSex(true);
            return true;
        } else {
            return Boolean.parseBoolean(SexStr);
        }
    }

    /**
     * 返回 FillOrderActivity
     */
    private void goback(int mSign) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mSign", String.valueOf(mSign));
        switch (mSign) {
            case VIEW_CHECK_IN_LOGIN:
                mCheckInNameStr = mNameEt.getText().toString();
                mCheckInSex = mChoiceSexView.getSex();
                mCheckInPhoneStr = mPhoneEt.getText().toString();
                mCheckInEmailStr = mEmailEt.getText().toString();
                mIsCheckInSava = mSwitchView.getIsState();
                intent.putExtra("mCheckInNameStr", mCheckInNameStr);
                intent.putExtra("mCheckInSex", mCheckInSex + "");
                intent.putExtra("mCheckInPhoneStr", mCheckInPhoneStr);
                intent.putExtra("mCheckInEmailStr", mCheckInEmailStr);
                intent.putExtra("mIsCheckInSava", mIsCheckInSava + "");

                break;

            case VIEW_CHECK_IN_UNLOGIN:
                mCheckInNameStr = mNameEt.getText().toString();
                mCheckInSex = mChoiceSexView.getSex();
                mCheckInPhoneStr = mPhoneEt.getText().toString();
                mCheckInEmailStr = mEmailEt.getText().toString();
                mIsCheckInSava = mSwitchView.getIsState();
                intent.putExtra("mCheckInNameStr", mCheckInNameStr);
                intent.putExtra("mCheckInSex", mCheckInSex + "");
                intent.putExtra("mCheckInPhoneStr", mCheckInPhoneStr);
                intent.putExtra("mCheckInEmailStr", mCheckInEmailStr);
                intent.putExtra("mIsCheckInSava", mIsCheckInSava + "");

                break;

            case VIEW_BOOKED:
                mBookedNameStr = mNameEt.getText().toString();
                mBookedSex = mChoiceSexView.getSex();
                mBookedPhoneStr = mPhoneEt.getText().toString();
                mBookedEmailStr = mEmailEt.getText().toString();
                mIsBookedSava = mSwitchView.getIsState();
                intent.putExtra("mBookedNameStr", mBookedNameStr);
                intent.putExtra("mBookedSex", mBookedSex + "");
                intent.putExtra("mBookedPhoneStr", mBookedPhoneStr);
                intent.putExtra("mBookedEmailStr", mBookedEmailStr);
                intent.putExtra("mIsBookedSava", mIsBookedSava + "");

                break;

            default:
                break;

        }
        FillPersonInfoActivity.this.setResult(
                FillOrderActivity.RESULT_OK, intent);

    }

    /**
     * 设置预订人界面 默认值
     *
     * @param intent
     */
    private void setBookedDefaultValue(Intent intent) {
        mBookedNameStr = intent.getStringExtra("mBookedNameStr");
        mBookedSex = Str2Boolean(intent.getStringExtra("mBookedSex"));
        mBookedPhoneStr = intent.getStringExtra("mBookedPhoneStr");
        mBookedEmailStr = intent.getStringExtra("mBookedEmailStr");
        mIsBookedSava = Boolean.parseBoolean(intent.getStringExtra("mIsBookedSava"));
        mNameEt.setText(mBookedNameStr);
        mChoiceSexView.setSex(mBookedSex);
        mPhoneEt.setText(mBookedPhoneStr);
        mEmailEt.setText(mBookedEmailStr);
        mSwitchView.setState(mIsBookedSava);
    }

    /**
     * 设置入住人界面 默认值
     *
     * @param intent
     */
    private void setCheckInDefaultValue(Intent intent) {
        mCheckInNameStr = intent.getStringExtra("mCheckInNameStr");
        mCheckInSex = Str2Boolean(intent.getStringExtra("mCheckInSex"));
        mCheckInPhoneStr = intent.getStringExtra("mCheckInPhoneStr");
        mCheckInEmailStr = intent.getStringExtra("mCheckInEmailStr");
        mIsCheckInSava = Boolean.parseBoolean(intent.getStringExtra("mIsCheckInSava"));
        mNameEt.setText(mCheckInNameStr);
        mChoiceSexView.setSex(mCheckInSex);
        mPhoneEt.setText(mCheckInPhoneStr);
        mEmailEt.setText(mCheckInEmailStr);
        mSwitchView.setState(mIsCheckInSava);
    }

    /**
     * 显示选中入住人信息
     *
     * @param data
     */
    private void setUIInfoValue(JSONObject data) {
        mCheckInNameStr = data.optString("occupancy_name");
        mCheckInSex = Str2Boolean(data.optString("occupancy_sex"));
        mCheckInPhoneStr = data.optString("occupancy_mobile");
        mCheckInEmailStr = data.optString("occupancy_mail");
        mNameEt.setText(mCheckInNameStr);
        mChoiceSexView.setSex(mCheckInSex);
        mPhoneEt.setText(mCheckInPhoneStr);
        mEmailEt.setText(mCheckInEmailStr);
    }


    /**
     * 显示预订人界面UI
     */
    private void showBookedUI() {
        mTitle.setText(this.getResources().getString(R.string.booked_person_info));
        mUnloginLineRl.setVisibility(View.GONE);
        mUserInfoRl.setVisibility(View.VISIBLE);
        mOftenPersonRl.setVisibility(View.GONE);
        mPhonePromptLl.setVisibility(View.GONE);
        mPhoneEt.setBackgroundResource(R.drawable.input_bg);
        mEmailHeadTv.setVisibility(View.VISIBLE);
        mSaveAutoFillLl.setVisibility(View.VISIBLE);
        mSaveAutoFillTv.setText(MyApplication.getContext().getString(R.string.save_and_next_auto_fill));
    }

    /**
     * 显示未登录入住人界面UI
     */
    private void showCheckInUnLoginUI() {
        mTitle.setText(this.getResources().getString(R.string.check_in_person_info));
        mUnloginLineRl.setVisibility(View.VISIBLE);
        mUserInfoRl.setVisibility(View.GONE);
        mOftenPersonRl.setVisibility(View.GONE);
        mPhonePromptLl.setVisibility(View.VISIBLE);
        mPhoneEt.setBackgroundResource(R.drawable.input_red_bg);
        mEmailHeadTv.setVisibility(View.GONE);
        mSaveAutoFillLl.setVisibility(View.GONE);
    }

    /**
     * 显示已登录入住人界面UI
     */
    private void showCheckInLoginUI() {
        mTitle.setText(this.getResources().getString(R.string.check_in_person_info));
        mUnloginLineRl.setVisibility(View.GONE);
        mUserInfoRl.setVisibility(View.VISIBLE);
        mOftenPersonRl.setVisibility(View.VISIBLE);
        mPhonePromptLl.setVisibility(View.GONE);
        mEmailHeadTv.setVisibility(View.VISIBLE);
        mPhoneEt.setBackgroundResource(R.drawable.input_bg);
        mSaveAutoFillLl.setVisibility(View.VISIBLE);
        mSaveAutoFillTv.setText(MyApplication.getContext().getString(R.string.save_often_person));
    }

    /**
     * 通过性别的称呼
     *
     * @param sex
     */
    private String getCallSex(boolean sex) {
        if (sex) {
            return MyApplication.getContext().getString(R.string.man);
        } else {
            return MyApplication.getContext().getString(R.string.woman);
        }
    }


    /**
     * 添加编辑框监听
     */
    private void setTextChangedListener() {
        mNameEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().length() > 0) {
                    mNameDeleteIv.setVisibility(View.VISIBLE);
                } else {
                    mNameDeleteIv.setVisibility(View.GONE);
                }
            }
        });
        mPhoneEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().length() > 0) {
                    mPhoneDeleteIv.setVisibility(View.VISIBLE);
                } else {
                    mPhoneDeleteIv.setVisibility(View.GONE);
                }
            }
        });

        mEmailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().length() > 0) {
                    mEmailDeleteIv.setVisibility(View.VISIBLE);
                } else {
                    mEmailDeleteIv.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 弹出层 选材质 选型号
     *
     * @author heshicaihao
     */
    class MyPopupWindow extends PopupWindow {

        public PopupListAdapter mPopupAdapter;
        public Activity context;

        public MyPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
            super(context);
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(
                    R.layout.view_popup_window_often_person, null);
            initView(mMenuView, itemsOnClick);
        }

        /**
         * 找ID
         *
         * @param view
         * @param itemsOnClick
         */
        private void initView(View view, View.OnClickListener itemsOnClick) {
            mOKBtn = (TextView) view.findViewById(R.id.ok_btn);
            mPopupListview = (MyListView) view
                    .findViewById(R.id.popup_list_view);
            mOKBtn.setOnClickListener(itemsOnClick);
            setPopupWindow();
            setMenuTouch();
            mPopupAdapter = new PopupListAdapter(context, mDataPopupWindow);
            mPopupListview.setAdapter(mPopupAdapter);
            setOnItemClick();

        }

        /**
         * 设置 材质的 ItemClick 事件
         */
        private void setOnItemClick() {
            mPopupListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    mFirstPosition = position;
                    try {
                        JSONObject object = (JSONObject) mDataPopupWindow.get(position);
                        setUIInfoValue(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPopupAdapter.setSeclection(position);
                    mPopupAdapter.notifyDataSetInvalidated();
                }
            });
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

    /**
     * 材质 内容的 适配器
     *
     * @author PC
     */
    class PopupListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private JSONArray mList;
        private int clickTemp = 0;// 选中的位置

        public PopupListAdapter(Context context, JSONArray list) {
            this.mList = list;
            this.mInflater = LayoutInflater.from(context);
        }

        public void setSeclection(int position) {
            clickTemp = position;
        }

        @Override
        public int getCount() {
            int count = mList == null ? 0 : mList.length();
            return count;
        }

        @Override
        public Object getItem(int position) {
            try {
                return mList == null ? null : mList.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PopupHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.item_popup_window_list, null);
                holder = new PopupHolder();
                holder.titleImageView = (ImageView) convertView
                        .findViewById(R.id.title_iv);
                holder.titleTextView = (TextView) convertView
                        .findViewById(R.id.title_tv);
                holder.contentTextView = (TextView) convertView
                        .findViewById(R.id.content_tv);
                holder.frameRelativeLayout = (RelativeLayout) convertView
                        .findViewById(R.id.frame_rl);
                holder.iconImageView = (ImageView) convertView
                        .findViewById(R.id.icon_iv);

                convertView.setTag(holder);
            } else {
                holder = (PopupHolder) convertView.getTag();
            }
            if (clickTemp == position) {
                holder.titleImageView.setImageResource(R.mipmap.pic_pressed);
                holder.frameRelativeLayout
                        .setBackgroundResource(R.drawable.plan_bg_pressed);
                holder.iconImageView.setVisibility(View.VISIBLE);
                holder.titleTextView.setTextColor(Color.parseColor("#FFAC13"));
                holder.contentTextView
                        .setTextColor(Color.parseColor("#FFAC13"));
            } else {
                holder.titleImageView.setImageResource(R.mipmap.pic_normal);
                holder.frameRelativeLayout
                        .setBackgroundResource(R.drawable.plan_bg_normal);
                holder.iconImageView.setVisibility(View.GONE);
                holder.titleTextView.setTextColor(Color.parseColor("#666666"));
                holder.contentTextView
                        .setTextColor(Color.parseColor("#666666"));
            }

            try {
                JSONObject object = (JSONObject) mList.get(position);
                String occupancy_name = object.optString("occupancy_name");
                boolean mCheckInSex = Str2Boolean(object.optString("occupancy_sex"));
                String call = getCallSex(mCheckInSex);
                String title = occupancy_name + " " + call;
                holder.titleTextView.setText(title);

                String occupancy_mobile = object.optString("occupancy_mobile");
                String occupancy_mail = object.optString("occupancy_mail");
                String title_content = occupancy_mobile + "    " + occupancy_mail;
                holder.contentTextView.setText(title_content);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

}