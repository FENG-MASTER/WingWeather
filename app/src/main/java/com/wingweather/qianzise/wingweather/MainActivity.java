package com.wingweather.qianzise.wingweather;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.roughike.bottombar.BottomBar;

import com.roughike.bottombar.OnTabClickListener;
import com.wingweather.qianzise.wingweather.activity.BaseActivity;
import com.wingweather.qianzise.wingweather.activity.SettingsActivity;
import com.wingweather.qianzise.wingweather.adapter.MainPagerAdapter;
import com.wingweather.qianzise.wingweather.base.Config;
import com.wingweather.qianzise.wingweather.base.MyPreferences;
import com.wingweather.qianzise.wingweather.observer.BusAction;
import com.wingweather.qianzise.wingweather.view.CircleImageView;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnLongClickListener {
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

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar navigationBar;

    private int imageViewSetting=0;

    private boolean isVisibleForMenu =true;//标志位,决定是否显示菜单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState){
        ButterKnife.bind(this);

        navigationBar
                .addItem(new BottomNavigationItem(R.drawable.line_chart, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.chart_select, "图表").setInactiveIconResource(R.drawable.delicious).setActiveColor(Color.RED))
                .addItem(new BottomNavigationItem(R.drawable.doughnut_chart, "其他"))
                .initialise();


        setSupportActionBar(toolbar);
        setTitle(" ");

        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        bindViewPagerWitBottomBar(viewPager,navigationBar);


        setDynamicMenu();
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
                    setImageToView(getBitmapFromIntent(data));

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


    private Bitmap getBitmapFromIntent(Intent intent){
        Bundle bundle=intent.getExtras();
        Bitmap bitmap=null;
        if (bundle!=null){
            bitmap=bundle.getParcelable("data");
        }
        return bitmap;
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

                    isVisibleForMenu =false;
                    invalidateOptionsMenu();
                }else {
                    if(!isVisibleForMenu){
                        isVisibleForMenu =true;
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
    private void bindViewPagerWitBottomBar(final ViewPager viewPager, final BottomNavigationBar bottomBar){

        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position>=0&&position<MainPagerAdapter.COUNT){
                    viewPager.setCurrentItem(position,true);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                EventBus.getDefault().post(new TabRePressEvent(position));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                bottomBar.selectTabAtPosition(position,true);
                bottomBar.selectTab(position,false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private static int lastPress=0;
    private void selected(int pos){
        if (lastPress==pos){
            EventBus.getDefault().post(new TabRePressEvent(pos));
        }else {
            lastPress=pos;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_setting:
                //打开设置
                startActivity(SettingsActivity.class);
                break;
            case R.id.item_change_main_image:
                sentOpenImageIntent(Config.CODE_MAIN_IMAGE);
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
        if (isVisibleForMenu){
            getMenuInflater().inflate(R.menu.option,menu);
            return true;

        }else {
            return super.onPrepareOptionsMenu(menu);
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  mBottomBar.onSaveInstanceState(outState);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public static class TabRePressEvent{
        public int pos;
        public TabRePressEvent(int pos){
            this.pos=pos;
        }
    }

}
