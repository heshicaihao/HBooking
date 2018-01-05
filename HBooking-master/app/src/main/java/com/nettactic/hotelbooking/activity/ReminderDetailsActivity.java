package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

/***
 * 温馨提示
 *
 */
public class ReminderDetailsActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTitle;
    private TextView mCancelBtn;
    private TextView mCompleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mCompleteBtn =  (TextView) findViewById(R.id.complete_btn);

        mTitle.setText(this.getResources().getString(R.string.see_reminder));
        mCancelBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
    }

    private void initData() {

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

}
