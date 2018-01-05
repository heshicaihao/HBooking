package com.nettactic.hotelbooking.pay;


import android.app.Activity;
import android.content.Context;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.constants.MyConstants;
import com.nettactic.hotelbooking.net.AsyncCallBack;
import com.nettactic.hotelbooking.net.NetHelper;
import com.nettactic.hotelbooking.utils.LogUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class WechatPay {
    private String TAG = "WechatPay";
    private String APPID = MyConstants.WX_APP_ID;
    private IWXAPI mApi;
    private String mAccountId;
    private String mAccountToken;

    private Activity mActivity;
    private String mGoodsNameStr;
    private String mGoodsInfoStr;
    private String mOutTradeNo;
    private String mTotalStr;

    public WechatPay() {
        super();
    }

    public WechatPay(Activity mActivity, Context mContext,
                     String mGoodsNameStr, String mGoodsInfoStr, String mOutTradeNo,
                     String mTotalStr, String mAccountId, String mAccountToken) {
        super();
        this.mActivity = mActivity;
        this.mGoodsNameStr = mGoodsNameStr;
        this.mGoodsInfoStr = mGoodsInfoStr;
        this.mOutTradeNo = mOutTradeNo;
        this.mTotalStr = mTotalStr;
        this.mAccountId = mAccountId;
        this.mAccountToken = mAccountToken;
        mApi = WXAPIFactory.createWXAPI(mContext, APPID);
        mApi.registerApp(APPID);
    }

    public void pay() {

        boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (isPaySupported) {
            sendPay();
        } else {
            ToastUtils.show(R.string.wechat_inpaysupported);
            // mActivity.finish();
        }

    }

    private void sendPay() {
        LogUtils.logd(TAG, "mOutTradeNo:" + mOutTradeNo);
        LogUtils.logd(TAG, "mGoodsNameStr:" + mGoodsNameStr);
        LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
        LogUtils.logd(TAG, "mAccountId:" + mAccountId);
        LogUtils.logd(TAG, "mAccountToken:" + mAccountToken);
        NetHelper.wechatPay(mOutTradeNo, mGoodsNameStr, mTotalStr, mAccountId, mAccountToken, new AsyncCallBack() {

            @Override
            public void onSuccess(String result) {
                LogUtils.logd(TAG, "sendPay+Success");
                resolveSendPay(result);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.logd(TAG, "sendPay+onFailure");
                ToastUtils.show(R.string.server_failure);
            }
        });
    }

    private void resolveSendPay(String json) {
        try {
            LogUtils.logd(TAG, "json:" + json.toString());
            JSONObject JSONObject = new JSONObject(json);
            JSONObject result = JSONObject.optJSONObject("result");
            LogUtils.logd(TAG, "result:" + result.toString());
            PayReq request = new PayReq();
            request.appId = APPID;
            request.partnerId = result.optString("partener_id");
            request.prepayId = result.optString("prepay_id");
            request.packageValue = result.optString("package");
            request.nonceStr = result.optString("nonce_str");
            request.timeStamp = result.optString("timestamp");
            request.sign = result.optString("sign");
            request.extData = "app data";
            mApi.sendReq(request);
            mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
