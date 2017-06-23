package com.highnes.tour.adapter.my;

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
import com.highnes.tour.beans.my.CollectTicketInfo;
import com.highnes.tour.beans.my.CollectTourInfo;
import com.highnes.tour.beans.my.CollectYeyingInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.ArithUtil;
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
public class CollectYeyingAdapter extends BaseListAdapter<CollectYeyingInfo.CamkeepInfo> {

	public CollectYeyingAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_collect_ticket, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CollectYeyingInfo.CamkeepInfo info = (CollectYeyingInfo.CamkeepInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.Name);
		holder.tvInfo.setText(info.City);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.ImageUrl, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvInfo;
		private ImageView ivImg;
	}
}
