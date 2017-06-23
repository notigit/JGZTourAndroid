package com.highnes.tour.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by noti on 2017/5/25.
 */

public class JumpMapUtils  {

    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";//百度包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";//高德包名
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pkName = packageInfos.get(i).packageName;
                if (pkName.equals(packageName)) return true;
            }
        }
        return false;
    }

    /**
     * 判断是否安装了某应用
     * @param packageName
     * @return
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 打开百度地图
     * 协议规范 intent://product/[service/]action[?parameters]#Intent;scheme=bdapp;package=package;end
     //parameters功能参数定义，具体规范见功能协议说明
     * 文档地址：http://developer.baidu.com/map/uri-introandroid.htm
     * 现在使用的是高德地图是经纬度数据 火星坐标：gcj02
     * 百度地图的经纬度类型：默认为bd09经纬度坐标。
     * 允许的值为bd09ll、bd09mc、gcj02、wgs84。bd09ll表示百度经纬度坐标，
     * gcj02表示经过国测局加密的坐标，
     * wgs84表示gps获取的坐标。
     */
    public static void openBaiduMap(Context activity, String lat, String log){

            if(isInstallByread(BAIDUMAP_PACKAGENAME)){
                Intent intent = new Intent();
//                intent.setData(Uri.parse("baidumap://map/direction?origin="+origin+"&"
//                        + "destination="+destination+"&mode=driving&region="+region));
                intent.setData(Uri.parse("baidumap://map/show?center="+lat+","+log));
                activity.startActivity(intent); //启动调用
            }else{
                Toast.makeText(activity, "请下载安装百度地图客户端", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * 打开高德地图
     * @param activity
     * @param sname 目的地名称
     * @param lat 纬度
     * @param log 经度
     */
    public static void openGaoDeMap(Context activity, String sname, String lat, String log)
    {
            if(isInstallByread(AMAP_PACKAGENAME)) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("androidamap://route?sourceApplication=金龟子旅行"
                        + "&slat="+lat+"&slon="+log+"&sname="+sname+"&dlat="
                        + "&dlon=&dname=&dev=0&m=1&t=0&showType=1"));
                activity.startActivity(intent);
            }else {
                Toast.makeText(activity, "请下载安装高德地图客户端", Toast.LENGTH_SHORT).show();
            }
    }
}
