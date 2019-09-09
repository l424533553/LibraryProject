package com.xuanyuan.library.utils.log;

import android.os.Environment;
import android.util.Log;

import com.xuanyuan.library.utils.date.MyDateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2018/2/17.
 * Log日志写入  工具
 * 初始化，须在使用之前设置，最好在Application创建时调用  init()
 */
public class LogWriteUtils {

    /**
     * 写文件的锁对象 ，防止同时写入文档  引起线程不安全，引发数据混乱
     */
    private static final Object mLogLock = new Object();

    private static boolean isDebugMode = false;   // 默认false为 非开发模式， 将内容写入log文件中。  true: 开发模式，将以log的形式显示出来
    private static String SDK_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath(); // 外部根路径 /storage/emulated/0
    private static String STORAGE = Environment.getDataDirectory().getAbsolutePath(); //  内部路径 /data
    private static final char VERBOSE = 'v';
    private static final char INFO = 'i';
    private static final char DEBUG = 'd';
    private static final char WARN = 'w';
    private static final char ERROR = 'e';

    private static String logFilePath;

    /**
     * 获得文件储存路径,在后面加"/Logs"建立子文件夹
     */
    public static boolean initLog(boolean isDebugMode) {
        LogWriteUtils.isDebugMode = isDebugMode;
        logFilePath = getFileRootPath();
        File fileLog = new File(logFilePath);
        if (!fileLog.exists()) {
            return fileLog.mkdirs();
        }
        return true;
    }

    /**
     * VM  判断sdcard是否可用  ，SDK 已经挂起了
     */
    private static boolean isMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * VM 获得文件存储  根路径
     */
    private static String getFileRootPath() {
        if (isMounted()) {
            return SDK_STORAGE + "logs" + "/";
        } else {
            return STORAGE + "logs" + "/";
        }
    }

    public static void v(String tag, String msg) {
        if (isDebugMode) {
            Log.v(tag, msg);
        } else {
            synchronized (mLogLock) {
                writeToFile(VERBOSE, tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (isDebugMode) {
            Log.d(tag, msg);
        } else {
            synchronized (mLogLock) {
                writeToFile(DEBUG, tag, msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (isDebugMode) {
            Log.i(tag, msg);
        } else {
            synchronized (mLogLock) {
                writeToFile(INFO, tag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (isDebugMode) {
            Log.w(tag, msg);
        } else {
            synchronized (mLogLock) {
                writeToFile(WARN, tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (isDebugMode) {
            Log.e(tag, msg);
        } else {
            synchronized (mLogLock) {
                writeToFile(ERROR, tag, msg);
            }
        }
    }

    /**
     * 将log信息写入文件中
     */
    private static void writeToFile(char type, String tag, String msg) {
        String fileName;
        if (type == 'e') {
            fileName = logFilePath + "/error" + ".log";
        } else {
            fileName = logFilePath + "/info" + ".log";
        }

        String log = MyDateUtils.getCurrentDateTime() + "------" + type + "级日志= " + tag + " " + msg + "\n";
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(log);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}