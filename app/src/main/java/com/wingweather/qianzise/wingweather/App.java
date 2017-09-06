package com.wingweather.qianzise.wingweather;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.wingweather.qianzise.wingweather.util.MyPreferences;

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
