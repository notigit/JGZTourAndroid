package com.highnes.tour.ui.activities.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.highnes.tour.R;
import com.highnes.tour.beans.my.LoginUserInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.MyFragment;
import com.highnes.tour.ui.fragment.main.NearbyFragment;
import com.highnes.tour.utils.ExampleUtil;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.MD5Utils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * <PRE>
 * 作用:
 *    登录页面。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class LoginActivity extends BaseTitleActivity {

	/** 改变第1个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_ONE = 0;
	/** 改变第2个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_TWO = 1;
	private int currPager = CHANGE_MENU_ONE;
	private ViewPager mTabPager; // 当前卡片页内容
	// 所有的页面
	private List<View> views;
	// 菜单下划线宽度
	private int lineWidth;
	// 菜单下划线
	private View viewLine;
	// 菜单
	private TextView[] tvMenu = new TextView[2];

	// ------------手机号登录--------------
	/** 是否显示密码 */
	private boolean isShowPwd = false;
	private ImageView ivShowPwd;
	/** 密码 */
	private EditText etPhone, etPhone2, etPwd, etCode;
	private TextView tvCode;
	private String mCode = "";

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (count > 0 && timer != null) {
				timer.cancel();
				timer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 填充手机号码到文本框
		String phone = SPUtils.get(mContext, PreSettings.USER_PHONE.getId(), PreSettings.USER_PHONE.getDefaultValue()).toString();
		etPhone.setText(phone);
		etPhone.setSelection(phone.length());
		etPhone2.setText(phone);
		etPhone2.setSelection(phone.length());
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_login;
	}

	@Override
	protected void findViewById2T() {
		mTabPager = getViewById(R.id.pager);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		viewLine = getViewById(R.id.line);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("登录");
		showBackwardView("", SHOW_ICON_DEFAULT);

		initPager();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setListener2T() {
		tvMenu[0].setOnClickListener(this);
		tvMenu[1].setOnClickListener(this);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 菜单1
		case R.id.tv_menu_0:
			currPager = CHANGE_MENU_ONE;
			selectPage(CHANGE_MENU_ONE);
			mTabPager.setCurrentItem(0);
			break;
		// 菜单2
		case R.id.tv_menu_1:
			currPager = CHANGE_MENU_TWO;
			selectPage(CHANGE_MENU_TWO);
			mTabPager.setCurrentItem(1);
			break;
		// 是否显示密码
		case R.id.iv_show_pwd:
			if (isShowPwd) {
				ivShowPwd.setImageResource(R.drawable.ic_look_no);
				etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				etPwd.setSelection(etPwd.getText().length());
			} else {
				ivShowPwd.setImageResource(R.drawable.ic_look);
				etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				etPwd.setSelection(etPwd.getText().length());
			}
			isShowPwd = !isShowPwd;
			break;
		// 去注册
		case R.id.tv_register:
			openActivity(RegisterActivity.class);
			break;
		// 账号登录
		case R.id.btn_login:
			processLogin();
			break;
		// 短信登录
		case R.id.btn_login2:
			processLoginPhone();
			break;
		// 忘记密码
		case R.id.tv_get_pwd:
			openActivity(GetPwdActivity.class);
			break;
		// 获取验证码
		case R.id.tv_get_code:
			judgeCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化pager
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void initPager() {
		LayoutInflater mLi = LayoutInflater.from(mContext);
		View view1 = mLi.inflate(R.layout.include_my_login, null);
		View view2 = mLi.inflate(R.layout.include_my_login_fast, null);
		// 每个页面的view数据
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// 计算滑动条的宽度
		lineWidth = getWindowManager().getDefaultDisplay().getWidth() / views.size();
		viewLine.getLayoutParams().width = lineWidth;
		viewLine.requestLayout();

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		initPagerView(0, view1);
		initPagerView(1, view2);
	}

	/**
	 * 初始化Pager
	 * 
	 * @param view
	 */
	private void initPagerView(int index, View view) {
		switch (index) {
		case 0:
			etPwd = (EditText) view.findViewById(R.id.et_pwd);
			etPhone = (EditText) view.findViewById(R.id.et_phone);
			ivShowPwd = (ImageView) view.findViewById(R.id.iv_show_pwd);
			ivShowPwd.setOnClickListener(this);
			view.findViewById(R.id.btn_login).setOnClickListener(this);
			view.findViewById(R.id.tv_register).setOnClickListener(this);
			view.findViewById(R.id.tv_get_pwd).setOnClickListener(this);
			break;
		case 1:
			etPhone2 = (EditText) view.findViewById(R.id.et_phone2);
			etCode = (EditText) view.findViewById(R.id.et_code);
			tvCode = (TextView) view.findViewById(R.id.tv_get_code);
			tvCode.setOnClickListener(this);
			view.findViewById(R.id.btn_login2).setOnClickListener(this);
			break;

		default:
			break;
		}
	}

	/**
	 * 选择的页面，修改字体颜色
	 * 
	 * @param selectPage
	 *            选择的页
	 */
	private void selectPage(int selectPage) {
		tvMenu[0].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
		tvMenu[1].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
		tvMenu[selectPage].setTextColor(ResHelper.getColorById(mContext, R.color.font_green));
	}

	/*
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int selectPage) {
			switch (selectPage) {
			// 选中第1页
			case CHANGE_MENU_ONE:
				currPager = CHANGE_MENU_ONE;
				selectPage(CHANGE_MENU_ONE);
				break;
			// 选中第2页
			case CHANGE_MENU_TWO:
				currPager = CHANGE_MENU_TWO;
				selectPage(CHANGE_MENU_TWO);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 移动滑动条
			float tagerX = arg0 * lineWidth + arg2 / views.size();
			ViewPropertyAnimator.animate(viewLine).translationX(tagerX).setDuration(0);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 处理登录
	 */
	private void processLogin() {
		String phone = etPhone.getText().toString().trim();
		String password = etPwd.getText().toString().trim();
		// 手机号码验证
		if (StringUtils.isEmpty(phone)) {
			showToast("您还未输入手机号码");
			return;
		} else {
			if (!RegExpUtils.isMobileNO(phone)) {
				showToast("请输入正确的手机号码");
				return;
			}
		}
		// 密码验证
		if (StringUtils.isEmpty(password)) {
			showToast("您还未输入密码");
			return;
		} else {
			if (!RegExpUtils.isPassword(password)) {
				showToast("请输入6-12位密码，字母数字和特殊符号");
				return;
			}
		}
		requestByLogin(phone, MD5Utils.string2MD5(password));
	}

	/**
	 * 处理denglu
	 */
	private void processLoginPhone() {
		String phone = etPhone2.getText().toString().trim();
		String code = etCode.getText().toString().trim();
		// 手机号码验证
		if (StringUtils.isEmpty(phone)) {
			showToast("您还未输入手机号码");
			return;
		} else {
			if (!RegExpUtils.isMobileNO(phone)) {
				showToast("请输入正确的手机号码");
				return;
			}
		}
		if (StringUtils.isEmpty(code)) {
			showToast("请输入验证码");
			return;
		} else {
			if (!mCode.equals(code)) {
				showToast("验证码不正确");
				return;
			}
		}
		requestByLoginPhone(phone);
	}

	// -----------------------网络请求-----------------------
	/**
	 * Login 网络请求
	 * 
	 * @param Phone
	 *            手机号
	 * @param Password
	 *            密码(MD5加密后)
	 */
	private void requestByLogin(String Phone, final String Password) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Phone", Phone);
		paramDatas.put("Password", Password);
		new NETConnection(mContext, UrlSettings.URL_USER_LOGIN, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonLogin(result, Password);
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
	 * Login 网络请求
	 * 
	 * @param Phone
	 *            手机号
	 */
	private void requestByLoginPhone(String Phone) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Phone", Phone);
		new NETConnection(mContext, UrlSettings.URL_USER_LOGIN_PHONE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonLogin(result, null);
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
	 * Login JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonLogin(String result, String Password) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("登录成功!");
			LoginUserInfo info = GsonUtils.json2T(result, LoginUserInfo.class);
			// 登录状态
			SPUtils.put(mContext, PreSettings.USER_TYPE.getId(), 1);
			// 密码
			if (!StringUtils.isEmpty(Password)) {
				SPUtils.put(mContext, PreSettings.USER_PWD.getId(), Password);
			}
			// 用户ID
			SPUtils.put(mContext, PreSettings.USER_ID.getId(), info.value.get(0).UserID);
			// 生日
			SPUtils.put(mContext, PreSettings.USER_BIRTHDAY.getId(), info.value.get(0).Birth);
			// 邮箱
			SPUtils.put(mContext, PreSettings.USER_EMAIL.getId(), info.value.get(0).Email);
			// 昵称
			SPUtils.put(mContext, PreSettings.USER_NICKNAME.getId(), info.value.get(0).UserName);
			// 头像
			SPUtils.put(mContext, PreSettings.USER_AVATOR.getId(), info.value.get(0).HeadImg);
			// 姓名
			SPUtils.put(mContext, PreSettings.USER_NAME.getId(), info.value.get(0).Name);
			// 积分
			SPUtils.put(mContext, PreSettings.USER_POINT.getId(), info.value.get(0).Ponit);
			// 性别
			SPUtils.put(mContext, PreSettings.USER_SEX.getId(), info.value.get(0).getSex());
			// 手机号
			SPUtils.put(mContext, PreSettings.USER_PHONE.getId(), info.value.get(0).Phone);
			// PHP id
			SPUtils.put(mContext, PreSettings.USER_NUNBERID.getId(), info.value.get(0).NumberID);
			Intent mIntent = new Intent();
			MyFragment.isLoad = true;
			mIntent.setAction(MyFragment.ACTION_CALLBACK_MY);
			sendBroadcast(mIntent);
			//H5登录
			Intent mIntent2 = new Intent();
			NearbyFragment.isLoad = true;
			mIntent2.setAction(NearbyFragment.ACTION_CALLBACK_NEARBY);
			sendBroadcast(mIntent2);
			// 调用JPush API设置Alias
			setAlias(info.value.get(0).UserID);
			JPushInterface.resumePush(mContext);
			finishActivity();
		} else {
			showToast("用户名或密码错误!");
		}
	}

	/**
	 * RegCode 网络请求
	 * 
	 */
	private void requestByRegCode(final String Phone) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("to", Phone);
		new NETConnection(mContext, UrlSettings.URL_USER_REG_CODE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonRegCode(result);
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
	 * RegCode JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonRegCode(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			mCode = JsonUtils.getString(result, "number", "");
			showToast("验证码发送成功!");
		} else {
			showToast("验证码发送失败!");
		}
	}

	// ----------定时器------------------------------------------------------
	// 是否能点击获取验证码
	private boolean isGetCode = true;
	private int count = 60;
	@SuppressLint("HandlerLeak")
	Handler handlerTime = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (count > 0) {
					tvCode.setText("(" + Integer.toString(count--) + ")秒");
					isGetCode = false;
				} else {
					tvCode.setText("获取验证码");
					timer.cancel();
					isGetCode = true;
					if (task != null) {
						task.cancel(); // 将原任务从队列中移除
					}
				}
			}
			super.handleMessage(msg);
		};
	};
	// 定时器
	Timer timer;
	TimerTask task;

	/**
	 * 判断验证码
	 */
	private void judgeCode() {
		if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
			showToast("请输入手机号码");
			return;
		} else {
			if (RegExpUtils.isMobileNO(etPhone.getText().toString().trim())) {
				if (isGetCode) {
					requestByRegCode(etPhone.getText().toString().trim());
					count = 60;
					timer = new Timer();
					task = new TimerTask() {
						@Override
						public void run() {
							// 需要做的事:发送消息
							Message message = new Message();
							message.what = 1;
							handlerTime.sendMessage(message);
						}
					};
					timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
				}
			} else {
				showToast("请输入正确的手机号码");
			}
		}
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>以下为极光推送 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private final static int MSG_SET_ALIAS = 2001;
	private final static int MSG_SET_TAGS = 2002;
	public static boolean isForeground = false;
	public static final String MESSAGE_RECEIVED_ACTION = "com.highnes.tour.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void setAlias(String alias) {
		if (TextUtils.isEmpty(alias)) {
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			return;
		}
		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
				break;
			}
			super.handleMessage(msg);
		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				LogUtils.i("别名设置成功" + alias);
				break;

			case 6002:
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				} else {
					LogUtils.i("没有网络");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				LogUtils.e(logs);
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				LogUtils.i(logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				LogUtils.i(logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
				} else {
					LogUtils.i("No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				LogUtils.e(logs);
			}
		}

	};
}
