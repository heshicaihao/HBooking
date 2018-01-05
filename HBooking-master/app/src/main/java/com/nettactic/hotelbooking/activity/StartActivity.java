package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.ScreenUtils;
import com.nettactic.hotelbooking.utils.SharedpreferncesUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 开始界面
 */
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        initData();

    }

    protected void initView() {
        DelayedJumpNext();
    }

    protected void initData() {
        ScreenUtils.getScreenInfo(this);
    }

    /**
     * 延时跳转下一页
     */
    private void DelayedJumpNext() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    public void run() {
                        if (SharedpreferncesUtil
                                .getGuided(getApplicationContext())) {
//                            gotoMainActivity();
                            gotoJumpActivity();
                        } else {
//                            gotoWelcomeActivity();
                            gotoJumpActivity();
                        }
                    }
                });
            }
        }, 2000);
    }

    /**
     * 跳转到 JumpActivity
     */
    private void gotoJumpActivity() {
        Intent intent = new Intent(this, JumpActivity.class);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
        StartActivity.this.finish();
    }

    private void gotoMainActivity() {
        int CurrentTabNum = 0;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CurrentTabNum", CurrentTabNum);
        startActivity(intent);
        StartActivity.this.finish();
    }

    private void gotoWelcomeActivity() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        StartActivity.this.startActivity(intent);
        StartActivity.this.finish();
    }

    @Override
    protected void onResume() {
//        JPushInterface.onResume(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
//        JPushInterface.onPause(this);
        super.onPause();

    }
}
