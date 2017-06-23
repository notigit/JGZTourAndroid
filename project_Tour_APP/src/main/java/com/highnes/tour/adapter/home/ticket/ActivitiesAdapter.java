package com.highnes.tour.adapter.home.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.conf.UrlSettings;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..活动。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-03   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ActivitiesAdapter extends BaseListAdapter<TicketInfo.StretchInfo> {

	public ActivitiesAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_ticket_activities, null);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		TicketInfo.StretchInfo info = (TicketInfo.StretchInfo) getItem(position);
		// 设置页面显示值
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.StretchImageUrl, holder.ivImg, options, null);
		holder.root.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private LinearLayout root;
		private ImageView ivImg;
	}


	private class MyOnClickListener implements OnClickListener {
		private TicketInfo.StretchInfo mInfo;

		public MyOnClickListener(TicketInfo.StretchInfo mInfo) {
			this.mInfo = mInfo;
		}

		@Override
		public void onClick(View v) {
			if (mOnCellItem != null) {
				mOnCellItem.onItem(mInfo);
			}
		}

	}

	private OnCellItem mOnCellItem;

	public void setmOnCellItem(OnCellItem mOnCellItem) {
		this.mOnCellItem = mOnCellItem;
	}

	public interface OnCellItem {
		void onItem(TicketInfo.StretchInfo info);

	}
}
