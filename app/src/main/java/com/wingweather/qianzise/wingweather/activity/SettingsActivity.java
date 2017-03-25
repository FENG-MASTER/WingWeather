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


public class SettingsActivity extends BaseActivity {
    @BindView(R.id.et_setting_city_left)
    EditText left;
    @BindView(R.id.et_setting_city_right)
    EditText right;

    private boolean isVilid1=true;
    private boolean isVilid2=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getFragmentManager().beginTransaction().
                replace(R.id.fl_setting,new SettingFragment()).commit();
        initEdit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    private void initEdit(){
        left.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                Apii.getInstance().checkCity(s.toString(), new Apii.Listener<Boolean>() {
                    @Override
                    public void onReceive(Boolean aBoolean) {
                        if (!aBoolean){
                            left.setError("找不到"+s.toString());
                            isVilid1=false;
                        }else {
                            MyPreferences.getInstance().setCityName1(s.toString());
                            isVilid1=true;
                        }
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
                Apii.getInstance().checkCity(s.toString(), new Apii.Listener<Boolean>() {
                    @Override
                    public void onReceive(Boolean aBoolean) {
                        if (!aBoolean){
                            right.setError("找不到"+s.toString());
                            isVilid2=false;
                        }else {
                            MyPreferences.getInstance().setCityName2(s.toString());
                            isVilid2=true;
                        }
                    }
                });

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }else {
            return super.onOptionsItemSelected(item);

        }
    }

    public static class SettingFragment extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferenc);
            setSummary();
        }

        
        private void setSummary(){
            ListPreference city1= (ListPreference) findPreference(Config.KEY_CITY1);
            ListPreference city2= (ListPreference) findPreference(Config.KEY_CITY2);

            city1.setSummary(city1.getEntry());
            city2.setSummary(city2.getEntry());

        }


    }


    @Override
    public void onBackPressed() {
        if (isVilid2&&isVilid1){
            super.onBackPressed();
        }
    }
}
