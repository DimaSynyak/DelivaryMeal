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
    public static RestaurantBody restaurantBody;
    private RestaurantList restaurantList;

    public RestaurantBody(AppCompatActivity activity) {
        this.activity = activity;
        restaurantBody = this;
        restaurantList = RestaurantList.getInstance();
    }

    public void init(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (Restaurant restaurant: restaurantList.getRestaurants()) {
            ft.add(R.id.restaurants_list, new RestaurantFragment(activity, restaurant));
        }
        ft.commit();
    }

    public void replace(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (Restaurant restaurant: restaurantList.getRestaurants()) {
            ft.replace(R.id.restaurants_list, restaurant.getFragment());
        }
        ft.commit();
    }

    public static RestaurantBody getInstance(AppCompatActivity activity){
        if (restaurantBody == null){
            restaurantBody = new RestaurantBody(activity);
        }

        return restaurantBody;
    }
}
