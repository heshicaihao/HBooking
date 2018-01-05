package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

public class FillOtherDemandsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mCancelBtn;
    private TextView mCompleteBtn;
    private EditText mOtherDemandsEdit;
    private  String mOtherDemandsStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_other_demands);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mCompleteBtn = (TextView) findViewById(R.id.complete_btn);
        mOtherDemandsEdit = (EditText) findViewById(R.id.other_demands_edit);

        mTitle.setText(this.getResources().getString(R.string.fill_other_demands));
        mCancelBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
    }

    private void initData() {
        getIntentData();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.complete_btn:
                goback();

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
     * 从Intent获取数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mOtherDemandsStr = intent.getStringExtra("other_demands");
        if (MyApplication.getContext().getString(R.string.nothing).equals(mOtherDemandsStr)){
            mOtherDemandsStr = "";
        }
        mOtherDemandsEdit.setText(mOtherDemandsStr);

    }

    /**
     * 返回 FillOrderActivity
     */
    private void goback() {
        mOtherDemandsStr =  mOtherDemandsEdit.getText().toString().trim();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("other_demands",mOtherDemandsStr);
        FillOtherDemandsActivity.this.setResult(
                FillOrderActivity.RESULT_OK, intent);
        this.finish();
        AndroidUtils.exitActvityAnim(this);
    }

}
