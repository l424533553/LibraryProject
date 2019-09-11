package com.xuanyuan.library;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import com.xuanyuan.jarlibrary.R;
import com.xuanyuan.library.base.activity.MyAppCompatActivity;

public class MyTestActivity extends MyAppCompatActivity implements View.OnClickListener {

//    @Override
//    public boolean isCheckPermissions() {
//        return false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        findViewById(R.id.btnStartJobService).setOnClickListener(this);
    }

    @Override
    protected String[] getPermissionsArray() {
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

    }


}
