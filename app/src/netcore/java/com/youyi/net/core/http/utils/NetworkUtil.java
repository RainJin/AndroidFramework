package com.youyi.net.core.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 判断网络连接类型的工具类
 * Created by Rain on 2016/2/16.
 */
public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getName();

    /**
     * WIFI网络
     */
    public static final int NETTYPE_WIFI = 0x01;
    /**
     * CMWAP网络
     */
    public static final int NETTYPE_CMWAP = 0x02;
    /**
     * CMNET网络
     */
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 检测网络是否可用
     *
     * @return isConnected
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 判断wifi是否连接
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        return getNetworkType(context) == NETTYPE_WIFI;
    }

}
