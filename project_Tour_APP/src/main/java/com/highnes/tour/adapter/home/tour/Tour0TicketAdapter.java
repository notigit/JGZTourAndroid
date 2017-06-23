package com.highnes.tour.adapter.home.tour;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.tour.Tour0Info;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.ArithUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..旅游..门票推荐。
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
public class Tour0TicketAdapter extends BaseListAdapter<Tour0Info.HotSoldInfo> {

	public Tour0TicketAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_ticket, null);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvLoc = (TextView) view.findViewById(R.id.tv_item_loc);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour0Info.HotSoldInfo info = (Tour0Info.HotSoldInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.SoldName);
		holder.tvLoc.setText(info.SCity);
		holder.tvMoneyNow.setText(ArithUtil.format2Int(info.SNewPrice) + "");
		holder.tvMoneyOld.setText("￥"+info.SOldPrice + "");
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.SPhoto, holder.ivImg, options, null);
		holder.root.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private LinearLayout root;
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvMoneyOld;
		private TextView tvLoc;
		private ImageView ivImg;
	}
	
	private class MyOnClickListener implements OnClickListener {
		private Tour0Info.HotSoldInfo mInfo;

		public MyOnClickListener(Tour0Info.HotSoldInfo mInfo) {
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
		void onItem(Tour0Info.HotSoldInfo info);

	}
}
