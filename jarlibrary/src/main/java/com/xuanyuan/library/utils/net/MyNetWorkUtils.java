package com.xuanyuan.library.utils.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import android.net.NetworkRequest;
import android.os.Build;
import com.xuanyuan.library.utils.log.LogWriteUtils;

/**
 * 作者：罗发新
 * 时间：2019/4/30 0030    星期二
 * 邮件：424533553@qq.com
 * describe:  @authorcj判断网络工具类
 */
public class MyNetWorkUtils {
    private static final String TAG = MyNetWorkUtils.class.getSimpleName();
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 网络可用的
     */
    public static final int NETWORK_AVAILABLE = 0;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 1;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 2;

    /**
     * 判断 网络是否 可用
     * <p>
     * 判断是否有可用状态的Wifi，以下情况返回false：
     * 1. 设备wifi开关关掉;
     * 2. 已经打开飞行模式；
     * 3. 设备所在区域没有信号覆盖；
     * 4. 设备在漫游区域，且关闭了网络漫游。
     */
    public static boolean isNetworkAvailable(Context context) {
        int NETWORK_TYPE = 0;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager == null) {
            NETWORK_TYPE = NETWORK_NONE;
        }
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo == null) {
            NETWORK_TYPE = NETWORK_NONE;
        } else {
            if (networkInfo.length == 0) {
                NETWORK_TYPE = NETWORK_NONE;
            }
        }

        for (NetworkInfo networkInfo1 : networkInfo) {
            // 判断当前网络状态是否为连接状态
            if (networkInfo1.getState() == NetworkInfo.State.CONNECTED) {
                NETWORK_TYPE = NETWORK_AVAILABLE;
            }
        }
        if (NETWORK_TYPE < NETWORK_AVAILABLE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取网络类型  和方法 isNetworkAvailable类似
     * <p>
     * 手机移动信号如下：
     * GPRS    2G(2.5) General Packet Radia Service 114kbps
     * EDGE    2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
     * UMTS    3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
     * CDMA    2G 电信 Code Division Multiple Access 码分多址
     * EVDO_0  3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
     * EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
     * 1xRTT   2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
     * HSDPA   3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
     * HSUPA   3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
     * HSPA    3G (分HSDPA,HSUPA) High Speed Packet Access
     * IDEN    2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
     * EVDO_B  3G EV-DO Rev.B 14.7Mbps 下行 3.5G
     * LTE     4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
     * EHRPD   3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
     * HSPAP   3G HSPAP 比 HSDPA 快些
     * 备注:在网络状态变化时这个获取的貌似不是很准确,有空调试下...
     */
    public static int getNetWorkState(Context context) {
        //得到连接管理器对象
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager == null) {
            return NETWORK_NONE;
        } else {
            //such as WIFI, MOBILE, ETHERNET, BLUETOOTH, etc.  获得活跃的网络信息
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                    return NETWORK_WIFI;
                } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                    // currentAPN.equals("cmwap")、uniwap、3gwap
                    String currentAPN = activeNetworkInfo.getExtraInfo();
                    return NETWORK_MOBILE;
                } else {
                    return NETWORK_AVAILABLE;
                }
            }
            return NETWORK_NONE;
        }
    }

    //TODO  验证打开 设置界面
//    ConnectivityManager#registerDefaultNetworkCallback
//    ConnectivityManager#registerNetworkCallback

    /**
     * 打开网络设置界面
     * <p>3.0以下打开设置界面</p>
     *
     * @param context 上下文
     */
    public static void openWirelessSettings(Context context) {
        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }

    // 你对我的

    /**
     * 获取ConnectivityManager
     */
    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
     * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。
     * 以下情况它是不可用的，将返回false：
     * 1. 设备打开飞行模式；
     * 2. 设备所在区域没有信号覆盖；
     * 3. 设备在漫游区域，且关闭了网络漫游。
     *
     * @return boolean
     */
    public static boolean isMobile(Context context) {
        NetworkInfo[] nets = getConnectivityManager(context).getAllNetworkInfo();
        if (nets != null) {
            for (NetworkInfo net : nets) {
                if (net.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return net.isAvailable();
                }
            }
        }
        return false;
    }

    /**
     * 打印当前各种网络状态
     *
     * @return boolean
     */
    public boolean printNetworkInfo(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            // 获取有效的网络状态
//            NetworkInfo in = connectivity.getActiveNetworkInfo();
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    LogWriteUtils.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable());
                    LogWriteUtils.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected());
                    LogWriteUtils.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting());
                    LogWriteUtils.i(TAG, "NetworkInfo[" + i + "]: " + info[i]);
                }
                LogWriteUtils.i(TAG, "\n");
            } else {
                LogWriteUtils.i(TAG, "getAllNetworkInfo is null");
            }
        }
        return false;
    }


    /**
     * 网络监听
     * sdk N=24及其以后，不在支持静态广播
     * 1.要么动态注册广播
     */
    private void listenerNet(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    ///网络不可用的情况下的方法
                }

                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    ///网络可用的情况下的方法

//                    //观察者注册,订阅消息  ，不需要主动取消订阅
//                    LiveEventBus.get()
//                            .with(KEY_TEST_OBSERVE, String.class)
//                            .observe(this, new Observer<String>() {
//                                @Override
//                                public void onChanged(@Nullable String s) {
//                                    Toast.makeText(EventBusActivity.this, s, Toast.LENGTH_SHORT).show();
//                                    textView.setText(s);
//                                }
//                            });
                }
            });
        }

    }
}
