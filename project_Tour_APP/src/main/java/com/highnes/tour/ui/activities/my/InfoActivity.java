package com.highnes.tour.ui.activities.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.MyFragment;
import com.highnes.tour.utils.ClipImgUtils;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.FileUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.date.wheel.ChangeYersMonthDayDialog;
import com.highnes.tour.view.date.wheel.ChangeYersMonthDayDialog.OnBirthListener;
import com.highnes.tour.view.dialog.ECListDialog.OnDialogItemClickListener;
import com.highnes.tour.view.imageview.CircleImageView;
import com.highnes.tour.view.layout.RippleView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    个人中心页面..个人资料。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class InfoActivity extends BaseTitleActivity {
	// 分别是相机、图库、裁剪的回调
	private static int CAMERA_REQUEST_CODE = 1;
	private static int GALLERY_REQUEST_CODE = 2;
	private static int CROP_REQUEST_CODE = 3;
	// 点击相机拍照的URI
	private Uri mOutPutFileUri;

	/** 头像 */
	private CircleImageView ivHand;
	// 昵称/真实姓名/手机号码/生日/性别/邮箱
	private TextView tvNickName, tvName, tvPhone, tvBirthday, tvSex, tvEmail;
	// 是否退出
	private RippleView rvExit;
	// 选择的生日
	private String birthday;

	@Override
	protected void onResume() {
		super.onResume();
		showInfo();
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_info;
	}

	@Override
	protected void findViewById2T() {
		ivHand = getViewById(R.id.iv_avator);
		tvPhone = getViewById(R.id.tv_phone);
		tvName = getViewById(R.id.tv_name);
		tvNickName = getViewById(R.id.tv_nickname);
		tvBirthday = getViewById(R.id.tv_birthday);
		tvSex = getViewById(R.id.tv_sex);
		tvEmail = getViewById(R.id.tv_email);
		rvExit = getViewById(R.id.rv_exit);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("个人资料");
		showBackwardView("", SHOW_ICON_DEFAULT);
		String userType = SPUtils.get(mContext, PreSettings.USER_TYPE.getId(), PreSettings.USER_TYPE.getDefaultValue()).toString();
		// 设置是否显示退出
		rvExit.setVisibility(StringUtils.isEquals("0", userType) ? View.GONE : View.VISIBLE);
		initOptions(R.drawable.ic_avator_default);
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_avator).setOnClickListener(this);
		getViewById(R.id.ll_nickname).setOnClickListener(this);
		getViewById(R.id.ll_name).setOnClickListener(this);
		getViewById(R.id.ll_sex).setOnClickListener(this);
		getViewById(R.id.ll_birthday).setOnClickListener(this);
		getViewById(R.id.ll_email).setOnClickListener(this);
		getViewById(R.id.tv_exit).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	/**
	 * 初始化信息
	 */
	private void showInfo() {
		try {
			// 头像
			String avator = SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue()).toString();
			ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + avator, ivHand, options, null);
			// 昵称
			String nickname = SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString();
			tvNickName.setText(nickname);
			// 姓名
			String name = SPUtils.get(mContext, PreSettings.USER_NAME.getId(), PreSettings.USER_NAME.getDefaultValue()).toString();
			tvName.setText(name);
			// 手机
			String phone = SPUtils.get(mContext, PreSettings.USER_PHONE.getId(), PreSettings.USER_PHONE.getDefaultValue()).toString();
			tvPhone.setText(phone);
			// 性别
			String sex = SPUtils.get(mContext, PreSettings.USER_SEX.getId(), PreSettings.USER_SEX.getDefaultValue()).toString();
			tvSex.setText("1".equals(sex) ? "男" : "女");
			// 邮箱
			String email = SPUtils.get(mContext, PreSettings.USER_EMAIL.getId(), PreSettings.USER_EMAIL.getDefaultValue()).toString();
			if (StringUtils.isEmpty(email)) {
				tvEmail.setText("接受出团通知");
			} else {
				tvEmail.setText(email);
			}
			// 生日
			String birthday = SPUtils.get(mContext, PreSettings.USER_BIRTHDAY.getId(), PreSettings.USER_BIRTHDAY.getDefaultValue()).toString();
			if (StringUtils.isEmpty(birthday)) {
				tvBirthday.setText("收获生日惊喜");
			} else {
				tvBirthday.setText(DateUtils.formatDates(birthday, "yyyy-MM-dd"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 修改头像
		case R.id.ll_avator:
			changeHandPortrait();
			break;
		// 修改性别
		case R.id.ll_sex:
			changeSex();
			break;
		// 昵称
		case R.id.ll_nickname: {
			String value = SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString();
			Bundle bundle = new Bundle();
			bundle.putString("lab", "昵称：");
			bundle.putString("value", value);
			bundle.putInt("mType", 0);
			openActivity(ChangeValueActivity.class, bundle);
		}
			break;
		// 姓名
		case R.id.ll_name: {
			String value = SPUtils.get(mContext, PreSettings.USER_NAME.getId(), PreSettings.USER_NAME.getDefaultValue()).toString();
			Bundle bundle = new Bundle();
			bundle.putString("lab", "姓名：");
			bundle.putString("value", value);
			bundle.putInt("mType", 2);
			openActivity(ChangeValueActivity.class, bundle);
		}
			break;
		// 邮箱
		case R.id.ll_email: {
			String value = SPUtils.get(mContext, PreSettings.USER_EMAIL.getId(), PreSettings.USER_EMAIL.getDefaultValue()).toString();
			Bundle bundle = new Bundle();
			bundle.putString("lab", "邮箱：");
			bundle.putString("value", value);
			bundle.putInt("mType", 3);
			openActivity(ChangeValueActivity.class, bundle);
		}
			break;
		// 生日
		case R.id.ll_birthday:
			selectTime();
			break;

		// 注销
		case R.id.tv_exit:
			displayExitDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 生日
	 * 
	 */
	private void selectTime() {
		ChangeYersMonthDayDialog mChangeBirthDialog = new ChangeYersMonthDayDialog(mContext);
		mChangeBirthDialog.setMinAge(10);// 设置最小闲置10岁
		mChangeBirthDialog.setDate(1990, 01, 01);
		mChangeBirthDialog.show();
		mChangeBirthDialog.setBirthdayListener(new OnBirthListener() {

			@Override
			public void onClick(String year, String month, String day) {
				birthday = year + "-" + month + "-" + day;
				requestByUpdateBirth();
			}
		});
	}

	/**
	 * 修改头像
	 */
	private void changeHandPortrait() {
		showDialogList(new OnDialogItemClickListener() {

			@Override
			public void onDialogItemClick(Dialog d, int position) {
				if (0 == position) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, GALLERY_REQUEST_CODE);
				} else {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(FileUtils.getCacheImageFiles(mContext), System.currentTimeMillis() + ".jpg");
					mOutPutFileUri = Uri.fromFile(file);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			}
		}, "修改头像", R.array.change_avatar);
	}

	/**
	 * 修改性别
	 */
	private void changeSex() {
		showDialogList(new OnDialogItemClickListener() {

			@Override
			public void onDialogItemClick(Dialog d, int position) {
				if (0 == position) {
					// 男
					requestByUpdateSex("1");
				} else {
					requestByUpdateSex("0");
				}
			}
		}, "选择性别", R.array.sex);
	}

	/**
	 * 保存照片到本地
	 * 
	 * @param bm
	 * @return 照片的uri
	 */
	private Uri saveBitmap(Bitmap bm) {
		// 创建图片
		File img = new File(FileUtils.getCacheImageFiles(mContext), System.currentTimeMillis() + ".png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 60, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置裁剪
	 * 
	 * @param uri
	 */
	private void startImageZoom(Uri uri) {
		long startT = System.currentTimeMillis();
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = ClipImgUtils.getPath(mContext, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}

		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
		LogUtils.i("times==>>" + (System.currentTimeMillis() - startT));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_REQUEST_CODE) {// 相机
			if (data == null) {
				if (mOutPutFileUri == null) {
					return;
				} else {
					startImageZoom(mOutPutFileUri);
				}
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bm = extras.getParcelable("data");
					Uri uri = saveBitmap(bm);
					startImageZoom(uri);
				}
			}
		} else if (requestCode == GALLERY_REQUEST_CODE) {// 图库
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			startImageZoom(uri);
		} else if (requestCode == CROP_REQUEST_CODE) {// 裁剪
			try {
				if (data == null) {
					return;
				}
				Bundle extras = data.getExtras();
				if (extras == null) {
					return;
				}
				Bitmap bm = extras.getParcelable("data");
				ivHand.setImageBitmap(bm);
				// 把当前选择的头像保存到运行缓存中
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
				byte[] bytes = stream.toByteArray();
				String handStr = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
				requestByUpdateAvator(handStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 注销
	 */
	private void displayExitDialog() {
		try {
			showDialog("是否注销当前账户？", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						try {
							String phone = SPUtils.get(mContext, PreSettings.USER_PHONE.getId(), PreSettings.USER_PHONE.getDefaultValue()).toString();
							Boolean isPage = (Boolean) SPUtils.get(mContext, PreSettings.APP_FIRST_IN.getId(), PreSettings.APP_FIRST_IN.getDefaultValue());
							SPUtils.clear(mContext);
							SPUtils.put(mContext, PreSettings.USER_PHONE.getId(), phone);
							SPUtils.put(mContext, PreSettings.APP_FIRST_IN.getId(), isPage);
							SPUtils.put(mContext, PreSettings.USER_TYPE.getId(), 0);
							openActivity(LoginActivity.class);
							Intent mIntent = new Intent();
							MyFragment.isLoad = true;
							mIntent.setAction(MyFragment.ACTION_CALLBACK_MY);
							sendBroadcast(mIntent);
							finishActivity();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateData 网络请求
	 * 
	 */
	private void requestByUpdateSex(final String value) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("UserName", SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString());
		paramDatas.put("Sex", "1".equals(value) ? "男" : "女");
		paramDatas.put("Email", SPUtils.get(mContext, PreSettings.USER_EMAIL.getId(), PreSettings.USER_EMAIL.getDefaultValue()).toString());
		paramDatas.put("Name", SPUtils.get(mContext, PreSettings.USER_NAME.getId(), PreSettings.USER_NAME.getDefaultValue()).toString());
		paramDatas.put("Birth", SPUtils.get(mContext, PreSettings.USER_BIRTHDAY.getId(), PreSettings.USER_BIRTHDAY.getDefaultValue()).toString());
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_UPDATEUSER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpdateData(result, value);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Specialty JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUpdateData(String result, String value) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				SPUtils.put(mContext, PreSettings.USER_SEX.getId(), Integer.valueOf(value));
				showInfo();
				showToast("修改成功");
			} else {
				showToast("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateData 网络请求
	 * 
	 */
	private void requestByUpdateBirth() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("UserName", SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString());
		String sex = SPUtils.get(mContext, PreSettings.USER_SEX.getId(), PreSettings.USER_SEX.getDefaultValue()).toString();
		paramDatas.put("Sex", "1".equals(sex) ? "男" : "女");
		paramDatas.put("Email", SPUtils.get(mContext, PreSettings.USER_EMAIL.getId(), PreSettings.USER_EMAIL.getDefaultValue()).toString());
		paramDatas.put("Name", SPUtils.get(mContext, PreSettings.USER_NAME.getId(), PreSettings.USER_NAME.getDefaultValue()).toString());
		paramDatas.put("Birth", birthday);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_UPDATEUSER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpdateBirth(result, birthday);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Specialty JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUpdateBirth(String result, String value) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				SPUtils.put(mContext, PreSettings.USER_BIRTHDAY.getId(), value+"T00:00:00");
				showInfo();
				showToast("修改成功");
			} else {
				showToast("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * UpdateData 网络请求
	 * 
	 */
	private void requestByUpdateAvator(final String value) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("fileup", value);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_UPDATEAVATOR, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpdateAvator(result, value);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Specialty JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUpdateAvator(String result, String value) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				JSONArray arr = JsonUtils.getJSONArray(result, "data", null);
				String Photo = JsonUtils.getString(arr.getJSONObject(0), "Photo", "");
				SPUtils.put(mContext, PreSettings.USER_AVATOR.getId(), Photo);
				showInfo();
				Intent mIntent = new Intent();
				MyFragment.isLoad = true;
				mIntent.setAction(MyFragment.ACTION_CALLBACK_MY);
				sendBroadcast(mIntent);
				showToast("修改成功");
			} else {
				showToast("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
