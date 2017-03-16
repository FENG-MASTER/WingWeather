package com.wingweather.qianzise.wingweather.model;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.api.Apii;
import com.wingweather.qianzise.wingweather.base.Config;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;
import com.wingweather.qianzise.wingweather.observer.WeatherObservable;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lecho.lib.hellocharts.model.Line;

/**
 * 天气模型外层包装类
 */

public class Weather implements Observer<Object>{
    private WeatherBean mWeatherBean;
    private String cityname;
    private List<WeatherChangeListener> listeners=new ArrayList<>();
    private Line hourlyTempLine=null;
    private boolean hourlyTempLineing=false;


    private Weather(String cityname) {
        this.cityname = cityname;
    }


    public void setWeatherBean(WeatherBean weatherBean) {
        this.mWeatherBean = weatherBean;
    }

    public String getCityName() {
        if (cityname != null) {
            return cityname;
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
        l.add(new Pair<>("天气状况", getCondition()));
        l.add(new Pair<>("空气状况", getAQI()));
        l.add(new Pair<>("降水概率", getRainProbability()));
        l.add(new Pair<>("风力", getWindLevel()));
        l.add(new Pair<>("PM2.5", getPM25()));
        return l;
    }


    public void updateHourlyTempLine(){
        hourlyTempLineing=true;
        WeatherObservable o=new WeatherObservable(cityname);
        o.getWeatherLineDate().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(this);
    }

    public Line getHourlyTempLine(){
        if (hourlyTempLine==null&&!hourlyTempLineing){
            updateHourlyTempLine();
            return null;
        }else {
            return hourlyTempLine;
        }

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
        void onWeatherChange(Weather weather);
    }

}
