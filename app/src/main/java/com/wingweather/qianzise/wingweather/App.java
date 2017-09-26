package com.wingweather.qianzise.wingweather;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.umeng.analytics.MobclickAgent;
import com.wingweather.qianzise.wingweather.util.MyPreferences;

import java.util.HashMap;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by qianzise on 2017/2/27 0027.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        MobclickAgent.setDebugMode(true);
      //  MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    public static Context getContext(){
        return context;
    }

    public String getCity1(){
       return MyPreferences.getInstance().getCityName1();
    }

    public String getCity2(){
        return MyPreferences.getInstance().getCityName2();
    }
}
