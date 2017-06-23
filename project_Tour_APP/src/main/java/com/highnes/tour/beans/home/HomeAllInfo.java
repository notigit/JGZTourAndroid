package com.highnes.tour.beans.home;

import java.util.List;

import com.highnes.tour.utils.DateUtils;

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
public class HomeAllInfo {

	public String status;
	public List<AdvertisementInfo> Advertisement;
	public List<BenefitInfo> Benefit;
	public List<NewsInfo> News;
	public List<SoldInfo> Sold;
	public List<StretchInfo> Stretch;

	/**
	 * 
	 * @param status
	 *            状态码 1是正确，0是错误
	 * @param advertisement
	 *            轮播图
	 * @param benefit
	 *            抢购信息
	 * @param news
	 *            头条
	 * @param sold
	 *            门票
	 * @param stretch
	 *            活动
	 */
	public HomeAllInfo(String status, List<AdvertisementInfo> advertisement, List<BenefitInfo> benefit, List<NewsInfo> news, List<SoldInfo> sold,
			List<StretchInfo> stretch) {
		super();
		this.status = status;
		Advertisement = advertisement;
		Benefit = benefit;
		News = news;
		Sold = sold;
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

	public static class BenefitInfo {
		public String Adate;
		public Double NewPrice;
		public String Odate;
		public Double OldPrice;
		public String Photo;
		public String SoldID;
		public String Title;
		public String TourismID;
		public String ID;
		private Long restTime;// 剩余的时间戳

		public Long getRestTime() {
			Long oTime = DateUtils.convert2long(DateUtils.formatDates(Odate, "yyyy-MM-dd HH:mm:ss"));
			Long aTime = System.currentTimeMillis();
			restTime = oTime - aTime;
			return restTime;
		}

		public void setRestTime(Long restTime) {
			this.restTime = restTime;
		}

		/**
		 * 抢购信息
		 * 
		 * @param adate
		 *            开始时间
		 * @param newPrice
		 *            现价
		 * @param odate
		 *            结束时间
		 * @param oldPrice
		 *            原价
		 * @param photo
		 *            抢购主图
		 * @param soldID
		 *            抢购门票id
		 * @param title
		 *            标题
		 * @param tourismID
		 *            抢购旅游产品id
		 */
		public BenefitInfo(String adate, Double newPrice, String odate, Double oldPrice, String photo, String soldID, String title, String tourismID) {
			super();
			Adate = adate;
			NewPrice = newPrice;
			Odate = odate;
			OldPrice = oldPrice;
			Photo = photo;
			SoldID = soldID;
			Title = title;
			TourismID = tourismID;
		}

	}

	public static class NewsInfo {
		public String NewsAddTime;
		public String NewsID;
		public String NewsTitle;
		public String NewsType;

		/**
		 * 头条
		 * 
		 * @param newsAddTime
		 *            添加时间
		 * @param newsID
		 *            消息id
		 * @param newsTitle
		 *            消息标题
		 * @param newsType
		 *            消息类型
		 */
		public NewsInfo(String newsAddTime, String newsID, String newsTitle, String newsType) {
			super();
			NewsAddTime = newsAddTime;
			NewsID = newsID;
			NewsTitle = newsTitle;
			NewsType = newsType;
		}

	}

	public static class SoldInfo {
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
		public SoldInfo(String sCity, Double sNewPrice, Double sOldPrice, String sPhoto, String sold, String soldName) {
			super();
			SCity = sCity;
			SNewPrice = sNewPrice;
			SOldPrice = sOldPrice;
			SPhoto = sPhoto;
			Sold = sold;
			SoldName = soldName;
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
