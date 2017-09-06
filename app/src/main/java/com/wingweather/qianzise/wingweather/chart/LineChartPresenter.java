package com.wingweather.qianzise.wingweather.chart;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import com.wingweather.qianzise.wingweather.model.Weather;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by qianzise on 2017/3/18 0018.
 * 线性图表显示组件
 */
public abstract class LineChartPresenter extends IChartPresenter {

    protected List<Line> lines=new ArrayList<>();
    protected LineChartView lineChartView;

    /**
     * 获得一条line供显示
     * 这个方法需要实现的是根据weather对象,新建一个要展示的line对象
     * @param weather 对象
     * @return line
     */
    abstract Line getLine(Weather weather);


    @Override
    void initChart(ViewGroup viewGroup) {
        initLineChart(viewGroup.getContext());
        viewGroup.addView(lineChartView);
    }

    /**
     * 用代码新建lineChartView
     * @param context 上下文
     */
    private void initLineChart(Context context){
        lineChartView=new LineChartView(context);
        ViewGroup.LayoutParams layoutParams=
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        lineChartView.setLayoutParams(layoutParams);

    }



    protected void handle(Weather weather){


        Line line=getLine(weather);

        line.setColor(weather.getColor());
        line.setPointColor(Color.GREEN);
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点
        lines.add(line);


        LineChartData data=new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴

        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(15);//设置字体大小
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setName(getName());
        axisX.setHasLines(true); //x 轴分割线
        Axis axisY=new Axis();
        axisY.setHasLines(true);
        axisY.setValues(null);//去掉Y轴的标签显示
        axisY.setHasTiltedLabels(false);
        data.setAxisYLeft(axisY);

//        Viewport viewport=chartView.getCurrentViewport();
//        viewport.nav=0;
//        viewport.top=3;
//        chartView.setCurrentViewport(viewport);
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        lineChartView.setZoomEnabled(false);
        lineChartView.setLineChartData(data);


    }
}
