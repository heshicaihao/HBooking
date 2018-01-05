package com.nettactic.hotelbooking.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.nettactic.hotelbooking.MainActivity;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.adapter.SuperViewPagerAdapter;
import com.nettactic.hotelbooking.base.BaseActivity;
import com.nettactic.hotelbooking.utils.AnimationUtil;
import com.nettactic.hotelbooking.utils.SharedpreferncesUtil;
import com.nettactic.hotelbooking.widget.ViewPagerPoint;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 欢迎界面
 */
public class WelcomeActivity extends BaseActivity implements
        View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewpager;
    private ViewPagerPoint mPagerPoint;
    private TextView mJump;
    private TextView mStart;

    private SuperViewPagerAdapter mAdapter;
    private List<View> mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.jump_text:
                if (SharedpreferncesUtil.getGuided(getApplicationContext())) {
                    WelcomeActivity.this.finish();
                    AnimationUtil.getIntence().cancelAlphaAnimation();
                } else {
                    SharedpreferncesUtil.setGuided(getApplicationContext());
                    startActivity(this, MainActivity.class);
                    AnimationUtil.getIntence().cancelAlphaAnimation();
                    WelcomeActivity.this.finish();
                }
                break;
            case R.id.start_text:
                if (SharedpreferncesUtil.getGuided(getApplicationContext())) {
                    WelcomeActivity.this.finish();
                    AnimationUtil.getIntence().cancelAlphaAnimation();
                } else {
                    SharedpreferncesUtil.setGuided(getApplicationContext());
                    startActivity(this, MainActivity.class);
                    AnimationUtil.getIntence().cancelAlphaAnimation();
                    WelcomeActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }


    protected void initView() {
        mViewpager = (ViewPager) findViewById(R.id.my_viewpager);
        mPagerPoint = (ViewPagerPoint) findViewById(R.id.viewpage_item);
        mJump = (TextView) findViewById(R.id.jump_text);
        mStart = (TextView) findViewById(R.id.start_text);

        mViewpager.addOnPageChangeListener(this);
        mJump.setOnClickListener(this);
        mStart.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPagerPoint.notifyDataSetChanged(position);
        setItemShow(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initData() {

        mViews = getViewData();

        mPagerPoint.setBitmap(R.mipmap.no_icon, R.mipmap.yes_icon);
        mPagerPoint.setCount(mViews.size());
        mPagerPoint.notifyDataSetChanged(0);

        mViewpager.setOffscreenPageLimit(3);
        mAdapter = new SuperViewPagerAdapter(mViews);
        mViewpager.setAdapter(mAdapter);
        initUserInfo();

    }

    /**
     * 向List加 数据
     *
     * @return
     */
    public List<View> getViewData() {
        mViews = new ArrayList<View>();

        View view1 = View.inflate(this, R.layout.viewpager_one, null);
        View view2 = View.inflate(this, R.layout.viewpager_two, null);
        View view3 = View.inflate(this, R.layout.viewpager_three, null);

        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);

        return mViews;
    }

    /**
     * 根据传入参数设置显示 和 动画
     *
     * @param index
     */
    public void setItemShow(int index) {
        if (index == mViews.size() - 1) {
            AnimationUtil.getIntence().startAlphaAnimation(mStart);
            mPagerPoint.setVisibility(View.GONE);
            mJump.setVisibility(View.GONE);
            mStart.setVisibility(View.VISIBLE);
        } else {
            AnimationUtil.getIntence().cancelAlphaAnimation();
            mPagerPoint.setVisibility(View.VISIBLE);
            mJump.setVisibility(View.VISIBLE);
            mStart.setVisibility(View.GONE);

        }
    }

    /**
     * 初始化 用户信息
     */
    private void initUserInfo() {
        user.setUname("");
        user.setPassword("");
        user.setId("");
        user.setToken("");
//        user.setIs_login(false);
        user.setIs_login(true);
        user.setTemp_id("");
        user.setTemp_token("");
        user.setIs_temp_login(false);
        user.setOpen_id("");
        user.setType("");
        user.setUsername("");
        user.setHead_portrait("");
        user.setIs_three_login(false);
        user.setIs_order_null(true);
        mUserController.saveUserInfo(user);
    }

}
