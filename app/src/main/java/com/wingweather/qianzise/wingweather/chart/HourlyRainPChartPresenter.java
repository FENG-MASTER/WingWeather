package com.wingweather.qianzise.wingweather.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 显示每小时下雨可能性线性图的Presenter
 */

public class HourlyRainPChartPresenter extends LineChartPresenter {

    private Weather weather1;
    private Weather weather2;


    @Override
    void init(Weather weather1,Weather weather2) {
        this.weather1=weather1;
        this.weather2=weather2;

        if (weather1.getHourlyRainPLine()!=null){
            handle(weather1);
        }

        if (weather2.getHourlyRainPLine()!=null){
            handle(weather2);
        }

    }

    @Override
   public String getName() {
        return "24小时降水可能";
    }

    @Override
   public int getDrawable() {
        return R.drawable.rain;
    }

    @Override
    public void onWeatherChange(@NonNull Weather weather) {
        if (weather.getHourlyRainPLine()!=null){
            handle(weather);
        }
    }

    @Override
    Line getLine(Weather weather) {
        return weather.getHourlyRainPLine();
    }
}
