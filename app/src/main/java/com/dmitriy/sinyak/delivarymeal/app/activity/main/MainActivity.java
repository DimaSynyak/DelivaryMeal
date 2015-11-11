package com.dmitriy.sinyak.delivarymeal.app.activity.main;

import android.app.ActionBar;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.CountThread;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.MainAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadBarFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IActivity {

    private Language language;
    private RestaurantBody restaurantBody;
    private CustomViewAbove customViewAbove;
    private int languageContainerId;

    private LoadBarFragment loadBarFragment;
    private LoadPageFragment loadPageFragment;
    private LoadPercent loadPercent;

    private static ChangeLocale changeLocale;

    private FragmentTransaction ft;
    private android.support.v7.app.ActionBar actionBar;
    private TextView staticText;
    private TextView dynamicTextView;
    private  boolean bError = false;

    private MainActivity mainActivity;
    private boolean stateBundle;
    private Bundle bundle;
    private SlidingMenuConfig slidingMenuConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundle = savedInstanceState;
        super.onCreate(savedInstanceState);
//        if (RestaurantList.getRestaurants() != null){
//            RestaurantList.getRestaurants().clear();
//        }

        String local = Locale.getDefault().getLanguage();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);


        /*INIT LANGUAGE*/
        languageContainerId = R.id.languageContainer;
        language = new Language(this);
        language.setLanguageString(local);
        language.init();
        /*END INIT LANGUAGE*/

        actionBar = getSupportActionBar();
        actionBar.hide();

        loadBarFragment = new LoadBarFragment();
        loadPageFragment = new LoadPageFragment();

        mainActivity = this;

        new MainAsyncTask(this).execute(language.getURL());
        customViewAbove = CustomViewAbove.customViewAbove;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

        if (slidingMenuConfig != null)
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

    public int getLanguageContainerId() {
        return languageContainerId;
    }

    public void setLanguageContainerId(int languageContainerId) {
        this.languageContainerId = languageContainerId;
    }

    @Override
    public void changeLanguage(Languages languages) {
        if (language.getLanguages() == languages) {
            return;
        }

        Fragment iconFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.languagesFrame);
        MainActivity.ChangeLocale changeLocale = null;
        Locale myLocale = null;

        switch (languages){
            case RU:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_RU);
                break;
            }
            case EE:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ee);
                myLocale = new Locale("et");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_EE);
                break;
            }
            case EN:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_en);
                myLocale = new Locale("en");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_EN);
                break;
            }
            default: return;
        }

        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        language.setLanguages(languages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle != null) {
            language.getLanguagesImg().updateLanguage();
        }
    }

/*
* AsyncTask for change locale
* */

    public class ChangeLocale extends AsyncTask<String, Void, String>{

        private FragmentTransaction ft;
        private Thread th;
        private Fragment fragment;
        private Count count;
        private Thread countThread;
        private CountThread countThreadR;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            List<Restaurant> restaurants = RestaurantList.getRestaurants();
            if (restaurants != null && restaurants.size() > 0) {
                ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                for (Restaurant restaurant : RestaurantList.getRestaurants()) {
                    ft.remove(restaurant.getFragment());
                }
                ft.commit();

                restaurants.clear();
            }


            count = new Count(5);
            loadPercent = new LoadPercent(count);
            loadPercent.start();
        }

        @Override
        protected String doInBackground(String... params) {

            synchronized (count){
                while (!count.isStateLoadFragment()){
                    try {
                       count.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            dynamicTextView = (TextView) loadPageFragment.getView().findViewById(R.id.dynamicText);

            countThreadR = new CountThread(mainActivity, count, dynamicTextView);
            countThread = new Thread(countThreadR);
            countThread.start();

            while (true) {
                Document doc = null;
                try {
                    count.complete();
                    doc = Jsoup.connect(params[0]).get();
                    count.complete();
                    bError = false;
                    Elements elements = doc.getElementsByClass("food-item");

                    if (elements.size() == 0)
                        return null; //WARNING change (pick out) ui
                    count.complete();

                    for (Element element : elements) {

                        Restaurant restaurant = new Restaurant();

                        restaurant.setCostMeal(element.getElementsByClass("and-cost-mil").get(0).html());
                        restaurant.setCostDeliver(element.getElementsByClass("and-cost-deliver").get(0).html());
                        restaurant.setTimeDeliver(element.getElementsByClass("and-time-deliver").get(0).html());

                        restaurant.setCostMealStatic(getResources().getString(R.string.min_cost_order));
                        restaurant.setCostDeliverStatic(getResources().getString(R.string.cost_deliver));
                        restaurant.setTimeDeliverStatic(getResources().getString(R.string.time_deliver));

                        restaurant.setImgSRC(element.getElementsByTag("img").get(0).attr("src"));
                        restaurant.setName(element.getElementsByClass("and-name").html());
                        restaurant.setProfile(element.getElementsByClass("and-profile").html());
                        restaurant.setStars(element.getElementsByClass("star").get(0).getElementsByTag("span").attr("style"));
                        restaurant.setMenuLink(element.getElementsByClass("food-img").get(0).getElementsByTag("a").attr("href"));

                        URL imgURL = new URL(restaurant.getImgSRC());
                        restaurant.setImgBitmap(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                        RestaurantList.addRestaurant(restaurant);
                    }
                    count.complete();
                    count.complete();
                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                } catch (IOException e) {
                    bError = true;

                    try {
                        TimeUnit.MILLISECONDS.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ft = mainActivity.getSupportFragmentManager().beginTransaction();
            ft.remove(loadPageFragment);
            ft.commit();

            restaurantBody = RestaurantBody.getInstance(MainActivity.this);
            restaurantBody.init();
        }
    }

    /*
    * LOAD PERCENT CLASS
    * */

    private class LoadPercent {
        private Thread th;
        private Count count;


        public LoadPercent(final Count count) {
            this.count = count;

            this.th = new Thread(new Runnable() {
                @Override
                public void run() {             /*load first ui to activity*/
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.languageContainer, loadPageFragment);
                        ft.commit();

                        while (!loadPageFragment.isAdded()){
                            TimeUnit.MILLISECONDS.sleep(100);
                        }
                        synchronized (count) {
                            count.setStateLoadFragment(true);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void start(){
            th.start();
        }
    }

    public LoadBarFragment getLoadBarFragment() {
        return loadBarFragment;
    }

    public TextView getDynamicTextView() {
        return dynamicTextView;
    }

    public ChangeLocale getChangeLocale(){
        changeLocale = new ChangeLocale();
        return changeLocale;
    }

    public android.support.v7.app.ActionBar getActionBarActivity() {
        return actionBar;
    }

    public RestaurantBody getRestaurantBody() {
        return restaurantBody;
    }

    public SlidingMenuConfig getSlidingMenuConfig() {
        return slidingMenuConfig;
    }

    public void setSlidingMenuConfig(SlidingMenuConfig slidingMenuConfig) {
        this.slidingMenuConfig = slidingMenuConfig;
    }
}
