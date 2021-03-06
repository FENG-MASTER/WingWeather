package com.wingweather.qianzise.wingweather.observer;


import android.graphics.drawable.Drawable;

import com.wingweather.qianzise.wingweather.api.Apii;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;
import com.wingweather.qianzise.wingweather.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;

/**
 * 天气对象的信息发生器
 */
public class WeatherObservable {

    private String cityName;


    public WeatherObservable(String cityName){
        this.cityName=cityName;
    }

    /**
     * 按照城市名获取到天气bean的发生器
     * @return 发生器
     */
    public Observable<WeatherBean> getUpdateWeatherBeanObservable(){
        return Observable.create(e -> Apii.getInstance().getAll(cityName, weatherBean -> e.onNext(weatherBean)));
    }


    /**
     * 根据{@link WeatherObservable#getUpdateWeatherBeanObservable()} 返回值再加工
     * 返回weather对象的发生器
     * @return 发生器
     */
    public Observable<Weather> getUpdateWeatherObservable(){
       return getUpdateWeatherBeanObservable().map(weatherBean -> Weather.newInstance(weatherBean));
    }

    /**
     * 根据{@link WeatherObservable#getWeatherHourlyTempPoints()}返回值继续加工
     * 返回24小时温度信息的Line发生器
     * @return 发生器
     */
    public Observable<Line> getWeatherLineDate(){
        return getWeatherHourlyTempPoints().map(pointValues -> {
            Line line=new Line();
            line.setValues(pointValues);
            return line;
        });
    }

    /**
     * 根据{@link WeatherObservable#getUpdateWeatherObservable()} 的返回值再加工
     * 返回24小时温度的List<PointValue>的发生器
     * @return 发生器
     */
    public Observable<List<PointValue>> getWeatherHourlyTempPoints(){
        // TODO:这里不怎么会用RX,先这样写吧,以后会了怎么合并之后再改
       return getUpdateWeatherObservable().map(weather -> {
           List<PointValue> list=new ArrayList<PointValue>();
           Iterator<WeatherBean.infoBean.HourlyForecastBean> i=
                   weather.getHourlyInfos().iterator();
           while (i.hasNext()){
               WeatherBean.infoBean.HourlyForecastBean bean=i.next();
               list.add(new PointValue(
                       Util.date2hour(bean.getDate()),
                       Integer.valueOf(bean.getTmp())));
           }

           return list;
       });
    }

    public Observable<PointValue>  getWeatherHourlyTempPointss(){
        return getUpdateWeatherObservable().flatMap(new Function<Weather, ObservableSource<PointValue>>() {
            @Override
            public ObservableSource<PointValue> apply(Weather weather) throws Exception {
               return Observable.fromIterable(weather.getHourlyInfos()).map(hourlyForecastBean -> new PointValue(
                       Util.date2hour(hourlyForecastBean.getDate()),
                       Integer.valueOf(hourlyForecastBean.getTmp())));
            }
        });


    }

    /**
     * 根据{@link WeatherObservable#getUpdateWeatherObservable()} 的返回值再加工
     * 返回24小时降雨可能性的List<PointValue>的发生器
     * @return 发生器
     */
    public Observable<List<PointValue>> getWeatherHourlyRainPPoints(){
        // TODO:这里不怎么会用RX,先这样写吧,以后会了怎么合并之后再改
        return getUpdateWeatherObservable().map(new Function<Weather, List<PointValue>>() {
            @Override
            public List<PointValue> apply(Weather weather) throws Exception {
                List<PointValue> list=new ArrayList<PointValue>();
                Iterator<WeatherBean.infoBean.HourlyForecastBean> i=
                        weather.getHourlyInfos().iterator();
                while (i.hasNext()){
                    WeatherBean.infoBean.HourlyForecastBean bean=i.next();
                    list.add(new PointValue(
                            Util.date2hour(bean.getDate()),
                            Integer.valueOf(bean.getPop())));
                }

                return list;
            }
        });
    }


    public Observable<Line> getHourlyRainPLine(){
        return getWeatherHourlyRainPPoints().map(new Function<List<PointValue>, Line>() {
            @Override
            public Line apply(List<PointValue> pointValues) throws Exception {
                Line line=new Line();
                line.setValues(pointValues);
                return line;
            }
        });
    }

    public static Observable<Drawable> getWeatherConDrawable(final String code){
        return Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(final ObservableEmitter<Drawable> e) throws Exception {
                Apii.getInstance().getWeatherConDrawable(code, new Apii.Listener<Drawable>() {
                    @Override
                    public void onReceive(Drawable drawable) {
                        e.onNext(drawable);
                    }
                });
            }
        });

    }


}
