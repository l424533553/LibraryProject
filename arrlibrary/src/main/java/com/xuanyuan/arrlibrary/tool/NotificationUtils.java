package com.xuanyuan.arrlibrary.tool;//package com.luofx.utils.apk;//package com.coolshow.mybmobtest.luofx.utils.apk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xuanyuan.arrlibrary.R;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 下载新版本的服务类
 */
public class NotificationUtils {

    private NotificationManager nm;
    private Notification notification;
    //标题标识，
    private int titleId = 0;

    /**
     * 初始化通知栏
     */
    public void initNotification(Context context, int titleId) {
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.mipmap.icon_clear;
        notification.tickerText = "开始下载";
        notification.when = System.currentTimeMillis();
        notification.contentView = new RemoteViews(getClass().getName(), R.layout.notify_download);
        this.titleId = titleId;
        nm.notify(titleId, notification);
    }

    /**
     * 取消对应 titleId 的弹出框
     */
    public void cancel() {
        if (notification != null) {
            nm.cancel(titleId);
        }
    }

    /**
     * 安装对应的文件，
     */
    public void installApkFile(Context context, File updateFile) {
//        Intent intent = my.getInstallIntent(context, updateFile);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        notification.flags = Notification.FLAG_AUTO_CANCEL;//点击通知栏之后 消失
//        notification.contentIntent = pendingIntent;//启动指定意图
    }

}
