package com.newren.smartprotecter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.newren.smartprotecter.R;
import com.newren.smartprotecter.fragment.FragmentAccident;
import com.newren.smartprotecter.fragment.FragmentMe;
import com.newren.smartprotecter.fragment.FragmentPublishment;
import com.newren.smartprotecter.fragment.FragmentSetting;


public class MainActivity extends FragmentActivity {
    Fragment accident = null;
    Fragment publishment = null;
    Fragment me = null;
    Fragment setting = null;
    RadioGroup myTabRg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        accident = new FragmentAccident();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accident).commit();
        myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
        myTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbAccident:
                        accident = new FragmentAccident();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accident)
                                .commit();
                        break;
                    case R.id.rbPublish:
                        if (publishment == null) {
                            publishment = new FragmentPublishment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, publishment).commit();
                        break;
                    case R.id.rbMe:
                        me = new FragmentMe();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, me)
                                .commit();
                        break;
                    case R.id.rbSetting:
                        setting = new FragmentSetting();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, setting)
                                .commit();
                        break;
                    default:
                        break;
                }

            }
        });
    }

}