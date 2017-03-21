package com.wingweather.qianzise.wingweather.observer.Bus;

import com.wingweather.qianzise.wingweather.model.Suggestion;

/**
 * 当suggestion显示发生变化的时候产生这个事件
 */

public class SuggestionChangeAction {
    public int getIndex() {
        return index;
    }

    private int index=0;


    public SuggestionChangeAction(int index) {
        this.index = index;

    }
}
