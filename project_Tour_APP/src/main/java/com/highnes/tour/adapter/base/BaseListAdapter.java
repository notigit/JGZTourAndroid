package com.highnes.tour.adapter.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.highnes.tour.BaseApplication;
import com.highnes.tour.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * <PRE>
 * 作用:
 *    所有Adapter的父类。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-08-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected BaseApplication application;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<T> mList = new ArrayList<T>();
	// 用于加载网络图片
	protected DisplayImageOptions options;

	public BaseListAdapter(Context paramContext) {
		this.mContext = paramContext;
		this.mInflater = LayoutInflater.from(paramContext);
		this.application = ((BaseApplication) paramContext.getApplicationContext());
	}

	/**
	 * 初始化Options
	 */
	protected void initOptions() {
		/**
		 * 开始加载图片
		 */
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_img).showImageForEmptyUri(R.drawable.ic_default_img)
				.showImageOnFail(R.drawable.ic_default_img).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(100)).build();
	}

	/**
	 * 初始化options
	 * 
	 * @param resId
	 *            设置图片的资源ID
	 */
	protected void initOptions(@DrawableRes int resId) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageForEmptyUri(resId).showImageOnFail(resId).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(100)).build();
	}

	public void add(T paramT) {
		if (this.mList == null)
			return;
		this.mList.add(paramT);
		notifyDataSetChanged();
	}

	public void addAll(List<T> paramList) {
		if (this.mList == null) {
			this.mList = new ArrayList<T>();
		}
		this.mList.addAll(paramList);
		notifyDataSetChanged();
	}

	public void addAll(List<T> paramList, int paramInt) {
		if (this.mList == null)
			return;
		this.mList.remove(paramInt);
		notifyDataSetChanged();
	}
	public void addAll( int paramInt,List<T> paramList) {
		if (this.mList == null)
			return;
		this.mList.addAll(paramInt, paramList);
		notifyDataSetChanged();
	}

	public int getCount() {
		if (this.mList == null)
			return 0;
		return this.mList.size();
	}

	public Object getItem(int paramInt) {
		if (this.mList == null)
			return null;
		return this.mList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		if (this.mList == null)
			paramInt = 0;
		return paramInt;
	}

	public List<T> getList() {
		return this.mList;
	}

	public void remove(int paramInt) {
		if (this.mList == null)
			return;
		this.mList.remove(paramInt);
		notifyDataSetChanged();
	}

	public void remove(T paramT) {
		if (this.mList == null)
			return;
		this.mList.remove(paramT);
		notifyDataSetChanged();
	}

	public void removeAll() {
		if (this.mList == null)
			return;
		this.mList.clear();
		notifyDataSetChanged();
	}

	/**
	 * 初始化数据
	 * 
	 * @param paramList
	 */
	public void setList(List<T> paramList) {
		this.mList = paramList;
		notifyDataSetChanged();
	}

	/**
	 * 添加数据
	 * 
	 * @param paramList
	 */
	public void addList(List<T> paramList) {
		this.mList.addAll(paramList);
		notifyDataSetChanged();
	}
	protected OnLastItem mOnLastItem; 
	public interface OnLastItem{
		void onLastItem();
	}
	public void setmOnLastItem(OnLastItem mOnLastItem) {
		this.mOnLastItem = mOnLastItem;
	}
	
}