package com.wingweather.qianzise.wingweather;

import android.app.Application;
import android.content.Context;

/**
 * Created by qianzise on 2017/2/27 0027.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
