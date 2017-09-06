package com.wingweather.qianzise.wingweather.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wingweather.qianzise.wingweather.MainActivity;
import com.wingweather.qianzise.wingweather.R;

/**
 * 启动页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_splash);
        //TODO:这种启动页的方式不会预先加载acitivity,影响性能
        new Handler().postDelayed(() -> {
            startActivity(MainActivity.class);
            finish();
        },500);

    }




}
