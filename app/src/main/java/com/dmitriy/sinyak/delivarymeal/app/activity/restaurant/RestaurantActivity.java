package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.PaymentActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniMenuFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.ChangeLanguageAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.RestaurantAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.tools.Tools;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

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
    private Restaurant restaurant;
    private RestaurantAsyncTask restaurantAsyncTask;
    private Languages oldLanguage;

    private int positionRestaurant;
    private ChangeLanguageAsyncTask changeLocale;
    private DisplayMetrics metrics;
    private ImageView garbageButton;
    private TextView garbageNum;


    private boolean firstFlag = true;

    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().setCustomView(R.layout.title);

        Typeface geometric = Typeface.createFromAsset(getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getAssets(), "fonts/arimo/Arimo_Regular.ttf");

         /*INIT LANGUAGE*/
        languageContainerId = R.id.restaurantLanguageContainer;
        language = new Language(this);
        language.init();
        /*END INIT LANGUAGE*/

        restaurantActivity = this;
        Garbage.getInstance().setActivity(this);

        restaurantHeadContainer = (FrameLayout) findViewById(R.id.restaurantHeadContainer);

        garbageButton = (ImageView) findViewById(R.id.garbageButton);
        garbageNum = (TextView) findViewById(R.id.garbageNum);

        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        restaurant = RestaurantList.getRestaurants().get((Integer) getIntent().getSerializableExtra("restaurant"));
        positionRestaurant = RestaurantList.getRestaurants().indexOf(restaurant);

        restaurantAsyncTask = null;
        restaurantAsyncTask = new RestaurantAsyncTask(this);
        restaurantAsyncTask.execute(restaurant.getMenuLink());

        initFragment(restaurant);
        scrollInit();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        garbageNum = (TextView) findViewById(R.id.garbageNum);
        garbageNum.setTypeface(geometric);
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
                if (changeLocale != null){
                   if (!changeLocale.isCancelled())
                       return;
                }

                if (restaurantAsyncTask != null){
                    restaurantAsyncTask.getLoadPageFragment().getThread().interrupt();
                    restaurantAsyncTask.cancel(true);
                    restaurantAsyncTask = null;
                }

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
            case R.id.garbageButton:{
                Intent intent = new Intent(this, PaymentActivity.class);
//                intent.putExtra("language", String.valueOf(this));
//                intent.putExtra("restaurant", RestaurantList.getRestaurants().indexOf(restaurant));
                startActivity(intent);
                break;
            }
        }
    }

    public void initFragment(Restaurant restaurant){
        restaurantHeadFragment = new RestaurantHeadFragment(restaurant);
        restaurantMiniHeadFragment = new RestaurantMiniHeadFragment(restaurant);
        restaurantMiniMenuFragment = new RestaurantMiniMenuFragment(restaurant);
        restaurantMiniHeadFragment.setRestaurantHeadFragment(restaurantHeadFragment);

        ft = getSupportFragmentManager().beginTransaction();
        if (firstFlag) {
            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
        }
        else {
            ft.add(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
        }

        ft.commit();
    }

    public void removeFragment(){
        restaurantHeadFragment.setRestaurant(restaurant);
        restaurantMiniHeadFragment.setRestaurant(restaurant);
        restaurantMiniMenuFragment.setRestaurant(restaurant);

        ft = getSupportFragmentManager().beginTransaction();

        if (restaurantHeadFragment.isAdded()){
            ft.remove(restaurantHeadFragment);
        }
        else if (restaurantMiniHeadFragment.isAdded()){
            ft.remove(restaurantMiniHeadFragment);
        }

        ft.commit();
    }

    public void scrollInit() {
        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

                            ((MarginLayoutParams)scrollView.getLayoutParams()).setMargins(0, (int) (180*metrics.density),0,0);

                        } else if (scrollView.getScrollY() > MAX_SCROLLY && firstFlag) {
                            firstFlag = false;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantHeadFragment);
                            ft.commit();

                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
                            ft.commit();
                            ((MarginLayoutParams)scrollView.getLayoutParams()).setMargins(0, (int) (50*metrics.density), 0, 0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MealList.getMeals().clear();
    }

    @Override
    public void changeLanguage(Languages languages) {
        if (language.getLanguages() == languages) {
            return;
        }

        if (changeLocale != null){
            if (!changeLocale.isCancelled())
                return;
        }

        if (restaurantAsyncTask != null){
            if (!restaurantAsyncTask.isCancelled()){
                return;
            }
        }

        Fragment iconFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.languagesFrame);
        changeLocale = new ChangeLanguageAsyncTask(this);
        Locale myLocale = null;
        Tools tools = Tools.getInstance();

        oldLanguage = language.getLanguages();
        language.setLanguages(languages);

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
    }

    public SlidingMenuConfig getSlidingMenuConfig() {
        return slidingMenuConfig;
    }

    public void setSlidingMenuConfig(SlidingMenuConfig slidingMenuConfig) {
        this.slidingMenuConfig = slidingMenuConfig;
    }
}
