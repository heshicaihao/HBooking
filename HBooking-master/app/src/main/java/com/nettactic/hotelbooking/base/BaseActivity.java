package com.nettactic.hotelbooking.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.activity.LoginActivity;
import com.nettactic.hotelbooking.activity.OtherWebActivity;
import com.nettactic.hotelbooking.adapter.TypeFourEListViewAdapter;
import com.nettactic.hotelbooking.adapter.TypeOneEListViewAdapter;
import com.nettactic.hotelbooking.adapter.TypeThreeEListViewAdapter;
import com.nettactic.hotelbooking.adapter.TypeTwoEListViewAdapter;
import com.nettactic.hotelbooking.bean.UserBean;
import com.nettactic.hotelbooking.common.AppManager;
import com.nettactic.hotelbooking.common.UserController;
import com.nettactic.hotelbooking.dialog.CustomProgressDialog;
import com.nettactic.hotelbooking.dialog.FrameProgressDialog;
import com.nettactic.hotelbooking.dialog.UpdateDialog;
import com.nettactic.hotelbooking.utils.AndroidUtils;

import org.json.JSONArray;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = getClass().getName();
    public UserController mUserController;
    public UserBean user;
    public CustomProgressDialog dialog;
    public FrameProgressDialog frameDialog;
    private static AlphaAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAlphaAnimation();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AndroidUtils.exitActvityAnim(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }

    private void init() {
        AppManager.getAppManager().addActivity(this);
        mUserController = UserController.getInstance(this);
        user = new UserBean();
        dialog = new CustomProgressDialog(this);
        frameDialog = new FrameProgressDialog(this);
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 打开H5界面
     *
     * @param context
     */
    public void startOtherWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, OtherWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void startMainActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterAnimMain(this);

    }

    public void startActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterActvityAnim(this);

    }

    public void startOutsideActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterOutsideActvityAnim(this);

    }

    public void showFrameDialog() {
        frameDialog.show();
    }

    public void dismissFrameDialog() {
        frameDialog.dismiss();
    }

    public void showmeidialog() {

        dialog.show();
    }

    public void dismissmeidialog() {
        dialog.dismiss();
    }

    /**
     * 提示登录
     */
    public void hintLogin(Context context) {
        showLoginDialog(context);
    }


    /**
     * 显示对话框
     *
     * @param context
     */
    public void showLoginDialog(final Context context) {

        UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
        builder.setMessage(context.getString(R.string.you_no_login));
        builder.setTitle(context.getString(R.string.prompt_message));
        builder.setPositiveButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        builder.setNegativeButton(context.getString(R.string.now_login),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 设置你的操作事项
                        startActivity(context, LoginActivity.class);
                        dialog.dismiss();

                    }
                });

        builder.create().show();
    }

    /**
     * 调到拨打电话的拨号盘并输入
     *
     * @param num 电话号码
     */
    public void telCall(String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /**
     * 设置 可扩展列表
     * @param activity
     * @param data JSONArray 数据
     * @param listView ExpandableListView 控件
     * @param type 样式
     */
    public static void setMyELVAdapter(Activity activity, JSONArray data, ExpandableListView listView, int type) {
        BaseExpandableListAdapter mAdapter = null;
        switch (type) {
            case 1:
                mAdapter = new TypeOneEListViewAdapter(activity, data, listView);
                break;
            case 2:
                mAdapter = new TypeTwoEListViewAdapter(activity, data, listView);
                break;
            case 3:
                mAdapter = new TypeThreeEListViewAdapter(activity, data, listView);
                break;
            case 4:
                mAdapter = new TypeFourEListViewAdapter(activity, data, listView);
                break;
            default:
                mAdapter = new TypeOneEListViewAdapter(activity, data, listView);
                break;
        }
        listView.setAdapter(mAdapter);
    }

    /**
     * 淡入淡出开始动画
     */
    public static void startAlphaAnimation(View textShow){
        if ( animation == null ) {
            // 创建一个AlphaAnimation对象
            animation = new AlphaAnimation(0.01f, 1f);
            // 设置动画执行的时间（单位：毫秒）
            animation.setDuration(1000);
            // 设置重复次数
//			animation.setRepeatCount(5);
        }
        // 把动画设置给控件
        textShow.setAnimation(animation);
        // 开始动画
        animation.start();
    }

    /**
     * 结束动画
     */
    private void cancelAlphaAnimation(){
        if ( animation!=null ) {
            animation.cancel();
        }
    }


}
