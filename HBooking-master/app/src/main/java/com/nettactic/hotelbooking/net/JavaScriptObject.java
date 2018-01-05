package com.nettactic.hotelbooking.net;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * H5 调用的类
 *
 * @author heshicaihao
 */
public class JavaScriptObject {

    private Context mContxt;
    private Activity mActivity;

    public JavaScriptObject(Context mContxt, Activity mActivity, String mUser_id) {
        this.mContxt = mContxt;
        this.mActivity = mActivity;
    }

    @JavascriptInterface
    public void gotoPromoteShare() {
//		share();
    }


}
