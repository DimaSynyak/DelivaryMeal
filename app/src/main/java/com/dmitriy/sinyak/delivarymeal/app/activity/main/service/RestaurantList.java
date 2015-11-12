package com.dmitriy.sinyak.delivarymeal.app.activity.main.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 05.11.2015.
 */
public class RestaurantList {

    private static List<Restaurant> restaurants;
    private static int positionRestaurant;

    public static List<Restaurant> getRestaurants() {
        if (restaurants == null){
            restaurants = new ArrayList<Restaurant>();
        }

        return restaurants;
    }

    public static void setRestaurants(List<Restaurant> _restaurants) {
        restaurants = _restaurants;
    }

    public static void addRestaurant(Restaurant restaurant){
        getRestaurants().add(restaurant);
    }

    public static void removeRestaurant(Restaurant restaurant){
        getRestaurants().remove(restaurant);
    }

    public static int getPositionRestaurant() {
        return positionRestaurant;
    }

    public static void setPositionRestaurant(int positionRestaurant) {
        RestaurantList.positionRestaurant = positionRestaurant;
    }
}
