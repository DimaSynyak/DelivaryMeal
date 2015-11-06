package com.dmitriy.sinyak.delivarymeal.app.activity.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments.RestaurantFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantBody {

    private AppCompatActivity activity;
    private FragmentTransaction ft;

    public RestaurantBody(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void init(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (Restaurant restaurant: RestaurantList.getRestaurants()) {
            ft.add(R.id.restaurants_list, new RestaurantFragment(activity, restaurant));
        }
        ft.commit();
    }
}
