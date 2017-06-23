package com.highnes.tour.adapter.home;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.adapter.home.HomeTicketAdapter.OnCellItem;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.HotelInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.utils.ArithUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..限时秒杀。
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
public class HomeHotelAdapter extends BaseListAdapter<HotelInfo.DataInfo> {

	public HomeHotelAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_hotel, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvPoint = (TextView) view.findViewById(R.id.tv_item_point);
			holder.tvType = (TextView) view.findViewById(R.id.tv_item_type);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HotelInfo.DataInfo info = (HotelInfo.DataInfo) getItem(position);
		holder.tvTitle.setText(info.title);
//		holder.tvMoneyNow.setText(ArithUtil.format2Int(Double.valueOf(info.minprice)));
		holder.tvMoneyNow.setText(info.minprice);
		holder.tvPoint.setText(info.score+"分");
		holder.tvType.setText(info.star);
		// 设置页面显示值
		ImageLoader.getInstance().displayImage(PreSettings.URL_SERVER_SHOP.getDefaultValue().toString()+info.coversrc, holder.ivImg, options, null);
		holder.root.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvPoint, tvType;
		private ImageView ivImg;
		private LinearLayout root;
	}
	
	private class MyOnClickListener implements OnClickListener {
		private HotelInfo.DataInfo mInfo;

		public MyOnClickListener(HotelInfo.DataInfo mInfo) {
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
		void onItem(HotelInfo.DataInfo info);

	}
}
