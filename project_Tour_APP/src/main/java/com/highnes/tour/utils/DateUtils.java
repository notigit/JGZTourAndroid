package com.highnes.tour.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;

/**
 * <PRE>
 * 作用:
 *    时间工具类。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-01   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
	/**
	 * 将日期格式的字符串转换为长整型
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long convert2long(String date) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	@SuppressWarnings("deprecation")
	public static long convert2longT(String dates) {
		Date date = new Date(dates);
		return date.getTime();
	}

	public static long convert2longT(Date date) {
		return date.getTime();
	}

	/**
	 * 返回当前系统时间
	 * 
	 * @return 返回yyyy-MM-dd HH:mm:ss格式的时间
	 */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回当前系统时间
	 * 
	 * @return 返回HH:mm 的格式
	 */
	public static String getCurrentTimes() {
		return getCurrentTime("HH:mm");
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToDateTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToDateTime2(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 是否大于当前的时间
	 * 
	 * @param time
	 *            比较的时间 比如 18点半 1830
	 * @return 当前20点 返回 false
	 */
	public static boolean isGTRCurrentTime(int time) {

		int currTime = Integer.valueOf(getCurrentTime("HHmm"));
		// 2000>1830 true
		return currTime < time ? true : false;
	}

	/**
	 * 是否大于当前的时间
	 * 
	 * @param time
	 *            比较的时间 比如 18点半 1830
	 * @return 当前20点 返回 false
	 */
	public static boolean isGTRCurrentDate(int date) {

		int currTime = Integer.valueOf(getCurrentTime("yyyyMMdd"));
		// 2000>1830 true
		return currTime <= date ? true : false;
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToDate(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToDate(long time, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToHMSTime(long date) {
		Long miao = 0L; // 剩余的总的秒数
		Long HHH = 0L;
		Long mmm = 0L;
		Long sss = 0L;
		int yyyy = Integer.valueOf(convertToDate(date, "yyyy"));
		int MM = Integer.valueOf(convertToDate(date, "MM"));
		int dd = Integer.valueOf(convertToDate(date, "dd"));
		int HH = Integer.valueOf(convertToDate(date, "HH"));
		int mm = Integer.valueOf(convertToDate(date, "mm"));
		int ss = Integer.valueOf(convertToDate(date, "ss"));
		if (yyyy > 0) {
			miao += yyyy * 365 * 24 * 60 * 60;
			HHH += yyyy * 365 * 24;
			if (MM > 0) {
				miao += MM * 30 * 24 * 60 * 60;
				HHH += MM * 30 * 24;
				if (dd > 0) {
					miao += dd * 24 * 60 * 60;
					HHH += dd * 24;
					if (HH > 0) {
						miao += HH * 60 * 60;
						HHH += HH;
						if (mm > 0) {
							miao += mm * 60;
							mmm += mm;
							if (ss > 0) {
								sss += ss;
								miao += ss;
							}
						}
					}
				}
			}
		}
		return HHH + "," + mmm + "," + sss;
	}

	/**
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToHMS(long times) {
		double days = Math.floor(times / 1000 / 86400);
		double hourtime = (times - days * 86400);
		double hours = Math.floor(hourtime / 3600);
		double mintime = hourtime - hours * 3600;
		double minutes = Math.floor(mintime / 60);
		double second = mintime - minutes * 60;
		if (times <= 0) {
			return "0,0,0";
		} else {
			if (days > 0) {
				if (days * 24 + hours > 99) {
					return "99," + minutes + "," + second;
				} else {
					return days * 24 + hours + "," + minutes + "," + second;
				}
			} else {
				return hours + "," + minutes + "," + second;
			}
		}
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToDateChina(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 根据出生日期获得年龄
	 * 
	 * @param birthDay
	 *            传入的生日
	 * @return 年龄
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
				}
			} else {
				age--;
			}
		} else {
		}

		return age;
	}

	/**
	 * 根据月日获得星座
	 * 
	 * @param month
	 *            传入的月份
	 * @param day
	 *            传入的日
	 * @return 返回星座
	 */
	public static String getAstro(int month, int day) {
		String[] astro = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
		int[] arr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };// 两个星座分割日
		int index = month;
		// 所查询日期在分割日之前，索引-1，否则不变
		if (day < arr[month - 1]) {
			index = index - 1;
		}
		// 返回索引指向的星座string
		return astro[index];
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @param start
	 *            开始时间 yyyy-MM-dd
	 * @param end
	 *            结束时间 yyyy-MM-dd
	 * @return 相差的天数
	 */
	@SuppressLint("SimpleDateFormat")
	public static int getGapCount(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 转换日期
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getFormat(String date, String format) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat2 = new SimpleDateFormat(format);
		try {
			Date d = dateFormat.parse(date);
			return dateFormat2.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 把日期加减
	 * 
	 * @param date
	 *            需要变化的日期（yyyy-MM-dd）
	 * @param day
	 *            变化的天数 增加传正数，减少传负数
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateAdd(String date, int day) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = format.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(c.DATE, day);
			Date temp_date = c.getTime();
			return format.format(temp_date);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期加减
	 * 
	 * @param date
	 *            需要变化的日期（yyyy-MM-dd）
	 * @param day
	 *            变化的天数 增加传正数，减少传负数
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateAddChina(String date, int day) {
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Date d = format.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(c.DATE, day);
			Date temp_date = c.getTime();
			return format.format(temp_date);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期加减
	 * 
	 * @param date
	 *            需要变化的日期（yyyy-MM-dd）
	 * @param month
	 *            变化的月数 增加传正数，减少传负数
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateAddMonth(String date, int month) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = format.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(c.MONTH, month);
			Date temp_date = c.getTime();
			return format.format(temp_date);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把分钟加减
	 * 
	 * @param date
	 *            需要变化的日期（HH:mm）
	 * @param month
	 *            变化的月数 增加传正数，减少传负数
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateAddMin(String date, int min) {
		DateFormat format = new SimpleDateFormat("HH:mm");
		try {
			Date d = format.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(c.MINUTE, min);
			Date temp_date = c.getTime();
			return format.format(temp_date);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期变化格式
	 * 
	 * @param date
	 *            传入的日期
	 * @param format
	 *            需要变化的日期（yyyy-MM-dd）
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDate(String date, String formatStr) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatReturn = new SimpleDateFormat(formatStr);
		try {
			Date d = format.parse(date);
			return formatReturn.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 把日期变化格式
	 * 
	 * @param date
	 *            传入的日期
	 * @param format
	 *            需要变化的日期（yyyy-MM-dd）
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String format2Date(String date, String formatStr) {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat formatReturn = new SimpleDateFormat(formatStr);
		try {
			Date d = format.parse(date);
			return formatReturn.format(d);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期变化格式
	 * 
	 * @param date
	 *            传入的日期
	 * @param format
	 *            需要变化的日期（yyyy-MM-dd）
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDates(String date, String formatStr) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// format.setTimeZone(TimeZone.getTimeZone("UTC"));
		DateFormat formatReturn = new SimpleDateFormat(formatStr);
		try {
			Date d = format.parse(date);
			return formatReturn.format(d);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期变化格式
	 * 
	 * @param date
	 *            传入的日期
	 * @param format
	 *            需要变化的日期（yyyy-MM-dd）
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDateChina(String date, String formatStr) {
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		DateFormat formatReturn = new SimpleDateFormat(formatStr);
		try {
			Date d = format.parse(date);
			return formatReturn.format(d);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期变化格式
	 * 
	 * @param date
	 *            传入的日期
	 * @param format
	 *            需要变化的日期（yyyy-MM-dd HH:mm:ss）
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDateAll(String date, String formatStr) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat formatReturn = new SimpleDateFormat(formatStr);
		try {
			Date d = format.parse(date);
			return formatReturn.format(d);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 把日期变化格式 月份变化
	 * 
	 * @param date
	 *            传入的日期
	 * @return 变化后的日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDate(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM");
		DateFormat formatYears = new SimpleDateFormat("yyyy");
		DateFormat formatReturn = new SimpleDateFormat("MM");
		try {
			Date d = format.parse(date);
			String month = "";
			switch (Integer.parseInt(formatReturn.format(d))) {

			case 1:
				month = formatYears.format(d) + "";
				break;

			case 2:
				month = "2月";
				break;
			case 3:
				month = "3月";
				break;
			case 4:
				month = "4月";
				break;
			case 5:
				month = "5月";
				break;
			case 6:
				month = "6月";
				break;
			case 7:
				month = "7月";
				break;
			case 8:
				month = "8月";
				break;
			case 9:
				month = "9月";
				break;
			case 10:
				month = "10月";
				break;
			case 11:
				month = "11月";
				break;
			case 12:
				month = "12月";
				break;
			default:
				month = "temp";
				break;
			}
			return month;

		} catch (ParseException e) {
			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            设置的需要判断的时间 //格式如2012-09-08
	 * 
	 * 
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getWeek(String pTime) {

		String Week = "周";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	public static String formatTimeString(String time) {
		return formatTimeString(convert2long(time));
	}

	/**
	 * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
	 * 
	 * @param context
	 * @param when
	 * @return String
	 */
	public static String formatTimeString(long when) {
		Time then = new Time();
		then.set(when);
		Time now = new Time();
		now.setToNow();

		String formatStr;
		if (then.year != now.year) {
			formatStr = "yyyy年MM月dd日";
		} else if (then.yearDay != now.yearDay) {
			// If it is from a different day than today, show only the date.
			formatStr = "MM月dd日";
		} else {
			// Otherwise, if the message is from today, show the time.
			formatStr = "HH时MM分";
		}

		if (then.year == now.year && then.yearDay == now.yearDay) {
			return "今天";
		} else if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {
			return "昨天";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			String temp = sdf.format(when);
			if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {
				temp = temp.substring(1);
			}
			return temp;
		}
	}

	/**
	 * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
	 * 
	 * @param context
	 * @param when
	 * @return String
	 */
	public static String formatTimerString(long when) {
		Time then = new Time();
		then.set(when);
		Time now = new Time();
		now.setToNow();

		String formatStr;
		if (then.year != now.year) {
			formatStr = "yyyy年MM月dd日";
		} else if (then.yearDay != now.yearDay) {
			formatStr = "MM月dd日";
		} else {
			formatStr = "HH时MM分";
		}

		if (then.year == now.year && then.yearDay == now.yearDay) {
			return "今天";
		} else if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {
			return "昨天";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			String temp = sdf.format(when);
			if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {
				temp = temp.substring(1);
			}
			return temp;
		}
	}

	/**
	 * 计算时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getStandardDate(long time) {
		return getStandardDate(convertToDateTime(time));
	}

	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            yyyy-MM-dd HH:mm
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStandardDate(String times) {
		if (times == null || "".equals(times)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date timeStr;
		try {
			timeStr = format.parse(times);

			long t = timeStr.getTime();
			long time = System.currentTimeMillis() - t;
			long mill = (long) Math.ceil(time / 1000);// 秒前

			long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

			long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

			long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
			if (day - 1 > 0) {
				sb.append(day + "天");
			} else if (hour - 1 > 0) {
				if (hour >= 24) {
					sb.append("1天");
				} else {
					sb.append(hour + "小时");
				}
			} else if (minute - 1 > 0) {
				if (minute == 60) {
					sb.append("1小时");
				} else {
					sb.append(minute + "分钟");
				}
			} else if (mill - 1 > 0) {
				if (mill == 60) {
					sb.append("1分钟");
				} else {
					sb.append(mill + "秒");
				}
			} else {
				sb.append("刚刚");
			}
			if (!sb.toString().equals("刚刚")) {
				sb.append("前");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 将多少秒装换成HH:mm:ss格式
	 * 
	 * @param mill
	 *            秒
	 * @return HH:mm:ss格式
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatMill(int mill) {

		String time = "";
		try {
			Format f0 = new SimpleDateFormat("ss");
			SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
			Date d = (Date) f0.parseObject(mill + "");
			time = f1.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 将多少秒装换成HH:mm:ss格式
	 * 
	 * @param mill
	 *            秒
	 * @return HH:mm:ss格式
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatMill(Long mill) {

		String time = "";
		try {
			Format f0 = new SimpleDateFormat("ss");
			SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
			Date d = (Date) f0.parseObject(mill + "");
			time = f1.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	// ----------------------------
	private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeDelta(Date date1, Date date2) {
		long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
		int secondsDelta = (int) timeDelta;
		return secondsDelta;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param dateStr1
	 *            :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2
	 *            :yyyy-MM-dd HH:mm:ss
	 */
	public static int getTimeDelta(String dateStr1, String dateStr2) {
		Date date1 = parseDateByPattern(dateStr1.length() == 16 ? dateStr1 + ":00" : dateStr1, yyyyMMddHHmmss);
		Date date2 = parseDateByPattern(dateStr2, yyyyMMddHHmmss);
		return getTimeDelta(date1, date2);
	}

	public static Date parseDateByPattern(String dateStr, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * return a bitmap from service
	 * 
	 * @param url
	 * @return bitmap type
	 */
	public final static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;

		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;

			conn = (HttpURLConnection) myFileUrl.openConnection();

			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 获取本周一的日期
	 * 
	 * @return
	 */
	public final static String getMondayByWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取本月的第一天的日期
	 * 
	 * @return
	 */
	public final static String getFirstDayByMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(cal.getTime());
	}
}
