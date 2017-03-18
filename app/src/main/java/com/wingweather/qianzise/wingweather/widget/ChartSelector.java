package com.wingweather.qianzise.wingweather.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.util.Pair;
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
import java.util.Iterator;
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

    private List<Pair<Integer,String>> mList;

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


    public void setData(List<Pair<Integer,String>> list){
        mList=list;
        setAdapter();
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
        setAdapter();

        if (listenerTemp!=null){
            setOnItemClickListener(listenerTemp);
            listenerTemp=null;
        }
    }

    private void setAdapter(){
        if (gridView==null){
            return;
        }
        gridView.setAdapter(
                new SimpleAdapter(
                        mContext,getData(),R.layout.pop_item,
                        new String[]{"img","name"},
                        new int[]{R.id.iv_pop_item,R.id.tv_pop_item}));
        gridView.invalidate();


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

        Iterator<Pair<Integer,String>> iterator=mList.iterator();

        Pair<Integer,String> temp;
        while (iterator.hasNext()){
            temp=iterator.next();

            Map<String,Object> map1=new HashMap<>();
            map1.put("img",temp.first);
            map1.put("name",temp.second);
            list.add(map1);
        }

        return list;
    }




}
