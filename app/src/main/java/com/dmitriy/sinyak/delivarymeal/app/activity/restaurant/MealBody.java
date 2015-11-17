package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;

/**
 * Created by 1 on 02.11.2015.
 */
public class MealBody {

    private AppCompatActivity activity;
    private FragmentTransaction ft;
    private int countID = 1;

    public MealBody(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void init(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (Meal meal: MealList.getMeals()) {
            meal.setId(countID++);
            ft.add(R.id.restaurantMenuContainer,  new RestaurantMealFragment(meal));
        }
        ft.commit();
    }
}
