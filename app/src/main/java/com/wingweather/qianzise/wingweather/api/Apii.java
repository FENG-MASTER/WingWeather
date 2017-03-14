package com.wingweather.qianzise.wingweather.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;
import com.wingweather.qianzise.wingweather.util.Util;

/**
 * Created by qianzise on 2017/2/27 0027.
 */

public class Apii {
    private static final String BASE_API_URL=" https://free-api.heweather.com/v5/";
    private static final String DAYILY_FORECAST="forecast/";
    private static final String NOW_FORECAST="now/";
    private static final String HOURLY_FORECAST="hourly/";
    private static final String LIVE_INFO="suggestion/";
    private static final String ALL_INFO="weather/";

    private static final String KEY="aac0bdff0c8c475da834086428ece029";

    private static Apii _instance=null;

    public static Apii getInstance(){
        if (_instance==null){
            _instance=new Apii();
        }
        return _instance;
    }

    private RequestQueue mQueue;

    private Apii(){
        mQueue= Volley.newRequestQueue(App.getContext());
    }


    private static boolean checkForResult(String jsonObject){
        return  true;
    }


    private void sendRequest(String fullURL, final Response.Listener<String> listener){

        StringRequest  stringRequest=new StringRequest(Request.Method.GET, fullURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (checkForResult(response)){
                    listener.onResponse(response);
                }else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err",error.toString());
            }
        });


        mQueue.add(stringRequest);

    }


    public void getAll(String city, final Listener<WeatherBean> listener){
        if (listener==null||city==null){
            return;
        }

        city= Util.hanziToPinyin_city(city);

        sendRequest(BASE_API_URL + ALL_INFO + "?" + "city=" + city + "&key=" + KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
               WeatherBean weatherBean = gson.fromJson(response, WeatherBean.class);
                listener.onReceive(weatherBean);
            }
        });

    }




    public interface Listener<T>{
        void onReceive(T t);
    }

}
