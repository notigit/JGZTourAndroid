package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    旅游度假..景区门票。
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
public class Tour1Info {

	public String status;
	public List<TouAreaInfo> TouArea;
	public List<TourismsInfo> Tourisms;
	public List<TourismsInfo> ZBTourisms;
	public String SoldConut;

	public static class TouAreaInfo {
		public String AreaName;
		public String ID;

		/**
		 * 热门目的地
		 * 
		 * @param areaName
		 *            名称
		 * @param iD
		 */
		public TouAreaInfo(String areaName, String iD) {
			super();
			AreaName = areaName;
			ID = iD;
		}

	}

	public static class TourismsInfo {
		public String Grades;
		public String ID;
		public String ImageUrl;
		public String Price;
		public String Rostered;
		public String Title;
		public String TourismType;
		public String TypeName;

		/**
		 * 本地景点
		 * 
		 * @param grades
		 *            满意度
		 * @param iD
		 * @param imageUrl
		 *            封面图
		 * @param price
		 *            起价
		 * @param rostered
		 *            班期
		 * @param title
		 *            标题
		 * @param tourismType
		 *            旅游类型
		 * @param typeName
		 *            景点类型
		 */
		public TourismsInfo(String grades, String iD, String imageUrl, String price, String rostered, String title, String tourismType, String typeName) {
			super();
			Grades = grades;
			ID = iD;
			ImageUrl = imageUrl;
			Price = price;
			Rostered = rostered;
			Title = title;
			TourismType = tourismType;
			TypeName = typeName;
		}

	}

	public static class ZBTourismsInfo {
		public String Grades;
		public String ID;
		public String ImageUrl;
		public String Price;
		public String Rostered;
		public String Title;
		public String TourismType;
		public String TypeName;

		/**
		 * 周边景点 同上
		 * 
		 * @param grades
		 * @param iD
		 * @param imageUrl
		 * @param price
		 * @param rostered
		 * @param title
		 * @param tourismType
		 * @param typeName
		 */
		public ZBTourismsInfo(String grades, String iD, String imageUrl, String price, String rostered, String title, String tourismType, String typeName) {
			super();
			Grades = grades;
			ID = iD;
			ImageUrl = imageUrl;
			Price = price;
			Rostered = rostered;
			Title = title;
			TourismType = tourismType;
			TypeName = typeName;
		}

	}
}
