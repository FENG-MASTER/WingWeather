package com.wingweather.qianzise.wingweather.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 工具类
 */

public class Util {


    /**
     * 用于取出date里的当前所在hour
     * @param date
     * @return
     */
    public static int date2hour(String date){
        if (date==null||date.isEmpty()){
            return 0;
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar calendar=Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.HOUR_OF_DAY);

    }



    public static Bitmap getBitmapFromIntent(Intent intent){
        Bundle bundle=intent.getExtras();
        Bitmap bitmap=null;
        if (bundle!=null){
            bitmap=bundle.getParcelable("data");
        }
        return bitmap;
    }

    public static int getIconID(String s){
        switch (s){
            case "温度":
                return R.drawable.temperature_icon;
            case "天气状况":
                return R.drawable.weather_icon;
            case "相对湿度":
                return R.drawable.humidity_icon;
            case "降水概率":
                return R.drawable.rain_icon;
            case "风力":
                return R.drawable.wind_icon;
            case "空气状况":
                return R.drawable.aqi_icon;
            case "PM2.5":
                return R.drawable.pm25_icon;
            default:
                return R.drawable.weather_icon;
        }
    }
}
