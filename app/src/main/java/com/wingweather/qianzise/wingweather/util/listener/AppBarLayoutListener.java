package com.wingweather.qianzise.wingweather.util.listener;

import android.support.design.widget.AppBarLayout;

/**
 * Created by qianzise on 2017/9/24.
 */

public abstract class AppBarLayoutListener implements AppBarLayout.OnOffsetChangedListener {

    private int state=EXPANDED;

    public static final int EXPANDED=1;
    public static final int COLLAPSED=2;


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset==0&&state==EXPANDED){
            state=COLLAPSED;
            onStateChange(state);
        }else if(Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()){
            if (state!=EXPANDED){
                state=EXPANDED;
                onStateChange(state);
            }
        }

    }

    public abstract void onStateChange(int state);
}
