package com.highnes.tour.adapter.home;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.HomeTimeListInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
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
public class HomeTimesListAdapter extends BaseListAdapter<HomeTimeListInfo.BenefitInfo> {
	DecimalFormat df;

	public HomeTimesListAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
		df = new DecimalFormat("00");
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_time_list, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HomeTimeListInfo.BenefitInfo info = (HomeTimeListInfo.BenefitInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.Title);
		holder.tvMoneyNow.setText("￥" + ArithUtil.format2Int(info.NewPrice));
		holder.tvMoneyOld.setText("￥" + ArithUtil.format2Int(info.OldPrice));
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Photo, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvMoneyOld;
		private ImageView ivImg;
	}

}
