package com.wingweather.qianzise.wingweather.activity;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.api.Apii;
import com.wingweather.qianzise.wingweather.util.Config;
import com.wingweather.qianzise.wingweather.util.MyPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 设置页面
 */
public class SettingsActivity extends BaseActivity {
    @BindView(R.id.et_setting_city_left)
    EditText left;
    @BindView(R.id.et_setting_city_right)
    EditText right;

    /*城市是否填写正确标志位*/
    private boolean isVilid1 = true;
    private boolean isVilid2 = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getFragmentManager().beginTransaction().
                replace(R.id.fl_setting, new SettingFragment()).commit();
        initEdit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    private void initEdit() {
        left.setText(MyPreferences.getInstance().getCityName1());
        right.setText(MyPreferences.getInstance().getCityName2());


        left.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                /*调用API检查是否有对应的城市*/
                Apii.getInstance().checkCity(s.toString(), aBoolean -> {
                    if (!aBoolean) {
                        left.setError("找不到" + s.toString());
                        isVilid1 = false;
                    } else {
                        MyPreferences.getInstance().setCityName1(s.toString());
                        isVilid1 = true;
                    }
                });

            }
        });

        right.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                Apii.getInstance().checkCity(s.toString(), aBoolean -> {
                    if (!aBoolean) {
                        right.setError("找不到" + s.toString());
                        isVilid2 = false;
                    } else {
                        MyPreferences.getInstance().setCityName2(s.toString());
                        isVilid2 = true;
                    }
                });

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //点击home则返回
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        //只有城市都填写正确,才能返回
        if (isVilid2 && isVilid1) {
            super.onBackPressed();
        }
    }

    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferenc);//读取设置配置
            initColorList();
        }


        /**
         * 使用设置好的颜色
         */
        private void initColorList() {
            ListPreference color1 = (ListPreference) findPreference(Config.KEY_COLOR1);
            ListPreference color2 = (ListPreference) findPreference(Config.KEY_COLOR2);

            color1.setSummary(color1.getEntry());
            color2.setSummary(color2.getEntry());

        }


    }
}
