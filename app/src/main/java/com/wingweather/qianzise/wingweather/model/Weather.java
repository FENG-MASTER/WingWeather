package com.wingweather.qianzise.wingweather.model;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.base.Config;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;
import com.wingweather.qianzise.wingweather.observer.WeatherObservable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lecho.lib.hellocharts.model.Line;

/**
 * 天气模型外层包装类,直接做成可订阅对象就好了
 */

public class Weather implements Observer<Object>{
    private WeatherBean mWeatherBean;//bean本体
    private String cityName;//城市名
    private int mColor=Color.BLACK;//用于显示line的颜色

    private List<WeatherChangeListener> listeners=new ArrayList<>();//存储所有监听者的list

    private Line hourlyTempLine=null;//每小时天气状况line对象
    private boolean hourlyTempLineing=false;//标志位,true表示正在更新hourlyTempLine对象

    private Line hourlyRainPLine=null;//每小时天气状况line对象
    private boolean hourlyRainPLineing=false;//标志位,true表示正在更新hourlyTempLine对象





    private Weather(String cityName) {
        this.cityName = cityName;
    }


    public void setWeatherBean(WeatherBean weatherBean) {
        this.mWeatherBean = weatherBean;
    }

    public String getCityName() {
        if (cityName != null) {
            return cityName;
        } else {
            return Config.DEF_CITY1;
        }
    }

    public String getTemperture_Now() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);

        }
        return mWeatherBean.getInfo().getNow().getTmp()+"°";
    }

    public String getConditionCode(){
        if (mWeatherBean == null) {
            return "100";

        }
        return mWeatherBean.getInfo().getNow().getCondition().getCode();
    }

    public String getCondition() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);

        }
        return mWeatherBean.getInfo().getNow().getCondition().getTxt();
    }

    public String getAQI() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);

        }
        return mWeatherBean.getInfo().getAirQuality().getCityAqi().getQualityText();
    }


    public String getRainProbability() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);
        }
        return mWeatherBean.getInfo().getDaily_forecast().get(0).getPop()+"%";
    }

    public String getWindLevel() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);
        }

        return mWeatherBean.getInfo().getDaily_forecast().get(0).getWind().getSc();
    }

    public String getPM25() {
        if (mWeatherBean == null) {
            return App.getContext().getString(R.string.no_data);
        }

        return mWeatherBean.getInfo().getAirQuality().getCityAqi().getPm25();
    }

    public List<WeatherBean.infoBean.HourlyForecastBean> getHourlyInfos(){
        if (mWeatherBean==null){
            return new ArrayList<>();
        }
        return mWeatherBean.getInfo().getHourly_forecast();
    }



    public List<Pair<String, String>> getBaseInfo() {
        List<Pair<String, String>> l = new ArrayList<>();

        l.add(new Pair<>("温度", getTemperture_Now()));
        l.add(new Pair<>("天气状况", getConditionCode()));
        l.add(new Pair<>("空气状况", getAQI()));
        l.add(new Pair<>("降水概率", getRainProbability()));
        l.add(new Pair<>("风力", getWindLevel()));
        l.add(new Pair<>("PM2.5", getPM25()));
        return l;
    }


    private void updateHourlyTempLine(){
        hourlyTempLineing=true;
        WeatherObservable o=new WeatherObservable(cityName);
        o.getWeatherLineDate().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Line>() {
            @Override
            public void accept(Line line) throws Exception {
                setHourlyTempLine(line);//把返回的信息拿去更新下自身
                hourlyTempLineing=false;
            }
        }).subscribe(this);
    }

    private void updateHourlyRainPLine() {
        hourlyRainPLineing=true;

        WeatherObservable o=new WeatherObservable(cityName);
        o.getHourlyRainPLine().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Line>() {
            @Override
            public void accept(Line line) throws Exception {
                setHourlyRainPLine(line);
                hourlyRainPLineing=false;
            }
        }).subscribe(this);

    }

    private void setHourlyTempLine(Line line){
        hourlyTempLine=line;
    }

    public Line getHourlyTempLine(){
        if (hourlyTempLine==null&&!hourlyTempLineing){
            updateHourlyTempLine();
            return null;
        }else {
            return hourlyTempLine;
        }

    }

    public Line getHourlyRainPLine(){
        if (hourlyRainPLine==null&&!hourlyRainPLineing){
            updateHourlyRainPLine();
            return null;
        }else {
            return hourlyRainPLine;
        }
    }

    private void setHourlyRainPLine(Line line){
        hourlyRainPLine=line;
    }


    public void setColor(int color){
        mColor=color;
    }

    public int getColor(){
        return mColor;
    }


    private static List<Weather> cache = new ArrayList<>();

    /**
     * 简单做个缓存咯
     *
     * @param name 城市名
     * @return 天气
     */
    public static Weather newInstance(String name) {
        Weather weather = new Weather(name);

        Iterator<Weather> i = cache.iterator();
        Weather temp;
        boolean has=false;
        while (i.hasNext()) {
            temp = i.next();
            if (temp.getCityName().equals(name)) {
                weather = temp;
                has=true;
                break;
            }
        }

        if (!has){
            cache.add(weather);
        }
        return weather;

    }

    public static Weather newInstance(WeatherBean weatherBean) {
        String name=weatherBean.getInfo().getCity().getCityName();
        Weather weather = new Weather(name);
        weather.setWeatherBean(weatherBean);

        Iterator<Weather> i = cache.iterator();
        Weather temp;
        boolean has=false;

        while (i.hasNext()) {
            temp = i.next();
            if (temp.getCityName().equals(name)) {
                temp.setWeatherBean(weatherBean);
                weather=temp;
                has=true;
                break;
            }
        }
        if (!has){
            cache.add(weather);
        }
        return weather;

    }

    public void addWeatherChangeListener(@NonNull WeatherChangeListener listener){
        listeners.add(listener);
    }

    public void removeWeatherChangeListener(@NonNull WeatherChangeListener  listener){
        listeners.remove(listener);
    }

    public void sendToListener(){
        for (WeatherChangeListener l:listeners) {
            l.onWeatherChange(this);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object value) {
        sendToListener();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }


    public interface WeatherChangeListener{
        void onWeatherChange(@NonNull Weather weather);
    }

}
