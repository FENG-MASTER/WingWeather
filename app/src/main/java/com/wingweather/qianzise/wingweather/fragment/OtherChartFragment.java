package com.wingweather.qianzise.wingweather.fragment;

import android.os.Bundle;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;

/**
 * Created by qianzise on 2017/3/14 0014.
 */

public class OtherChartFragment extends BaseWeatherFragment {
    @Override
    public void initViewAfterBind() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_otherchart;
    }

    public static OtherChartFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        OtherChartFragment fragment = new OtherChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onWeatherChange(Weather weather) {

    }
}
