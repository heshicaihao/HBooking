package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;

public class JumpActivity extends BaseActivity implements View.OnClickListener {

    private TextView mJumpBtn;
    private TimeCount mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        initView();
    }

    private void initView() {
        mJumpBtn = (TextView) findViewById(R.id.jump_btn);

        mTime = new TimeCount(4000, 1000);
        mTime.start();
        mJumpBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_btn:
                JumpActivity.this.finish();
                gotoMainActivity();
                mTime.cancel();

                break;

            default:

                break;

        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mJumpBtn.setText("");
            JumpActivity.this.finish();
            gotoMainActivity();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String text = "跳过（" + millisUntilFinished / 1000
                    + "）";
            mJumpBtn.setText(text);
        }
    }

    /**
     * 跳转到 MainActivity
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(JumpActivity.this, MainActivity.class);
        JumpActivity.this.startActivity(intent);
        AndroidUtils.enterAnimMain(JumpActivity.this);
    }

}
