package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesTitle;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniHeadFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head.RestaurantMiniMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

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

    private RestaurantMealFragment mealFragment1;
    private RestaurantMealFragment mealFragment2;
    private RestaurantMealFragment mealFragment3;
    private RestaurantMealFragment mealFragment4;
    private RestaurantMealFragment mealFragment5;


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

        slidingMenuConfig = new SlidingMenuConfig(this);
        slidingMenuConfig.initSlidingMenu();
        customViewAbove = CustomViewAbove.customViewAbove;

        initFragment();
        scrollInit();
        restaurantHeadContainer = (FrameLayout) findViewById(R.id.restaurantHeadContainer);
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
        }
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

    private void initFragment(){
        restaurantHeadFragment = new RestaurantHeadFragment();
        restaurantMiniHeadFragment = new RestaurantMiniHeadFragment();
        restaurantMiniMenuFragment = new RestaurantMiniMenuFragment();

        mealFragment1 = new RestaurantMealFragment();
        mealFragment2 = new RestaurantMealFragment();
        mealFragment3 = new RestaurantMealFragment();
        mealFragment4 = new RestaurantMealFragment();
        mealFragment5 = new RestaurantMealFragment();

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
        ft.add(R.id.restaurantMenuContainer, mealFragment1);
        ft.add(R.id.restaurantMenuContainer, mealFragment2);
        ft.add(R.id.restaurantMenuContainer, mealFragment3);
        ft.add(R.id.restaurantMenuContainer, mealFragment4);
        ft.add(R.id.restaurantMenuContainer, mealFragment5);
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
                        if (scrollY < -100 && !firstFlag){
                            firstFlag = true;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantMiniHeadFragment);
                            ft.commit();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                            ft.commit();
                        }else
                        if (scrollY > 300 && firstFlag){
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


}
