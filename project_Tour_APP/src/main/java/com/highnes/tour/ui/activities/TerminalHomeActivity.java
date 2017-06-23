package com.highnes.tour.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.highnes.tour.R;
import com.highnes.tour.adapter.UtilsAdapter;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.HomeFastInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.activities.home.MsgActivity;
import com.highnes.tour.ui.activities.home.NewsActivity;
import com.highnes.tour.ui.activities.home.SosActivity;
import com.highnes.tour.ui.activities.home.TimeListActivity;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.MapRoadActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketSearchActivity;
import com.highnes.tour.ui.activities.home.tour.TourActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.poster.PosterFragment;
import com.highnes.tour.utils.AMapHelper;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.SortComparator;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.utils.ToastHelper;
import com.highnes.tour.view.gridview.MyGridView;
import com.highnes.tour.view.layout.RippleView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullableScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/3.
 */

public class TerminalHomeActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    /** 广播回调监听_刷新 */
    public static final String ACTION_CALLBACK_HOME = "com.highnes.tour.action_callback_home";
//    // 标题
//    private LinearLayout llTitle;
//    private View vTitle;
    // 轮播图
    private PosterFragment mPoster;

    private PullableScrollView svRoot;
    private int maxH;
    private PullToRefreshLayout pull;

    // 中间的菜单
    private RippleView[] rvMenu = new RippleView[8];
    MyGridView myGridView;
    // 定位
    private TextView tvCity;
    // 当前的定位信息
    private BDLocation mBdLocation;

    private List<HomeAllInfo.AdvertisementInfo> mBanner;
    private UtilsAdapter utilsAdapter;
    //更新
    private TextView update;
    @Override
    public void onResume() {
        super.onResume();
        if (mPoster != null && mBanner!=null) {
            initBanner(mBanner);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != updateReceiver){
            unregisterReceiver();
        }
    }

    // 注销广播
    private void unregisterReceiver() {
        mContext.unregisterReceiver(updateReceiver);
    }

    /**
     * 注册一个广播接收器
     */
    private void registBoradcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CALLBACK_HOME);
        mContext.registerReceiver(updateReceiver, intentFilter);
    }

    /**
     * 自定义广播接收器，用于显示未读数
     */
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            performHandlePostDelayed(3, 100);
        }

    };

    @Override
    protected @LayoutRes
    int getLayoutId() {
        return R.layout.terminal_home;
    }

    @Override
    protected void findViewById() {
        update = getViewById(R.id.home_update);
        pull = getViewById(R.id.pull);
        svRoot = getViewById(R.id.sv_shops);
        rvMenu[0] = getViewById(R.id.rv_menu_0);
        rvMenu[1] = getViewById(R.id.rv_menu_1);
        rvMenu[2] = getViewById(R.id.rv_menu_2);
        rvMenu[3] = getViewById(R.id.rv_menu_3);
        rvMenu[4] = getViewById(R.id.rv_menu_4);
        rvMenu[5] = getViewById(R.id.rv_menu_5);
        rvMenu[6] = getViewById(R.id.rv_menu_6);
        rvMenu[7] = getViewById(R.id.rv_menu_7);
        myGridView = getViewById(R.id.find_util);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 滑动透明标题的高度
        maxH = DensityUtils.dip2px(mContext, 150);
        String city = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
        SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), city);
        initOptions();
        //设置是否可以上拉
        pull.setHasPullUp(false);
        svRoot.smoothScrollTo(0, 20);
        utilsAdapter = new UtilsAdapter(mContext);
        utilsAdapter.setList(getFastData());
        myGridView.setAdapter(utilsAdapter);
        utilsAdapter.setOnUtilsClickListener(new UtilsAdapter.OnUtilsClickListener() {
            @Override
            public void utilsClick(HomeFastInfo info, int position) {
                proLogicFast(position);
            }
        });
        registBoradcast();
    }

    /**
     * 便民服务的菜单数据
     *
     * @return
     */
    private List<HomeFastInfo> getFastData() {
        List<HomeFastInfo> list = new ArrayList<HomeFastInfo>();
        list.add(new HomeFastInfo("电子导游", "drawable://" + R.drawable.ic_home_menu8, ""));
        list.add(new HomeFastInfo("违章查询", "drawable://" + R.drawable.ic_home_menu9, ""));
        list.add(new HomeFastInfo("ETC查询", "drawable://" + R.drawable.ic_home_menu10, ""));
        list.add(new HomeFastInfo("路况查询", "drawable://" + R.drawable.ic_home_menu11, ""));
        list.add(new HomeFastInfo("天气查询", "drawable://" + R.drawable.ic_home_menu12, ""));
        list.add(new HomeFastInfo("资费查询", "drawable://" + R.drawable.ic_home_menu13, ""));
        list.add(new HomeFastInfo("航班动态", "drawable://" + R.drawable.ic_home_menu14, ""));
        list.add(new HomeFastInfo("道路救援", "drawable://" + R.drawable.ic_home_menu15, ""));
//        list.add(new HomeFastInfo("", "drawable://" + R.drawable.ic_home_menu16, ""));
        return list;
    }

    @Override
    protected void setListener() {
        pull.setOnRefreshListener(this);
        for (int i = 0; i < rvMenu.length; i++) {
            rvMenu[i].setOnClickListener(this);
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        performHandlePostDelayed(0, 200); // 首页接口
        performHandlePostDelayed(1, 800); // 定位
        performHandlePostDelayed(2, 1000); // 酒店
        performHandlePostDelayed(3, 1200); // 消息
    }

    @Override
    protected void performHandlePostDelayedCallBack(int type) {
        if (0 == type) {
            String jsonHome = SPUtils.get(mContext, DefaultData.JSON_HOME, "").toString();
            if (!StringUtils.isEmpty(jsonHome)) {
                parseJsonHomeAll(jsonHome);
            }
        } else if (1 == type) {
            super.performHandlePostDelayedCallBack(type);
            AMapHelper helper = new AMapHelper();
            helper.startLocation(mContext, new AMapHelper.MyOnReceiveLocation() {

                @Override
                public void onReceiveLocation(BDLocation location) {
                    mBdLocation = location;
                    if (mBdLocation != null) {
                        LogUtils.d("--定位成功！经度：" + mBdLocation.getLongitude() + "，纬度：" + mBdLocation.getLatitude() + ",城市：" + mBdLocation.getCity());
                        SPUtils.put(mContext, PreSettings.USER_LAT.getId(), mBdLocation.getLatitude());
                        SPUtils.put(mContext, PreSettings.USER_LNG.getId(), mBdLocation.getLongitude());
                        String city = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
                        if (!StringUtils.isEmpty(mBdLocation.getCity()) && !city.equals(mBdLocation.getCity())) {
                            // 当前城市不同
                            displayLocDialog(mBdLocation.getCity());
                        } else {
                            requestByHomeAll(true, city);
                        }
                    } else {
                        LogUtils.e("--定位失败！");
                        String cityName = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
                        requestByHomeAll(false, cityName);
                    }
                }
            });
        }
    }

//
//    /**
//     * 初始化tab
//     *
//     * @param channelList
//     *            菜单
//     */
//    @SuppressLint("InflateParams")
//    private void initTab(List<HomeFastInfo> channelList) {
//
//        for (int i = 1; i < channelList.size(); i++) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_fast_item, null);
//            RippleView rvFast = (RippleView) view.findViewById(R.id.rv_root);
//            TextView tvName = (TextView) view.findViewById(R.id.tv_item_name);
//            ImageView ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
//            rvFast.setTag(i);
//            tvName.setText(channelList.get(i).name);
//            Log.e("TAG", "initTab: "+channelList.get(i).imgUrl );
//            ImageLoader.getInstance().displayImage(channelList.get(i).imgUrl, ivImg, options, null);
//            rvFast.setOnClickListener(this);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.width = (int) DensityUtils.dp2pxF(mContext, 70);
//            lp.height = lp.width;
//            view.setLayoutParams(lp);
//        }
//    }

    /**
     * 显示位置不同的提示框
     */
    private void displayLocDialog(final String city) {
        showDialog("定位到您当前的位置是" + city + "\n是否立即切换？", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (2 == which) {
                    tvCity.setText(city);
                    SPUtils.put(mContext, PreSettings.USER_CITY.getId(), city);
                    requestByHomeAll(true, city);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //更新
            case R.id.home_update:

                break;
            // 门票
            case R.id.rv_menu_0:
                openActivity(TicketActivity.class);
                break;
            // 旅游度假
            case R.id.rv_menu_1:
                openActivity(TourActivity.class);
                break;
            // 城市选择
            case R.id.button_backward_txt19:
            case R.id.button_down_img19:
            case R.id.button_backward_txt:
            case R.id.button_down_img:
                Intent intent = new Intent(mContext, CityActivity.class);
                startActivityForResult(intent, DefaultData.REQUEST_CODE);
                break;
            // 点击头条
            case R.id.tv_news:
                openActivity(NewsActivity.class);
                break;
            // 便民菜单
            case R.id.rv_root:
                proLogicFast((int) (v).getTag());
                break;
            // 门票更多
            case R.id.tv_ticket_more: {
                Bundle bundle = new Bundle();
                bundle.putString("nearbyType", "");
                bundle.putString("nearbyValue", "");
                bundle.putString("themeType", "");
                bundle.putString("themeValue", "");
                openActivity(FilterTicketActivity.class, bundle);
            }
            break;
            // 酒店、酒店更多
            case R.id.rv_menu_2:
            case R.id.tv_hotel_more: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 10001);
                bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_HOTEL_MORE+ AppUtils.getTemp());
                openActivity(PHPWebViewActivity.class, bundle);
            }
            break;
            // 消息
            case R.id.button_down_img_right19:
            case R.id.button_down_img_right:
                if (AppUtils.isLogin(mContext)) {
                    openActivity(MsgActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            // 美食
            case R.id.rv_menu_3: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 10002);
                bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_MEISHI+AppUtils.getTemp());
                openActivity(PHPWebViewActivity.class, bundle);
            }
            break;
            // 土特产
            case R.id.rv_menu_6: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 10003);
                bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_TECHAN+AppUtils.getTemp());
                openActivity(PHPWebViewActivity.class, bundle);
            }
            break;
            // 娱乐
            case R.id.rv_menu_7: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 10004);
                bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_YULE+AppUtils.getTemp());
                openActivity(PHPWebViewActivity.class, bundle);
            }
            break;
            // 飞机票
            case R.id.rv_menu_4:
                LogUtils.d("--飞机票");
                showNoData();
                break;
            // 火车票
            case R.id.rv_menu_5:
                showNoData();
                break;
            // 限时抢购更多
            case R.id.tv_time_more:
                openActivity(TimeListActivity.class);
                break;
            // 搜索
            case R.id.ll_search19:
            case R.id.ll_search:
                openActivity(TicketSearchActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 处理便民菜单
     *
     * @param tag
     */
    private void proLogicFast(int tag) {

        switch (tag) {
            // 违章查询
            case 0: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 1004);
                bundle.putString("mTitle", "违章查询");
                bundle.putString("mUrl", UrlSettings.URL_H5_HOME_WZCX);
                openActivity(WebViewTitleActivity.class, bundle);
            }
            break;
            // ETC查询
            case 1: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 1005);
                bundle.putString("mTitle", "ETC查询");
                bundle.putString("mUrl", UrlSettings.URL_H5_HOME_ETC);
                openActivity(WebViewTitleActivity.class, bundle);
            }
            break;
            // 路况查询
            case 2:
                openActivity(MapRoadActivity.class);
                break;
            // 天气查询
            case 3: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 1007);
                bundle.putString("mTitle", "天气查询");
                bundle.putString("mUrl", UrlSettings.URL_H5_HOME_TQCX);
                openActivity(WebViewTitleActivity.class, bundle);
            }

            break;
            // 资费查询
            case 4: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 1008);
                bundle.putString("mTitle", "资费查询");
                bundle.putString("mUrl", UrlSettings.URL_H5_HOME_ZCCX);
                openActivity(WebViewTitleActivity.class, bundle);
            }
            break;
            // 航班动态
            case 5: {
                Bundle bundle = new Bundle();
                bundle.putInt("mType", 1009);
                bundle.putString("mTitle", "航班动态");
                bundle.putString("mUrl", UrlSettings.URL_H5_HOME_HBDT);
                openActivity(WebViewTitleActivity.class, bundle);
            }
            break;
            // 道路救援
            case 6:
                openActivity(SosActivity.class);
                break;
            case 7:
                ToastHelper.showToast(this,"暂未开放");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DefaultData.REQUEST_CODE) {
//            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                String cityName = data.getExtras().getString(DefaultData.EXTRA_RESULT, "重庆市");
                String currCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
                tvCity.setText(cityName);
                if (currCity.equals(cityName)) {
                    // 当前的城市
                    requestByHomeAll(true, cityName);
                } else {
                    SPUtils.put(mContext, PreSettings.USER_CITY.getId(), cityName);
                    SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
                    requestByHomeAll(true, cityName);
                }

            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        performHandlePostDelayed(1, 800); // 定位
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pull.refreshFinish(PullToRefreshLayout.SUCCEED);
        pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    // -----------------------网络请求-----------------------
    /**
     * HomeAll 网络请求
     *
     * @param isLoc
     *            是否能定位到数据
     * @param cityName
     *            查询的城市
     *
     */
    private void requestByHomeAll(boolean isLoc, String cityName) {

        Map<String, Object> paramDatas = new HashMap<String, Object>();
        String url = "";
        if (isLoc) {
            // 定位到数据
            url = UrlSettings.URL_HOME_ALL_CITY;
            paramDatas.put("City", cityName);
        } else {
            // 定位不到数据
            url = UrlSettings.URL_HOME_ALL;
        }

        showLoading();
        new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                stopLoading();
                pull.refreshFinish(PullToRefreshLayout.SUCCEED);
                pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                parseJsonHomeAll(result);
            }
        }, new NETConnection.FailCallback() {

            @Override
            public void onFail(String info) {
                stopLoading();
                pull.refreshFinish(PullToRefreshLayout.FAIL);
                pull.loadmoreFinish(PullToRefreshLayout.FAIL);
                showToast(info);
                initBanner(null);
            }
        }, paramDatas);
    }

    /**
     * HomeAll JSON信息
     *
     * @param result
     *            JSON
     */
    private void parseJsonHomeAll(String result) {
        try {
            if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
                final HomeAllInfo info = GsonUtils.json2T(result, HomeAllInfo.class);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 对轮播图进行排序。1/2/3
                        Collections.sort(info.Advertisement, new SortComparator.HomeTicketComparator());
                        mBanner = info.Advertisement;
                        initBanner(info.Advertisement);
                    }
                }, 200);
                SPUtils.put(mContext, DefaultData.JSON_HOME, result);
            } else {
                initBanner(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 显示Banner
     */
    private void initBanner(final List<HomeAllInfo.AdvertisementInfo> info) {
        try {
            mPoster = new PosterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.poster_container, mPoster).commit();
            if (info != null && info.size() > 0) {
                // 轮播图
                final List<String> list = new ArrayList<String>();
                for (int i = 0; i < info.size(); i++) {
                    list.add(UrlSettings.URL_IMAGE + info.get(i).PhotoUrl);
                }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mPoster.initPoster(list);
                        mPoster.setOnItemClick(new PosterFragment.OnItemClick() {

                            @Override
                            public void onItemClick(int position) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("mType", 1001);
                                bundle.putString("mTitle", "详情");
                                bundle.putString("mUrl", info.get(position).JumpUrl);
                                openActivity(WebViewTitleActivity.class, bundle);

                            }
                        });
                    }
                }, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // -----------------------网络请求-----------------------
}
