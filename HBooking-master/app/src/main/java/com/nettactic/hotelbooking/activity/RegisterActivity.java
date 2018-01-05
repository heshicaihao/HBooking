package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.net.MyURL;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StringUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.nettactic.hotelbooking.widget.ChoiceSexView;
import com.nettactic.hotelbooking.widget.SwitchView;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, SwitchView.OnStateChangedListener {

    private TextView mTitle;
    private ImageView mClose;

    private boolean mIsMan = true;
    private boolean mIsAgree = true;
    private SwitchView mSwitchView;
    private TextView mUserAgreement;
    private TextView mRegister;
    private TextView mGetCode;

    private EditText mNameEt;
    private ChoiceSexView mChoiceSexView;
    private EditText mPhoneEt;
    private EditText mEmailEt;

    private ImageView mNameDeleteIv;
    private ImageView mPhoneDeleteIv;
    private ImageView mEmailDeleteIv;
    private String mCheckInNameStr;
    private boolean mCheckInSex = true;
    private String mCheckInPhoneStr;
    private String mCheckInEmailStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();

    }

    private void initData() {
        String html = "<font color='#9a7941'><u>同意条款和条件</u></font>";
        mUserAgreement.setText(Html.fromHtml(html));
        mTitle.setText(MyApplication.getContext().getString(R.string.add_user));
        mChoiceSexView.setSex(true);
        mSwitchView.setState(true);

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (ImageView) findViewById(R.id.close);
        mUserAgreement = (TextView) findViewById(R.id.agreed_terms_and_conditions);
        mRegister = (TextView) findViewById(R.id.register);
        mGetCode = (TextView) findViewById(R.id.get_code);

        mNameEt = (EditText) findViewById(R.id.name);
        mChoiceSexView = (ChoiceSexView) findViewById(R.id.choice_sex_view);
        mPhoneEt = (EditText) findViewById(R.id.phone);
        mEmailEt = (EditText) findViewById(R.id.email);
        mSwitchView = (SwitchView) findViewById(R.id.switch_view);
        mNameDeleteIv = (ImageView) findViewById(R.id.name_delete);
        mPhoneDeleteIv = (ImageView) findViewById(R.id.phone_delete);
        mEmailDeleteIv = (ImageView) findViewById(R.id.email_delete);

        setTextChangedListener();
        mSwitchView.setOnStateChangedListener(this);
        mClose.setOnClickListener(this);
        mUserAgreement.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mRegister.setOnClickListener(this);
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

            case R.id.get_code:
                mIsMan = mChoiceSexView.getSex();
                if (mIsMan) {
                    ToastUtils.show(MyApplication.getContext().getString(R.string.man));
                } else {
                    ToastUtils.show(MyApplication.getContext().getString(R.string.woman));
                }
                break;

            case R.id.register:
                if (mIsAgree) {
                    if (!proofInputInfo()) {
                        return;
                    }


                } else {
                    ToastUtils.show(MyApplication.getContext().getString(R.string.read_text));
                }

                break;

            case R.id.agreed_terms_and_conditions:
                startOtherWeb(this, this.getString(R.string.treaty),
                        MyURL.TREATY_URL);
                break;

            case R.id.close:
                this.finish();
                AndroidUtils.exitOutsideActvityAnim(this);

                break;

            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitOutsideActvityAnim(this);

        }

        return false;

    }


    @Override
    public void toggleToOn() {
        mSwitchView.toggleSwitch(true);
        mIsAgree = true;
    }

    @Override
    public void toggleToOff() {
        mSwitchView.toggleSwitch(false);
        mIsAgree = false;
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
}
