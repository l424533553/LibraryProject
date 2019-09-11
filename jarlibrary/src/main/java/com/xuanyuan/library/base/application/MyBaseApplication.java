package com.xuanyuan.library.base.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.bugly.beta.Beta;
import com.xuanyuan.library.help.BuglyUPHelper;
import com.xuanyuan.library.config.IConfig;
import com.xuanyuan.library.help.CrashHandler;
import com.xuanyuan.library.utils.log.LogWriteUtils;
import com.xuanyuan.library.utils.system.SystemUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 * Bugly框架 包  ，必须继承MultiDexApplication  ，以为方法数超过了限制
 * <p>
 * 1.Android3.1版本后，每个App都必须要有至少有一个Activity，并且需要配置intent-filter，
 * 配置中的内容可以修改，比如设置 后看不见软件的图标
 * <intent-filter>
 * <action android:name="android.intent.action.MAIN" />
 * <category android:name="android.intent.category.LAUNCHER" />
 * </intent-filter>
 */
@SuppressLint("Registered")
public abstract class MyBaseApplication extends MultiDexApplication implements IConfig {
    protected Context context;
    protected boolean DEBUG_MODE = false;
    //  线程池  记得要关闭
    protected ExecutorService threadPool;
    protected ExecutorService singleThread;
    private static final String TAG = "MyBaseApplication";//升级的 生命周期

    public ExecutorService getSingleThread() {
        return singleThread;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public Context getContext() {
        return context;
    }

    protected abstract boolean isOpenCrashHandler();

    // 设置DebugMode 模式 DEBUG_MODE为true或false
    protected abstract boolean isDebugMode();

    //是否开启Buglyl
    protected abstract boolean isOpenBugly();

    SystemUtils systemUtils = new SystemUtils();

    /**
     * 
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        isDebugMode();
        threadPool = Executors.newFixedThreadPool(5);
        singleThread = Executors.newSingleThreadExecutor();
        initLiveEventBus();

        //debug 打印log ,不是debug模式 写入 log.log文件中
        LogWriteUtils.initLog(isDebugMode());

        if (isOpenCrashHandler()) {
            // 异常处理，不需要处理时注释掉这两句即可！
            // 该种方法自己定义异常捕获方法，并将异常数据上报给后台
            CrashHandler crashHandler = CrashHandler.getInstance();
            // 注册crashHandler
            crashHandler.init(getApplicationContext());
        }

        if (DEBUG_MODE) {//开启策略审查模式
            systemUtils.setStrictMode();
            //监听 Activity 和 Application  ,一般上线的产品不用,注意一定要解注册
            systemUtils.registerLifeCallBack(this);
        }

        if (isOpenBugly()) {//开启Bugly  ,设置是否是测试设备
            BuglyUPHelper buglyUPHelper = new BuglyUPHelper(this, false);
            buglyUPHelper.startBugly();
        }


    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 安装tinker， 不同的方式选择不一样
        if (isOpenBugly()) {
            Beta.installTinker();
        }
//      Beta.installTinker(this);
    }


    /**
     * VM   初始化  LiveEventBus
     */
    private void initLiveEventBus() {
        LiveEventBus.get().config().supportBroadcast(this).lifecycleObserverAlwaysActive(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //关闭线程池
        // 程序终止的时候执行, 这个方法只用于Android仿真机测试的时候，真机中不会出现
        Log.d(TAG, "onTerminate");
        threadPool.shutdown();
        singleThread.shutdown();
    }


    @Override
    public void onLowMemory() {
        // 低内存的时候执行
//        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 程序在内存清理的时候执行
/*        Log.d(TAG, "onTrimMemory");
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                //TODO  应用进入隐藏模式
                break;
        }*/


    }


}
