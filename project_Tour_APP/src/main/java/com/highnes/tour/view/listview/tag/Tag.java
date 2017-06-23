package com.highnes.tour.view.listview.tag;

import java.io.Serializable;

public class Tag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2684657309332033242L;

	private int backgroundResId;
	private int id;
	private boolean isChecked = false;
	private boolean isShowDel = true;
	private int leftDrawableResId;
	private int rightDrawableResId;
	private String title;
	private String stuId;

	public Tag() {

	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public Tag(int paramInt, String paramString) {
		this.id = paramInt;
		this.title = paramString;
	}

	public int getBackgroundResId() {
		return this.backgroundResId;
	}

	public int getId() {
		return this.id;
	}

	public int getLeftDrawableResId() {
		return this.leftDrawableResId;
	}

	public int getRightDrawableResId() {
		return this.rightDrawableResId;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public boolean isShowDel() {
		return this.isShowDel;
	}

	public void setBackgroundResId(int paramInt) {
		this.backgroundResId = paramInt;
	}

	public void setChecked(boolean paramBoolean) {
		this.isChecked = paramBoolean;
	}

	public void setShowDel(boolean paramBoolean) {
		this.isShowDel = paramBoolean;
	}

	public void setId(int paramInt) {
		this.id = paramInt;
	}

	public void setLeftDrawableResId(int paramInt) {
		this.leftDrawableResId = paramInt;
	}

	public void setRightDrawableResId(int paramInt) {
		this.rightDrawableResId = paramInt;
	}

	public void setTitle(String paramString) {
		this.title = paramString;
	}
}
