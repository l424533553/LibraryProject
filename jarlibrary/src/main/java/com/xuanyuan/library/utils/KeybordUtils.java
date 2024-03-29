package com.xuanyuan.library.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * 说明：软键盘的关闭打开
 * 作者：User_luo on 2018/7/9 17:02
 * 邮箱：424533553@qq.com
 */
public class KeybordUtils {

    /**
     * 打开软键盘
     *
     * @param mEditText kongjain
     * @param mContext  shangxiawen
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }


    /**
     * 隐藏键盘
     */
//    public void hintKeyBoard(Context context, View view) {
//        //拿到InputMethodManager
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        //如果window上view获取焦点 && view不为空
//        if (imm.isActive() &&   getCurrentFocus() != null) {
//            //拿到view的token 不为空
//            if (getCurrentFocus().getWindowToken() != null) {
//                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }


    /**
     * 判断当前软键盘是否打开
     *
     * @param activity activity
     * @return 是否是打开的
     */
    public static boolean isSoftInputShow(Activity activity) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);
            if (inputmanger != null) {
                return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
            }
        }
        return false;
    }


    /**
     * 设置后，点击 EditText 将不会再弹出软件盘
     * @param editText  控件
     */
    public void disableShowInput(final EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(editText.length());
            }
        });
    }

}
