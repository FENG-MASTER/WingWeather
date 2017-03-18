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
 * @see IChartPresenter 的一个实现类,实现了每小时温度线性图表的显示
 *
 */

public class HourlyTemperatureChartPresenter extends LineChartPresenter {


    private Weather weather1;
    private Weather weather2;

    @Override
    void init(Weather weather1,Weather weather2) {
        this.weather1=weather1;
        this.weather2=weather2;

        if (weather1.getHourlyTempLine()!=null){
            handle(weather1);
        }

        if (weather2.getHourlyTempLine()!=null){
            handle(weather2);
        }

    }

    @Override
    public String getName() {
        return "24小时温度";
    }

    @Override
    public int getDrawable() {
        return R.drawable.temp;
    }

    @Override
    public void onWeatherChange(@NonNull Weather weather) {
        if (weather.getHourlyTempLine()!=null){
            handle(weather);
        }
    }


    @Override
    Line getLine(Weather weather) {
        return weather.getHourlyTempLine();
    }
}
