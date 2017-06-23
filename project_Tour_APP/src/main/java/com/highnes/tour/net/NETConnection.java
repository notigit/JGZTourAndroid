package com.highnes.tour.net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.NetUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    网络通信的基类。
 * 注意事项:
 *    1、采用POST方式的网络请求。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-02-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class NETConnection {
	private int errorCode = 0;

	/**
	 * 成功的回调方法
	 */
	public static interface SuccessCallback {
		/**
		 * 成功方法
		 * 
		 * @param result
		 *            返回结果（解密后的数据）
		 */
		void onSuccess(String result);
	}

	/**
	 * 失败的回调方法
	 */
	public static interface FailCallback {
		/**
		 * 失败方法
		 * 
		 * @param errNum
		 *            0：网络连接延迟，1：服务器错误，2：网络连接不可用
		 */
		void onFail(String info);
	}

	/**
	 * 根据错误的代码编号提示信息
	 * 
	 * @param errNum
	 *            0：网络连接延迟，1：服务器错误，2：网络连接不可用
	 */
	private String callInfo(int errNum) {
		// 0：网络连接延迟，1：服务器错误，2：网络连接不可用
		String info = "";
		switch (errNum) {
		case 0:
			info = "网络连接延迟";
			break;
		case 1:
			info = "服务器错误";
			break;
		case 2:
			info = "网络连接不可用";
			break;
		default:
			break;
		}
		return info;
	}

	/**
	 * NETConnection构造方法
	 * 
	 * @param url
	 *            通信地址
	 * @param successCallback
	 *            成功的回调方法
	 * @param failCallback
	 *            失败的回调方法
	 * @param data
	 *            请求的数据集
	 */
	public NETConnection(Context context, final String url, final SuccessCallback successCallback, final FailCallback failCallback,
			final Map<String, Object> data) {

		if (!NetUtils.isNetworkConnected(context)) {
			failCallback.onFail(callInfo(2));
			return;
		}

		// 防止HTTP线程阻塞，启动异步通信
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					// 先将参数放入List，再对参数进行URL编码
					List<BasicNameValuePair> dataParams = new LinkedList<BasicNameValuePair>();
					for (Map.Entry<String, Object> map : data.entrySet()) {
						dataParams.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
					}
					StringBuffer sb = new StringBuffer();
					for (Map.Entry<String, Object> map : data.entrySet()) {
						sb.append(map.getKey()+"="+map.getValue()+"&");
					}
					// 对参数编码
					String param = URLEncodedUtils.format(dataParams, "UTF-8");
					LogUtils.i("---------发送给服务器的数据：" + url + "?" + sb.toString());
					// 将URL与参数分离
					HttpPost method = new HttpPost(url);
					HttpEntity entity = new UrlEncodedFormEntity(dataParams, HTTP.UTF_8);
					method.setEntity(entity);
					// 设置头文件
					method.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

					DefaultHttpClient httpClient = new DefaultHttpClient();

					// 请求超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 12000);
					// 读取超时
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 12000);
					HttpResponse response = httpClient.execute(method);
					String result = "";
					if (200 == response.getStatusLine().getStatusCode()) {
						result = EntityUtils.toString(response.getEntity(), "UTF-8");
					}
					LogUtils.i("---------服务器请求结果：" + response.getStatusLine().getStatusCode() + "--" + result);
					return result.toString();

				} catch (Exception e) {
					if (failCallback != null) {
						errorCode = 0;
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// 结果处理
				if (!StringUtils.isEmpty(result)) {
					if (successCallback != null) {
						successCallback.onSuccess(result);
					}
				} else {
					if (failCallback != null) {
						failCallback.onFail(callInfo(errorCode));
					}
				}
			}
		}.execute();
	}

	/**
	 * NETConnection构造方法
	 * 
	 * @param url   
	 * 
	 *            通信地址
	 * @param successCallback
	 *            成功的回调方法
	 * @param failCallback
	 *            失败的回调方法
	 * @param data
	 *            请求的数据集
	 */
	public NETConnection(Context context, final String url, final SuccessCallback successCallback, final FailCallback failCallback,
			final Map<String, Object> data,boolean isLongTime) {
		
		if (!NetUtils.isNetworkConnected(context)) {
			failCallback.onFail(callInfo(2));
			return;
		}
		
		// 防止HTTP线程阻塞，启动异步通信
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... params) {
				try {
					
					// 先将参数放入List，再对参数进行URL编码
					List<BasicNameValuePair> dataParams = new LinkedList<BasicNameValuePair>();
					for (Map.Entry<String, Object> map : data.entrySet()) {
						dataParams.add(new BasicNameValuePair(map.getKey(), map.getValue().toString()));
					}
					// 对参数编码
					String param = URLEncodedUtils.format(dataParams, "UTF-8");
					StringBuffer sb = new StringBuffer();
					for (Map.Entry<String, Object> map : data.entrySet()) {
						sb.append(map.getKey()+"="+map.getValue()+"&");
					}
					LogUtils.i("---------发送给服务器的数据：" + url + "?" + sb.toString());
					// 将URL与参数分离
					HttpPost method = new HttpPost(url);
					HttpEntity entity = new UrlEncodedFormEntity(dataParams, HTTP.UTF_8);
					method.setEntity(entity);
					// 设置头文件
					method.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					
					// 请求超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
					// 读取超时
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
					HttpResponse response = httpClient.execute(method);
					String result = "";
					if (200 == response.getStatusLine().getStatusCode()) {
						result = EntityUtils.toString(response.getEntity(), "UTF-8");
					}
					LogUtils.i("---------服务器请求结果--状态码：" + response.getStatusLine().getStatusCode() + "--内容：" + result);
					return result.toString();
					
				} catch (Exception e) {
					if (failCallback != null) {
						errorCode = 0;
						e.printStackTrace();
					}
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// 结果处理
				if (!StringUtils.isEmpty(result)) {
					if (successCallback != null) {
						successCallback.onSuccess(result);
					}
				} else {
					if (failCallback != null) {
						failCallback.onFail(callInfo(errorCode));
					}
				}
			}
		}.execute();
	}
}
