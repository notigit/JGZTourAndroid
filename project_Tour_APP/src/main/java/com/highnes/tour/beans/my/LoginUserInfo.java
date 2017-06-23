package com.highnes.tour.beans.my;

import java.util.List;

import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    个人信息。
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
public class LoginUserInfo {
	public String status;
	public List<ValueInfo> value;

	public static class ValueInfo {
		public String UserID;
		public String Email;
		public String UserName;
		public String Phone;
		public String HeadImg;
		public String Sex;
		public String Name;
		public String GradeName;
		public String Birth;
		public Integer Ponit;
		public Integer NumberID;

		public Integer getSex() {
			return !StringUtils.isEmpty(Sex) && Sex.equals("女") ? 0 : 1;
		}

		/**
		 * 
		 * @param iD
		 *            用户ID
		 * @param areaName
		 *            社区名称
		 * @param email
		 *            邮箱
		 * @param hnumber
		 *            门牌号
		 * @param name
		 *            姓名
		 * @param phone
		 *            手机号
		 * @param qQ
		 *            qq
		 * @param ImageUrl
		 *            头像
		 * @param sex
		 *            性别
		 */
		public ValueInfo(String iD, String email, String name, String phone, String imageUrl, String sex) {
			super();
			UserID = iD;
			Email = email;
			UserName = name;
			Phone = phone;
			HeadImg = imageUrl;
			Sex = sex;
		}

	}

}
