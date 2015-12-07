package com.dmitriy.sinyak.delivarymeal.app.activity.main.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 05.11.2015.
 */
public class RestaurantList {

    private static List<Restaurant> restaurants;
    private int positionRestaurant;
    private static RestaurantList restaurantList;

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
        Restaurant restaurant = null;
        for (Restaurant rest:restaurants ){
            int id = rest.getId();
            if (positionRestaurant == id) {
                restaurant = rest;
                break;
            }
        }

        return restaurant;
    }

    public void clear(){
        if (restaurants == null){
            return;
        }
        restaurants.clear();
    }
}
