package com.wingweather.qianzise.wingweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wingweather.qianzise.wingweather.base.Config;

/**
 * Created by qianzise on 2017/3/5 0005.
 */

public class MyPreferences {
    private static MyPreferences instance=new MyPreferences();

    private SharedPreferences sharedPreferences;

    public static MyPreferences getInstance(){
        return instance;
    }

    private MyPreferences(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(App.getContext());

    }

    public String getCityName2(){
        return  getSharedPreferences().getString(Config.KEY_CITY1,Config.DEF_CITY1);
    }

    public String getCityName1(){
        return  getSharedPreferences().getString(Config.KEY_CITY2,Config.DEF_CITY2);
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }



}
