package com.highnes.tour.beans.find;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    发现推荐的门票。
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
public class FindDetailTicketInfo {

	public String status;
	public List<SoldInfo> Sold;

	public static class SoldInfo {
		public String Locality;
		public Double NewPrice;
		public Double OldPrice;
		public String ImageUrl;
		public String SoldID;
		public String SoldName;
		public String AreaID;
	}
}
