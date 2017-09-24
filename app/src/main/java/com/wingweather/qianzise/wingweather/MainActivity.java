package com.wingweather.qianzise.wingweather;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.view.animation.AlphaAnimation;
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
import com.wingweather.qianzise.wingweather.util.listener.AppBarLayoutListener;
import com.wingweather.qianzise.wingweather.view.CircleImageView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主activity
 */
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

    //首页中的大图
    @BindView(R.id.im_toolbar_main)
    ImageView mainImage;
    //左边的头像
    @BindView(R.id.ci_left)
    CircleImageView leftAvatar;
    //右边的头像
    @BindView(R.id.ci_right)
    CircleImageView rightAvatar;

    //左边抽屉
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    //导航栏
    @BindView(R.id.nv_main)
    NavigationView navigationView;
    @BindView(R.id.ll_toolbar_date)
    LinearLayout dateLayout;

    //月份显示
    @BindView(R.id.tv_toolbar_month)
    TextView month;
    //日显示
    @BindView(R.id.tv_toolbar_day)
    TextView day;

    /**
     * 是否正在播放日期动画标志位
     */
    private boolean dateAnimatorFlag=false;

    /**
     * 显示日期动画
     */
    private ObjectAnimator visbleAnimator;
    /**
     * 隐藏日期动画
     */
    private ObjectAnimator inVisbleAnimator;

    private Uri imageUri;

    //存储所有fragment
    private List<BaseWeatherFragment> fragments = new ArrayList<>();
    //当前显示fragment
    private int currentFragmentIndex = 0;

    private int imageViewSetting = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
        initFragment();
        initNav();
        AvaterControl control = new AvaterControl();
    }

    private void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(" ");

        initDate();

        visbleAnimator=ObjectAnimator.ofFloat(dateLayout,"alpha",0f,1f).setDuration(500);
        visbleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dateAnimatorFlag=true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dateAnimatorFlag=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        inVisbleAnimator=ObjectAnimator.ofFloat(dateLayout,"alpha",1f,0f).setDuration(500);
        inVisbleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dateAnimatorFlag=true;

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dateAnimatorFlag=false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        setCitesName();
        setImageListener();
        setAppbarListener();

    }

    private void initDate(){

        month.setText((Calendar.getInstance().get(Calendar.MONTH)+1)+"月");
        day.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"日");
    }


    private void setAppbarListener(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayoutListener() {
            @Override
            public void onStateChange(int state) {
                if (dateAnimatorFlag){
                    return;
                }
                if (state==EXPANDED){
                    inVisbleAnimator.start();
                }else if (state==COLLAPSED){
                    visbleAnimator.start();

                }
            }
        });

    }

    private void initFragment() {
        fragments.clear();

        BaseWeatherFragment fragment1 = MainInfoFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        BaseWeatherFragment fragment2 = ChartFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        BaseWeatherFragment fragment3 = OtherFragment.newInstance(
                MyPreferences.getInstance().getCityName1(),
                MyPreferences.getInstance().getCityName2());
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment1).commit();
    }

    /**
     * 初始化导航栏
     */
    private void initNav() {
        //设置导航栏选中listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.menu_item_main:
                        //主页
                        showFragment(0);
                        return true;
                    case R.id.menu_item_chart:
                        //图表页
                        showFragment(1);
                        return true;
                    case R.id.menu_item_other:
                        //其他页
                        showFragment(2);
                        return true;
                    case R.id.menu_item_setting:
                        //打开设置
                        startActivity(SettingsActivity.class);
                        return true;
                    case R.id.menu_item_change_main_image:
                        //修改主页图片
                        sentOpenImageIntent(Config.CODE_MAIN_IMAGE);
                        return true;
                    case R.id.menu_item_exit:
                        //退出
                        finish();
                        return true;
                    case R.id.menu_test:
                        startActivity(TestActivity.class);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * 读取配置中城市,并且设置
     */
    private void setCitesName() {
        leftCityName.setText(MyPreferences.getInstance().getCityName1());
        rightCityName.setText(MyPreferences.getInstance().getCityName2());

    }

    /**
     * 头像框长按监听(换头像功能)
     */
    private void setImageListener() {
        rightAvatar.setOnLongClickListener(this);
        leftAvatar.setOnLongClickListener(this);
    }

    /**
     * 显示对应编号fragment
     * @param i 编号
     */
    private void showFragment(int i) {
        if (i >= fragments.size()) {
            return;
        }
        if (i != currentFragmentIndex) {
            Fragment from = fragments.get(currentFragmentIndex);
            Fragment to = fragments.get(i);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setCustomAnimations();
            if (to.isAdded()) {
                //如果已经添加,则隐藏当前,显示对应fragment即可
                transaction.hide(from).show(to).commit();
            } else {
                //如果fragment没有添加,则隐藏当前,添加fragment即可
                transaction.add(R.id.fl_content, to).hide(from).commit();
            }

        }

        currentFragmentIndex = i;


    }

    /**
     * 选择图片intent
     * @param code c
     */
    private void sentOpenImageIntent(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("crop", true);
//        intent.putExtra("return-data", true);
        startActivityForResult(intent, code);
    }

    @Override
    public boolean onLongClick(View v) {
        int code = 0;
        switch (v.getId()) {
            case R.id.ci_left:
                //点击左侧头像
                code = Config.CODE_LEFT_IMAGE;
                break;

            case R.id.ci_right:
                //点击右侧头像
                code = Config.CODE_RIGHT_IMAGE;
                break;
            default:
                break;
        }
        sentOpenImageIntent(code);
        return true;
    }

    /**
     * intent回调
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data intent数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Config.CODE_MAIN_IMAGE:
                    //修改大图
                    imageViewSetting = requestCode;
                    zoomImage(data.getData(), 0, 0);
                    break;
                case Config.CODE_LEFT_IMAGE:
                case Config.CODE_RIGHT_IMAGE:
                    //要执行个缩放,必须记录下是那个view请求的改变图片
                    imageViewSetting = requestCode;
                    zoomImage(data.getData(), 1, 1);
                    break;
                case Config.CODE_ZOOM_IMAGE:
                    //缩放过后
                    setImageToView(Util.getBitmapFromUri(imageUri));
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }

        } else {
            Snackbar.make(toolbar, "选择图片出错!", Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * 裁剪图片intent
     * @param uri 图片uri
     * @param w 宽
     * @param h 高
     */
    private void zoomImage(Uri uri, int w, int h) {
        imageUri=uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", w);
        intent.putExtra("aspectY", h);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", w * 100);
        intent.putExtra("outputY", h * 100);
      //  intent.putExtra("scale", true);
        //注意,请设置成false,因为IBinder传输最大40K,如果图片大于这个,会出错
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection",true);

        startActivityForResult(intent, Config.CODE_ZOOM_IMAGE);
    }

    private void setImageToView(Bitmap bitmapFromUri) {
        switch (imageViewSetting) {
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
        imageViewSetting = 0;
    }

    /**
     * 返回键按下
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //先收回导航栏
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 控制头像显示的内部类{@link SuggestionChangeAction}
     */
    public class AvaterControl {

        public AvaterControl() {
            EventBus.getDefault().register(this);

        }

        @Subscribe
        public void alterAvater(SuggestionChangeAction a) {
            showAvater(a.getIndex());
        }

        public void showAvater(int n) {

            switch (n) {
                case Config.LEFT:
                    leftAvatar.animate().alpha(1).setDuration(500).start();
                    leftCityName.animate().alpha(1).setDuration(500).start();
                    rightAvatar.animate().alpha(0).setDuration(500).start();
                    rightCityName.animate().alpha(0).setDuration(500).start();
                    break;
                case Config.RIGHT:
                    leftAvatar.animate().alpha(0).setDuration(500).start();
                    leftCityName.animate().alpha(0).setDuration(500).start();
                    rightAvatar.animate().alpha(1).setDuration(500).start();
                    rightCityName.animate().alpha(1).setDuration(500).start();
                    break;
                default:
                    leftAvatar.animate().alpha(1).setDuration(500).start();
                    leftCityName.animate().alpha(1).setDuration(500).start();
                    rightAvatar.animate().alpha(1).setDuration(500).start();
                    rightCityName.animate().alpha(1).setDuration(500).start();
            }
        }
    }

}
