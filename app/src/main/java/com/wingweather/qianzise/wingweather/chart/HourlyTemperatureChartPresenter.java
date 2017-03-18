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

public class HourlyTemperatureChartPresenter extends IChartPresenter {


    private Weather weather1;
    private Weather weather2;

    private LineChartView lineChartView;
    private List<Line> lines=new ArrayList<>();

    @Override
    void initChart(ViewGroup viewGroup) {
        initLineChart(viewGroup.getContext());
        viewGroup.addView(lineChartView);
    }

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


    private void initLineChart(Context context){
        lineChartView=new LineChartView(context);
        ViewGroup.LayoutParams layoutParams=
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        lineChartView.setLayoutParams(layoutParams);


    }


    private void handle(Weather weather){
        Line line=weather.getHourlyTempLine();

        line.setColor(weather.getColor());
        line.setPointColor(Color.GREEN);
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点
        lines.add(line);
        update();
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
        lineChartView.setZoomEnabled(false);
        lineChartView.setLineChartData(data);

    }
}
