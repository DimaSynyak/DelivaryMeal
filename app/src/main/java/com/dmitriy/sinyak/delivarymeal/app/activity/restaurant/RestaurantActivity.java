package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.app.ActionBar;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.ChangeLanguageAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.CountThread;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.LoadPercent;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniMenuFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.tools.Tools;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener, IActivity {
    private SlidingMenuConfig slidingMenuConfig;
    private Language language;
    private CustomViewAbove customViewAbove;
    private RestaurantHeadFragment restaurantHeadFragment;
    private RestaurantMiniHeadFragment restaurantMiniHeadFragment;
    private RestaurantMiniMenuFragment restaurantMiniMenuFragment;
    private ScrollView scrollView;
    private FrameLayout restaurantHeadContainer;
    private int languageContainerId;
    private static int MIN_SCROLLY = -100;
    private static int MAX_SCROLLY = 300;
    private RestaurantActivity restaurantActivity;
    private MealBody mealBody;
    private Restaurant restaurant;

    private LoadPageFragment loadPageFragment;
    private LoadPercent loadPercent;
    private TextView dynamicTextView;

    private TextView restaurantTitle;
    private int positionRestaurant;


    private boolean firstFlag = true;

    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().setCustomView(R.layout.title);

         /*INIT LANGUAGE*/
        languageContainerId = R.id.restaurantLanguageContainer;
        language = new Language(this);
        language.init();
        /*END INIT LANGUAGE*/

        restaurantActivity = this;
        loadPageFragment = new LoadPageFragment();

        restaurantHeadContainer = (FrameLayout) findViewById(R.id.restaurantHeadContainer);

        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        restaurant = RestaurantList.getRestaurants().get((Integer) getIntent().getSerializableExtra("restaurant"));
        positionRestaurant = RestaurantList.getRestaurants().indexOf(restaurant);

        new MainService().execute(restaurant.getMenuLink());

        initFragment(restaurant);
        scrollInit();
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
                finish();
                break;
            }
            case R.id.fullRestaurantImg:{
                ft = getSupportFragmentManager().beginTransaction();
                ft.remove(restaurantMiniHeadFragment);
                ft.commit();
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                ft.commit();
                firstFlag = true;
                break;
            }
        }
    }


    private void initFragment(Restaurant restaurant){
        restaurantHeadFragment = new RestaurantHeadFragment(restaurant);
        restaurantMiniHeadFragment = new RestaurantMiniHeadFragment(restaurant);
        restaurantMiniMenuFragment = new RestaurantMiniMenuFragment(restaurant);
        restaurantMiniHeadFragment.setRestaurantHeadFragment(restaurantHeadFragment);

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);

        ft.commit();
    }

    public void changeFragment(Restaurant restaurant){
        restaurantHeadFragment.setRestaurant(restaurant);
        restaurantMiniHeadFragment.setRestaurant(restaurant);
        restaurantMiniMenuFragment.setRestaurant(restaurant);

        ft = getSupportFragmentManager().beginTransaction();

        if (restaurantHeadFragment.isAdded()){
            ft.replace(R.id.restaurantHeadContainer, restaurantHeadFragment);
        }
        else if (restaurantMiniHeadFragment.isAdded()){
            ft.replace(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
        }

        ft.commit();
    }

    public void scrollInit() {
        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int scrollY = v.getScrollY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_SCROLL:
                    case MotionEvent.ACTION_MOVE:
                        if (scrollView.getScrollY() < MIN_SCROLLY && !firstFlag) {
                            firstFlag = true;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantMiniHeadFragment);
                            ft.commit();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                            ft.commit();
                        } else if (scrollView.getScrollY() > MAX_SCROLLY && firstFlag) {
                            firstFlag = false;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantHeadFragment);
                            ft.commit();

                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
                            ft.commit();
                        }
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.customViewAbove = customViewAbove;
    }

    @Override
    public CustomViewAbove getCustomViewAbove() {
        return this.customViewAbove;
    }

    @Override
    public int getLanguageContainerId() {
        return languageContainerId;
    }

    @Override
    public void setLanguageContainerId(int languageContainerId) {
        this.languageContainerId = languageContainerId;
    }

    /*
    * MAIN SERVICE ASYNC TASK
    * */

    private class MainService extends AsyncTask<String, Void, String> {
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
            loadPercent = new LoadPercent(count, restaurantActivity);
            loadPercent.start();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                synchronized (count){
                    while (!count.isStateLoadFragment()){
                        count.wait(100);
                    }
                }

                dynamicTextView = (TextView) loadPageFragment.getView().findViewById(R.id.dynamicText);

                countThreadR = new CountThread(restaurantActivity, count, dynamicTextView);
                countThread = new Thread(countThreadR);
                countThread.start();

                Document doc = null;

                count.complete();
                doc = Jsoup.connect(params[0]).get();
                count.complete();
                Elements elements = doc.getElementsByClass("item-food");

                if (elements.size() == 0)
                    return null; //WARNING change (pick out) ui
                count.complete();
                for (Element element :elements) {

                    Meal meal = new Meal();

                    meal.setName(element.getElementsByClass("and-name").get(0).html());
                    meal.setComposition(element.getElementsByClass("and-composition").get(0).html());
                    meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
                    meal.setCost(element.getElementsByClass("as").get(0).html());
                    meal.setImgURL(element.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                    URL imgURL = new URL(meal.getImgURL());
                    meal.setImg(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                    MealList.addMeal(meal);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ft = restaurantActivity.getSupportFragmentManager().beginTransaction();
            ft.remove(loadPageFragment);
            ft.commit();

            slidingMenuConfig = new SlidingMenuConfig(RestaurantActivity.this);
            slidingMenuConfig.initSlidingMenu();
            customViewAbove = CustomViewAbove.customViewAbove;

            mealBody = new MealBody(restaurantActivity);
            mealBody.init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MealList.getMeals().clear();
    }


    /*
* AsyncTask for change locale
* */

//    public class ChangeLocale extends AsyncTask<String, Void, String>{
//
//        private FragmentTransaction ft;
//        private Thread th;
//        private Fragment fragment;
//        private Count count;
//        private Thread countThread;
//        private CountThread countThreadR;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            List<Meal> meals = MealList.getMeals();
//            if (meals != null && meals.size() > 0) {
//                ft = RestaurantActivity.this.getSupportFragmentManager().beginTransaction();
//                for (Meal meal : MealList.getMeals()) {
//                    ft.remove(meal.getFragment());
//                }
//                ft.commit();
//
//                meals.clear();
//            }
//
//            count = new Count(5);
//            loadPercent = new LoadPercent(count, restaurantActivity);
//            loadPercent.start();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                synchronized (count){
//                    while (!count.isStateLoadFragment()){
//                        count.wait(100);
//                    }
//                }
//
//                dynamicTextView = (TextView) loadPageFragment.getView().findViewById(R.id.dynamicText);
//
//                countThreadR = new CountThread(restaurantActivity, count, dynamicTextView);
//                countThread = new Thread(countThreadR);
//                countThread.start();
//
//                Document doc = null;
//
//                count.complete();
//                doc = Jsoup.connect(params[0]).get();
//                count.complete();
//                Elements elements = doc.getElementsByClass("item-food");
//
//                if (elements.size() == 0)
//                    return null; //WARNING change (pick out) ui
//                count.complete();
//                for (Element element :elements) {
//
//                    Meal meal = new Meal();
//
//                    meal.setName(element.getElementsByClass("and-name").get(0).html());
//                    meal.setComposition(element.getElementsByClass("and-composition").get(0).html());
//                    meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
//                    meal.setCost(element.getElementsByClass("as").get(0).html());
//                    meal.setImgURL(element.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));
//
//                    URL imgURL = new URL(meal.getImgURL());
//                    meal.setImg(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
//                    MealList.addMeal(meal);
//                }
//                count.complete();
//                count.complete();
//                synchronized (count) {
//                    while (!count.isStateData()) {
//                        count.wait(100);
//                    }
//                }
//                return null;
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            ft = restaurantActivity.getSupportFragmentManager().beginTransaction();
//            ft.remove(loadPageFragment);
//            ft.commit();
//
//            mealBody = new MealBody(restaurantActivity);
//            mealBody.init();
//        }
//    }


//    public ChangeLocale getChangeLocale(){
//        changeLocale = new ChangeLocale();
//        return changeLocale;
//    }

    @Override
    public void changeLanguage(Languages languages) {
        if (language.getLanguages() == languages) {
            return;
        }

        Fragment iconFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.languagesFrame);
        ChangeLanguageAsyncTask changeLocale = new ChangeLanguageAsyncTask(this);
        Locale myLocale = null;
        Tools tools = Tools.getInstance();

        switch (languages){
            case RU:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
                changeLocale.execute(language.getURL());
                break;
            }
            case EE:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ee);
                myLocale = new Locale("et");
                changeLocale.execute(language.getURL());
                break;
            }
            case EN:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_en);
                myLocale = new Locale("en");
                changeLocale.execute(language.getURL());
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

    public int getPositionRestaurant() {
        return positionRestaurant;
    }

    public void setPositionRestaurant(int positionRestaurant) {
        this.positionRestaurant = positionRestaurant;
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }
}
