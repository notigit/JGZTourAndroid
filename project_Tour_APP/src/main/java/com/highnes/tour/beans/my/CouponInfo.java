package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    积分。
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
public class CouponInfo {

	public String status;
	public String PCount="0"; //没有使用
	public List<RedPacketInfo> RedPacket;
	public List<RedPacketInfo> OutRedPacket;

	public static class RedPacketInfo {
		public Double DisMoney;
		public String EndDate;
		public String ID;
		public Double LowMoney;
		public String ProName;
		public String StartDate;
		public String CouID;
	}

	public static class OutRedPacketInfo {
		public Double DisMoney;
		public String EndDate;
		public String ID;
		public Double LowMoney;
		public String ProName;
		public String StartDate;
		public String CouID;
	}

}
