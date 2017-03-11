package com.wingweather.qianzise.wingweather.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.MyPreferences;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.fragment.BaseWeatherFragment;
import com.wingweather.qianzise.wingweather.fragment.SideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianzise on 2017/3/2 0002.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int COUNT=3;
    List<BaseWeatherFragment> fList=new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        BaseWeatherFragment fragment1=SideFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());


        BaseWeatherFragment fragment2=SideFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());

        BaseWeatherFragment fragment3=SideFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());

        fList.add(fragment1);
        fList.add(fragment2);
        fList.add(fragment3);
    }

    @Override
    public Fragment getItem(int position) {
        return fList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fList.get(position).getFragmentName();
    }

    @Override
    public int getCount() {
        return fList.size();
    }
}
