package com.dmitriy.sinyak.delivarymeal.app.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.config.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.LanguagesTitle;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingMenuConfig slidingMenuConfig;

    private LanguagesTitle languagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);
        languagesFragment = new LanguagesTitle(this);

        slidingMenuConfig = new SlidingMenuConfig(this);
        slidingMenuConfig.initSlidingMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        slidingMenuConfig.onClickDp(v.getId());
        languagesFragment.onClickDp(v.getId());
        switch (v.getId()){
            case R.id.menuClick:{
                if (CustomViewAbove.customViewAbove.getCurrentItem() == 1){
                    CustomViewAbove.customViewAbove.setCurrentItem(0);
                }
                else {
                    CustomViewAbove.customViewAbove.setCurrentItem(1);
                }
                break;
            }
        }
    }
}
