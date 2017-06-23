package com.highnes.tour.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
import com.highnes.tour.ui.activities.PayActivity;
import com.highnes.tour.ui.activities.PaySuccessActivity;
import com.highnes.tour.ui.activities.home.TimeDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailOrderActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketSearchActivity;
import com.highnes.tour.ui.activities.home.tour.FilterTour1Activity;
import com.highnes.tour.ui.activities.home.tour.FilterTour2Activity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailOrderActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailOrderActivity;
import com.highnes.tour.ui.activities.home.tour.TourActivity;
import com.highnes.tour.ui.fragment.main.NearbyFragment;
import com.highnes.tour.utils.AppManager;
import com.highnes.tour.utils.LogUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 * 
 * @author Administrator
 * 
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.WIN_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			LogUtils.d("--支付回调");
			Constants.WIN_PAY_CODE = -999;
			switch (resp.errCode) {
			// 正确返回
			case BaseResp.ErrCode.ERR_OK:
				LogUtils.d("--aa--支付成功999");
				Constants.WIN_PAY_CODE = 999;
				if (0 == Constants.WIN_PAY_TYPE) {
					// 发送广播
					Intent mIntent = new Intent(PayActivity.ACTION_CALLBACK_PAY);
					sendBroadcast(mIntent);
				} else if (1 == Constants.WIN_PAY_TYPE) {
					// 支付成功广播通知
					Intent mIntent = new Intent();
					mIntent.setAction(PHPWebViewActivity.ACTION_CALLBACK_PAY_SUCCEED);
					sendBroadcast(mIntent);
				}
				break;
			// 支付失败
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				LogUtils.d("--支付失败");
				break;
			// 认证被否决
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				LogUtils.d("--认证被否决");
				break;
			// 一般错误
			case BaseResp.ErrCode.ERR_COMM:
				LogUtils.d("--一般错误" + BaseResp.ErrCode.ERR_COMM);
				break;
			// 不支持错误
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				LogUtils.d("--不支持错误");
				break;
			// 用户取消
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				LogUtils.d("--用户取消");
				break;

			default:
				break;
			}
			WXPayEntryActivity.this.finish();
		}
	}
}