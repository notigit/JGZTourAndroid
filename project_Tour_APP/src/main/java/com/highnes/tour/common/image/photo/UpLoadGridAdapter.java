package com.highnes.tour.common.image.photo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.highnes.tour.R;
import com.highnes.tour.common.image.photo.utils.BitmapCompress;

public class UpLoadGridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private Context context;
	private int maxImages;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public UpLoadGridAdapter(Context context, int maxImages) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.maxImages = maxImages;
	}

	public void update() {
		// loading();
	}

	public int getCount() {
		//超过最大值就不显示添加图标
		return BitmapCompress.bmp.size() >= maxImages ? BitmapCompress.bmp.size() : (BitmapCompress.bmp.size() + 1);
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.image_item_published_grida, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == BitmapCompress.bmp.size()) {
			if (position >= maxImages) {
				holder.image.setVisibility(View.GONE);
				holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_deletepic_unfocused));
			} else {
				holder.image.setVisibility(View.VISIBLE);
				holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_addpic_unfocused));
			}
		} else {
			holder.image.setImageBitmap(BitmapCompress.bmp.get(position));
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}
