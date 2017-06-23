package com.highnes.tour.beans.home.ticket;

/**
 * <PRE>
 * 作用:
 *    首页..门票..详情..购票。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-17   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketDetailBuyInfo {
	public String Details;
	public String Type;
	public String ClienteleUser;
	public String IsRefunds;
	public String IsReserved;
	public String IsUpdate;
	public String NewPrice;
	public String OldPrice;
	public String SID;
	public String SType;
	public String SoldName;
	public String Title;
	public String PriceStart;

	public TicketDetailBuyInfo(String details, String type, String clienteleUser, String isRefunds, String isReserved, String isUpdate, String newPrice,
			String oldPrice, String sID, String sType, String soldName, String title) {
		super();
		Details = details;
		Type = type;
		ClienteleUser = clienteleUser;
		IsRefunds = isRefunds;
		IsReserved = isReserved;
		IsUpdate = isUpdate;
		NewPrice = newPrice;
		OldPrice = oldPrice;
		SID = sID;
		SType = sType;
		SoldName = soldName;
		Title = title;
	}

}
