package com.wingweather.qianzise.wingweather;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabClickListener;
import com.wingweather.qianzise.wingweather.adapter.MainPagerAdapter;
import com.wingweather.qianzise.wingweather.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_main)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.ll_top_image_content)
    LinearLayout linearLayout;

    private BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);



    }

    private void initView(Bundle savedInstanceState){
        ButterKnife.bind(this);

         mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.cl_main),
                findViewById(R.id.myScrollingContent), savedInstanceState);
        mBottomBar.setItems(R.menu.bottom);

        setSupportActionBar(toolbar);
        setTitle(" ");

        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        bindViewPagerWitBottomBar(viewPager,mBottomBar);



    }

    private void bindViewPagerWitBottomBar(final ViewPager viewPager, final BottomBar bottomBar){
        bottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                if (position>0&&position<MainPagerAdapter.COUNT){
                    viewPager.setCurrentItem(position,true);
                }
            }

            @Override
            public void onTabReSelected(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
