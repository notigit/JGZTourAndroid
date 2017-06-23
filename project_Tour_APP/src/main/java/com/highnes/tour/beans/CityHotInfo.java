package com.highnes.tour.beans;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页..定位..热门城市。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-18   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CityHotInfo {

	public String status;
	public List<HitCityInfo> HitCity;

	public static class HitCityInfo {
		public String CityName;
		public String ID;
		public Integer Sort;

		public HitCityInfo(String cityName, String iD, Integer sort) {
			super();
			CityName = cityName;
			ID = iD;
			Sort = sort;
		}

	}
}
