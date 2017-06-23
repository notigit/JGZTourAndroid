package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    旅游度假..国内游。
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
public class Tour2Info {

	public String status;
	public List<HotAreaInfo> HotArea;
	public List<HotCityInfo> HotCity;
	public List<HotTourismInfo> HotTourism;

	public static class HotAreaInfo {
		public String AreaName;
		public String ID;
		public String Photo;
		public String Title;

		/**
		 * 热门景点
		 * 
		 * @param areaName
		 *            名称
		 * @param iD
		 * @param photo
		 * @param title
		 */
		public HotAreaInfo(String areaName, String iD, String photo, String title) {
			super();
			AreaName = areaName;
			ID = iD;
			Photo = photo;
			Title = title;
		}

	}

	public static class HotTourismInfo {
		public String Grades;
		public String ID;
		public String ImageUrl;
		public String Price;
		public String Rostered;
		public String Title;
		public String TouCount;
		public String AdvTitle;
	}

	public static class HotCityInfo {
		public String CityName;
		public String ID;
	}
}
