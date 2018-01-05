package com.nettactic.hotelbooking.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;

/**
 * 加减显示控件
 *
 * @author heshicaihao
 *         2017-05-03 15:48:23
 */
public class ChoiceSexView extends LinearLayout {
    Context context;
    RelativeLayout mManRl;
    TextView mManTv;
    ImageView mManIv;
    RelativeLayout mWomanRl;
    TextView mWomanTv;
    ImageView mWomanIv;

    boolean isMan = false;

    public ChoiceSexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_choice_sex, this);
        isMan = true;
        control();

    }

    private void control() {
        mManRl = (RelativeLayout) findViewById(R.id.man_rl);
        mManTv = (TextView) findViewById(R.id.man_tv);
        mManIv = (ImageView) findViewById(R.id.man_iv);
        mWomanRl = (RelativeLayout) findViewById(R.id.woman_rl);
        mWomanTv = (TextView) findViewById(R.id.woman_tv);
        mWomanIv = (ImageView) findViewById(R.id.woman_iv);

        choiceSex(isMan);
        setViewListener();
    }


    /**
     * 获取 性别
     *
     * @return
     */
    public boolean getSex() {
        return isMan;
    }

    /**
     * 设置性别
     *
     * @param isMan
     */
    public void setSex(boolean isMan) {
        this.isMan = isMan;
        choiceSex(isMan);
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        mManRl.setOnClickListener(new OnManClickListener());
        mWomanRl.setOnClickListener(new OnWomanClickListener());

    }

    private void choiceSex(boolean IsMan) {
        mManRl.setBackgroundResource(R.drawable.btn_left_bg_normal);
        mManIv.setImageResource(R.mipmap.man_normal);
        mManTv.setTextColor(this.getResources().getColor(R.color.text_color_gray_a));
        mWomanRl.setBackgroundResource(R.drawable.btn_right_bg_normal);
        mWomanIv.setImageResource(R.mipmap.woman_normal);
        mWomanTv.setTextColor(this.getResources().getColor(R.color.text_color_gray_a));

        if (IsMan) {
            mManRl.setBackgroundResource(R.drawable.btn_left_bg_pressed);
            mManIv.setImageResource(R.mipmap.man_pressed);
            mManTv.setTextColor(this.getResources().getColor(R.color.white));

        } else {
            mWomanRl.setBackgroundResource(R.drawable.btn_right_bg_pressed);
            mWomanIv.setImageResource(R.mipmap.woman_pressed);
            mWomanTv.setTextColor(this.getResources().getColor(R.color.white));

        }

    }

    /**
     * 男士 按钮事件监听器
     */
    class OnManClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (!isMan) {
                isMan = true;
                choiceSex(isMan);
            }
        }
    }

    /**
     * 女士 按钮事件监听器
     */
    class OnWomanClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (isMan) {
                isMan = false;
                choiceSex(isMan);
            }
        }
    }
}
