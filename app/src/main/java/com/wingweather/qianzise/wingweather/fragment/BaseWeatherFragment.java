package com.wingweather.qianzise.wingweather.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingweather.qianzise.wingweather.util.MyPreferences;
import com.wingweather.qianzise.wingweather.model.Weather;

import butterknife.ButterKnife;


/**
 * 基础天气fragment,所有基于天气的fragment都应该继承此类,包装了天气的加载和更新功能
 */
public abstract class BaseWeatherFragment extends Fragment implements Weather.WeatherChangeListener {

    protected static final String CITY1 = "CITY1";
    protected static final String CITY2 = "CITY2";

    private String cityName1;
    private String cityName2;

    protected Weather weather1;
    protected Weather weather2;

    protected String tag="null";

    public String getFragmentName(){
        return tag;
    }

    public BaseWeatherFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName1 = getArguments().getString(CITY1);
            cityName2 = getArguments().getString(CITY2);
        }

        setCities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, v);
        initViewAfterBind();
        return v;
    }

    public abstract void initViewAfterBind();

    private void setCities(){
        weather1=Weather.newInstance(cityName1);
        weather2=Weather.newInstance(cityName2);
        weather1.setColor(MyPreferences.getInstance().getLineColor(0));
        weather2.setColor(MyPreferences.getInstance().getLineColor(1));

        weather1.addWeatherChangeListener(this);
        weather2.addWeatherChangeListener(this);


    }

    /**
     * 子类fragment要加载的layout
     * @return 加载的资源id
     */
    public abstract int getLayoutID();


}
