package com.wingweather.qianzise.wingweather.fragment;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.adapter.InfoAdapter;
import com.wingweather.qianzise.wingweather.model.Weather;

import butterknife.BindView;

public class MainInfoFragment extends BaseWeatherFragment {


    @BindView(R.id.rc_list_info)
    RecyclerView recyclerView;

    public MainInfoFragment() {

    }

    public static MainInfoFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        MainInfoFragment fragment = new MainInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViewAfterBind() {
        initList();
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_base_info;
    }


    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new InfoAdapter(getContext(),weather1,weather2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }


    @Override
    public void onWeatherChange(Weather weather) {
        recyclerView.setAdapter(new InfoAdapter(getContext(),weather1,weather2));
        recyclerView.invalidate();
    }

    @Override
    public String getFragmentName() {
        return "基础信息页面";
    }
}
