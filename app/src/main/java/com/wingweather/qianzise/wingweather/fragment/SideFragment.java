package com.wingweather.qianzise.wingweather.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.adapter.DividerItemDecoration;
import com.wingweather.qianzise.wingweather.adapter.InfoAdapter;
import com.wingweather.qianzise.wingweather.api.Apii;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.model.gson.WeatherBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SideFragment extends BaseWeatherFragment {


    @BindView(R.id.rc_list_info)
    RecyclerView recyclerView;

    public SideFragment() {

    }

    public static SideFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        SideFragment fragment = new SideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViewAfterBind() {
        initList();
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_side;
    }


    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new InfoAdapter(getContext(),weather1,weather2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL
        ));

    }


    @Override
    public void onWeatherChange(Weather weather) {
        recyclerView.setAdapter(new InfoAdapter(getContext(),weather1,weather2));
        recyclerView.invalidate();
    }

}
