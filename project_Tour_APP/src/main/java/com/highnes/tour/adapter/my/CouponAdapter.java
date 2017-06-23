package com.highnes.tour.adapter.my;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.beans.my.CollectTicketInfo;
import com.highnes.tour.beans.my.CollectTourInfo;
import com.highnes.tour.beans.my.CollectYeyingInfo;
import com.highnes.tour.beans.my.CouponInfo;
import com.highnes.tour.beans.my.PointInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		收藏..野营。
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
public class CouponAdapter extends BaseListAdapter<CouponInfo.RedPacketInfo> {

	private boolean isHas = true;

	public CouponAdapter(Context paramContext, boolean isHas) {
		super(paramContext);
		this.isHas = isHas;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_coupon, null);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			holder.tvMinPrice = (TextView) view.findViewById(R.id.tv_min_price);
			holder.tvLib = (TextView) view.findViewById(R.id.tv_lbe);
			holder.ivBottom = (ImageView) view.findViewById(R.id.iv_bottom);
			holder.root = (RelativeLayout) view.findViewById(R.id.root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CouponInfo.RedPacketInfo info = (CouponInfo.RedPacketInfo) getItem(position);
		holder.tvMinPrice.setText(ArithUtil.format2Int(info.DisMoney));
		holder.tvTitle.setText(info.ProName);
		holder.tvInfo.setText("满" + ArithUtil.format2Int(info.LowMoney) + "立减" + ArithUtil.format2Int(info.DisMoney));
		holder.tvTime.setText("有效期：" + DateUtils.formatDates(info.StartDate, "yyyy/MM/dd") + "--" + DateUtils.formatDates(info.EndDate, "yyyy/MM/dd"));
		if (isHas) {
			// 未使用
			holder.tvLib.setTextColor(ResHelper.getColorById(mContext, R.color.theme_orange));
			holder.tvMinPrice.setTextColor(ResHelper.getColorById(mContext, R.color.theme_orange));
			holder.tvTime.setTextColor(ResHelper.getColorById(mContext, R.color.white));
			holder.ivBottom.setImageResource(R.drawable.re_coupon_orange);
		} else {
			holder.tvLib.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
			holder.tvMinPrice.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
			holder.tvTime.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_gray));
			holder.ivBottom.setImageResource(R.drawable.re_coupon_gray);
		}
		holder.root.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvInfo, tvMinPrice, tvTime, tvLib;
		private ImageView ivBottom;
		private RelativeLayout root;
	}
	
	private class MyOnClickListener implements OnClickListener{

		CouponInfo.RedPacketInfo info;
		public MyOnClickListener(CouponInfo.RedPacketInfo info) {
			this.info = info;
		}
		@Override
		public void onClick(View v) {
			if(onItemCall!=null){
				onItemCall.itemCall(info);
			}
		}
		
	}
	private OnItemCall onItemCall;
	
	public void setOnItemCall(OnItemCall onItemCall) {
		this.onItemCall = onItemCall;
	}

	public interface OnItemCall{
		void itemCall(CouponInfo.RedPacketInfo info);
	}
}
