package com.xuanyuan.library.help.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;


/**
 * 说明：
 * 作者：User_luo on 2018/7/16 11:23
 * 邮箱：424533553@qq.com
 */
public class NetBroadCastReciver extends BroadcastReceiver {

    /**
     * 只有当网络改变的时候才会 经过广播。
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        judgeBroadReciver(intent);

    }


    /**
     * @param intent 广播的Intent,判断类型 并进行处理
     */
    private void judgeBroadReciver(Intent intent) {
        //此处是主要代码，
        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //TODO 具体业务
        } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            //此处无实际作用，只是看开关是否开启  ， 判断wifi是打开还是关闭
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED: //wifi 可用
                    // WIFI 可用
                    break;
            }
        }

    }


}
