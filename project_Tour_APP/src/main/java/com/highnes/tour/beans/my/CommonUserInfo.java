package com.highnes.tour.beans.my;

import java.io.Serializable;
import java.util.List;

import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    常用信息 。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CommonUserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String status;
	public List<PeopleInfo> people;

	public static class PeopleInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String ID;
		public String IDcard;
		public Boolean IsDefault;
		public Boolean IsBaby = false;
		public String Name;
		public String Phone;
		private boolean isSelect = false;

		public boolean isSelect() {
			return isSelect;
		}

		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}

		public Boolean getIsBaby() {
			return IsBaby == null ? false : IsBaby;
		}

		/**
		 * 
		 * @param iD
		 *            用户id
		 * @param iDcard
		 *            身份证
		 * @param isDefault
		 *            是否是默认的取票人
		 * @param name
		 *            姓名
		 * @param phone
		 *            手机号码
		 */
		public PeopleInfo(String iD, String iDcard, Boolean isDefault, String name, String phone) {
			super();
			ID = iD;
			IDcard = iDcard;
			IsDefault = isDefault;
			Name = name;
			Phone = phone;
		}

		/**
		 * 
		 * @param iD
		 *            用户id
		 * @param iDcard
		 *            身份证
		 * @param isDefault
		 *            是否是默认的取票人
		 * @param name
		 *            姓名
		 * @param phone
		 *            手机号码
		 */
		public PeopleInfo(String iD, String iDcard, Boolean isDefault, String name, String phone, Boolean IsBaby) {
			super();
			ID = iD;
			IDcard = iDcard;
			IsDefault = isDefault;
			Name = name;
			Phone = phone;
			this.IsBaby = IsBaby;
		}

	}

}
