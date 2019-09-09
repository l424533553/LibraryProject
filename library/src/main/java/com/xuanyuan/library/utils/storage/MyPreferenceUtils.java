package com.xuanyuan.library.utils.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * 说明：SharedPreferences工具类
 * 作者：User_luo on 2018/6/13 09:33
 * 邮箱：424533553@qq.com
 * <p>
 * 偏好设置 工具类  ,建议只保存简单的基本类型数值，不必保存复杂的 对象文件
 * 私有模式
 * Context.MODE_PRIVATE 的值是 0;
 * ①只能被创建这个文件的当前应用访问
 * ②若文件不存在会创建文件；若创建的文件已存在则会覆盖掉原来的文件
 * <p>
 * 追加模式
 * Context.MODE_APPEND 的值是 32768;
 * ①只能被创建这个文件的当前应用访问
 * ②若文件不存在会创建文件；若文件存在则在文件的末尾进行追加内容
 * <p>
 * 可读模式
 * Context.MODE_WORLD_READABLE的值是1;
 * ①创建出来的文件可以被其他应用所读取
 * <p>
 * 可写模式
 * Context.MODE_WORLD_WRITEABLE的值是2
 * ①允许其他应用对其进行写入。
 */
public class MyPreferenceUtils {
    private static SharedPreferences mSp;
    private final static String SP_NAME = "config";

    /**
     * 获得sharePreference内存对象
     * <p>
     * Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
     * Context.MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件。
     * Context.MODE_WORLD_READABLE 和  Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
     * MODE_WORLD_READABLE：表示当前文件可以被其他应用读取。
     * MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
     * MODE_ENABLE_WRITE_AHEAD_LOGGING   数据库打开默认启用了写前日志记录
     */
    public static SharedPreferences getSp(Context context) {
        if (mSp == null) {
            mSp = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    /**
     * 获取boolean类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }

    /**
     * 获取boolean类型的值,如果没有对应的值，默认值返回false
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }


    /**
     * 设置int类型的值
     */
    public static void setInt(Context context, String key, int value) {
        Editor editor = getSp(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 设置boolean类型的值
     */
    public static void setBoolean(Context context, String key, boolean value) {
        Editor editor = getSp(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取String类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static String getString(Context context, String key, String defValue) {
        return getSp(context).getString(key, defValue);
    }

    /**
     * 获取int类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    /**
     * 获取String类型的值,如果没有对应的值，默认值返回null
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 设置String类型的值
     */
    public static void setString(Context context, String key, String value) {
        Editor editor = getSp(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取long类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }

    /**
     * 获取long类型的值,如果没有对应的值，默认值返回0
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static Long getLong(Context context, String key) {
        return getLong(context, key, 0);
    }

    /**
     * 设置Long类型的值
     */
    public static void setLong(Context context, String key, long value) {
        Editor editor = getSp(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 根据key值删除指定的数据
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        Editor editor = getSp(context).edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有数据
     *
     * @return true 成功
     */
    public boolean clear(Context context) {
        return getSp(context).edit().clear().commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context 上下文
     * @param key     key
     * @return 是否包含值
     */
    public static boolean contains(Context context, String key) {
        return getSp(context).contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context 获得所有的 内容键值对
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return getSp(context).getAll();
    }
}
