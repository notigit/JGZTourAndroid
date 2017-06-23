package com.highnes.tour.ui.activities.find;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.find.FindDetailCommentAdapter;
import com.highnes.tour.beans.find.FindDetailCommentInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    足迹..详情..评论。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-06   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindDetailCommentActivity extends BaseTitleActivity implements OnRefreshListener {
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
	private String mNoteID;

	private FindDetailCommentAdapter commentAdapter;
	private ListView lvComment;
	// 评论
	private TextView tvCommentCount;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_find_detail_comment;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvComment = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
		tvCommentCount = getViewById(R.id.tv_comment_count);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mNoteID = getIntent().getExtras().getString("mNoteID", "");
		setTitle("所有评论");
		showBackwardView("", SHOW_ICON_DEFAULT);
		commentAdapter = new FindDetailCommentAdapter(mContext);
		lvComment.setAdapter(commentAdapter);
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_add_comment).setOnClickListener(this);

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByQueryComment();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 发送评论
		case R.id.tv_add_comment:
			showDialog();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByQueryComment();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByQueryComment();
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
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
			page = 1;
			requestByQueryComment();
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
			int pages = Integer.valueOf(info.CommentCount) / pagesize;
			pageTotle = Integer.valueOf(info.CommentCount) % pagesize == 0 ? pages : pages + 1;
			tvCommentCount.setText(info.CommentCount);
			if (info.NotesComment != null && info.NotesComment.size() > 0) {
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				if (1 == page) {
					commentAdapter.removeAll();
					commentAdapter.setList(info.NotesComment);
				} else {
					commentAdapter.addAll(info.NotesComment);
				}
				llShowData.setVisibility(View.GONE);
			} else {
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
				commentAdapter.removeAll();
				// 显示如果没有数据的时候的处理
				llShowData.setVisibility(View.VISIBLE);
			}
		} else {
			pull.refreshFinish(PullToRefreshLayout.NO_DATA);
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}
	}
}
