package com.xuanyuan.library.help;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Activity 控制管理器
 */
public class ActivityController {

    private static final List<Activity> activities = new ArrayList<>();
    private static boolean mIsFinishAll = false;

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     *
     * @return  获取顶部的 ActivityName
     */
    public static String getTopActivityName(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (am != null) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            if (cn != null) {
                return cn.getClassName();
            }
        }
        return null;
    }

    public static void removeActivity(Activity activity) {
        if (!mIsFinishAll) {
            activities.remove(activity);
        }
    }

    public static void finishAll() {

        for (Activity activity : activities) {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        System.exit(0);
    }

    /**
     * 安全结束Activity的方法
     * @param whenTheArrayListFinish 借口回调,防止未完成遍历的情况就删除或者增加集合操作
     */
    public static void finishAllSafe(WhenTheArrayListFinish whenTheArrayListFinish) {
        mIsFinishAll = true;
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (!next.isFinishing()) {
                next.finish();
                iterator.remove();
            }
        }
        whenTheArrayListFinish.readComplete();
        mIsFinishAll = false;
    }

    public interface WhenTheArrayListFinish {
        void readComplete();
    }

    public static void finishActivityOutOfMainActivity() {
        for (Activity activity : activities) {
            String c = activity.getComponentName().getClassName();
            if (!activity.isFinishing() && !activity.getComponentName().getClassName().equals("com.fuexpress.kr.MyBuglyActivity")) {
                activity.finish();
            }
        }
    }

    private static final List<Activity> cardActiviies = new ArrayList<>();

    public static void cardAddActivity(Activity activity) {
        cardActiviies.add(activity);
    }

    public static void cardFinish() {
        for (Activity activity : cardActiviies) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
