package com.xuanyuan.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 说明：Toast 工具类
 * 作者：User_luo on 2018/5/7 17:04
 * 邮箱：424533553@qq.com
 */
public class MyToast {

    //
    private static Toast toast;

    /**
     * show toast
     *
     * @param context context
     * @param msg     message string
     */
    @SuppressLint("ShowToast")
    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastNetError(Context context) {
        Toast.makeText(context.getApplicationContext(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, String info) {
        Toast toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
//        LinearLayout layout = (LinearLayout) toast.getView();
//        layout.setBackgroundColor(Color.RED);
        TextView v = toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        v.setTextSize(18);
        toast.setText(info);

        toast.show();
    }
}
