package com.wingweather.qianzise.wingweather.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.wingweather.qianzise.wingweather.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择器
 * 用于选择图表形式
 */

public class ChartSelector extends AlertDialog {
    @BindView(R.id.gv_chart_select)
    GridView gridView;

    private Context mContext;
    private GridView.OnItemClickListener listenerTemp=null;

    public ChartSelector(Context context) {
        super(context);
        mContext=context;
    }

    public ChartSelector(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }

    public ChartSelector(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_chart_select);

        ButterKnife.bind(this);
        initView();
        setCanceledOnTouchOutside(true);
    }

    private void initView(){

        gridView.setAdapter(
                new SimpleAdapter(
                        mContext,getData(),R.layout.pop_item,
                        new String[]{"img","name"},
                        new int[]{R.id.iv_pop_item,R.id.tv_pop_item}));

        if (listenerTemp!=null){
            setOnItemClickListener(listenerTemp);
            listenerTemp=null;
        }
    }

    public void setOnItemClickListener(GridView.OnItemClickListener listener){
        if (gridView!=null){
            gridView.setOnItemClickListener(listener);
        }else {
            listenerTemp=listener;
        }
    }




    private List<Map<String,Object>> getData(){
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();
        map1.put("img",R.drawable.fab_ico);
        map1.put("name","每小时温度线性图");
        list.add(map1);

        Map<String,Object> map2=new HashMap<>();
        map2.put("img",R.drawable.fab_ico);
        map2.put("name","每天温度线性图");
        list.add(map2);

        Map<String,Object> map3=new HashMap<>();
        map3.put("img",R.drawable.fab_ico);
        map3.put("name","图表1");
        list.add(map3);

        Map<String,Object> map4=new HashMap<>();
        map4.put("img",R.drawable.fab_ico);
        map4.put("name","图表1");
        list.add(map4);

        return list;
    }




}
