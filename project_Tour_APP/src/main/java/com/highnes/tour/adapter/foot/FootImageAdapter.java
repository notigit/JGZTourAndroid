package com.highnes.tour.adapter.foot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.conf.UrlSettings;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		活动..热门活动列表..图片适配器。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-06-17   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FootImageAdapter extends BaseListAdapter<String> {

	public FootImageAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_photo_item, null);
			holder.ivCentent = (ImageView) view.findViewById(R.id.iv_item);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String info = (String) getItem(position);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info, holder.ivCentent, options, null);
		if (position == getCount() - 1 && null != mOnLastItem) {
			mOnLastItem.onLastItem();
		}
		return view;
	}

	private class ViewHolder {
		private ImageView ivCentent;
	}

}
