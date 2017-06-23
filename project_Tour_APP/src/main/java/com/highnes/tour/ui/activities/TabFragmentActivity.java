package com.highnes.tour.ui.activities;

/**
 * 
 * <PRE>
 * 
 * 	             :=O8EEE88OOOOOOOOOO8Eo~
 * 	            ~;:I===+O8OOoOOOOOO8OOO8=
 * 	         ;ii==i====II;Ii8OOOO+ii++oO8#+
 * 	       :i===i====i+O8E+IOEE+
 * 	      I======ii=I:~.......I
 * 	    ~=i==iiii;....~~~~~::~~::~`
 * 	  ~i==ii;~....~:~~~~~~::~.``
 * 	 =+i=i:..~~~~~~~~~~~~.                      ``
 * 	 ~  ;=i+i~~~~~~~~:::~`                       ~
 * 	  ;:~==i~.~~~~~:.  .:.                 `i`   ;~ ~
 * 	  =:`=i;.~~~~~~     `                   ;i  :~: ;=
 * 	  ;i Ii:.:~~~~                          ~===;I: Io
 * 	   i; i;.~~~~                           ~ii=::= +O
 * 	 i ;i: I.~~~                            ;===.=..O+
 * 	 i=i==I ~~:.                           `i=i~~E`+O=
 * 	 ~i===== .:` i8.                       ==i:`#~;OO~
 * 	  ;ii=iii `~ ~OO=                     :i=`~E;:OOo
 * 	   :=== .=    +OO8I                 `;I .o8;:OO8
 * 	    .i=I.OE8OOOoOOO8o;`            .~~I8OOIiOOO
 * 	      =ii`~EOOOOOOOOO888oiI;:::;=+OO88OOOOOO8+
 * 	       .=i; IEOOoOOOOOOOOOO8888OOOOOOOOOoO8o`
 * 	          IiI ~iE8OOOooOOOOOOOOoOOOOOO888=`
 * 	            .Ii:`:=o8E8O8OOOOO88OO888+:`
 * 	                `.``  `~:;IIIII;:.`
 *                
 *                
 * 	 Code is far away from bug with the animal protecting.
 * 	  海豚保佑 代码永无BUG.
 * 
 * </PRE>
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.highnes.tour.MainActivity;
import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.fragment.main.FindFragment;
import com.highnes.tour.ui.fragment.main.FootFragment;
import com.highnes.tour.ui.fragment.main.HomeFragment;
import com.highnes.tour.ui.fragment.main.MyFragment;
import com.highnes.tour.ui.fragment.main.NearbyFragment;
import com.highnes.tour.utils.AppManager;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ExampleUtil;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.NotificationHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.view.ToastDialog;
import com.highnes.tour.view.viewpager.CustomViewPager;

/**
 * <PRE>
 * 作用:
 *    app主框架页面。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-15   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TabFragmentActivity extends BaseActivity {
	// 主页的4个页面
	private List<Fragment> mFragment;

	public static final int TAB_page0 = 0;
	public static final int TAB_page1 = 1;
	public static final int TAB_page2 = 2;
	public static final int TAB_page3 = 3;
	public static final int TAB_page4 = 4;

	private RadioButton radio0, radio1, radio2, radio3, radio4;
	private CustomViewPager viewPager;
	// 当前的页面
	private int currPage = TAB_page0;
	private int lastPage = currPage;

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
		// if (currPage == TAB_page1) {
		// // 重新加载周边
		// LogUtils.d("--重新加载周边");
		// Intent mIntent = new Intent();
		// NearbyFragment.isLoad = true;
		// mIntent.setAction(NearbyFragment.ACTION_CALLBACK_NEARBY);
		// sendBroadcast(mIntent);
		// }
	}

	@Override
	protected int getLayoutId() {
		openActivity(MainActivity.class);
		return R.layout.activity_tab_fragment;
	}

	@Override
	protected void findViewById() {
		viewPager = getViewById(R.id.viewpager);
		radio0 = getViewById(R.id.radio_0);
		radio1 = getViewById(R.id.radio_1);
		radio2 = getViewById(R.id.radio_2);
		radio3 = getViewById(R.id.radio_3);
		radio4 = getViewById(R.id.radio_4);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setFullStatusBar();
		mFragment = new ArrayList<Fragment>();
		mFragment.add(new HomeFragment());
		// mFragment.add(new NearbyFragment());
		mFragment.add(new Fragment());
		mFragment.add(new FindFragment());
		mFragment.add(new FootFragment());
		mFragment.add(new MyFragment());
		// 设置不可以滑动..周边页面原因
		viewPager.setPagingEnabled(false);
		FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragment);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(currPage);
		// 设置viewPager页面缓存的大小，防止Fragment重新加载
		viewPager.setOffscreenPageLimit(mFragment.size() - 1);

		// // 使用用户ID作为唯一ID

		String userID = SPUtils.get(getApplicationContext(), PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
		if (!TextUtils.isEmpty(userID)) {
			JPushInterface.resumePush(getApplicationContext());
			// 调用JPush API设置Alias
			setAlias(userID);
		} else {
			JPushInterface.stopPush(getApplicationContext());
		}

		
	}

	@Override
	protected void setListener() {
		radio0.setOnClickListener(this);
		radio1.setOnClickListener(this);
		radio2.setOnClickListener(this);
		radio3.setOnClickListener(this);
		radio4.setOnClickListener(this);
		addListener();
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 2000);
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		requestByData(true);
	}

	/**
	 * 滑动viewPager的事件处理
	 */
	@SuppressWarnings("deprecation")
	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int id) {

				switch (id) {
				case TAB_page0:
					radio0.setChecked(true);
					setTitleInfo(id);
					break;
				case TAB_page1:
					radio1.setChecked(true);
					break;
				case TAB_page2:
					radio2.setChecked(true);
					setTitleInfo(id);
					break;
				case TAB_page3:
					radio3.setChecked(true);
					setTitleInfo(id);
					break;
				case TAB_page4:
					radio4.setChecked(true);
					setTitleInfo(id);
					break;
				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.radio_0:
			viewPager.setCurrentItem(TAB_page0);
			setTitleInfo(TAB_page0);
			lastPage = TAB_page0;
			break;
		case R.id.radio_1:
			// viewPager.setCurrentItem(TAB_page1);
			// setTitleInfo(TAB_page1);
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 20000);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_NEARBY_INDEX+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
			selectPageIndex(lastPage);
			break;
		case R.id.radio_2:
			viewPager.setCurrentItem(TAB_page2);
			setTitleInfo(TAB_page2);
			lastPage = TAB_page2;
			break;
		case R.id.radio_3:
			viewPager.setCurrentItem(TAB_page3);
			setTitleInfo(TAB_page3);
			lastPage = TAB_page3;
			break;
		case R.id.radio_4:
			viewPager.setCurrentItem(TAB_page4);
			setTitleInfo(TAB_page4);
			lastPage = TAB_page4;
			break;
		default:
			break;
		}

	}

	private void selectPageIndex(int index) {
		switch (index) {
		case TAB_page0:
			radio0.setChecked(true);
			break;
		case TAB_page1:
			radio1.setChecked(true);
			break;
		case TAB_page2:
			radio2.setChecked(true);
			break;
		case TAB_page3:
			radio3.setChecked(true);
			break;
		case TAB_page4:
			radio4.setChecked(true);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param itemId
	 */
	private void setTitleInfo(int itemId) {
		Intent mIntent = new Intent();
		currPage = itemId;
		switch (itemId) {
		case TAB_page0:
			mIntent.setAction(HomeFragment.ACTION_CALLBACK_HOME);
			break;
		case TAB_page1:
			// mIntent.setAction(NearbyFragment.ACTION_CALLBACK_NEARBY);
			break;
		case TAB_page2:
			mIntent.setAction(FindFragment.ACTION_CALLBACK_FIND);
			break;
		case TAB_page3:
			mIntent.setAction(FootFragment.ACTION_CALLBACK_FOOT);
			break;
		case TAB_page4:
			MyFragment.isLoad = true;
			mIntent.setAction(MyFragment.ACTION_CALLBACK_MY);
			break;
		default:
			break;
		}
		sendBroadcast(mIntent);
	}

	private class FragmentAdapter extends FragmentPagerAdapter {
		private List<Fragment> mList;

		public FragmentAdapter(FragmentManager fm, List<Fragment> mList) {
			super(fm);
			this.mList = mList;
		}

		@Override
		public Fragment getItem(int id) {
			return mList.get(id);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == ((Fragment) obj).getView();
		}
	}

	private boolean isExit = false;

	/**
	 * 2秒退出程序
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		try {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
					Timer tExit = null;
					if (isExit == false) {
						isExit = true; // 准备退出
						Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
						tExit = new Timer();
						tExit.schedule(new TimerTask() {
							@Override
							public void run() {
								isExit = false; // 取消退出
							}
						}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

					} else {
						AppManager.getAppManager().finishAllActivity();
						this.finish();
					}
				}
				return true;
			}
			return super.dispatchKeyEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			return super.dispatchKeyEvent(event);
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByData(final boolean isShow) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Type", "Android");
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_SETTINGS_UPDATE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonData(result, isShow);
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
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonData(String result, boolean isShow) {
		try {
			JSONObject objs = new JSONObject(result);
			JSONObject obj = JsonUtils.getJSONObject(objs, "AppVersions", null);
			final String DownAddrs = JsonUtils.getString(obj, "DownAddrs", "");
			final Integer LowestVersions = JsonUtils.getInt(obj, "LowestVersions", 0);
			final Integer VersionsNumber = JsonUtils.getInt(obj, "VersionsNumber", 0);
			final String UpdateTime = JsonUtils.getString(obj, "UpdateTime", "");
			final String RenewalDetails = JsonUtils.getString(obj, "RenewalDetails", "");

			if (VersionsNumber > AppUtils.getVersionCode(mContext)) {
				if (!isShow)
					return;
				// ismwt 是否强行更新判断
				ToastDialog toast = new ToastDialog(mContext, "发现新版本", RenewalDetails, new ToastDialog.OnClickCallback() {

					@Override
					public void onClickYes() {
					}

					@Override
					public void onClickNo() {
						// 立即更新
						NotificationHelper helper = new NotificationHelper(mContext, UrlSettings.URL_APK + DownAddrs);
						helper.startDownloadNotify();
					}
				});
				toast.setOnKeyListener(new DialogOnKeyListener());
				// 必须强制更新
				if (LowestVersions > AppUtils.getVersionCode(mContext)) {
					toast.showOneBtn();
					toast.show();
				} else {
					toast.show();
				}
			} else {
				LogUtils.i("没有发现新版本");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.i("没有发现新版本");
		}
	}

	/**
	 * Dialog 监听返回事件
	 */
	public class DialogOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			}
			return false;
		}

	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>以下为极光推送 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private final static int MSG_SET_ALIAS = 2001;
	private final static int MSG_SET_TAGS = 2002;
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
		mHandler2.sendMessage(mHandler2.obtainMessage(MSG_SET_ALIAS, alias));
	}

	public Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
				break;
			}

		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs = "";
			switch (code) {
			case 0:
				LogUtils.i("别名设置成功" + alias);
				break;

			case 6002:
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler2.sendMessageDelayed(mHandler2.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
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
					mHandler2.sendMessageDelayed(mHandler2.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
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
