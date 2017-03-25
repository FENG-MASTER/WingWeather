package com.wingweather.qianzise.wingweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.wingweather.qianzise.wingweather.App;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

    public void setCityName1(String name){
        getSharedPreferences().edit().putString(Config.KEY_CITY1,name).apply();
    }

    public void setCityName2(String name){
        getSharedPreferences().edit().putString(Config.KEY_CITY2,name).apply();
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }


    public static boolean saveImage(int id, Bitmap bitmap){
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=App.getContext().openFileOutput(Config.IMAGE+id+".png", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        return true;
    }

    public static Bitmap loadImage(int id){
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=App.getContext().openFileInput(Config.IMAGE+id+".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return BitmapFactory.decodeStream(fileInputStream);
    }

    public int getLineColor(int index){
        if (index>2||index<0){
            try {
                throw new Exception("index超出范围");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return index==1?Color.parseColor(getSharedPreferences().getString(Config.KEY_COLOR1,"#000000")):Color.parseColor(getSharedPreferences().getString(Config.KEY_COLOR2,"#000000"));

    }





}
