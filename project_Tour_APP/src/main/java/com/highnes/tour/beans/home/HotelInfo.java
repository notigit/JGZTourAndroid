package com.highnes.tour.beans.home;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    消息。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class HotelInfo {
	public String errcode;  //1表示正常
	public String count; //总条数
	public List<DataInfo> data;

	public static class DataInfo {
		public String coversrc; //图片地址
		public String id; //酒店id
		public String maxprice; //原价
		public String minprice; //现价
		public String star; //类型
		public String title; //酒店名称
		public String url; //跳转地址
		public String score; //评分
	}

}
