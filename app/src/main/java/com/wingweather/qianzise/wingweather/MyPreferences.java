package com.wingweather.qianzise.wingweather;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qianzise on 2017/3/5 0005.
 */

public class MyPreferences {
    private static MyPreferences instance=new MyPreferences();

    public static String PRENAME="wingWeather.pre";
    private SharedPreferences sharedPreferences;

    public static MyPreferences getInstance(){
        return instance;
    }

    private MyPreferences(){
        sharedPreferences=App.getContext().getSharedPreferences(PRENAME, Context.MODE_PRIVATE);
        if (getSharedPreferences().contains("city1")&&getSharedPreferences().contains("city2")){

        }else {
            setDefault();
        }
    }

    public void setCityName1(String name){
        SharedPreferences.Editor editor=getSharedPreferencesEdit();
        editor.putString("city1",name);
        editor.commit();
    }

    public String getCityName2(){
        return  getSharedPreferences().getString("city2","chengdu");
    }

    public void setCityName2(String name){
        SharedPreferences.Editor editor=getSharedPreferencesEdit();
        editor.putString("city2",name);
        editor.commit();
    }

    public String getCityName1(){
        return  getSharedPreferences().getString("city1","chengdu");
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public SharedPreferences.Editor getSharedPreferencesEdit(){
        return sharedPreferences.edit();
    }

    private void setDefault(){
        SharedPreferences.Editor editor=getSharedPreferencesEdit();
        editor.putString("city1","chengdu");
        editor.putString("city2","zhanjiang");
        editor.commit();
    }
}
