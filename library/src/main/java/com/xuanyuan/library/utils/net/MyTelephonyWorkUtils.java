package com.xuanyuan.library.utils.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * 作者：罗发新
 * 时间：2019/4/30 0030    星期二
 * 邮件：424533553@qq.com
 * describe:  @authorcj判断手机状态 的工具类
 */
public class MyTelephonyWorkUtils {
    /**
     * get network type by {@link TelephonyManager}
     * <p>
     * such as 2G, 3G, 4G, etc.
     *
     * @return {@link TelephonyManager#NETWORK_TYPE_CDMA}, {@link TelephonyManager#NETWORK_TYPE_GPRS},
     * {@link TelephonyManager#NETWORK_TYPE_LTE}...
     */
    public int getTelNetworkTypeINT(Context context) {
        return getTelephonyManager(context).getNetworkType();
    }

    /**
     * 获取TelephonyManager
     */
    public TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 打开手机的移动数据开关
     * 需要使用系统权限:android:sharedUserId=”android.uid.system”
     * <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />
     */
    public void isMobileEnabled(Context context, boolean enabled) {
        TelephonyManager telephonyService = getTelephonyManager(context);
        try {
            Method setDataEnabled = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setDataEnabled.invoke(telephonyService, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用映射机制打开 设备移动网络
     * 设备是否打开移动网络开关
     *
     * @return boolean 打开移动网络返回true，反之false
     */
    @SuppressLint("PrivateApi")
    public boolean isMobileEnabled(Context context) {
        try {
            Method method = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
//            Method setDataEnabled = ConnectivityManager.class.getDeclaredMethod("setDataEnabled", int.class, boolean.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(MyNetWorkUtils.getConnectivityManager(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 反射失败，默认开启
        return true;
    }

}
