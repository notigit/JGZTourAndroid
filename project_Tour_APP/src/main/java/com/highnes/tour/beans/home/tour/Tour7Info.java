package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    旅游度假..野营。
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
public class Tour7Info {

	public String status;
	public String Count;
	public List<CampingInfo> Camping;

	public static class CampingInfo {
		public String CamAreaID;
		public Integer Grades;
		public String ID;
		public String ImageUrl;
		public Double NewPrice;
		public String Name;
		public String Title;
		public Integer Pcount;

		/**
		 * 
		 * @param camAreaID
		 *            景区ID
		 * @param grades
		 *            满意度
		 * @param iD
		 * @param imageUrl
		 * @param newPrice
		 * @param name
		 * @param title
		 * @param pcount
		 *            参与过的人数
		 */
		public CampingInfo(String camAreaID, Integer grades, String iD, String imageUrl, Double newPrice, String name, String title, Integer pcount) {
			super();
			CamAreaID = camAreaID;
			Grades = grades;
			ID = iD;
			ImageUrl = imageUrl;
			NewPrice = newPrice;
			Name = name;
			Title = title;
			Pcount = pcount;
		}

	}
}