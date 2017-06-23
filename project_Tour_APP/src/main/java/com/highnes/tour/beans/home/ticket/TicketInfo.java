package com.highnes.tour.beans.home.ticket;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页的接口汇总。
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
public class TicketInfo {

	public String status;
	public List<AdvertisementInfo> Advertisement;
	public List<AreaTypeInfo> AreaType;
	public List<SoldInfo> sold;
	public List<SoldInfo> Zsold;
	public List<StretchInfo> Stretch;

	/**
	 * 
	 * @param status
	 * @param advertisement
	 *            轮播图
	 * @param areaType
	 *            类型
	 * @param sold
	 *            门票
	 * @param stretch
	 *            活动
	 */
	public TicketInfo(String status, List<AdvertisementInfo> advertisement, List<AreaTypeInfo> areaType, List<SoldInfo> sold, List<StretchInfo> stretch) {
		super();
		this.status = status;
		Advertisement = advertisement;
		AreaType = areaType;
		this.sold = sold;
		Stretch = stretch;
	}

	public static class AdvertisementInfo {
		public String ID;
		public String JumpUrl;
		public String PhotoUrl;
		public Integer Sort;

		/**
		 * 轮播图
		 * 
		 * @param iD
		 *            资源id
		 * @param jumpUrl
		 *            跳转地址
		 * @param photoUrl
		 *            图片地址
		 * @param sort
		 *            排序
		 */
		public AdvertisementInfo(String iD, String jumpUrl, String photoUrl, Integer sort) {
			super();
			ID = iD;
			JumpUrl = jumpUrl;
			PhotoUrl = photoUrl;
			Sort = sort;
		}

	}

	public static class AreaTypeInfo {
		public String ID;
		public String ImageUrl;
		public String TypeName;

		/**
		 * 景区类型
		 * 
		 * @param iD
		 *            id
		 * @param imageUrl
		 *            图标
		 * @param typeName
		 *            类型名称
		 */
		public AreaTypeInfo(String iD, String imageUrl, String typeName) {
			super();
			ID = iD;
			ImageUrl = imageUrl;
			TypeName = typeName;
		}

	}

	public static class SoldInfo {
		public String ImageUrl;
		public String Title;
		public String NewPrice;
		public String OldPrice;
		public String SoldID;
		public String SoldName;
		public String Landmark;
		public String Latitude;
		public String IsRefunds;
		public String IsReserved;
		public String IsUpdate;
		public String AreaID;

		/**
		 * 门票信息
		 * 
		 * @param imageUrl
		 *            门票主图
		 * @param locality
		 *            地址
		 * @param newPrice
		 *            现价
		 * @param oldPrice
		 *            原价
		 * @param soldID
		 *            门票id
		 * @param soldName
		 *            门票名
		 * @param latitude
		 *            纬度
		 * @param title
		 *            标题
		 * @param landmark
		 *            经度
		 * @param isRefunds
		 *            是否可退 true表示可以
		 * @param isReserved
		 *            是否当日可订 true表示可以
		 * @param isUpdate
		 *            是否可改签 true表示可以
		 * @param AreaID
		 *            景区门票
		 */
		public SoldInfo(String imageUrl, String title, String newPrice, String oldPrice, String soldID, String soldName, String landmark, String latitude,
				String isRefunds, String isReserved, String isUpdate, String AreaID) {
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
			this.AreaID = AreaID;
		}
	}

	public static class StretchInfo {
		public String StretchID;
		public String StretchImageUrl;
		public String StretchName;
		public String StretchTitle;

		/**
		 * 活动
		 * 
		 * @param stretchID
		 *            活动id
		 * @param stretchImageUrl
		 *            活动主图
		 * @param stretchName
		 *            活动名称
		 * @param stretchTitle
		 *            活动标题
		 */
		public StretchInfo(String stretchID, String stretchImageUrl, String stretchName, String stretchTitle) {
			super();
			StretchID = stretchID;
			StretchImageUrl = stretchImageUrl;
			StretchName = stretchName;
			StretchTitle = stretchTitle;
		}

	}
}
