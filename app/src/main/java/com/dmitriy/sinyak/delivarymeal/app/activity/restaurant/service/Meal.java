package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service;

import android.graphics.Bitmap;

/**
 * Created by 1 on 06.11.2015.
 */
public class Meal {
    private String name;
    private String cost;
    private String weight;
    private String composition;
    private Bitmap img;
    private String imgURL;


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
}
