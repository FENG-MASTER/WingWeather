package com.wingweather.qianzise.wingweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wingweather.qianzise.wingweather.R;

/**
 * 基础activity,现在暂时没啥大用
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final void startActivity(Class clazz){
        Intent intent=new Intent(this,clazz);
        startActivity(intent);
    }

}
