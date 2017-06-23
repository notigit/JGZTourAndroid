package com.highnes.tour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.ui.activities.GuidePageActivity;
import com.highnes.tour.ui.activities.TabFragmentActivity;
import com.highnes.tour.utils.SPUtils;

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
public class MainActivity extends Activity {
	/** 判断是否是第一次运行 */
	private boolean isFirstIn = false;
	/** 跳转到主界面int型标记 */
	private static final int GO_USER_FIRST_PAGE = 100;
	/** 跳转到引导页面int型标记 */
	private static final int GO_GUIDE = 101;
	/** 设置延迟时间 */
	private static final long DELAY_MILLIS = 4000;
	/**
	 * 主要功能，跳转到不同的界面
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GO_USER_FIRST_PAGE://开屏页
				goUserFirstPage();
				break;
			case GO_GUIDE://引导页
				goGuidePage();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);// 上往下推出效果
		setContentView(R.layout.activity_main);
		/* 取得相应的值，如果没有该值，说明还未写入，用true作为默认值 */
		isFirstIn = (Boolean) SPUtils.get(this, PreSettings.APP_FIRST_IN.getId(), PreSettings.APP_FIRST_IN.getDefaultValue());
		/* 判断是否是第一次运行，是的话跳转到引导页,使用Handler的postDelayed方法 */
		if (isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_USER_FIRST_PAGE, DELAY_MILLIS);
		}
	}

	/**
	 * 去引导页
	 */
	protected void goGuidePage() {
		Intent i = new Intent(MainActivity.this, GuidePageActivity.class);
		startActivity(i);
		finish();
	}

	/**
	 * 去用户首页
	 */
	protected void goUserFirstPage() {
//		Intent i = new Intent(MainActivity.this, TabFragmentActivity.class);
//		startActivity(i);
		finish();
	}




}
