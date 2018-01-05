package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.MyApplication;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;

/**
 * 修改资料
 */
public class ReviseMyDataActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_my_data);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mFloatingActionButton = (ImageButton) findViewById(R.id.floating_btn);

        startAlphaAnimation(mFloatingActionButton);
        mTitle.setText(MyApplication.getContext().getString(R.string.revise_my_data));
        mFloatingActionButton.setOnClickListener(this);

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_btn:
                mFloatingActionButton.setVisibility(View.GONE);
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mFloatingActionButton.setVisibility(View.GONE);
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }


}
