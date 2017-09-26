package com.wingweather.qianzise.wingweather.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;

import java.io.FileInputStream;
import java.io.IOException;
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
     *
     * @param date
     * @return
     */
    public static int date2hour(String date) {
        if (date == null || date.isEmpty()) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.HOUR_OF_DAY);

    }


    public static Bitmap getBitmapFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        Bitmap bitmap = null;
        if (bundle != null) {
            bitmap = bundle.getParcelable("data");
        }
        return bitmap;
    }

    /**
     * 从uri中获取图片对象
     *
     * @param uri uri
     * @return bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri) {

        try {
            return MediaStore.Images.Media.getBitmap(App.getContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getIconID(String s) {
        switch (s) {
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

    /**
     * 获取当前所在城市名 (XX市)
     *
     * 如果没有相应定位处理模块,或者没有权限的时候会返回null
     *
     * @return 城市名
     * @throws IOException Exception
     */
    public static String getLocation() throws IOException {

        if (!Geocoder.isPresent()) {
            //如果没有地理解析模块 直接返回空
            return null;
        }

        Geocoder geocoder = new Geocoder(App.getContext());
        LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(App.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(App.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        List<Address> fromLocation = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
        if (fromLocation != null && !fromLocation.isEmpty()) {
            Address address = fromLocation.get(0);
            String city = address.getLocality();
            return city.substring(0, city.length() - 1);
        }
        return null;

    }
}
