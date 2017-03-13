package com.wingweather.qianzise.wingweather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 工具类
 */

public final class MUtil {

    /**
     * 用于取出date里的当前所在hour
     * @param date
     * @return
     */
    public static int date2hour(String date){
        if (date==null||date.isEmpty()){
            return 0;
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar calendar=Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.HOUR_OF_DAY);

    }

}
