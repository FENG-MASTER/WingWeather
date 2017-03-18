package com.wingweather.qianzise.wingweather.activity;



import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.base.Config;


public class SettingsActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getFragmentManager().beginTransaction().
                replace(R.id.fl_setting,new SettingFragment()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
}
