package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by qianzise on 2017/3/16 0016.
 */

public class ChartSelecter extends PopupWindow {
    @BindView(R.id.gv_chart_select)
    GridView gridView;

    private Context mContext;

    public ChartSelecter(Context context) {
        super(context);
        mContext=context;

        initView();

    }



    private void initView(){
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.pop_chart_select,null);
        setContentView(view);

        ButterKnife.bind(this,view);

        gridView.setAdapter(
                new SimpleAdapter(
                        mContext,getData(),R.layout.pop_item,
                        new String[]{"img","name"},
                        new int[]{R.id.iv_pop_item,R.id.tv_pop_item}));


    }

    private List<Map<String,Object>> getData(){
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();
        map1.put("img",R.drawable.fab_ico);
        map1.put("name","图表1");
        list.add(map1);

        Map<String,Object> map2=new HashMap<>();
        map2.put("img",R.drawable.fab_ico);
        map2.put("name","图表1");
        list.add(map2);

        Map<String,Object> map3=new HashMap<>();
        map3.put("img",R.drawable.fab_ico);
        map3.put("name","图表1");
        list.add(map3);

        return list;
    }




}
