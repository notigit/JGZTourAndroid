package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    支付信息。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-25   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour7OrderPayInfo {

	public String status;
	public String CamName;
	public String Count;
	public String GoTime;
	public String Multiply;
	public String OrderID;
	public String ProID;
	public String OverTime;
	public String Phone;
	public String Price;
	public String ServicePhone;
	public String TypeName;
	public String UserName;
	public String UserPonit;
	public List<UserRedInfo> UserRed;// 红包
	public static class UserRedInfo {
		// 红包
	}
}
