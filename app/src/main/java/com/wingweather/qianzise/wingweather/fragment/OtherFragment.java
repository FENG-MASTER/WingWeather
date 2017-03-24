package com.wingweather.qianzise.wingweather.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.adapter.DividerItemDecoration;
import com.wingweather.qianzise.wingweather.adapter.InfoAdapter;
import com.wingweather.qianzise.wingweather.adapter.SuggestionAdapter;
import com.wingweather.qianzise.wingweather.model.Suggestion;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.observer.Bus.SuggestionChangeAction;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */

public class OtherFragment extends BaseWeatherFragment {
    @BindView(R.id.rc_list_suggestion)
    RecyclerView recyclerView;

    @Override
    public void initViewAfterBind() {

        if (weather1.getSuggestions()!=null&&weather2.getSuggestions()!=null){
            recyclerView.setAdapter(
                    new SuggestionAdapter(
                            getContext(),weather1.getSuggestions(),weather2.getSuggestions()));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        if (weather1.getSuggestions()!=null&&weather2.getSuggestions()!=null){
            recyclerView.setAdapter(new SuggestionAdapter(getContext(),weather1.getSuggestions(),weather2.getSuggestions()));
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            EventBus.getDefault().post(new SuggestionChangeAction(-1));
        }else {
            recyclerView.setAdapter(new SuggestionAdapter(getContext(),weather1.getSuggestions(),weather2.getSuggestions()));
        }
    }
}
