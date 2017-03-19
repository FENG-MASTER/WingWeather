package com.wingweather.qianzise.wingweather.model;


/**
 * 信息类,用来存那些建议的
 */
public class Suggestion {

    private String name;
    private String index;
    private String detail;

    public Suggestion(String name, String index, String detail) {
        this.name = name;
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
}
