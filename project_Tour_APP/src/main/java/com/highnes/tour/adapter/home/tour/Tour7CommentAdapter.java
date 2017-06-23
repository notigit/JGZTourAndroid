package com.highnes.tour.adapter.home.tour;

import java.util.Arrays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.adapter.foot.FootImageAdapter;
import com.highnes.tour.beans.home.ticket.TicketDetailInfo;
import com.highnes.tour.beans.home.tour.Tour7DetailInfo;
import com.highnes.tour.beans.home.tour.TourDetailInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.StarBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		评论。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour7CommentAdapter extends BaseListAdapter<Tour7DetailInfo.CommentInfo> {

	public Tour7CommentAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
			holder.gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_avatar);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_remarks);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.mStarBar = (StarBar) view.findViewById(R.id.starBar);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour7DetailInfo.CommentInfo info = (Tour7DetailInfo.CommentInfo) getItem(position);
		holder.tvTitle.setText(info.Contents);
		holder.tvName.setText(info.UserName);
		if (StringUtils.isEmpty(info.AddTime)) {
			holder.tvTime.setText("");
		} else {
			holder.tvTime.setText(DateUtils.getStandardDate(info.AddTime));
		}

		if (StringUtils.isEmpty(info.ImageUrl)) {
			holder.gvPhoto.setVisibility(View.GONE);
		} else {
			holder.gvPhoto.setVisibility(View.VISIBLE);
			FootImageAdapter adapter = new FootImageAdapter(mContext);
			holder.gvPhoto.setAdapter(adapter);
			String temp[] = info.ImageUrl.split(",");
			adapter.setList(Arrays.asList(temp));
		}
		holder.mStarBar.setStarMark((Integer.valueOf(info.Grades)) / 20);
		holder.mStarBar.setIntegerMark(true);
		holder.mStarBar.setTouch(false);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.HeadImg, holder.ivImg, options, null);
		holder.gvPhoto.setOnItemClickListener(new MyOnItemClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle, tvTime, tvName;
		private ImageView ivImg;
		// 图片
		private GridView gvPhoto;
		private LinearLayout root;
		// 星星
		private StarBar mStarBar;
	}

	private class MyOnItemClickListener implements OnItemClickListener {
		Tour7DetailInfo.CommentInfo info;

		public MyOnItemClickListener(Tour7DetailInfo.CommentInfo info) {
			this.info = info;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (onItemCall != null) {
				onItemCall.onCall(info,position);
			}
		}

	}

	private OnItemCall onItemCall;

	public void setOnItemCall(OnItemCall onItemCall) {
		this.onItemCall = onItemCall;
	}

	public interface OnItemCall {
		void onCall(Tour7DetailInfo.CommentInfo info,int position);
	}

}