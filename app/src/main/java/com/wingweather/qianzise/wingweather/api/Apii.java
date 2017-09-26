package com.wingweather.qianzise.wingweather.api;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.model.gson.SearchResult;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;
import com.wingweather.qianzise.wingweather.util.Util;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * 用的和风天气的API
 *
 * 单例
 */
public class Apii {
    private static final String BASE_API_URL=" https://free-api.heweather.com/v5/";
    private static final String DAYILY_FORECAST="forecast/";
    private static final String NOW_FORECAST="now/";
    private static final String HOURLY_FORECAST="hourly/";
    private static final String LIVE_INFO="suggestion/";
    private static final String ALL_INFO="weather/";
    private static final String CON_ICON_URL="http://files.heweather.com/cond_icon/";
    private static final String CON_ICON_SUFFIX=".png";
    private static final String SEARCH="search";

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


    /**
     * 用于检测数据是否正常
     * TODO: 未完成
     * @param jsonObject 数据
     * @return 正常与否
     */
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
    private void sendImageRequest(String fullURL,Response.Listener<Bitmap> listener){

        ImageRequest imageRequest=new ImageRequest(fullURL, listener, 0, 0, ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err",error.toString());
            }
        });

        mQueue.add(imageRequest);
    }


    public void getAll(String city, final Listener<WeatherBean> listener){
        if (listener==null||city==null){
            return;
        }



        sendRequest(BASE_API_URL + ALL_INFO + "?" + "city=" + city + "&key=" + KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
               WeatherBean weatherBean = gson.fromJson(response, WeatherBean.class);
                listener.onReceive(weatherBean);
            }
        });

    }


    public void getWeatherConDrawable(String codeS, final Listener<Drawable> listener){
        int code=Integer.valueOf(codeS);
        sendImageRequest(CON_ICON_URL + code + CON_ICON_SUFFIX, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                BitmapDrawable bitmapDrawable=new BitmapDrawable(App.getContext().getResources(),response);
                listener.onReceive(bitmapDrawable);
            }
        });

    }

    public void checkCity(String cityName, final Listener<Boolean> listener){
        sendRequest(BASE_API_URL + SEARCH+"?"+"city="+cityName+"&key="+KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
               SearchResult result= gson.fromJson(response, SearchResult.class);
                listener.onReceive(result.hasFound());
            }
        });
    }


    public interface Listener<T>{
        void onReceive(T t);
    }

}
