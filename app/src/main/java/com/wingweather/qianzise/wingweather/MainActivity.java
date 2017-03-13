package com.wingweather.qianzise.wingweather;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabClickListener;
import com.wingweather.qianzise.wingweather.activity.SettingsActivity;
import com.wingweather.qianzise.wingweather.adapter.MainPagerAdapter;
import com.wingweather.qianzise.wingweather.view.CircleImageView;

import java.io.PipedReader;

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
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;




    private BottomBar mBottomBar;

    private boolean isVisbleForMenu=true;
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

        setDynamicMenu();
    }


    /**
     * 设置菜单根据滚动动态显示和隐藏
     */
    private void setDynamicMenu(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((Math.abs(verticalOffset)+10 >= appBarLayout.getTotalScrollRange())){
                    //滑动到顶了,这个时候应该取消右上角菜单的按钮显示

                    isVisbleForMenu=false;
                    invalidateOptionsMenu();
                }else {
                    if(!isVisbleForMenu){
                        isVisbleForMenu=true;
                        invalidateOptionsMenu();
                    }
                }
            }
        });


    }

    /**
     * 由于没有提供接口,只能自己把viewpager和bottombar的选择转换上做个一一对应的绑定
     * @param viewPager v
     * @param bottomBar b
     */
    private void bindViewPagerWitBottomBar(final ViewPager viewPager, final BottomBar bottomBar){
        bottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {

                if (position>=0&&position<MainPagerAdapter.COUNT){
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


    private void startActivity(Class clazz){
        Intent intent=new Intent(this,clazz);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_setting:
                //打开设置
                startActivity(SettingsActivity.class);
                break;
            case R.id.item_exit:
                //退出程序
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 动态创建个菜单选项
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();//清除下菜单才行,不然会出现很多个
        if (isVisbleForMenu){
            getMenuInflater().inflate(R.menu.option,menu);
            return true;

        }else {
            return super.onPrepareOptionsMenu(menu);
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
