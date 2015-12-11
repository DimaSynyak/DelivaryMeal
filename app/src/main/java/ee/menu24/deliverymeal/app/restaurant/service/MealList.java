package ee.menu24.deliverymeal.app.restaurant.service;

import android.support.v7.app.AppCompatActivity;

import ee.menu24.deliverymeal.app.restaurant.thread.UploadPageAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 06.11.2015.
 */
public class MealList {
    private static List<Meal> meals;
    private static boolean mealListCompleteFlag = true;
    private static UploadPageAsyncTask uploadPageAsyncTask;


    public synchronized static List<Meal> getMeals() {
        if (meals == null){
            meals = new ArrayList<Meal>();
        }

        return meals;
    }

    public static synchronized Meal getMeal(int id){
        for (Meal meal:meals){
            if (meal.getId() == id){
                return meal;
            }
        }

        return null;
    }

    public static synchronized void setMeals(List<Meal> _restaurants) {
        meals = _restaurants;
    }

    public static synchronized void addMeal(Meal meal){
        getMeals().add(meal);
    }

    public static void removeMeal(Meal meal){
        getMeals().remove(meal);
    }

    public static void clear(){
        if (meals == null)
            return;
        meals.clear();
    }

    public static boolean isMealListCompleteFlag() {
        return mealListCompleteFlag;
    }

    public static void setMealListCompleteFlag(boolean mealListCompleteFlag) {
        MealList.mealListCompleteFlag = mealListCompleteFlag;
    }

    public static void startUploadPageAsycTask(AppCompatActivity activity){
        uploadPageAsyncTask = new UploadPageAsyncTask(activity);
        uploadPageAsyncTask.execute();
    }

    public static UploadPageAsyncTask getUploadPageAsyncTask(){
        return uploadPageAsyncTask;
    }


    public static void onDestroy(){
        uploadPageAsyncTask.cancel(true);
        uploadPageAsyncTask = null;
    }


}
