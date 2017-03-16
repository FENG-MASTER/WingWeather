package com.wingweather.qianzise.wingweather.base;

/**
 * 常量存储
 */

public final class Config {

    public final static String KEY_CITY1="city1";
    public final static String KEY_CITY2="city2";

    public final static String DEF_CITY1="成都";
    public final static String DEF_CITY2="湛江";

    //图片文件前缀
    public final static String IMAGE="image_";

    //用于acitivity处理回调的请求码
    public final static int CODE_LEFT_IMAGE=1;
    public final static int CODE_RIGHT_IMAGE=2;
    public final static int CODE_MAIN_IMAGE=3;
    public final static int CODE_ZOOM_IMAGE=4;

    public static class ChartType{
        public static final int Hourly_Temp_Line=1;
        public static final int Daily_Temp_Line=2;
    }
}
