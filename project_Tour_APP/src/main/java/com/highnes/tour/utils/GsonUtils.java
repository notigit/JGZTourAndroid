package com.highnes.tour.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * <PRE>
 * 作用:
 *    Gson解析工具。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0        2016-02-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class GsonUtils {
	/**
	 * json转成对象
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T json2T(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * json转成list
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> json2List(String json, Class<T[]> type) {
		T[] list = new Gson().fromJson(json, type);
		return Arrays.asList(list);
	}

	/**
	 * json转成list //---------------------------不能使用---------------------------
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> json2ListTemp(String jsonString, Class<T[]> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<T[]>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * json转成list中有map的
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> json2ListMap(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * json转成map的
	 * 
	 * @param gsonString
	 * @return
	 */
	public static <T> Map<String, T> json2Map(String jsonString) {
		Map<String, T> map = new HashMap<String, T>();
		try {
			Gson gson = new Gson();
			map = gson.fromJson(jsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
