package com.wingweather.qianzise.wingweather.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.adapter.DividerItemDecoration;
import com.wingweather.qianzise.wingweather.adapter.InfoAdapter;
import com.wingweather.qianzise.wingweather.adapter.SuggestionAdapter;
import com.wingweather.qianzise.wingweather.model.Suggestion;
import com.wingweather.qianzise.wingweather.model.Weather;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by qianzise on 2017/3/14 0014.
 */

public class OtherFragment extends BaseWeatherFragment {
    @BindView(R.id.rc_list_suggestion)
    RecyclerView recyclerView;
    List<Suggestion> l=new ArrayList<>();

    @Override
    public void initViewAfterBind() {
        l.add(new Suggestion("1","2","3"));
        l.add(new Suggestion("1","2","3"));
        l.add(new Suggestion("1","2","3"));
        l.add(new Suggestion("1","2","3"));

        recyclerView.setAdapter(new SuggestionAdapter(getContext(),l));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL
        ));

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_other;
    }

    public static OtherFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onWeatherChange(Weather weather) {

    }
}
