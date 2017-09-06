package com.wingweather.qianzise.wingweather.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.wingweather.qianzise.wingweather.R;

/**
 * 基础activity,现在暂时没啥大用
 */

public class BaseActivity extends AppCompatActivity {

    private static final int CODE=101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPer();//请求权限
    }

    public final void startActivity(Class clazz){
        Intent intent=new Intent(this,clazz);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 动态请求权限
     */
    private void requestPer(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)//读取外置存储
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)//写外置存储
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)//获取网络状态
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)//读取手机状态
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_WIFI_STATE)//WIFI状态
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)//网络请求
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.INTERNET
                        },
                        CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
