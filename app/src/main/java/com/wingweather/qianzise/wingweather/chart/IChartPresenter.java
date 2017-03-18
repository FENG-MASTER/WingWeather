package com.wingweather.qianzise.wingweather.chart;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;

/**
 * 图表显示接口,用于显示各种各样的图表
 */

public abstract class IChartPresenter implements Weather.WeatherChangeListener{


    public final void initView(@NonNull ViewGroup viewGroup,Weather w1,Weather w2){
        viewGroup.removeAllViews();
        initChart(viewGroup);
        w1.addWeatherChangeListener(this);
        w2.addWeatherChangeListener(this);
        init(w1,w2);
    }

   abstract void initChart(ViewGroup viewGroup);

    abstract void init(Weather weather1,Weather weather2);

    abstract public String getName();

    abstract public  @DrawableRes int getDrawable();


}
