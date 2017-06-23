package com.highnes.tour.adapter.home.tour;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.tour.Tour1FilterListInfo;
import com.highnes.tour.beans.home.tour.Tour2AreaInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..旅游度假..周边游..筛选..列表。
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
public class Tour2FilterListAdapter extends BaseListAdapter<Tour2AreaInfo.TourismsInfo> {

	public Tour2FilterListAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_area, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTag0 = (TextView) view.findViewById(R.id.tv_item_tag0);
			holder.tvTag1 = (TextView) view.findViewById(R.id.tv_item_tag1);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			holder.tvDis = (TextView) view.findViewById(R.id.tv_item_dis);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour2AreaInfo.TourismsInfo info = (Tour2AreaInfo.TourismsInfo) getItem(position);
		// 设置页面显示值
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvTitle.setText(info.Title);
		holder.tvMoneyNow.setText("￥" + ArithUtil.format2Int(Double.valueOf(info.Price)) + "");
		holder.tvMoneyOld.setText("￥" + ArithUtil.format2Int(Double.valueOf(info.Price)) + "");
		holder.tvInfo.setText(info.Rostered);
		if (StringUtils.isEmpty(info.TourismType)) {
			holder.tvTag0.setVisibility(View.GONE);
		} else {
			holder.tvTag0.setVisibility(View.VISIBLE);
			holder.tvTag0.setText(info.TourismType);
		}
		if (StringUtils.isEmpty(info.TypeName)) {
			holder.tvTag1.setVisibility(View.GONE);
		} else {
			holder.tvTag1.setVisibility(View.VISIBLE);
			holder.tvTag1.setText(info.TypeName);
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
		private TextView tvTag0, tvTag1;
	}
}
