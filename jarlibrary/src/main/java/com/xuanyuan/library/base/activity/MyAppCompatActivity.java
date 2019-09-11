package com.xuanyuan.library.base.activity;//package com.luofx.base;//package com.luofx.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.xuanyuan.jarlibrary.R;
import com.xuanyuan.library.help.ActivityController;

import static com.xuanyuan.library.config.IConfig.BACK_TIME_DEFAULT;


/**
 * true Or false 是否需要注册事件总线机制
 * 1.需要在 子类 的  super.onCreate(savedInstanceState); 之前调用
 * 2.需要写 有      @Subscribe 关键字的 事件接收处理方法，否则报错异常
 * void onEventMain(BusEvent event);
 * 3.設置 this.isNeedRgEventBus=  true or  false
 */
@SuppressLint("Registered")
public abstract class MyAppCompatActivity extends BasePermissionsActivity implements IBaseActivity {

    /**
     * 是否已经权限授权
     */
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入到 Activity管理中
        ActivityController.addActivity(this);
        context = this;
    }

    /**
     * 设置返回操作
     */
    public void doBack(View view) {
        onBackPressed();
    }

    /**
     * 返回点击  退出时间
     */
    private long backTime;
    private long viewId;

    /**
     * 是否可以点击
     * 超过两秒 才能进行下一次点击操作
     */
    protected boolean isClickAble(View view) {
        boolean isClickAble = false;
        if (System.currentTimeMillis() - backTime > BACK_TIME_DEFAULT) {
            backTime = System.currentTimeMillis();
            isClickAble = true;
        } else {
            if (viewId != view.getId()) {//两次点击的按钮不一样
                isClickAble = true;
            }
        }
        viewId = view.getId();
        return isClickAble;
    }

    /**
     * 跳转 Activity
     *
     * @param cls      跳转的类
     * @param isFinish 是否结束当前Activity，  true:是
     */
    public void jumpActivity(Class<?> cls, boolean isFinish,boolean isAinmain) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            // Activity 的带平滑动画 跳转
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }

    @Override
    public void cancelScreenOn() {
        //擦除屏幕常亮标识
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //TODO  待验证
    // 屏幕是否一直保持常亮  ,可能需要重启该Activity
    @Override
    public void setScreenOn() {
        // 保持屏幕长亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setTitel() {

    }

    /**
     * 测试开发
     */
    private void test(){

//        wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE*, PixelFormat.TRANSPARENT);
//        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR*;
//        // 系统提示window
//        mParams.format = PixelFormat.TRANSLUCENT;// 支持透明 //
//        mParams.format = PixelFormat.RGBA_8888;
//        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        //窗口的透明度
//        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//        windowView = (FloatButtonLayout) layoutInflater.inflate(R.layout.float_button_layout, null);
//        wManager.addView(windowView, mParams);// 添加窗口

    }

}
