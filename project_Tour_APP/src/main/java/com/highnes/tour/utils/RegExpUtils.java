package com.highnes.tour.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 摘要：正则表达式验证
 * 
 * @date 2015-5-28
 * @author FUNY
 * 
 */
public class RegExpUtils {
	/**
	 * 正则表达式：验证用户名
	 */
	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

	/**
	 * 正则表达式：验证密码
	 */
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式：验证汉字
	 */
	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

	/**
	 * 正则表达式：验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 正则表达式：验证IP地址
	 */
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	/**
	 * 验证手机格式
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return true表示合法
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 *            email地址
	 * @return true表示合法
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 判断是否全是数字
	 * 
	 * @param str
	 *            验证的字符串
	 * @return true全是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 帐号是否合法(字母开头，允许5-16字节，允许字母数字)
	 * 
	 * @param account
	 *            账号
	 * @return true表示合法
	 */
	public static boolean isAccount(String account) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9]{4,15}$");
		Matcher isAccount = pattern.matcher(account);
		return isAccount.matches();
	}

	/**
	 * 替换手机号码为****
	 * 
	 */
	public static String replacePhoneNum(String mobile) {
		if (mobile.length() >= 11) {
			return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
		} else {
			return mobile;
		}
	}

	/**
	 * 验证用户密码(长度在6-12之间， 包括特殊符号)
	 * 
	 * @param account
	 *            账号
	 * @return true表示合法
	 */
	public static boolean isPassword(String password) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{6,11}$");
		Matcher isAccount = pattern.matcher(password);
		return isAccount.matches();
	}

	/**
	 * 验证用户密码(长度在6-32之间， 字母+数字组合)
	 * 
	 * @param password
	 *            账号
	 * @return true表示合法
	 */
	public static boolean isPass(String password) {
		Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$");
		Matcher isAccount = pattern.matcher(password);
		return isAccount.matches();
	}

	/**
	 * 验证用户密码是不是纯数字
	 * 
	 * @param account
	 *            账号
	 * @return true表示是纯数字
	 */
	public static boolean isPasswordNum(String account) {
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher isAccount = pattern.matcher(account);
		return isAccount.matches();
	}

	/**
	 * 验证用户名(长度在2-18之间， 包括中文、大小写字母和数字)
	 * 
	 * @param account
	 *            账号
	 * @return true表示合法
	 */
	public static boolean isName(String name) {

		Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,18}$");
		Matcher misAccount = pattern.matcher(name);

		return misAccount.matches();
	}

	@SuppressWarnings("static-access")
	/**
	 * 过滤掉特殊字符
	 * @param str 需要过滤的字符
	 * @return 过滤后的字符
	 */
	public static String stringFilter(String str) {
		// 只允许字母和数字和中文//[\\pP‘’“”
		String regEx = "^[A-Za-z\\d\\u4E00-\\u9FA5\\p{P}‘’“”]+$";
		Pattern p = Pattern.compile(regEx);
		StringBuilder sb = new StringBuilder(str);

		for (int len = str.length(), i = len - 1; i >= 0; --i) {

			if (!p.matches(regEx, String.valueOf(str.charAt(i)))) {
				sb.deleteCharAt(i);
			}
		}

		return sb.toString();
	}

	/**
	 * 校验用户名
	 * 
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	// /**
	// * 校验密码
	// *
	// * @param password
	// * @return 校验通过返回true，否则返回false
	// */
	// public static boolean isPassword(String password) {
	// return Pattern.matches(REGEX_PASSWORD, password);
	// }

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	// /**
	// * 校验邮箱
	// *
	// * @param email
	// * @return 校验通过返回true，否则返回false
	// */
	// public static boolean isEmail(String email) {
	// return Pattern.matches(REGEX_EMAIL, email);
	// }

	/**
	 * 校验汉字
	 * 
	 * @param chinese
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

//	/**
//	 * 校验身份证
//	 * 
//	 * @param idCard
//	 * @return 校验通过返回true，否则返回false
//	 */
//	public static boolean isIDCard(String idCard) {
//		return Pattern.matches(REGEX_ID_CARD, idCard);
//	}

	/**
	 * 校验URL
	 * 
	 * @param url
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUrl(String url) {
		return Pattern.matches(REGEX_URL, url);
	}

	/**
	 * 校验IP地址
	 * 
	 * @param ipAddr
	 * @return
	 */
	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String number) {
		String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
		return isCorrect(rgx, number);
	}

	// 正则验证
	private static boolean isCorrect(String rgx, String res) {
		Pattern p = Pattern.compile(rgx);
		Matcher m = p.matcher(res);
		return m.matches();
	}
}
