package com.highnes.tour.ui.activities.find;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.find.FindDetailCommentAdapter;
import com.highnes.tour.adapter.find.FindTicketAdapter;
import com.highnes.tour.beans.find.FindDetailCommentInfo;
import com.highnes.tour.beans.find.FindDetailTicketInfo;
import com.highnes.tour.beans.find.FindDetailTicketInfo.SoldInfo;
import com.highnes.tour.common.share.ShareHelper;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.ActionSheetDialog;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;
import com.highnes.tour.view.webview.ProgressWebView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * <PRE>
 * 作用:
 *    首页..门票。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-08   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindDetailActivity extends BaseTitleActivity implements OnRefreshListener {

	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private RelativeLayout root;
	// 滑动高度隐藏Title
	private int maxH;
	private PullToRefreshLayout pull;
	private PullableScrollView svRoot;
	// 图文介绍
	private ProgressWebView web;
	private boolean isFristLoadWeb = true;

	// 当前的类型ID/文章ID/标题
	private String mTypeID, mNoteID, mTitle;

	private FindTicketAdapter ticketAdapter;
	private ListView lvTicket;

	private FindDetailCommentAdapter commentAdapter;
	private ListView lvComment;
	private List<FindDetailCommentInfo.NotesCommentInfo> commentData;
	private int commentCount = 0;

	// 点赞/评论
	private TextView tvOkCount, tvCommentCount, tvCommentTitle;
	private int okCount = 0;
	private ImageView ivOk;

	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 3;
	// 总页数
	private int pageTotle = 0;

	private LinearLayout llTicket;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_find_detail;
	}

	@Override
	protected void findViewById2T() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		web = getViewById(R.id.webview);
		lvTicket = getViewById(R.id.lv_ticket);
		lvComment = getViewById(R.id.lv_comment);
		tvOkCount = getViewById(R.id.tv_ok_count);
		tvCommentCount = getViewById(R.id.tv_comment_count);
		tvCommentTitle = getViewById(R.id.tv_comment_title);
		ivOk = getViewById(R.id.iv_ok);
		llTicket = getViewById(R.id.ll_hot);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		mTypeID = getIntent().getExtras().getString("mTypeID", "");
		mNoteID = getIntent().getExtras().getString("mNoteID", "");
		mTitle = getIntent().getExtras().getString("mTitle", "");
		setTitle(mTitle);
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("", R.drawable.ic_share);
		pull.setHasPullDown(false);
		pull.setHasPullUp(false);
		svRoot.smoothScrollTo(0, 20);
		initWebView();
		loadWeb();

		ticketAdapter = new FindTicketAdapter(mContext);
		lvTicket.setAdapter(ticketAdapter);

		commentAdapter = new FindDetailCommentAdapter(mContext);
		lvComment.setAdapter(commentAdapter);
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_add_comment).setOnClickListener(this);
		getViewById(R.id.ll_comment).setOnClickListener(this);
		ivOk.setOnClickListener(this);
		tvOkCount.setOnClickListener(this);
		getViewById(R.id.tv_item_more).setOnClickListener(this);
		lvTicket.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FindDetailTicketInfo.SoldInfo info = (SoldInfo) lvTicket.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 200);
		performHandlePostDelayed(1, 500);
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		if (0 == type) {
			requestByQueryComment();
		} else {
			requestByTicket();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 返回
		case R.id.button_backward_img19:
		case R.id.button_backward_img:
			finishActivity();
			break;
		// 分享
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			if(AppUtils.isLogin(mContext)){
				shareApp();
			}else{
				openActivity(LoginActivity.class);
			}
			break;
		// 添加评论
		case R.id.tv_add_comment:
			showDialog();
			break;
		// 所有评论
		case R.id.ll_comment: {
			Bundle bundle = new Bundle();
			bundle.putString("mNoteID", mNoteID);
			openActivity(FindDetailCommentActivity.class, bundle);
		}
			break;
		// 点赞
		case R.id.iv_ok:
		case R.id.tv_ok_count:
			if (AppUtils.isLogin(mContext)) {
				requestByOk();
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 更多
		case R.id.tv_item_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "loc");
			bundle.putString("nearbyValue", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue())
					.toString());
			bundle.putString("themeType", "");
			bundle.putString("themeValue", "");
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	/**
	 * 分享dialog
	 */
	private void shareApp() {
		ActionSheetDialog asd = new ActionSheetDialog(FindDetailActivity.this).builder();
		asd.setCancelable(true).setCanceledOnTouchOutside(true).show();
		asd.setmOnItemClick(new ActionSheetDialog.OnItemClick() {

			@Override
			public void onClick(int rsid) {
				switch (rsid) {
				// 朋友圈
				case R.id.ll_share_weixin_cricle:
					checkWeiXin(true);
					break;
				// 微信
				case R.id.ll_share_weixin:
					checkWeiXin(false);
					break;
				// QQ
				case R.id.ll_share_qq:
					String urlIamge = UrlSettings.URL_IMAGE + SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue());
					String userID = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
					ShareHelper.share(2, FindDetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_FIND, mNoteID,userID), getResources()
							.getString(R.string.share_title), getResources().getString(R.string.share_text), urlIamge, new MyUMShareListener());
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		try {
			cleanWebDB();
			WebSettings webSettings = web.getSettings();
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
			webSettings.setUseWideViewPort(true);// 关键点
			webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 适应屏幕，内容将自动缩放
			webSettings.setDisplayZoomControls(false);
			webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
			webSettings.setAllowFileAccess(true); // 允许访问文件
			webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
			webSettings.setSupportZoom(true); // 支持缩放
			webSettings.setLoadWithOverviewMode(true);
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除WebView的缓存
	 */
	private void cleanWebDB() {
		mContext.deleteDatabase("webview.db");
		mContext.deleteDatabase("webviewCache.db");
	}

	/**
	 * 加载网页
	 */
	private void loadWeb() {
		if (isFristLoadWeb) {
			web.loadUrl(UrlSettings.URL_H5_FIND_DETAIL + mNoteID);
			isFristLoadWeb = false;
		}
	}

	// -----------------对话框-------------------
	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.activity_custom_dialogs_custom, null);
		builder.setCancelable(true);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText etComment = (EditText) view.findViewById(R.id.et_comment);
				proLogicComment(etComment.getText().toString().trim());
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}

	/**
	 * 准备去添加评论
	 * 
	 * @param txt
	 */
	private void proLogicComment(String txt) {
		if (StringUtils.isEmpty(txt)) {
			showToast("请输入评论的内容");
			return;
		}
		if (AppUtils.isLogin(mContext)) {
			requestByAddComment(txt);
		} else {
			openActivity(LoginActivity.class);
		}
	}

	// -----------------------网络请求-----------------------
	/**
	 * Ticket 网络请求
	 * 
	 */
	private void requestByTicket() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		new NETConnection(mContext, UrlSettings.URL_FIND_DETAIL_TICKET, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonTicket(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				showToast(info);
				llTicket.setVisibility(View.GONE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTicket(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final FindDetailTicketInfo info = GsonUtils.json2T(result, FindDetailTicketInfo.class);
			ticketAdapter.setList(info.Sold);
			if (info.Sold != null && info.Sold.size() > 0) {
				llTicket.setVisibility(View.VISIBLE);
			} else {
				llTicket.setVisibility(View.GONE);
			}
		} else {
			llTicket.setVisibility(View.GONE);
		}
	}

	/**
	 * 添加评论 网络请求
	 * 
	 */
	private void requestByAddComment(final String txt) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("id", mNoteID);
		paramDatas.put("Details", txt);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FIND_DETAIL_COMMENT_ADD, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonAddComment(result, txt);
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
	 * 评论 JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonAddComment(String result, String txt) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("添加评论成功");
			if (commentData == null) {
				commentData = new ArrayList<FindDetailCommentInfo.NotesCommentInfo>();
			}
			commentCount++;
			tvCommentCount.setText(commentCount + "");
			tvCommentTitle.setText("评论(" + commentCount + ")");
			String photo = SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue()).toString();
			String userName = SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString();
			FindDetailCommentInfo.NotesCommentInfo item = new FindDetailCommentInfo.NotesCommentInfo("", txt, photo, userName);
			commentData.add(0, item);
			commentAdapter.setList(commentData);
		} else {
			showToast("添加评论失败");
		}
	}

	/**
	 * 查询评论 网络请求
	 * 
	 */
	private void requestByQueryComment() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("id", mNoteID);
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FIND_DETAIL_COMMENT_QUERY, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonQueryComment(result);
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
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonQueryComment(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final FindDetailCommentInfo info = GsonUtils.json2T(result, FindDetailCommentInfo.class);
			commentCount = Integer.valueOf(info.CommentCount);
			okCount = Integer.valueOf(info.ThingCount);
			tvCommentCount.setText(commentCount + "");
			tvCommentTitle.setText("评论(" + commentCount + ")");
			tvOkCount.setText(info.ThingCount);
			if ("False".equals(info.IsTing)) {
				ivOk.setImageResource(R.drawable.ic_find_ok_normal);
				tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
			} else {
				ivOk.setImageResource(R.drawable.ic_find_ok_pressed);
				tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.theme_orange));
			}
			commentData = info.NotesComment;
			commentAdapter.setList(commentData);
		}
	}

	/**
	 * 查询点赞 网络请求
	 * 
	 */
	private void requestByOk() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("NotesID", mNoteID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FIND_DETAIL_OK, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOk(result);
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
	 * 点赞 JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOk(String result) {
		try {
			int status = Integer.valueOf(JsonUtils.getString(result, "status", "0"));
			switch (status) {
			// 取消成功
			case 0:
				okCount--;
				tvOkCount.setText(okCount + "");
				ivOk.setImageResource(R.drawable.ic_find_ok_normal);
				tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
				break;
			// 取消点赞失败
			case 1:
				break;
			// 身份过期
			case 2:
				break;
			// 点赞成功
			case 3:
				okCount++;
				tvOkCount.setText(okCount + "");
				ivOk.setImageResource(R.drawable.ic_find_ok_pressed);
				tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.theme_orange));
				break;
			// 点赞失败
			case 4:
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// --------------------------------分享----------------------
	/**
	 * 是否安装了微信
	 * 
	 * @param isWeiXinCricle
	 *            true 是朋友圈
	 */
	private void checkWeiXin(boolean isWeiXinCricle) {
		IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WIN_ID, false);
		// 判断微信是否安装
		if (api.isWXAppInstalled()) {
			String urlIamge = UrlSettings.URL_IMAGE + SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue());
			if (isWeiXinCricle) {
				String userID = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
				ShareHelper.share(0, FindDetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_FIND, mNoteID,userID),
						getResources().getString(R.string.share_title), getResources().getString(R.string.share_text), urlIamge, new MyUMShareListener());
			} else {
				String userID = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
				ShareHelper.share(1, FindDetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_FIND, mNoteID,userID),
						getResources().getString(R.string.share_title), getResources().getString(R.string.share_text), urlIamge, new MyUMShareListener());
			}
		} else {
			showToast("还没有安装微信");
			final Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://weixin.qq.com/d");
			intent.setData(uri);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("下载提示").setMessage("\n现在就下载微信APP吗？\n").setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					startActivity(intent);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	private class MyUMShareListener implements UMShareListener {

		@Override
		public void onCancel(SHARE_MEDIA platform) {
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
		}
	}
}
