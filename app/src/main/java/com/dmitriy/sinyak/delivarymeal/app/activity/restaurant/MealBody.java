package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.UploadPageAsyncTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 02.11.2015.
 */
public class MealBody {

    public static final Integer COUNT_FOOD = 10;
    private static Integer count_food;
    private static Integer count_food2;
    private AppCompatActivity activity;
    private FragmentTransaction ft;

    private static MealBody mealBody;

    public MealBody(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static MealBody getInstance(AppCompatActivity activity){
        if (mealBody == null){
            mealBody = new MealBody(activity);
        }

        return mealBody;
    }

    public void init(){

        count_food = COUNT_FOOD;
        count_food2 = COUNT_FOOD*2;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meal> meals = MealList.getMeals();
                for (int i = 0; i < COUNT_FOOD; i++) {
                    ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.restaurantMenuContainer,  new RestaurantMealFragment(meals.get(i)));
                    ft.commit();
                }

                count_food = count_food2 - 1;
                new UploadPageAsyncTask(activity).execute();
            }
        }).start();

    }

    public void update(final Boolean[] threadRunState){
        final List<Meal> meals = MealList.getMeals();

        synchronized (count_food) {
            if (count_food2 > meals.size()) {
                count_food2 = meals.size();
            }

            if (count_food >= meals.size()){
                return;
            }
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (count_food) {
                    ft = activity.getSupportFragmentManager().beginTransaction();

                    RestaurantMealFragment restaurantMealFragment = null;
                    for (int i = count_food - 1; i < count_food2; i++) {
                        restaurantMealFragment = new RestaurantMealFragment(meals.get(i));
                        ft.add(R.id.restaurantMenuContainer, restaurantMealFragment);
                    }

                    ft.commit();
                    while (!restaurantMealFragment.isAdded());
                    count_food += 1;
                    count_food2 += 1;
                }

                synchronized (threadRunState){
                    threadRunState[0] = false;
                }
            }
        }).start();
    }
}
