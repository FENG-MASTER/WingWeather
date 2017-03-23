package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by qianzise on 2017/3/23 0023.
 */

public class MyNestedScrollView extends NestedScrollView {
    private float mDownPosX;
    private float mDownPosY;

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean flag=super.onInterceptTouchEvent(ev);
        Log.e("my","onInterceptTouchEvent:"+flag);
        if (!flag){
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return flag;
    }

}
