package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments.RestaurantFragment;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 02.11.2015.
 */
public class Restaurant {
    private RestaurantFragment restaurantFragment1;
    private RestaurantFragment restaurantFragment2;
    private RestaurantFragment restaurantFragment3;
    private RestaurantFragment restaurantFragment4;
    private RestaurantFragment restaurantFragment5;

    private AppCompatActivity activity;
    private FragmentTransaction ft;

    public Restaurant(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void init(){
        restaurantFragment1 = new RestaurantFragment(activity);
        restaurantFragment2 = new RestaurantFragment(activity);
        restaurantFragment3 = new RestaurantFragment(activity);
        restaurantFragment4 = new RestaurantFragment(activity);
        restaurantFragment5 = new RestaurantFragment(activity);

        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurants_list, restaurantFragment1);
        ft.add(R.id.restaurants_list, restaurantFragment2);
        ft.add(R.id.restaurants_list, restaurantFragment3);
        ft.add(R.id.restaurants_list, restaurantFragment4);
        ft.add(R.id.restaurants_list, restaurantFragment5);

        ft.commit();

       Thread th = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   TimeUnit.MILLISECONDS.sleep(100);
                   restaurantFragment1.onClickListener();
                   restaurantFragment2.onClickListener();
                   restaurantFragment3.onClickListener();
                   restaurantFragment4.onClickListener();
                   restaurantFragment5.onClickListener();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });

        th.start();

    }
}
