package com.xuanyuan.library.utils.system;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

public class SystemUtils {

    private static final String TAG = "SystemUtils";//升级的 生命周期

    /**
     * 随机生成一个GUID
     *
     * @return
     */
    public static String getGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取android中的DeviceId
     *
     * @param mContext 上下文
     */
    public static String getDeviceId(Context mContext) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取android中的SN号
     *
     * @param mContext
     * @return
     */
    public static String getSN(Context mContext) {
        return android.provider.Settings.System.getString(mContext.getContentResolver(), "android_id");
    }


    /**
     * 获取软件的版本信息
     */
    private PackageInfo initVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }


    /**
     * 严格审查模式  ,限制策略，可进行性能优化
     */
    public void setStrictMode() {
        //线程策略（ThreadPolicy）
        StrictMode.ThreadPolicy.Builder policyBuilder = new StrictMode.ThreadPolicy.Builder();
        policyBuilder.detectDiskReads()//检测在UI线程读磁盘操作
                .detectDiskWrites()//检测UI线程写磁盘操作
                .detectCustomSlowCalls()//发现UI线程调用的哪些方法执行得比较慢
                .detectNetwork() //检测在UI线程执行网络操作
                .penaltyDialog()//一旦检测到弹出Dialog
                .penaltyDeath()//一旦检测到应用就会崩溃
                .penaltyFlashScreen()//一旦检测到应用将闪屏退出 有的设备不支持
                .penaltyDeathOnNetwork()//一旦检测到应用就会崩溃
                .penaltyDropBox()//一旦检测到将信息存到DropBox文件夹中 data/system/dropbox
                .penaltyLog()//一旦检测到将信息以LogCat的形式打印出来
                .permitDiskReads()//允许UI线程在磁盘上读操作
                .permitAll();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            policyBuilder.detectResourceMismatches();//最低版本为API23  发现资源不匹配
        }
        StrictMode.setThreadPolicy(policyBuilder.build());


        //***********************************************************************/
        //  创建虚拟机策略（VmPolicy）
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        builder.detectActivityLeaks()//最低版本API11 用户检查 Activity 的内存泄露情况
                .detectLeakedClosableObjects()//最低版本API11  资源没有正确关闭时触发
                .detectLeakedRegistrationObjects();//最低版本API16  BroadcastReceiver、ServiceConnection是否被释放

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.detectCleartextNetwork();//最低版本为API23  检测明文的网络
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();//最低版本为API18   检测file://或者是content://
        }
        builder.detectLeakedSqlLiteObjects()//最低版本API9   资源没有正确关闭时回触发
                // .setClassInstanceLimit(MyClass.class, 2)//设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露
                .penaltyLog()//与上面的一致
                .penaltyDeath()
                .detectAll();
        StrictMode.setVmPolicy(builder.build());
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused: " + activity.getLocalClassName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped: " + activity.getLocalClassName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
        }
    };
    private ComponentCallbacks2 componentCallbacks2 = new ComponentCallbacks2() {
        /*
               当开发者的app正在运行:
               Trim_memory_running_moderate：设备开始运行缓慢，当前app正在运行，不会被kill
               Trim_memory_running_low：设备运行更缓慢了，当前app正在运行，不会被kill。但是请回收unused资源，以便提升系统的性能。
               Trim_memory_running_critical：设备运行特别慢，当前app还不会被杀死，但是如果此app没有释放资源，系统将会kill后台进程

               当开发者的app's visibility 改变:
               Trim_memory_ui_hidden：当前app UI不再可见，这是一个回收大个资源的好时机

               当开发者的应用进程被置于background LRU list:
               trim_memory_background：系统运行慢，并且进程位于LRU list的上端。尽管app不处于高风险被kill。当前app应该释放那些容易恢复的资源
               trim_memory_moderate：系统运行缓慢，当前进程已经位于LRU list的中部，如果系统进一步变慢，便会有被kill的可能
               trim_memory_complete：系统运行慢，当前进程是第一批将被系统kill的进程。此app应该释放一切可以释放的资源。低于api 14的，用户可以使用onLowMemory回调。
               */
        @Override
        public void onTrimMemory(int level) {//参数等级 参考如下
//                ComponentCallbacks2.
            Log.d(TAG, "ComponentCallbacks==   onTrimMemory, level=" + level);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            Log.d(TAG, "ComponentCallbacks==   onConfigurationChanged, Configuration=");
        }

        @Override
        public void onLowMemory() {
            Log.d(TAG, "ComponentCallbacks==   onLowMemory");
        }
    };

    /**
     * 初始化 监听回调   主要为Activity生命周期回调 和  ComponentCallbacks2回调
     */
    public void registerLifeCallBack(Application application) {
//        unregisterActivityLifecycleCallbacks()  对应的 生命周期监听
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
//        unregisterComponentCallbacks();// ComponentCallbacks2 取消注册
        application.registerComponentCallbacks(componentCallbacks2);

    }

    public void unregisterLifeCallBack(Application application) {
//        unregisterActivityLifecycleCallbacks()  对应的 生命周期监听
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        application.registerComponentCallbacks(componentCallbacks2);
    }


}
