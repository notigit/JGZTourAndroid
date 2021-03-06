package com.highnes.tour.ui.activities.home.tour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour1CommentAdapter;
import com.highnes.tour.adapter.home.tour.Tour1CommentAdapter.OnItemCall;
import com.highnes.tour.beans.home.tour.TourDateInfo;
import com.highnes.tour.beans.home.tour.TourDetailInfo;
import com.highnes.tour.common.share.ShareHelper;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.ForumImageActivity;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.poster.PosterFragment;
import com.highnes.tour.ui.fragment.poster.PosterFragment.OnItemClick;
import com.highnes.tour.utils.AnimationUtils;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.ActionSheetDialog;
import com.highnes.tour.view.listview.MyListView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnPullListener;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;
import com.highnes.tour.view.pull.PullableScrollView.ScrollViewListener;
import com.highnes.tour.view.webview.ProgressWebView;
import com.nineoldandroids.view.ViewPropertyAnimator;
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
public class Tour1DetailActivity extends BaseActivity implements OnRefreshListener {
	/** 改变第1个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_ONE = 0;
	/** 改变第2个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_TWO = 1;
	/** 改变第3个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_THREE = 2;
	// 当前的Item
	private int currPager = CHANGE_MENU_ONE;

	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	// 收藏
	private ImageView ivCollect;
	// 滑动高度隐藏Title
	private int maxH;
	private PullToRefreshLayout pull;
	// 轮播图
	private PosterFragment mPoster;
	private PullableScrollView svRoot;
	// 时间/地址
	private TextView tvName, tvPrice;

	// 菜单下划线宽度
	private int lineWidth;
	// 菜单下划线
	private View viewLine0, viewLine1;
	// 是否显示第2个菜单
	private LinearLayout llShow;
	// 菜单
	private View[] viewMenu = new View[2];
	private TextView[] tvMenu = new TextView[6];
	private RelativeLayout root;
	// 菜单距离的高度位置
	private float menuH;
	// 三个模块
	private LinearLayout llBuy, llDetail, llComment;
	// 图文介绍
	private ProgressWebView web1;
	private ProgressWebView web2;
	private boolean isFristLoadWeb1 = true;

	// 评论
	private MyListView lvComment;
	private Tour1CommentAdapter commentAdapter;
	private LinearLayout llNotData;

	// 当前旅游产品的iD/套餐id/日期
	private String mTourID, mPackageID, mDate;
	// 当前门票的信息
	private TourDetailInfo mInfo;
	private LinearLayout llTab;

	private View viewLastDate;
	// 剩余的门票
	private int ticketCount = 0;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_home_tour_1_detail;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		tvName = getViewById(R.id.tv_title);
		tvPrice = getViewById(R.id.tv_price_now);
		viewMenu[0] = getViewById(R.id.layout_0);
		viewMenu[1] = getViewById(R.id.layout_1);
		tvMenu[0] = (TextView) viewMenu[0].findViewById(R.id.tv_menu_0);
		tvMenu[1] = (TextView) viewMenu[0].findViewById(R.id.tv_menu_1);
		tvMenu[2] = (TextView) viewMenu[0].findViewById(R.id.tv_menu_2);
		tvMenu[3] = (TextView) viewMenu[1].findViewById(R.id.tv_menu_0);
		tvMenu[4] = (TextView) viewMenu[1].findViewById(R.id.tv_menu_1);
		tvMenu[5] = (TextView) viewMenu[1].findViewById(R.id.tv_menu_2);
		viewLine0 = getViewById(R.id.line_0);
		viewLine1 = getViewById(R.id.line_1);
		llShow = getViewById(R.id.ll_show);
		llBuy = getViewById(R.id.ll_buy);
		llDetail = getViewById(R.id.ll_detail);
		llComment = getViewById(R.id.ll_comment);
		web1 = getViewById(R.id.webview1);
		web2 = getViewById(R.id.webview2);
		llTab = getViewById(R.id.ll_date);
		lvComment = getViewById(R.id.lv_comment);
		llNotData = getViewById(R.id.ll_class_hint);
		ivCollect = getViewById(R.id.iv_menu_0);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		initTitle();
		tvMenu[0].setText("行程");
		tvMenu[3].setText("行程");
		tvMenu[1].setText("费用与须知");
		tvMenu[4].setText("费用与须知");
		tvMenu[2].setText("评论(0)");
		tvMenu[5].setText("评论(0)");
		mTourID = getIntent().getExtras().getString("mTourID", "");
		pull.setHasPullDown(false);
		pull.setHasPullUp(false);
		initPager();
		svRoot.smoothScrollTo(0, 20);
		initWebView(web1);
		initWebView(web2);
		commentAdapter = new Tour1CommentAdapter(mContext);
		lvComment.setAdapter(commentAdapter);
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		setOnPullListener();
		setScrollViewListener();
		getViewById(R.id.rv_menu_0).setOnClickListener(this);
		getViewById(R.id.rv_menu_1).setOnClickListener(this);
		getViewById(R.id.rv_menu_3).setOnClickListener(this);
		getViewById(R.id.ll_comment_more).setOnClickListener(this);
		for (int i = 0; i < tvMenu.length; i++) {
			tvMenu[i].setOnClickListener(this);
		}
		commentAdapter.setOnItemCall(new OnItemCall() {

			@Override
			public void onCall(TourDetailInfo.CommInfo info, int position) {
				Bundle bundle = new Bundle();
				bundle.putInt("index", position);
				bundle.putString("title", info != null ? info.Contents : "");
				final List<String> list = new ArrayList<String>();
				String temp[] = info.ImageUrl.split(",");
				for (int i = 0; i < temp.length; i++) {
					list.add(UrlSettings.URL_IMAGE + temp[i]);
				}
				bundle.putStringArrayList("forumImages", (ArrayList<String>) list);
				openActivity(ForumImageActivity.class, bundle);
			}
		});
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		requestByTicket();
		loadWeb1();
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
		// 菜单1
		case R.id.tv_menu_0:
		case R.id.tv_menu_3:
			currPager = CHANGE_MENU_ONE;
			selectPage(CHANGE_MENU_ONE);
			scrollToLocation(menuH);
			loadWeb1();
			break;
		// 菜单2
		case R.id.tv_menu_1:
		case R.id.tv_menu_4:
			currPager = CHANGE_MENU_TWO;
			selectPage(CHANGE_MENU_TWO);
			scrollToLocation(menuH);
			loadWeb2();
			break;
		// 菜单3
		case R.id.tv_menu_2:
		case R.id.tv_menu_5:
			currPager = CHANGE_MENU_THREE;
			selectPage(CHANGE_MENU_THREE);
			scrollToLocation(menuH);
			break;
		// 拨打电话
		case R.id.rv_menu_1:
			displayPhoneDialog();
			break;
		// 收藏
		case R.id.rv_menu_0:
			displayCollect();
			break;
		// 分享
		case R.id.button_down_img_right19:
		case R.id.button_down_img_right:
			if(AppUtils.isLogin(mContext)){
				shareApp();
			}else{
				openActivity(LoginActivity.class);
			}
			break;
		// 日期
		case R.id.root:
			if (viewLastDate != null) {
				viewLastDate.setBackgroundResource(R.drawable.bg_date);
			}
			v.setBackgroundResource(R.drawable.bg_date_p);
			TourDateInfo dateInfo = (TourDateInfo) v.getTag();
			mPackageID = dateInfo.PID;
			viewLastDate = v;
			mDate = dateInfo.date;
			loadWeb2();
			requestByTicketCount(mDate);
			break;
		// 预订
		case R.id.rv_menu_3:
//			if (AppUtils.isLogin(mContext)) {
//				if (ticketCount < 1) {
//					showToast("没有更多的余票了");
//					return;
//				}
//				Bundle bundle = new Bundle();
//				bundle.putString("mTourID", mTourID);
//				bundle.putString("mPackageID", mPackageID);
//				bundle.putString("mDate", mDate);
//				openActivity(Tour1DetailOrderActivity.class, bundle);
//			} else {
//				openActivity(LoginActivity.class);
//			}
			Toast.makeText(mContext, "暂不支持预订", Toast.LENGTH_SHORT).show();
			break;
		// 所有的评论
		case R.id.ll_comment_more: {
			Bundle bundle = new Bundle();
			bundle.putString("mTourID", mTourID);
			openActivity(Tour1CommentListActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 拨打电话
	 */
	private void displayPhoneDialog() {
		if (TextUtils.isEmpty(mInfo.Tourisms.get(0).ServicePhone)) {
			showToast("没有获取到商家的电话");
			return;
		}
		try {
			if (mInfo != null) {
				showDialog("是否立即拨打" + mInfo.Tourisms.get(0).ServicePhone.trim() + "？", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (2 == which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mInfo.Tourisms.get(0).ServicePhone.trim()));
							startActivity(intent);
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 收藏判断
	 */
	private void displayCollect() {
		if (AppUtils.isLogin(mContext)) {
			requestByCollect();
		} else {
			openActivity(LoginActivity.class);
		}

	}

	/**
	 * 初始化pager
	 */
	@SuppressWarnings("deprecation")
	private void initPager() {
		// 计算滑动条的宽度
		lineWidth = getWindowManager().getDefaultDisplay().getWidth() / 3;
		viewLine0.getLayoutParams().width = lineWidth;
		viewLine0.requestLayout();
		viewLine1.getLayoutParams().width = lineWidth;
		viewLine1.requestLayout();
	}

	/**
	 * 选择的页面，修改字体颜色
	 * 
	 * @param selectPage
	 *            选择的页
	 */
	private void selectPage(int selectPage) {
		tvMenu[0].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[1].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[2].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[3].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[4].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[5].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[selectPage].setTextColor(ResHelper.getColorById(mContext, R.color.font_green));
		tvMenu[selectPage + 3].setTextColor(ResHelper.getColorById(mContext, R.color.font_green));
		// 移动滑动条
		float tagerX = selectPage * lineWidth;
		ViewPropertyAnimator.animate(viewLine0).translationX(tagerX).setDuration(0);
		ViewPropertyAnimator.animate(viewLine1).translationX(tagerX).setDuration(0);
		switch (selectPage) {
		case CHANGE_MENU_ONE:
			llBuy.setVisibility(View.VISIBLE);
			llDetail.setVisibility(View.GONE);
			llComment.setVisibility(View.GONE);
			break;
		case CHANGE_MENU_TWO:
			llBuy.setVisibility(View.GONE);
			llDetail.setVisibility(View.VISIBLE);
			llComment.setVisibility(View.GONE);
			break;
		case CHANGE_MENU_THREE:
			llBuy.setVisibility(View.GONE);
			llDetail.setVisibility(View.GONE);
			llComment.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/**
	 * scrollView移动到的位置
	 * 
	 * @param y
	 */
	private void scrollToLocation(final float y) {
		svRoot.post(new Runnable() {
			@Override
			public void run() {
				svRoot.smoothScrollTo(0, (int) y);
			}
		});
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				root.setBackgroundColor(Color.argb(255, 72, 211, 194));
			}
		}, 100);
	}

	/**
	 * 标题初始化
	 */
	private void initTitle() {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		ImageView ivBack, ivShare;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			ivShare = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
			menuH = DensityUtils.dp2pxF(mContext, 380 - 68);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			ivShare = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
			menuH = DensityUtils.dp2pxF(mContext, 380 - 50);
		}
		ivBack.setVisibility(View.VISIBLE);
		ivShare.setVisibility(View.VISIBLE);
		ivShare.setImageResource(R.drawable.ic_share);
		ivShare.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255 / maxH, 72, 211, 194));
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
		ActionSheetDialog asd = new ActionSheetDialog(Tour1DetailActivity.this).builder();
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
					ShareHelper.share(2, Tour1DetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_TOUR, mTourID,userID),
							getResources().getString(R.string.share_title), getResources().getString(R.string.share_text), urlIamge, new MyUMShareListener());
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 设置下拉的监听
	 */
	private void setOnPullListener() {
		pull.setOnPullListener(new OnPullListener() {

			@Override
			public void onPull(boolean isPull) {
				if (isPull) {
					// 隐藏title
					llTitle.setAnimation(AnimationUtils.setAnimationZeroToUp(500));
					llTitle.setVisibility(View.GONE);
				} else {
					// 显示title
					llTitle.setAnimation(AnimationUtils.setAnimationUpToZero(500));
					llTitle.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 设置滑动的监听
	 */
	private void setScrollViewListener() {
		svRoot.setScrollViewListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(PullableScrollView scrollView, int x, int y, int oldx, int oldy) {
				int alpha = 255 * y / maxH;
				if (alpha >= 0 && alpha <= 255) {
					// 48d3c2
					vTitle.setBackgroundColor(Color.argb(alpha, 72, 211, 194));
				}
				if (y > DensityUtils.dip2px(mContext, 150)) {
					root.setBackgroundColor(Color.argb(255, 72, 211, 194));
				}
				if (y < menuH) {
					llShow.setVisibility(View.GONE);
				} else {
					llShow.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	// -----------------------网络请求-----------------------
	/**
	 * Ticket 网络请求
	 * 
	 */
	private void requestByTicket() {
		// TODO 评论分页未做
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", mTourID);
		paramDatas.put("page", 1);
		paramDatas.put("rows", 5);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_DETAIL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonTicket(result);
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
	private void parseJsonTicket(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final TourDetailInfo info = GsonUtils.json2T(result, TourDetailInfo.class);
			mInfo = info;
			initAreaInfo(info);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initBanner(info.Photo);
				}
			}, 200);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (info.goodCom != null && info.goodCom.size() > 0) {
						llNotData.setVisibility(View.GONE);
						lvComment.setVisibility(View.VISIBLE);
						commentAdapter.setList(info.goodCom);
					} else {
						// 没有数据
						llNotData.setVisibility(View.VISIBLE);
						lvComment.setVisibility(View.GONE);
					}
				}
			}, 600);
		} else {
			// 没有数据
			llNotData.setVisibility(View.VISIBLE);
			lvComment.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化景区信息
	 * 
	 * @param info
	 */
	private void initAreaInfo(TourDetailInfo info) {
		try {
			tvName.setText(info.Tourisms.get(0).Title);
			tvPrice.setText(info.MinPrice);
			tvMenu[2].setText("评论(" + info.Count + ")");
			tvMenu[5].setText("评论(" + info.Count + ")");
			// 是否收藏
			if ("False".equals(info.Toukeep)) {
				ivCollect.setImageResource(R.drawable.ic_tour_collect);
			} else {
				ivCollect.setImageResource(R.drawable.ic_tour_collect_p);
			}

			// 初始化日期
			List<TourDateInfo> channelList = new ArrayList<TourDateInfo>();
			for (int i = 0; i < info.Package.size(); i++) {
				String startTime = info.Package.get(i).ATime;
				String endTime = info.Package.get(i).OTime;
				TourDateInfo dateInfo = null;
				int day = DateUtils.getGapCount(DateUtils.format2Date(startTime, "yyyy-MM-dd"), DateUtils.format2Date(endTime, "yyyy-MM-dd"));
				for (int j = 0; j <= day; j++) {
					// 判断时候是现在这个日期
					String dateTime = DateUtils.getDateAdd(DateUtils.format2Date(startTime, "yyyy-MM-dd"), j);
					if (DateUtils.isGTRCurrentDate(Integer.valueOf(DateUtils.getFormat(dateTime, "yyyyMMdd")))) {
						dateInfo = new TourDateInfo(DateUtils.getDateAdd(DateUtils.format2Date(startTime, "yyyy-MM-dd"), j), info.Package.get(i).Price,
								info.Package.get(i).PID);
						channelList.add(dateInfo);
					}
				}
			}
			initTab(channelList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化tab
	 * 
	 * @param channelList
	 *            菜单
	 */
	@SuppressLint("InflateParams")
	private void initTab(List<TourDateInfo> channelList) {
		// 最多显示30天
		int len = channelList.size() > 30 ? 30 : channelList.size();
		for (int i = 0; i < len; i++) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_detail_date_item, null);
			LinearLayout rvFast = (LinearLayout) view.findViewById(R.id.root);
			TextView tvName = (TextView) view.findViewById(R.id.tv_item_date);
			TextView ivPrice = (TextView) view.findViewById(R.id.tv_item_price);
			rvFast.setTag(channelList.get(i));
			tvName.setText(DateUtils.getFormat(channelList.get(i).date, "MM-dd") + " " + DateUtils.getWeek(channelList.get(i).date));
			ivPrice.setText("￥" + ArithUtil.formatPrice(Double.valueOf(channelList.get(i).price)));
			rvFast.setOnClickListener(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.width = (int) DensityUtils.dp2pxF(mContext, 90);
			lp.height = (int) DensityUtils.dp2pxF(mContext, 50);
			view.setLayoutParams(lp);
			llTab.addView(view);
			if (0 == i && channelList.size() > 0) {
				rvFast.setBackgroundResource(R.drawable.bg_date_p);
				viewLastDate = rvFast;
				mPackageID = channelList.get(0).PID;
				mDate = channelList.get(0).date;
				requestByTicketCount(mDate);
			}
		}

	}

	/**
	 * 显示Banner
	 */
	private void initBanner(final List<TourDetailInfo.PhotoInfo> info) {
		try {
			mPoster = new PosterFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.poster_tour_1_detail, mPoster).commit();
			if (info != null && info.size() > 0) {
				// 轮播图
				final List<String> list = new ArrayList<String>();
				for (int i = 0; i < info.size(); i++) {
					list.add(UrlSettings.URL_IMAGE + info.get(i).ImageUrl);
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mPoster.initPoster(list);
						mPoster.setOnItemClick(new OnItemClick() {

							@Override
							public void onItemClick(int position) {
								Bundle bundle = new Bundle();
								bundle.putInt("index", position);
								bundle.putString("title", mInfo != null ? mInfo.Tourisms.get(0).Title : "");
								bundle.putStringArrayList("forumImages", (ArrayList<String>) list);
								openActivity(ForumImageActivity.class, bundle);
							}
						});
					}
				}, 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(WebView web) {
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
	private void loadWeb1() {
		if (isFristLoadWeb1) {
			LogUtils.i("--加载h5--" + UrlSettings.URL_HOME_TOUR_DETAIL_INFO + mTourID);
			web1.loadUrl(UrlSettings.URL_HOME_TOUR_DETAIL_INFO + mTourID);
			isFristLoadWeb1 = false;
		}
	}

	/**
	 * 加载网页
	 */
	private void loadWeb2() {
		web2.loadUrl(UrlSettings.URL_HOME_TOUR_DETAIL_INFO2 + mPackageID);
	}

	/**
	 * 收藏 网络请求
	 * 
	 * @param TourismID
	 *            门票ID
	 * @param UserID
	 *            用户ID
	 * 
	 */
	private void requestByCollect() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("TourismID", mTourID);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_DETAIL_COLLECT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCollect(result);
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
	 * 收藏 JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonCollect(String result) {
		try {
			int status = Integer.valueOf(JsonUtils.getString(result, "status", "0"));
			switch (status) {
			// 游客
			case 0:
				break;
			// 收藏成功
			case 1:
				ivCollect.setImageResource(R.drawable.ic_tour_collect_p);
				break;
			// 收藏失败
			case 2:
				showToast("收藏失败");
				break;
			// 取消收藏成功
			case 3:
				ivCollect.setImageResource(R.drawable.ic_tour_collect);
				break;
			// 取消收藏失败
			case 4:
				showToast("取消收藏失败");
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
				ShareHelper.share(0, Tour1DetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_TOUR, mTourID,userID),
						getResources().getString(R.string.share_title), getResources().getString(R.string.share_text), urlIamge, new MyUMShareListener());
			} else {
				String userID = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
				ShareHelper.share(1, Tour1DetailActivity.this, getLoading(), String.format(UrlSettings.URL_SHARE_TOUR, mTourID,userID),
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

	/**
	 * Order 网络请求
	 * 
	 */
	private void requestByTicketCount(String time) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Touid", mTourID);
		paramDatas.put("Pid", mPackageID);
		paramDatas.put("Time", time);
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_DETAIL_COUNT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonTicketCount(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
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
	private void parseJsonTicketCount(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				ticketCount = Integer.valueOf(JsonUtils.getString(result, "points", "0"));
				if (ticketCount > 0) {
				} else {
					showToast("选择的日期无票");
				}
			} else {
				showToast("选择的日期无票");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
