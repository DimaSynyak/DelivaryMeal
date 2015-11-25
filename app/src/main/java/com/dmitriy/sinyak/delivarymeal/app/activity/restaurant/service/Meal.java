package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.RestaurantMealFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.OrderFragment;

/**
 * Created by 1 on 06.11.2015.
 */
public class Meal {

    private String id;
    private String name;
    private String cost;
    private String weight;
    private String composition;
    private Bitmap img;
    private String imgURL;
    private RestaurantMealFragment fragment;
    private int countMeal;
    private Garbage garbage;
    private OrderFragment orderFragment;


    private int remove_id;

    public Meal() {
        this.garbage = Garbage.getInstance();
    }

    public OrderFragment getOrderFragment() {
        return orderFragment;
    }

    public void setOrderFragment(OrderFragment orderFragment) {
        this.orderFragment = orderFragment;
    }

    public RestaurantMealFragment getFragment() {
        return fragment;
    }

    public void setFragment(RestaurantMealFragment fragment) {
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getCountMeal() {
        return countMeal;
    }

    public void setCountMeal(int countMeal) {
        this.countMeal = countMeal;
    }

    public void add(){
        this.countMeal++;
        garbage.add(this);
    }

    public void remove(){
        if (countMeal > 0){
            countMeal--;
            garbage.remove(this);
        }
    }

    public void removeAll(){
        countMeal = 0;
        garbage.removeAll();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        String id = null;
        if(o instanceof String) {
            id = (String) o;
        }
        else {
            return false;
        }

        if (this.id.equals(id))
            return true;
        else
            return false;
    }



}
