package com.nettactic.hotelbooking.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.activity.PaySuccessActivity;
import com.nettactic.hotelbooking.constants.MyConstants;
import com.nettactic.hotelbooking.utils.AndroidUtils;
import com.nettactic.hotelbooking.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI mApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_pay_result);

		mApi = WXAPIFactory.createWXAPI(this, MyConstants.WX_APP_ID);
		mApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				gotoPaySuccess();
				this.finish();
			} else {
				ToastUtils.show(R.string.pay_failure);
				AndroidUtils.finishActivity(this);
			}
		}
	}

	private void gotoPaySuccess() {
		Intent intent = new Intent(this, PaySuccessActivity.class);
		this.startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

}