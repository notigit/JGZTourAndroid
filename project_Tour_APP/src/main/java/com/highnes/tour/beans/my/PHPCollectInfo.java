package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    收藏..门票。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-10   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPCollectInfo {

	public int errcode;
	public String total; //总条数
	public List<DataInfo> data;

	public static class DataInfo {
		public String coversrc; //图片地址
		public String destitle; //信息展示
		public String id; //id
		public String title; //标题
		public String types; //类型
		public String url; //跳转地址
	}

}
