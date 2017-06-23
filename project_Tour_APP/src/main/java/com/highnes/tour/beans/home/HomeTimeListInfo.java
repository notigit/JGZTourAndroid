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
public class HomeTimeListInfo {

	public String status;
	public String Count;
	public List<BenefitInfo> Benefit;

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

}
