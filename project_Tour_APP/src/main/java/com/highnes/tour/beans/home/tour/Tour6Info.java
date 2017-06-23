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
public class Tour6Info {

	public String status;
	public List<HotCityInfo> HotCity;
	public List<HotTourismInfo> HotTourism;

	public static class HotCityInfo {
		public String CityName;
		public String ID;
		public String MadeinChina;
		public String Title;
		public String Photo;

		/**
		 * 
		 * @param cityName
		 * @param iD
		 * @param madeinChina
		 *            True表示国内
		 * @param title
		 * @param photo
		 */
		public HotCityInfo(String cityName, String iD, String madeinChina, String title, String photo) {
			super();
			CityName = cityName;
			ID = iD;
			MadeinChina = madeinChina;
			Title = title;
			Photo = photo;
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
		public String SetAdd;
	}

}
