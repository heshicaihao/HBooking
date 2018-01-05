package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StringUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    private int mStep = ONE;

    private TextView mTitle;
    private ImageView mClose;
    private RelativeLayout mOneRl;
    private RelativeLayout mTwoRl;
    private RelativeLayout mThreeRl;
    private TextView mOneBtn;
    private TextView mTwoBtn;
    private TextView mThreeBtn;

    private TextView mTwoHintTv;

    private EditText mOneEt;
    private EditText mTwoEt;
    private EditText mThreeEt;

    private String mOneEtStr;
    private String mTwoEtStr;
    private String mThreeEtStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (ImageView) findViewById(R.id.close);

        mOneRl = (RelativeLayout) findViewById(R.id.view_one_rl);
        mOneBtn = (TextView) findViewById(R.id.next_step_verify_phone_num);
        mOneEt = (EditText) findViewById(R.id.phone);

        mTwoRl = (RelativeLayout) findViewById(R.id.view_two_rl);
        mTwoBtn = (TextView) findViewById(R.id.next_step_reset_password);
        mTwoEt = (EditText) findViewById(R.id.code);
        mTwoHintTv = (TextView) findViewById(R.id.hint_tv);

        mThreeRl = (RelativeLayout) findViewById(R.id.view_three_rl);
        mThreeBtn = (TextView) findViewById(R.id.complete);
        mThreeEt = (EditText) findViewById(R.id.new_password);

        mClose.setOnClickListener(this);
        mOneBtn.setOnClickListener(this);
        mTwoBtn.setOnClickListener(this);
        mThreeBtn.setOnClickListener(this);

    }


    private void initData() {
        showStep(ONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.next_step_verify_phone_num:
                mOneEtStr = mOneEt.getText().toString().trim();
                if (!proofInputInfo()) {
                    return;
                }
                showStep(TWO);
                String hint = MyApplication.getContext().getString(R.string.sms_verification_code_sent_to)+mOneEtStr;
                mTwoHintTv.setText(hint);

                break;

            case R.id.next_step_reset_password:
                mTwoEtStr = mTwoEt.getText().toString().trim();
                if (StringUtils.isEmpty(mTwoEtStr)) {
                    ToastUtils.show(R.string.please_input_code_null);
                }else{
                    showStep(THREE);
                }

                break;

            case R.id.complete:
                mThreeEtStr = mTwoEt.getText().toString().trim();
                if (StringUtils.isEmpty(mThreeEtStr)) {
                    ToastUtils.show(R.string.please_input_new_password_null);
                }else{
                    this.finish();
                    AndroidUtils.exitOutsideActvityAnim(this);
                }

                break;

            case R.id.close:
                this.finish();
                AndroidUtils.exitOutsideActvityAnim(this);

                break;

            default:
                break;
        }
    }


    private void showStep(int mStep) {

        mOneRl.setVisibility(View.GONE);
        mTwoRl.setVisibility(View.GONE);
        mThreeRl.setVisibility(View.GONE);

        switch (mStep) {

            case ONE:
                mOneRl.setVisibility(View.VISIBLE);
                mTitle.setText(MyApplication.getContext().getString(R.string.reset_password_one));

                break;

            case TWO:
                mTwoRl.setVisibility(View.VISIBLE);
                mTitle.setText(MyApplication.getContext().getString(R.string.reset_password_two));

                break;

            case THREE:
                mThreeRl.setVisibility(View.VISIBLE);
                mTitle.setText(MyApplication.getContext().getString(R.string.reset_password_three));

                break;

            default:
                break;
        }
    }


    /**
     * 校对用户输入是否合规
     */
    private boolean proofInputInfo() {
        if (StringUtils.isEmpty(mOneEtStr)) {
            ToastUtils.show(R.string.please_input_phone_null);
            return false;
        }
        if (!AndroidUtils.isPhoneNumberValid(mOneEtStr)) {
            ToastUtils.show(R.string.please_input_phone_again);
            mOneEt.setText("");
            mOneEtStr = null;
            return false;
        }

        return true;
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
