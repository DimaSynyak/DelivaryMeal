package com.design.yogurt.app.main.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 05.11.2015.
 */
public class RestaurantList {

    private static List<Restaurant> restaurants;
    private int positionRestaurant;
    private static RestaurantList restaurantList;
    private Restaurant restaurant;

    public List<Restaurant> getRestaurants() {
        if (restaurants == null){
            restaurants = new ArrayList<Restaurant>();
        }

        return restaurants;
    }

    public static RestaurantList getInstance(){
        if (restaurantList == null){
            restaurantList = new RestaurantList();
        }

        return restaurantList;
    }

    public  void setRestaurants(List<Restaurant> _restaurants) {
        restaurants = _restaurants;
    }

    public  void addRestaurant(Restaurant restaurant){
        getRestaurants().add(restaurant);
    }

    public  void removeRestaurant(Restaurant restaurant){
        getRestaurants().remove(restaurant);
    }

    public int getPositionRestaurant() {
        return positionRestaurant;
    }

    public void setPositionRestaurant(int positionRestaurant) {
        this.positionRestaurant = positionRestaurant;
    }

    public Restaurant getRestaurant(){
        return restaurant;
    }

    public void clear(){
        if (restaurants == null){
            return;
        }
        restaurants.clear();
    }


    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
