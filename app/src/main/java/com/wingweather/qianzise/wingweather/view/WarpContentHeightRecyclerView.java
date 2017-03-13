package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qianzise on 2017/3/11 0011.
 */

public class WarpContentHeightRecyclerView extends RecyclerView {
    public WarpContentHeightRecyclerView(Context context) {
        super(context);
    }

    public WarpContentHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WarpContentHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

//        int height = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            height += h;
//        }
//
//        if (height<heightSpec){
//            heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        }
//
//        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean f=super.onInterceptTouchEvent(e);
        Log.e("my","RecyclerView  onInterceptTouchEvent:"+f);
        return f;
    }
}
