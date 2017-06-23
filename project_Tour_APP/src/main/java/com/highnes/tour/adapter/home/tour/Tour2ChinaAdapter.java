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
import com.highnes.tour.beans.home.tour.Tour2Info;
import com.highnes.tour.conf.UrlSettings;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		旅游度假..热门国内游。
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
public class Tour2ChinaAdapter extends BaseListAdapter<Tour2Info.HotTourismInfo> {

	public Tour2ChinaAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_2_china, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvCentent = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.tvPrice = (TextView) view.findViewById(R.id.tv_item_price);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour2Info.HotTourismInfo info = (Tour2Info.HotTourismInfo) getItem(position);
		holder.tvTitle.setText(info.Title);
		holder.tvPrice.setText("￥" + info.Price);
		holder.tvCentent.setText(info.TouCount + "人出游");
		holder.tvInfo.setText(info.AdvTitle);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.ImageUrl, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle, tvPrice, tvCentent, tvInfo;
		private ImageView ivImg;
	}
}
