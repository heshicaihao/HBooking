package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;

//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mClose;
    private TextView mRegister;
    private TextView mFindPassword;
    private EditText mAccount;
    private EditText mPassword;
    private ImageView mAccountDelete;
    private ImageView mPassDelete;
    private TextView mLogin;

    private String mPhoneStr;
    private String mPasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.close:
                this.finish();
                AndroidUtils.exitOutsideActvityAnim(this);

                break;

            case R.id.login:
                if (AndroidUtils.isNoFastClick()) {
                    getInfoInput();
//                    mDialog.show();
                    login();
                }

                break;

            case R.id.register:
                startOutsideActivity(this,RegisterActivity.class);
                this.finish();

                break;

            case R.id.find_password:
                startOutsideActivity(this,ResetPasswordActivity.class);
                this.finish();

                break;

            case R.id.account_delete:
                mAccount.setText("");
                mAccountDelete.setVisibility(View.GONE);
                break;

            case R.id.password_delete:
                mPassword.setText("");
                mPassDelete.setVisibility(View.GONE);
                break;

            default:
                break;

        }

    }

    private void login() {
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request
//        final Request request = new Request.Builder()
//                .url("https://github.com/hongyangAndroid")
//                .build();
//        //new call
//        Call call = mOkHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                //String htmlStr =  response.body().string();
//
//            }
//        });

    }


    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (ImageView) findViewById(R.id.close);

        mRegister = (TextView) findViewById(R.id.register);
        mFindPassword = (TextView) findViewById(R.id.find_password);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mAccountDelete = (ImageView) findViewById(R.id.account_delete);
        mPassDelete = (ImageView) findViewById(R.id.password_delete);
        mLogin = (TextView) findViewById(R.id.login);

        mTitle.setText(MyApplication.getContext().getString(R.string.user_login));
        mAccountDelete.setVisibility(View.GONE);
        mPassDelete.setVisibility(View.GONE);

        setTextChangedListener();
        mClose.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mFindPassword.setOnClickListener(this);
        mAccountDelete.setOnClickListener(this);
        mPassDelete.setOnClickListener(this);
        mLogin.setOnClickListener(this);

    }

    private void initData() {
    }

    /**
     * 添加编辑框监听
     */
    private void setTextChangedListener() {
        mAccount.addTextChangedListener(new TextWatcher() {

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
                    mAccountDelete.setVisibility(View.VISIBLE);
                } else {
                    mAccountDelete.setVisibility(View.GONE);
                }
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                getInfoInput();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().length() > 0) {
                    mPassDelete.setVisibility(View.VISIBLE);
                } else {
                    mPassDelete.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getInfoInput() {
        mPhoneStr = mAccount.getText().toString().trim();
        mPasswordStr = mPassword.getText().toString().trim();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitOutsideActvityAnim(this);

        }

        return false;

    }


}
