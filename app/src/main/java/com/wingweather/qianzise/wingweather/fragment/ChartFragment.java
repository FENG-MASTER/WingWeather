package com.wingweather.qianzise.wingweather.fragment;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;


import com.wingweather.qianzise.wingweather.MainActivity;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.chart.HourlyRainPChartPresenter;
import com.wingweather.qianzise.wingweather.chart.HourlyTemperatureChartPresenter;
import com.wingweather.qianzise.wingweather.chart.IChartPresenter;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.widget.ChartSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import lecho.lib.hellocharts.model.Line;

/**
 * 用于各种各样的图表
 */

public class ChartFragment extends BaseWeatherFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.fl_chart)
    FrameLayout frameLayout;

    ChartSelector selector;

    private List<IChartPresenter> iChartPresenterList=new ArrayList<>();
    private IChartPresenter presenter;

    public static ChartFragment newInstance(String param1, String param2) {

        Bundle args = new Bundle();
        args.putString(CITY1,param1);
        args.putString(CITY2,param2);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViewAfterBind() {

        iChartPresenterList.add(new HourlyTemperatureChartPresenter());
        iChartPresenterList.add(new HourlyRainPChartPresenter());

        selector=new ChartSelector(getContext(),R.style.dialog);
        selector.setData(presentersToSelectorData());
        selector.setOnItemClickListener(this);
        EventBus.getDefault().register(this);

        presenter=iChartPresenterList.get(0);
        presenter.initView(frameLayout,weather1,weather2);

    }

    private List<Pair<Integer,String>> presentersToSelectorData(){
        List<Pair<Integer,String>> list=new ArrayList<>();

        for (IChartPresenter p : iChartPresenterList) {
            Pair<Integer,String> pair=new Pair<>(p.getDrawable(),p.getName());
            list.add(pair);
        }
        return list;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hourlytemperaturechart;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tabRePress(MainActivity.TabRePressEvent event){
        if (event.pos==1){
            if (selector.isShowing()){
                selector.dismiss();
            }else {
                selector.show();
            }
        }
    }

    @Override
    public void onWeatherChange(Weather weather) {

    }


    /**
     * 这个是 {@link ChartSelector} 被选择后回调的函数
     * @param parent 无所谓
     * @param view 无所谓
     * @param position 用这个就好,这个表示所选中的位置,直接对应
     * @param id 目测和前面一样
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter=iChartPresenterList.get(position%iChartPresenterList.size());
        presenter.initView(frameLayout,weather1,weather2);
        selector.dismiss();
    }
}
