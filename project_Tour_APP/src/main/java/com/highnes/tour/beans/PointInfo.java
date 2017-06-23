package com.highnes.tour.beans;

import java.util.List;

/**
 * 积分与红包
 * 
 * @author Administrator
 * 
 */
public class PointInfo {
	public String Multiply;
	public String UserPonit;
	public String status;
	public List<UserRedInfo> UserRed;

	public static class UserRedInfo {
		public String CID;
		public String CName;
		public Double DisMoney;
		public String RID;

		public UserRedInfo(String cID, String cName, Double disMoney, String rID) {
			super();
			CID = cID;
			CName = cName;
			DisMoney = disMoney;
			RID = rID;
		}

	}

}
