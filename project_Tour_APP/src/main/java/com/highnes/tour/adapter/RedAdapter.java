package com.highnes.tour.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.PointInfo;
import com.highnes.tour.beans.find.FindInfo;
import com.highnes.tour.beans.find.FindListInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DensityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		红包。
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
public class RedAdapter extends BaseListAdapter<PointInfo.UserRedInfo> {

	public RedAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_pay_red, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		PointInfo.UserRedInfo info = (PointInfo.UserRedInfo) getItem(position);
		holder.tvName.setText(info.CName+"抵扣￥"+info.DisMoney);
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
	}
}
