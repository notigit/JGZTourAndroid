package com.highnes.tour.adapter.home.tour;

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
import com.highnes.tour.beans.home.ticket.TicketFilterListInfo;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.beans.home.tour.Tour1Info;
import com.highnes.tour.beans.home.tour.Tour7Info;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
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
public class Tour7Adapter extends BaseListAdapter<Tour7Info.CampingInfo> {

	public Tour7Adapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_yeying, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			holder.tvNumber = (TextView) view.findViewById(R.id.tv_item_number);
			holder.tvOk = (TextView) view.findViewById(R.id.tv_item_ok);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour7Info.CampingInfo info = (Tour7Info.CampingInfo) getItem(position);
		// 设置页面显示值
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvTitle.setText(info.Name);
		holder.tvMoneyNow.setText("￥" + ArithUtil.format2Int(info.NewPrice) + "");
		holder.tvMoneyOld.setText("￥" + ArithUtil.format2Int(info.NewPrice) + "");
		holder.tvInfo.setText(info.Title);
		holder.tvNumber.setText(info.Pcount + "人已参与");
		holder.tvOk.setText(info.Grades + "%满意度");
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.ImageUrl, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvMoneyOld;
		private TextView tvInfo, tvNumber, tvOk;
		private ImageView ivImg;
	}
}
