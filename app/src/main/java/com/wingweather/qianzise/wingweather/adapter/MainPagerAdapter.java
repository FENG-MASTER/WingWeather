package com.wingweather.qianzise.wingweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wingweather.qianzise.wingweather.util.MyPreferences;
import com.wingweather.qianzise.wingweather.fragment.BaseWeatherFragment;
import com.wingweather.qianzise.wingweather.fragment.ChartFragment;
import com.wingweather.qianzise.wingweather.fragment.MainInfoFragment;
import com.wingweather.qianzise.wingweather.fragment.OtherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *主页面的viewpager的适配器
 * 包含了三个页面
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int COUNT=3;
    List<BaseWeatherFragment> fList=new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        BaseWeatherFragment fragment1= MainInfoFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());


        BaseWeatherFragment fragment2= ChartFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());

        BaseWeatherFragment fragment3= OtherFragment.newInstance(
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
