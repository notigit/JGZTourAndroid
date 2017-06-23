package com.highnes.tour.utils;

import java.util.Comparator;

import com.highnes.tour.beans.CityHotInfo;
import com.highnes.tour.beans.CityHotInfo.HitCityInfo;
import com.highnes.tour.beans.find.FindInfo;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.ticket.TicketInfo;

/**
 * <PRE>
 * 作用:
 *    轮播图排序。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-05-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public interface SortComparator {
	/**
	 * compareTo()：大于0表示前一个数据比后一个数据大， 0表示相等，小于0表示前一个数据小于后一个数据
	 * 相等时会走到equals()，这里讲姓名年龄都一样的对象当作一个对象
	 */

	public class TicketComparator implements Comparator<TicketInfo.AdvertisementInfo> {

		@Override
		public int compare(TicketInfo.AdvertisementInfo lhs, TicketInfo.AdvertisementInfo rhs) {
			if (lhs.Sort > rhs.Sort) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 首页轮播图
	 * 
	 * @author Administrator
	 * 
	 */
	public class HomeTicketComparator implements Comparator<HomeAllInfo.AdvertisementInfo> {

		@Override
		public int compare(HomeAllInfo.AdvertisementInfo lhs, HomeAllInfo.AdvertisementInfo rhs) {
			if (lhs.Sort > rhs.Sort) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 热门城市
	 * 
	 * @author Administrator
	 * 
	 */
	public class CityHotComparator implements Comparator<CityHotInfo.HitCityInfo> {

		@Override
		public int compare(CityHotInfo.HitCityInfo lhs, CityHotInfo.HitCityInfo rhs) {
			if (lhs.Sort > rhs.Sort) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 发现轮播图
	 * 
	 * @author Administrator
	 * 
	 */
	public class FindComparator implements Comparator<FindInfo.AdvertisementInfo> {

		@Override
		public int compare(FindInfo.AdvertisementInfo lhs, FindInfo.AdvertisementInfo rhs) {
			if (lhs.Sort > rhs.Sort) {
				return 1;
			} else {
				return -1;
			}
		}

	}

}
