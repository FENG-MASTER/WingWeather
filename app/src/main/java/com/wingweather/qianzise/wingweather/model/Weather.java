package com.wingweather.qianzise.wingweather.model;

import android.util.Pair;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.api.Apii;
import com.wingweather.qianzise.wingweather.base.Config;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 天气模型外层包装类
 */

public class Weather {
    private WeatherBean mWeatherBean;
    private String cityname;
    private boolean isUpdate = false;

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

    /**
     * 检测是否已经更新过
     *
     * @return 是否更新
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    public void update(final Runnable runnable) {
        Apii.getInstance().getAll(getCityName(), new Apii.Listener<WeatherBean>() {
            @Override
            public void onReceive(WeatherBean weatherBean) {
                mWeatherBean = weatherBean;
                isUpdate = true;
                runnable.run();
            }
        });
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




}
