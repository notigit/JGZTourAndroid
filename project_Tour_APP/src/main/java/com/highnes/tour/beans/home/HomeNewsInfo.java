package com.highnes.tour.beans.home;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页..新闻。
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
public class HomeNewsInfo {
	public String status;
	public List<DataInfo> data;
	public String size;

	/**
	 * 
	 * @param status
	 *            状态值 0表示没有数据 1表示正常
	 * @param data
	 */
	public HomeNewsInfo(String status, List<DataInfo> data, String size) {
		super();
		this.status = status;
		this.data = data;
		this.size = size;
	}

	public static class DataInfo {
		public String ID;
		public String Title;
		public String Type;
		public String AddTime;
		public Integer LookCount;

		/**
		 * 单条 新闻
		 * 
		 * @param iD
		 *            id
		 * @param title
		 *            标题
		 * @param type
		 *            类型
		 * @param time
		 *            时间
		 * @param contents
		 *            内容
		 */
		public DataInfo(String iD, String title, String type, String addTime) {
			super();
			ID = iD;
			Title = title;
			Type = type;
			AddTime = addTime;
		}

	}

}
