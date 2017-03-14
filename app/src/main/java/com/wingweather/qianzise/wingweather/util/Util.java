package com.wingweather.qianzise.wingweather.util;

import android.content.Context;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;

import java.util.List;

/**
 * 工具类
 */

public class Util {

    public static String hanziToPinyin_city(String hanzi){
        Context context=App.getContext();
        String[] hanziArr=context.getResources().getStringArray(R.array.cityNameArr);
        String[] pinyinArr=context.getResources().getStringArray(R.array.cityVal);

        String cityPinyin=hanzi;
        for(int i =0;i<hanziArr.length;i++){
            if (hanziArr[i].equals(hanzi)){
                cityPinyin=pinyinArr[i];
            }
        }
        return  cityPinyin;
    }

}
