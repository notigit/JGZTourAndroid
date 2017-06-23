package com.highnes.tour.ui.activities.foot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.foot.FootDetailCommentAdapter;
import com.highnes.tour.adapter.foot.FootImageAdapter;
import com.highnes.tour.beans.foot.FootDetailInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.ForumImageActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.main.FootFragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    详情。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FootGroupDetailActivity extends BaseTitleActivity implements OnRefreshListener {

	private PullToRefreshLayout pull;
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	// 当前的类型
	private String mID;
	private TextView tvName, tvDel, tvTitle, tvTime, tvLoc, tvCommentCount, tvOkCount;
	private ImageView ivAvator, ivOkCount;
	// 图片
	private GridView gvPhoto;
	private List<String> mImage = new ArrayList<String>();
	private String mContents;
	// 点赞数量
	private int okCount = 0;

	// 评论
	private FootDetailCommentAdapter adapter;
	private ListView lvList;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_foot_group_detail;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
		gvPhoto = getViewById(R.id.gv_photo);
		ivAvator = getViewById(R.id.iv_avatar);
		tvTitle = getViewById(R.id.tv_item_remarks);
		tvTime = getViewById(R.id.tv_item_time);
		tvLoc = getViewById(R.id.tv_item_loc);
		tvCommentCount = getViewById(R.id.tv_item_comment);
		tvOkCount = getViewById(R.id.tv_item_agree);
		ivOkCount = getViewById(R.id.iv_item_agree);
		tvName = getViewById(R.id.tv_item_name);
		tvDel = getViewById(R.id.tv_item_del);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mID = getIntent().getExtras().getString("mID", "");
		setTitle("详情");
		showBackwardView("", SHOW_ICON_DEFAULT);
		// showForwardView("", R.drawable.ic_share);
		adapter = new FootDetailCommentAdapter(mContext);
		lvList.setAdapter(adapter);
		initOptions();
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.iv_item_comment).setOnClickListener(this);
		getViewById(R.id.tv_add_comment).setOnClickListener(this);
		tvCommentCount.setOnClickListener(this);
		ivOkCount.setOnClickListener(this);
		tvOkCount.setOnClickListener(this);
		tvDel.setOnClickListener(this);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// FindListInfo.NotesInfo info = (FindListInfo.NotesInfo)
				// lvList.getAdapter().getItem(position);
				// Bundle bundle = new Bundle();
				// bundle.putString("mTypeID", mID);
				// bundle.putString("mNoteID", info.ID);
				// bundle.putString("mTitle", mTitle);
				// openActivity(FindDetailActivity.class, bundle);
			}
		});

		gvPhoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("index", position);
				bundle.putString("title", mContents);
				bundle.putStringArrayList("forumImages", (ArrayList<String>) mImage);
				openActivity(ForumImageActivity.class, bundle);
			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 评论
		case R.id.tv_add_comment:
		case R.id.iv_item_comment:
		case R.id.tv_item_comment:
			showDialog();
			break;
		// 点赞
		case R.id.iv_item_agree:
		case R.id.tv_item_agree:
			if (AppUtils.isLogin(mContext)) {
				requestByOk();
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 举报与删除
		case R.id.tv_item_del:
			proLogicDel7Rep();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByData();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByData();
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * 删除与举报
	 */
	private void proLogicDel7Rep() {
		if (AppUtils.isLogin(mContext)) {
			if ("举报".equals(tvDel.getText().toString().trim())) {
				showDialog("确定要举报这条足迹吗？", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (2 == which) {
							requestByDel7Rep(false);
						}
					}
				});
			} else {
				showDialog("确定要删除这条足迹吗？", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (2 == which) {
							requestByDel7Rep(true);
						}
					}
				});
			}

		} else {
			openActivity(LoginActivity.class);
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

	/**
	 * Data 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByData() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		paramDatas.put("id", mID);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_GROUP_DETAIL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonData(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonData(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final FootDetailInfo info = GsonUtils.json2T(result, FootDetailInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				initInfo(info);
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				if (info.comment != null && info.comment.size() > 0) {
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.comment);
					} else {
						adapter.addAll(info.comment);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					adapter.removeAll();
					// 显示如果没有数据的时候的处理
					llShowData.setVisibility(View.VISIBLE);
				}
			} else {
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initInfo(FootDetailInfo info) {
		FootDetailInfo.DataInfo data = info.data.get(0);
		mContents = data.Contents;
		tvTitle.setText(data.Contents);
		if (StringUtils.isEmpty(data.AddTime)) {
			tvTime.setText("");
		} else {
			tvTime.setText(DateUtils.getStandardDate(DateUtils.formatDates(data.AddTime, "yyyy/MM/dd HH:mm:ss")));
		}
		if (StringUtils.isEmpty(data.AddName)) {
			tvLoc.setVisibility(View.INVISIBLE);
		} else {
			tvLoc.setText(data.AddName);
			tvLoc.setVisibility(View.VISIBLE);
		}
		tvCommentCount.setText(info.Count);
		okCount = Integer.valueOf(info.ThingCount);
		tvOkCount.setText(info.ThingCount);

		if (StringUtils.isEmpty(data.ImageUrls)) {
			gvPhoto.setVisibility(View.GONE);
		} else {
			gvPhoto.setVisibility(View.VISIBLE);
			FootImageAdapter adapter = new FootImageAdapter(mContext);
			gvPhoto.setAdapter(adapter);
			String arr[] = data.ImageUrls.split(",");
			List<String> list = Arrays.asList(arr);
			adapter.setList(list);
			mImage.clear();
			for (int i = 0; i < list.size(); i++) {
				mImage.add(UrlSettings.URL_IMAGE+list.get(i));
			}
		}
		String mUserID = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
		if (data.UserID.equals(mUserID)) {
			tvDel.setText("删除");
		} else {
			tvDel.setText("举报");
		}

		if ("False".equals(info.IsThing)) {
			ivOkCount.setImageResource(R.drawable.ic_heart_gray);
			tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		} else {
			ivOkCount.setImageResource(R.drawable.ic_heart_p);
			tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.theme_orange));
		}
		tvName.setText(data.UserName);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + data.UserHeadImg, ivAvator, options, null);
	}

	/**
	 * 添加评论 网络请求
	 * 
	 */
	private void requestByAddComment(final String txt) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("TribuneID", mID);
		paramDatas.put("Contents", txt);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_GROUP_DETAIL_COMMENT_ADD, new NETConnection.SuccessCallback() {

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
			page = 1;
			requestByData();
		} else {
			showToast("添加评论失败");
		}
	}

	/**
	 * 查询点赞 网络请求
	 * 
	 */
	private void requestByOk() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("TribuneID", mID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_GROUP_DETAIL_OK, new NETConnection.SuccessCallback() {

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
				ivOkCount.setImageResource(R.drawable.ic_heart_gray);
				tvOkCount.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
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
				ivOkCount.setImageResource(R.drawable.ic_heart_p);
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

	/**
	 * 删除与举报 网络请求
	 * 
	 */
	private void requestByDel7Rep(final boolean isDel) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("TribuneID", mID);
		String url = UrlSettings.URL_FOOT_GROUP_DETAIL_REP;
		if (isDel) {
			url = UrlSettings.URL_FOOT_GROUP_DETAIL_DEL;
		}
		showLoading();
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonDel7Rep(result, isDel);
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
	 * 删除与举报 JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonDel7Rep(String result, boolean isDel) {
		try {
			int status = Integer.valueOf(JsonUtils.getString(result, "status", "0"));
			switch (status) {
			// 失败
			case 0:
				if (isDel) {
					showToast("删除失败！");
				} else {
					showToast("举报失败！");
				}
				break;
			// 成功
			case 1:
				if (isDel) {
					showToast("删除成功！");
					FootFragment.isLoad = true;
					Intent mIntent = new Intent();
					mIntent.setAction(FootFragment.ACTION_CALLBACK_FOOT);
					sendBroadcast(mIntent);
					finishActivity();
				} else {
					showToast("举报成功！");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
