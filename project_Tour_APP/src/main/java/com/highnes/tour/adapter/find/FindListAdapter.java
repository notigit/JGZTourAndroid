package com.highnes.tour.adapter.find;

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
 * 		发现..景色。
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
public class FindListAdapter extends BaseListAdapter<FindListInfo.NotesInfo> {

	public FindListAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_find_item, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.root1 = (RelativeLayout) view.findViewById(R.id.root1);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FindListInfo.NotesInfo info = (FindListInfo.NotesInfo) getItem(position);
		holder.tvInfo.setText(info.Name);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.ImageUrl, holder.ivImg, options, null);
		return view;
	}

	private class ViewHolder {
		private TextView tvInfo;
		private ImageView ivImg;
		private RelativeLayout root1;
	}
}
