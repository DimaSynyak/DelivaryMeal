package com.dmitriy.sinyak.delivarymeal.app.activity.main;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesTitle;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IActivity {

    private SlidingMenuConfig slidingMenuConfig;
    private Language language;
    private Restaurant restaurant;
    private CustomViewAbove customViewAbove;
    private Fragment languagesFragment1;
    private int languageContainerId;

    public static final String RESTAURANTS_URL_RU = "http://www.menu24.ee/ru/restaurant/";
    public static final String RESTAURANTS_URL_EE = "http://www.menu24.ee/restaurant/";
    public static final String RESTAURANTS_URL_EN = "http://www.menu24.ee/en/restaurant/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);

        /*INIT LANGUAGE*/
        languageContainerId = R.id.languageContainer;
        language = new Language(this);
        language.setLanguages(Languages.EN);
        language.init();
        /*END INIT LANGUAGE*/

//        slidingMenuConfig = new SlidingMenuConfig(this);
//        slidingMenuConfig.initSlidingMenu();
//        customViewAbove = CustomViewAbove.customViewAbove;
//
//        restaurant = new Restaurant(this);
//        restaurant.init();

        new MainService().execute(RESTAURANTS_URL_RU);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        slidingMenuConfig.onClickDp(v.getId());
        switch (v.getId()){
            case R.id.menuClick:{
                if (customViewAbove.getCurrentItem() == 1){
                    customViewAbove.setCurrentItem(0);
                }
                else {
                    customViewAbove.setCurrentItem(1);
                }
                break;
            }
            case R.id.imageView3:{
                if (customViewAbove.getCurrentItem() == 0){
                    customViewAbove.setCurrentItem(1);
                }
                break;
            }
        }
    }

    public CustomViewAbove getCustomViewAbove() {
        return customViewAbove;
    }

    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.customViewAbove = customViewAbove;
    }

//    public LanguagesTitle getLanguagesFragment() {
//        return languagesFragment;
//    }
//
//    public void setLanguagesFragment(LanguagesTitle languagesFragment) {
//        this.languagesFragment = languagesFragment;
//    }

    public int getLanguageContainerId() {
        return languageContainerId;
    }

    public void setLanguageContainerId(int languageContainerId) {
        this.languageContainerId = languageContainerId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        language.getLanguagesImg().updateLanguage();
    }
    private class MainService extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            slidingMenuConfig = new SlidingMenuConfig(MainActivity.this);
            slidingMenuConfig.initSlidingMenu();
            customViewAbove = CustomViewAbove.customViewAbove;

            restaurant = new Restaurant(MainActivity.this);
            restaurant.init();
        }
    }
}
