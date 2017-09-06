package com.wingweather.qianzise.wingweather.model;


import android.support.annotation.NonNull;

/**
 * 信息类,用来存那些建议的
 */
public class Suggestion implements Comparable<Suggestion>{

    private String name;
    private String index;
    private String detail;
    private int iconID;

    public Suggestion(String name,int iconID, String index, String detail) {
        this.name = name;
        this.iconID=iconID;
        this.index = index;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getDetail() {
        return detail;
    }

    public int getIconID() {
        return iconID;
    }

    @Override
    public int compareTo(@NonNull Suggestion another) {
        return another.name.compareTo(name);
    }
}
