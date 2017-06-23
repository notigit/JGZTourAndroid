package com.highnes.tour.adapter.find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.find.FindDetailCommentInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		发现..评论。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-07-08   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindDetailCommentAdapter extends BaseListAdapter<FindDetailCommentInfo.NotesCommentInfo> {

	public FindDetailCommentAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_find_detail_comment, null);
			holder.tvCentent = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.ivAvatar = (ImageView) view.findViewById(R.id.iv_item_avatar);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FindDetailCommentInfo.NotesCommentInfo info = (FindDetailCommentInfo.NotesCommentInfo) getItem(position);
		// 设置页面显示值
		holder.tvCentent.setText(info.ComDetails);
		if (StringUtils.isEmpty(info.ComAddTime)) {
			holder.tvTime.setText("刚刚");
		} else {
			holder.tvTime.setText(DateUtils.getStandardDate(DateUtils.formatDates(info.ComAddTime, "yyyy/MM/dd HH:mm:ss")));
		}
		holder.tvName.setText(info.UserName);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.HeadImg, holder.ivAvatar, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvCentent;
		private TextView tvTime;
		private TextView tvName;
		private ImageView ivAvatar;
	}

}
