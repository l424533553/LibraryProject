package com.xuanyuan.library.apk_update;


import android.content.Context;
import android.os.Environment;
import com.xuanyuan.library.base.application.MyBaseApplication;


/**
 * 创建时间：2018/3/7
 * 编写人：czw
 * 功能描述 ：
 */

public interface Constant_APK {

    //    String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyBaseApplication.getInstance().getPackageName();

    String DOWNLOAD_DIR = "/downlaod/";

//    public static String getRootPath(Context context) {
//        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName();
//    }
}
