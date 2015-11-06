package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.app.ActionBar;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.RestaurantBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments.RestaurantFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniMenuFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

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

    private RestaurantMealFragment mealFragment1;
    private RestaurantMealFragment mealFragment2;
    private RestaurantMealFragment mealFragment3;
    private RestaurantMealFragment mealFragment4;
    private RestaurantMealFragment mealFragment5;
    private TextView restaurantTitle;


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

        restaurantHeadContainer = (FrameLayout) findViewById(R.id.restaurantHeadContainer);

        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        restaurant = RestaurantList.getRestaurants().get((Integer) getIntent().getSerializableExtra("restaurant"));
        new MainService().execute(restaurant.getMenuLink());
        initFragment();
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


    private void initFragment(){
        restaurantHeadFragment = new RestaurantHeadFragment(restaurant);
        restaurantMiniHeadFragment = new RestaurantMiniHeadFragment(restaurant);
        restaurantMiniMenuFragment = new RestaurantMiniMenuFragment(restaurant);
        restaurantMiniHeadFragment.setRestaurantHeadFragment(restaurantHeadFragment);

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);

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
                        if (scrollView.getScrollY() < MIN_SCROLLY && !firstFlag){
                            firstFlag = true;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantMiniHeadFragment);
                            ft.commit();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                            ft.commit();
                        }else
                        if (scrollView.getScrollY() > MAX_SCROLLY && firstFlag){
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


    private class MainService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
                Elements elements = doc.getElementsByClass("item-food");

                if (elements.size() == 0)
                    return null; //WARNING change (pick out) ui

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

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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
}
