package com.nettactic.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.common.UserController;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.StatusBarUtils;
import com.nettactic.hotelbooking.widget.AddAndSubView;

public class FillRoomNumActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mCancelBtn;
    private TextView mCompleteBtn;
    private AddAndSubView mAddAndSubView;
    private int mRoomNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_room_num);
        StatusBarUtils.myStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
        mCompleteBtn = (TextView) findViewById(R.id.complete_btn);
        mAddAndSubView = (AddAndSubView) findViewById(R.id.room_num);

        mTitle.setText(this.getResources().getString(R.string.revise_room_num));
        mAddAndSubView.setNum(1);
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
        String room_numStr = intent.getStringExtra("room_num");
        mRoomNum = Integer.valueOf(room_numStr);
        mAddAndSubView.setNum(mRoomNum);

    }


    /**
     * 返回 FillOrderActivity
     */
    private void goback() {
        mRoomNum = mAddAndSubView.getNum();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("room_num", mRoomNum + "");
        FillRoomNumActivity.this.setResult(
                FillOrderActivity.RESULT_OK, intent);
        this.finish();
        AndroidUtils.exitActvityAnim(this);
    }

}
