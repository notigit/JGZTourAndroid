package com.highnes.tour.ui.activities.home;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.MsgAdapter;
import com.highnes.tour.beans.home.MsgInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.HomeFragment;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.listview.swipe.SwipeMenu;
import com.highnes.tour.view.listview.swipe.SwipeMenuCreator;
import com.highnes.tour.view.listview.swipe.SwipeMenuItem;
import com.highnes.tour.view.listview.swipe.SwipeMenuListView;
import com.highnes.tour.view.listview.swipe.SwipeMenuListView.OnMenuItemClickListener;

/**
 * <PRE>
 * 作用:
 *    个人中心页面..预约记录。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MsgActivity extends BaseTitleActivity {

	// 菜单
	private SwipeMenuListView lvOrder;
	private MsgAdapter adapterMsg;
	private LinearLayout llNotData;

	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 50;
	// 总页数
	private int pageTotle = 0;

	@Override
	protected void onResume() {
		super.onResume();
		requestByOrderMsg();
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_msg;
	}

	@Override
	protected void findViewById2T() {
		lvOrder = getViewById(R.id.lv_msg);
		llNotData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("消息");
		showBackwardView("", SHOW_ICON_DEFAULT);
		initListView();
		adapterMsg = new MsgAdapter(mContext);
		lvOrder.setAdapter(adapterMsg);
		lvOrder.setEmptyView(llNotData);
	}

	@Override
	protected void setListener2T() {
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}
	
	@Override
	public void onClick(View v) {
//		super.onClick(v);
		switch (v.getId()) {
		//返回
		case R.id.ll_title_back19:
		case R.id.ll_title_back:
			Intent mIntent = new Intent();
			mIntent.setAction(HomeFragment.ACTION_CALLBACK_HOME);
			sendBroadcast(mIntent);
			finishActivity();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent mIntent = new Intent();
			mIntent.setAction(HomeFragment.ACTION_CALLBACK_HOME);
			sendBroadcast(mIntent);
			finishActivity();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private void initListView() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		lvOrder.setMenuCreator(creator);

		// step 2. listener item click event
		lvOrder.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				MsgInfo.NoticeInfo info = (MsgInfo.NoticeInfo) lvOrder.getAdapter().getItem(position);
				cancelMsgOrder(info.ID, position, info.Time);
			}
		});
		lvOrder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MsgInfo.NoticeInfo info = (MsgInfo.NoticeInfo) lvOrder.getAdapter().getItem(position);
				String userId = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 1902);
				bundle.putString("mTitle", "消息详情");
				bundle.putString("mUrl", String.format(UrlSettings.URL_H5_HOME_MSG_DETAIL, userId, info.NoticeID));
				openActivity(WebViewTitleActivity.class, bundle);
			}
		});
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	/**
	 * 
	 * @param id
	 *            预约的id
	 * @param position
	 *            位置
	 * @param time
	 *            时间
	 */
	private void cancelMsgOrder(String id, int position, String time) {
		// TODO 判断时间
		requestByOrderMsgCancel(id, position);
	}

	// -----------------------网络请求-----------------------

	/**
	 * Msg 网络请求
	 * 
	 */
	private void requestByOrderMsg() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_MSG, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrderMsg(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Order JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOrderMsg(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				MsgInfo info = GsonUtils.json2T(result, MsgInfo.class);
				adapterMsg.setList(info.Notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Order 网络请求
	 * 
	 */
	private void requestByOrderMsgCancel(String id, final int position) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("ID", id);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_MSG_DEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrderMsgCancel(result, position);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Order JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOrderMsgCancel(String result, int position) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				adapterMsg.remove(position);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
