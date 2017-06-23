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
public class PointAdapter extends BaseListAdapter<PointInfo.UserPointInfo> {

	public PointAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_point, null);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_date);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		PointInfo.UserPointInfo info = (PointInfo.UserPointInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.Source);
		if (1 == info.UpDown) {
			holder.tvInfo.setText("扣除积分：-" + info.Count + "分");
			holder.tvInfo.setTextColor(ResHelper.getColorById(mContext, R.color.theme_red));
		} else {
			holder.tvInfo.setText("获得积分：" + info.Count + "分");
			holder.tvInfo.setTextColor(ResHelper.getColorById(mContext, R.color.titlec));
		}
		holder.tvTime.setText(DateUtils.getStandardDate(info.AddTime));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvInfo, tvTime;
	}
}
