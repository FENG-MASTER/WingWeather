package com.wingweather.qianzise.wingweather.fragment;

import android.graphics.Color;
import android.os.Bundle;


import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.observer.WeatherObservable;
import com.wingweather.qianzise.wingweather.widget.ChartSelector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 用于显示24小时两个城市的表格
 */

public class HourlyTemperatureChartFragment extends BaseWeatherFragment
        implements Observer<Line>{
    @BindView(R.id.lineChart_hourly_temp)
    LineChartView chartView;


    private List<Line> lines=new ArrayList<>();

    public static HourlyTemperatureChartFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        HourlyTemperatureChartFragment fragment = new HourlyTemperatureChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViewAfterBind() {
        WeatherObservable weatherObservable1=new WeatherObservable(weather1.getCityName());
        WeatherObservable weatherObservable2=new WeatherObservable(weather2.getCityName());
        weatherObservable1.getWeatherLineDate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
        weatherObservable2.getWeatherLineDate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);


    }

    @Override
    public void weatherUpdateSucceed(Weather weather) {

    }

    private void handleLine(Line line){

    }

    private void update(){
        LineChartData data=new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴

        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(20);//设置字体大小
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        Axis axisY=new Axis();
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);

//        Viewport viewport=chartView.getCurrentViewport();
//        viewport.bottom=0;
//        viewport.top=3;
//        chartView.setCurrentViewport(viewport);
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        chartView.setZoomEnabled(false);
        chartView.setLineChartData(data);

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hourlytemperaturechart;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Line value) {
        value.setColor(Color.BLUE);
        value.setPointColor(Color.GREEN);
        value.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        value.setCubic(false);//曲线是否平滑，即是曲线还是折线
        value.setFilled(false);//是否填充曲线的面积
        value.setHasLabels(true);//曲线的数据坐标是否加上备注
        value.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        value.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点
        lines.add(value);
        update();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    @OnClick(R.id.lineChart_hourly_temp)
    public void select(){
        showSelector();
    }


    private void showSelector(){
        ChartSelector selector=new ChartSelector(getContext(),R.style.dialog);

        selector.show();
    }
}
