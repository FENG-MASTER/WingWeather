package com.wingweather.qianzise.wingweather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.activity.BaseActivity;
import com.wingweather.qianzise.wingweather.activity.SettingsActivity;
import com.wingweather.qianzise.wingweather.adapter.MainPagerAdapter;
import com.wingweather.qianzise.wingweather.fragment.BaseWeatherFragment;
import com.wingweather.qianzise.wingweather.fragment.ChartFragment;
import com.wingweather.qianzise.wingweather.fragment.MainInfoFragment;
import com.wingweather.qianzise.wingweather.fragment.OtherFragment;
import com.wingweather.qianzise.wingweather.util.Config;
import com.wingweather.qianzise.wingweather.util.MyPreferences;
import com.wingweather.qianzise.wingweather.observer.Bus.SuggestionChangeAction;
import com.wingweather.qianzise.wingweather.util.Util;
import com.wingweather.qianzise.wingweather.view.CircleImageView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnLongClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_main)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.ll_top_image_content)
    LinearLayout linearLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    //左边显示城市的view
    @BindView(R.id.tv_left_cityName)
    TextView leftCityName;
    //右边显示城市的view
    @BindView(R.id.tv_right_cityName)
    TextView rightCityName;

    @BindView(R.id.im_toolbar_main)
    ImageView mainImage;
    @BindView(R.id.ci_left)
    CircleImageView leftAvatar;
    @BindView(R.id.ci_right)
    CircleImageView rightAvatar;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nv_main)
    NavigationView navigationView;
    private List<BaseWeatherFragment> fragments=new ArrayList<>();
    private int currentFragmentIndex=0;

    private int imageViewSetting=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
        initFragment();
        initNav();
        AvaterControl control=new AvaterControl();
    }

    private void initView(Bundle savedInstanceState){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(" ");


        setCitesName();
        setImageListener();
    }

    private void setCitesName(){
        leftCityName.setText(MyPreferences.getInstance().getCityName1());
        rightCityName.setText(MyPreferences.getInstance().getCityName2());

    }

    private void setImageListener(){
        rightAvatar.setOnLongClickListener(this);
        leftAvatar.setOnLongClickListener(this);
    }



    @Override
    public boolean onLongClick(View v) {
        int code=0;
        switch (v.getId()){
            case R.id.ci_left:
                //点击左侧头像
                code=Config.CODE_LEFT_IMAGE;
                break;

            case R.id.ci_right:
                //点击右侧头像
                code=Config.CODE_RIGHT_IMAGE;
                break;
            default:
                break;
        }
        sentOpenImageIntent(code);
        return true;
    }

    private void sentOpenImageIntent(int code){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("crop", true);
//        intent.putExtra("return-data", true);
        startActivityForResult(intent,code);
    }

    private void zoomImage(Uri uri,int w,int h){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", w);
        intent.putExtra("aspectY", h);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", w*200);
        intent.putExtra("outputY", h*100);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, Config.CODE_ZOOM_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case Config.CODE_MAIN_IMAGE:
                    imageViewSetting=requestCode;
                    zoomImage(data.getData(),0,0);
                    break;
                case Config.CODE_LEFT_IMAGE:
                case Config.CODE_RIGHT_IMAGE:
                    //要执行个缩放,必须记录下是那个view请求的改变图片
                    imageViewSetting=requestCode;
                    zoomImage(data.getData(),1,1);
                    break;
                case Config.CODE_ZOOM_IMAGE:
                    //缩放过后
                    setImageToView(Util.getBitmapFromIntent(data));

                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }

        }else {
            Snackbar.make(toolbar,"选择图片出错!",Snackbar.LENGTH_SHORT).show();
        }

    }

    private void setImageToView(Bitmap bitmapFromUri) {
        switch (imageViewSetting){
            case Config.CODE_MAIN_IMAGE:
                mainImage.setImageBitmap(bitmapFromUri);
                break;
            case Config.CODE_LEFT_IMAGE:
                leftAvatar.setImageBitmap(bitmapFromUri);
                break;
            case Config.CODE_RIGHT_IMAGE:
                rightAvatar.setImageBitmap(bitmapFromUri);
                break;
            default:

                break;
        }
        imageViewSetting=0;
    }




    private void initNav(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.menu_item_main:
                        showFragment(0);
                        return true;
                    case R.id.menu_item_chart:
                        showFragment(1);
                        return true;
                    case R.id.menu_item_other:
                        showFragment(2);
                        return true;
                    case R.id.menu_item_setting:
                        //打开设置
                        startActivity(SettingsActivity.class);
                        return true;
                    case R.id.menu_item_change_main_image:
                        sentOpenImageIntent(Config.CODE_MAIN_IMAGE);
                        return true;
                    case R.id.menu_item_exit:
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void showFragment(int i) {
        if (i>=fragments.size()){
            return;
        }
        if (i!=currentFragmentIndex){
            Fragment from=fragments.get(currentFragmentIndex);
            Fragment to=fragments.get(i);

            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
//            transaction.setCustomAnimations();
            if (to.isAdded()){
                transaction.hide(from).show(to).commit();
            }else {
                transaction.add(R.id.fl_content,to).hide(from).commit();
            }

        }

        currentFragmentIndex=i;


    }

    private void initFragment(){
        fragments.clear();

        BaseWeatherFragment fragment1= MainInfoFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        BaseWeatherFragment fragment2= ChartFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        BaseWeatherFragment fragment3= OtherFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment1).commit();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }

    }

    /**
     * 控制头像显示的内部类
     */
    public class AvaterControl{

        public AvaterControl(){
            EventBus.getDefault().register(this);

        }

        public void showAvater(int n) {

            switch (n) {
                case Config.LEFT:
                    leftAvatar.animate().alpha(1).setDuration(500).start();
                    rightAvatar.animate().alpha(0).setDuration(500).start();
                    break;
                case Config.RIGHT:
                    leftAvatar.animate().alpha(0).setDuration(500).start();
                    rightAvatar.animate().alpha(1).setDuration(500).start();
                    break;
                default:
                    leftAvatar.animate().alpha(1).setDuration(500).start();
                    rightAvatar.animate().alpha(1).setDuration(500).start();
            }
        }


        @Subscribe
        public void alterAvater(SuggestionChangeAction a){
            showAvater(a.getIndex());
        }
    }

}
