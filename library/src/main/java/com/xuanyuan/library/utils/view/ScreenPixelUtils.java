package com.xuanyuan.library.utils.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;


/**
 * Created by Longer on 2016/10/26.
 */
public class ScreenPixelUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public  int px2dip(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取屏幕宽度:
    public  int getScreenWidthPixels(Activity activity) {
        //获取屏幕的宽高的像素
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕宽度:
    public  int getScreenHighPixels(Activity activity) {
        //获取屏幕的高的像素
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * get toolbar height   获取控件的 toolbar高度
     * @param context  上下文
     * @return     返回toolbar的高度
     */
    public  int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }


    /**
     * 关闭 导航栏 ， 使用时最好放在setView（）之前
     * @param isShowAuto  true:点击输入框时导航栏会 显示出来
     *                    false:导航栏一直关闭
     */
    private void closeStatusBar(Activity activity, boolean isShowAuto) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (isShowAuto) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                params.systemUiVisibility = SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            }
        }
        window.setAttributes(params);
    }

}
