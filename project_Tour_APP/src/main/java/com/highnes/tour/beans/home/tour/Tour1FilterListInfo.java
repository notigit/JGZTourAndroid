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
public class Tour1FilterListInfo {

	public String status;
	public List<TourismsInfo> Tourisms;
	public List<TourismsInfo> Tourism;
	public String Count;

	public static class TourismsInfo {
		public String Grades;
		public String ID;
		public String ImageUrl;
		public String Price;
		public String Rostered;
		public String Title;
		public String TourismType;
		public String TouCount;
		public String AdvTitle;
		public String BackAdd;
		public String TypeName;
		
		
	}

}
