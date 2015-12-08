package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 02.11.2015.
 */
public class MealBody {

    public static final Integer COUNT_FOOD = 10;
    private static Integer count_food;
    private static Integer count_food2;
    private AppCompatActivity activity;
    private FragmentTransaction ft;
    private Thread initThread;
    private List<Thread> updateThreadList;
    private List<Meal> meals;

    private static MealBody mealBody;

    public MealBody(AppCompatActivity activity) {
        this.activity = activity;
        updateThreadList = new ArrayList<>();
    }

    public static MealBody getInstance(AppCompatActivity activity){
        if (mealBody == null){
            mealBody = new MealBody(activity);
        }

        return mealBody;
    }

    public void init(){



       initThread = new Thread(new Runnable() {
            @Override
            public void run() {

                count_food = COUNT_FOOD;
                count_food2 = COUNT_FOOD*2;

               meals = MealList.getMeals();
                ft = activity.getSupportFragmentManager().beginTransaction();

                if (count_food > meals.size())
                    count_food = meals.size();

                for (int i = 0; i < count_food; i++) {
                    ft.add(R.id.restaurantMenuContainer, new RestaurantMealFragment(meals.get(i)));
                }

                ft.commit();

                count_food2 = count_food + 1;
                MealList.startUploadPageAsycTask(activity);
            }
        });
        initThread.start();

    }

    public void update(final Boolean[] threadRunState){
       meals = MealList.getMeals();

        synchronized (count_food) {


            if (count_food >= meals.size()){
                threadRunState[0] = false;
                return;
            }
        }

        Thread updateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (count_food) {

                    if (count_food2 > meals.size()) {
                        count_food2 = meals.size();
                    }

                    ft = activity.getSupportFragmentManager().beginTransaction();

                    RestaurantMealFragment restaurantMealFragment = null;
                    for (int i = count_food; i < count_food2; i++) {
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
        });

        updateThread.start();
//        updateThread.setDaemon(true);
        updateThreadList.add(updateThread);
    }

    public void onDestroy(){
        mealBody = null;

        if (updateThreadList == null)
            return;

        for (Thread thread :updateThreadList){
            if (!thread.isInterrupted()){
                thread.interrupt();
            }
        }


        updateThreadList.clear();
        updateThreadList = null;
        activity = null;
    }

    public void deleteAllFragments(){
        List<Meal> meals = MealList.getMeals();
        if (meals != null && meals.size() > 0) {
            ft =  activity.getSupportFragmentManager().beginTransaction();
            for (Meal meal : MealList.getMeals()) {
                if (meal.getFragment() != null && meal.getFragment().isAdded())
                    ft.remove(meal.getFragment());
            }
            ft.commit();


            MealList.clear();
        }
    }

}
