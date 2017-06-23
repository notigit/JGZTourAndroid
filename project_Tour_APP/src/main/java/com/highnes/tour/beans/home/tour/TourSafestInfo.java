package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    保险列表。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-29   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TourSafestInfo {

	public String status;
	public List<SafestInfo> TourismsSafest;

	public static class SafestInfo {
		public String ID;
		public Double Money;
		public String Name;
		public String Title;

		/**
		 * 
		 * @param iD
		 * @param money
		 * @param name
		 * @param title
		 */
		public SafestInfo(String iD, Double money, String name, String title) {
			super();
			ID = iD;
			Money = money;
			Name = name;
			Title = title;
		}

	}
}
