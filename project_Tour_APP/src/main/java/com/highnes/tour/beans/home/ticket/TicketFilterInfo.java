package com.highnes.tour.beans.home.ticket;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页..门票..筛选菜单。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-19   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketFilterInfo {

	public String status;
	public List<AreaTypeInfo> AreaType;
	public List<SiftingsInfo> Siftings;
	public List<HotCityInfo> HotCity;

	public static class AreaTypeInfo {
		public String TID;
		public String TypeName;

		/**
		 * 
		 * @param tID
		 *            主题id
		 * @param typeName
		 *            主题名称
		 */
		public AreaTypeInfo(String tID, String typeName) {
			super();
			TID = tID;
			TypeName = typeName;
		}

	}
	public static class HotCityInfo {
		public String HID;
		public String CtiyName;
		
	}

	public static class SiftingsInfo {
		public String Type;
		public List<SiftingInfo> sifting;

		/**
		 * 
		 * @param type
		 *            类型
		 * @param sifting
		 *            项
		 */
		public SiftingsInfo(String type, List<SiftingInfo> sifting) {
			super();
			Type = type;
			this.sifting = sifting;
		}

		public static class SiftingInfo {
			public String ID;
			public String Name;
			private boolean isSelect = false;

			public boolean isSelect() {
				return isSelect;
			}

			public void setSelect(boolean isSelect) {
				this.isSelect = isSelect;
			}

			/**
			 * 
			 * @param iD
			 *            id
			 * @param name
			 *            名称
			 */
			public SiftingInfo(String iD, String name) {
				super();
				ID = iD;
				Name = name;
			}

		}
	}
}
