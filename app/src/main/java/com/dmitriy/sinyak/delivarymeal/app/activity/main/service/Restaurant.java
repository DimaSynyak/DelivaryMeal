package com.dmitriy.sinyak.delivarymeal.app.activity.main.service;

import android.graphics.Bitmap;

/**
 * Created by 1 on 05.11.2015.
 */
public class Restaurant {
    private String name;
    private String composition; //sostav edi
    private float stars;
    private String cost;
    private String delivarCost;
    private String delivarTime;
    private Bitmap img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDelivarCost() {
        return delivarCost;
    }

    public void setDelivarCost(String delivarCost) {
        this.delivarCost = delivarCost;
    }

    public String getDelivarTime() {
        return delivarTime;
    }

    public void setDelivarTime(String delivarTime) {
        this.delivarTime = delivarTime;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
