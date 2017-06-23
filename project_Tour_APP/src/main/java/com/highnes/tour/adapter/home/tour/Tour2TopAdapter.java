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
 * 		旅游度假..人气排行榜。
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
public class Tour2TopAdapter extends BaseListAdapter<Tour2Info.HotAreaInfo> {

	public Tour2TopAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_2_top, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_name);
			holder.tvContent = (TextView) view.findViewById(R.id.tv_item_centent);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Tour2Info.HotAreaInfo info = (Tour2Info.HotAreaInfo) getItem(position);
		holder.tvTitle.setText(info.AreaName);
		holder.tvContent.setText(info.Title);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Photo, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle, tvContent;
		private ImageView ivImg;
	}
}
