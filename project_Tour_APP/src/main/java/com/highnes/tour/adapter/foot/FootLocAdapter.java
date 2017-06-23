package com.highnes.tour.adapter.foot;

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
import com.highnes.tour.beans.find.FindInfo;
import com.highnes.tour.beans.find.FindListInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DensityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		足迹..地址列表。
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
public class FootLocAdapter extends BaseListAdapter<PoiInfo> {

	public FootLocAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_loc, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		PoiInfo info = (PoiInfo) getItem(position);
		holder.tvName.setText(info.name);
		holder.tvAddress.setText(info.address);
		return view;
	}

	private class ViewHolder {
		private TextView tvAddress, tvName;
	}
}
