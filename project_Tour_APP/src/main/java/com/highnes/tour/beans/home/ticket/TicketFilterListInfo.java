package com.highnes.tour.beans.home.ticket;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页..门票..筛选列表。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-19   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketFilterListInfo {

	public String status;
	public List<SoldInfo> sold;
	public String Count;//总条数

	public static class SoldInfo {
		public String ImageUrl;
		public String Title;
		public Double NewPrice;
		public Double OldPrice;
		public String SoldID;
		public String SoldName;
		public Double Landmark;
		public Double Latitude;
		public Boolean IsRefunds;
		public Boolean IsReserved;
		public Boolean IsUpdate;
		public String AreaID;

		public SoldInfo(String imageUrl, String title, Double newPrice, Double oldPrice, String soldID, String soldName, Double landmark, Double latitude,
				Boolean isRefunds, Boolean isReserved, Boolean isUpdate, String areaID) {
			super();
			ImageUrl = imageUrl;
			Title = title;
			NewPrice = newPrice;
			OldPrice = oldPrice;
			SoldID = soldID;
			SoldName = soldName;
			Landmark = landmark;
			Latitude = latitude;
			IsRefunds = isRefunds;
			IsReserved = isReserved;
			IsUpdate = isUpdate;
			AreaID = areaID;
		}

	}
}
