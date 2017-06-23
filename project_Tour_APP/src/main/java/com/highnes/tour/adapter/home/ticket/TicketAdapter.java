package com.highnes.tour.adapter.home.ticket;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..门票推荐。
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
public class TicketAdapter extends BaseListAdapter<TicketInfo.SoldInfo> {
	private LatLng mLatlng;

	public TicketAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
		String lat = SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()).toString();
		String lng = SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()).toString();
		mLatlng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_ticket_list, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.ivTag0 = (ImageView) view.findViewById(R.id.iv_item_tag0);
			holder.ivTag1 = (ImageView) view.findViewById(R.id.iv_item_tag1);
			holder.ivTag2 = (ImageView) view.findViewById(R.id.iv_item_tag2);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			holder.tvDis = (TextView) view.findViewById(R.id.tv_item_dis);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		TicketInfo.SoldInfo info = (TicketInfo.SoldInfo) getItem(position);
		// 设置页面显示值
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvTitle.setText(info.SoldName);
		holder.tvMoneyNow.setText("￥" + ArithUtil.format2Int(Double.valueOf(info.NewPrice)) + "");
		holder.tvMoneyOld.setText("￥" + ArithUtil.format2Int(Double.valueOf(info.OldPrice)) + "");
		holder.tvInfo.setText(info.Title);
		// 距离 自己计算距离
		LatLng latLngShop = new LatLng(Double.valueOf(info.Latitude), Double.valueOf(info.Landmark));
		// 距离计算
		holder.tvDis.setText(AMapUtils.getDistance2XX(mLatlng, latLngShop));
		if (mLatlng.latitude == 0 || latLngShop.latitude == 0) {
			holder.tvDis.setVisibility(View.GONE);
		} else {
			holder.tvDis.setVisibility(View.VISIBLE);
		}
		if ("True".equals(info.IsReserved)) {
			holder.ivTag0.setVisibility(View.VISIBLE);
		} else {
			holder.ivTag0.setVisibility(View.GONE);

		}
		if ("True".equals(info.IsUpdate)) {
			holder.ivTag1.setVisibility(View.VISIBLE);
		} else {
			holder.ivTag1.setVisibility(View.GONE);

		}
		if ("True".equals(info.IsRefunds)) {
			holder.ivTag2.setVisibility(View.VISIBLE);
		} else {
			holder.ivTag2.setVisibility(View.GONE);

		}
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.ImageUrl, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvMoneyOld;
		private TextView tvInfo, tvDis;
		private ImageView ivImg;
		private ImageView ivTag0, ivTag1, ivTag2;
	}
}
