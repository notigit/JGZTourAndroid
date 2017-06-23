package com.highnes.tour.beans;

import java.io.Serializable;

import android.annotation.SuppressLint;

import com.highnes.tour.utils.CharacterParser;

public class CityInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name;
	private String sortLetters; //自定义字段：显示数据拼音的首字母
	public CityInfo(String name) {
		super();
		this.name = name;
	}
	@SuppressLint("DefaultLocale")
	public String getSortLetters() {
		// 汉字转换成拼音
		String pinyin = CharacterParser.getInstance().getSelling(name);
		String sortString = pinyin.substring(0, 1).toUpperCase();

		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			sortLetters = sortString.toUpperCase();
		} else {
			sortLetters = "#";
		}
		return sortLetters;
	}
}
