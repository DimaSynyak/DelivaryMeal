package com.dmitriy.sinyak.delivarymeal.app.activity.main;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.CountThread;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadBarFragment;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IActivity {

    private SlidingMenuConfig slidingMenuConfig;
    private Language language;
    private RestaurantBody restaurantBody;
    private CustomViewAbove customViewAbove;
    private Fragment languagesFragment1;
    private int languageContainerId;
    private LoadBarFragment loadBarFragment;
    private FragmentTransaction ft;
    private LoadPage loadPage;
    private android.support.v7.app.ActionBar actionBar;
    private TextView staticText;
    private TextView dynamicTextView;
    private  boolean bError = false;

    private MainActivity mainActivity;

    public static final String RESTAURANTS_URL_RU = "http://www.menu24.ee/ru/restaurant/";
    public static final String RESTAURANTS_URL_EE = "http://www.menu24.ee/restaurant/";
    public static final String RESTAURANTS_URL_EN = "http://www.menu24.ee/en/restaurant/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (RestaurantList.getRestaurants() != null){
//            RestaurantList.getRestaurants().clear();
//        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);


        /*INIT LANGUAGE*/
        languageContainerId = R.id.languageContainer;
        language = new Language(this);
        language.setLanguages(Languages.EN);
        language.init();
        /*END INIT LANGUAGE*/

       actionBar = getSupportActionBar();
        actionBar.hide();
        loadBarFragment = new LoadBarFragment();

        mainActivity = this;

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


    private class MainService extends AsyncTask<String, Void, String>{

        private FragmentTransaction ft;
        private Thread th;
        private Fragment fragment;
        private Count count;
        private Thread countThread;
        private CountThread countThreadR;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            count = new Count(5);
            loadPage = new LoadPage(count);
            loadPage.start();

        }

        @Override
        protected String doInBackground(String... params) {

           while (!count.isStateLoadFragment()){
               try {
                   TimeUnit.MILLISECONDS.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
            dynamicTextView = (TextView) loadBarFragment.getView().findViewById(R.id.dynamicText);

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
            ft.remove(loadBarFragment);
            ft.commit();
            actionBar.show();

            slidingMenuConfig = new SlidingMenuConfig(MainActivity.this);
            slidingMenuConfig.initSlidingMenu();
            customViewAbove = CustomViewAbove.customViewAbove;

            restaurantBody = new RestaurantBody(mainActivity);
            restaurantBody.init();
        }
    }

    private class LoadPage {
        private Thread th;
        private Count count;


        public LoadPage(final Count count) {
            this.count = count;

            this.th = new Thread(new Runnable() {
                @Override
                public void run() {             /*load first ui to activity*/
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.languageContainer, loadBarFragment);
                        ft.commit();

                        while (!loadBarFragment.isAdded()){
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


}
