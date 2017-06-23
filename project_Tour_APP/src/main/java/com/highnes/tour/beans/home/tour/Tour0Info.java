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
public class Tour0Info {

	public String status;
	public List<HotSoldInfo> HotSold;
	public List<SoldInfo> Sold;
	public String SoldConut;

	public static class HotSoldInfo {
		public String SCity;
		public Double SNewPrice;
		public Double SOldPrice;
		public String SPhoto;
		public String Sold;
		public String SoldName;
		public String AreaID;

		/**
		 * 门票
		 * 
		 * @param sCity
		 *            地址
		 * @param sNewPrice
		 *            现价
		 * @param sOldPrice
		 *            原价
		 * @param sPhoto
		 *            主图
		 * @param sold
		 *            门票id
		 * @param soldName
		 *            门票名称
		 */
		public HotSoldInfo(String sCity, Double sNewPrice, Double sOldPrice, String sPhoto, String sold, String soldName) {
			super();
			SCity = sCity;
			SNewPrice = sNewPrice;
			SOldPrice = sOldPrice;
			SPhoto = sPhoto;
			Sold = sold;
			SoldName = soldName;
		}

	}

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
