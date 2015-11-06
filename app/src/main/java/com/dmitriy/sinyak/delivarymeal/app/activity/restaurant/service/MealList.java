package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 06.11.2015.
 */
public class MealList {
    private static List<Meal> meals;

    public static List<Meal> getMeals() {
        if (meals == null){
            meals = new ArrayList<Meal>();
        }

        return meals;
    }

    public static void setMeals(List<Meal> _restaurants) {
        meals = _restaurants;
    }

    public static void addMeal(Meal meal){
        getMeals().add(meal);
    }

    public static void removeMeal(Meal meal){
        getMeals().remove(meal);
    }

}
