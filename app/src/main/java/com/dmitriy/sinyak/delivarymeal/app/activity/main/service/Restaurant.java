package com.dmitriy.sinyak.delivarymeal.app.activity.main.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.math.BigDecimal;

/**
 * Created by 1 on 05.11.2015.
 */
public class Restaurant {
    private String name;
    private String profile; //sostav edi
    private String stars;
    private String costMeal;
    private String costDeliver;
    private String timeDeliver;

    private String costMealStatic;
    private String costDeliverStatic;
    private String timeDeliverStatic;

    private String imgSRC;
    private Bitmap imgBitmap;
    private String menuLink;
    private Fragment fragment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Float getStars() {
        char[] str = stars.toCharArray();
        Float m = 0f;
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch :str){
            if (ch >= '0' && ch <= '9' || ch == '.'){
                stringBuilder.append(ch);
            }
        }
        m =5 * Float.parseFloat(String.valueOf(stringBuilder))/100;

        return roundDownScale2(m);
    }

    public float roundDownScale2(float aValue) {
        BigDecimal decimal = new BigDecimal(aValue);
//        decimal = decimal.setScale(1,BigDecimal.ROUND_UP);
        decimal = decimal.setScale(1,BigDecimal.ROUND_DOWN);
        return  decimal.floatValue();
    }

    public void setStars(String stars) {
        this.stars = stars;  //Warning
    }

    public String getCostMeal() {
        return costMeal;
    }

    public void setCostMeal(String costMeal) {
        this.costMeal = costMeal;
    }

    public String getCostDeliver() {
        return costDeliver;
    }

    public void setCostDeliver(String costDeliver) {
        this.costDeliver = costDeliver;
    }

    public String getTimeDeliver() {
        return timeDeliver;
    }

    public void setTimeDeliver(String timeDeliver) {
        this.timeDeliver = timeDeliver;
    }

    public String getImgSRC() {
        return imgSRC;
    }

    public void setImgSRC(String imgSRC) {
        this.imgSRC = imgSRC;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getCostMealStatic() {
        return costMealStatic;
    }

    public void setCostMealStatic(String costMealStatic) {
        this.costMealStatic = costMealStatic;
    }

    public String getCostDeliverStatic() {
        return costDeliverStatic;
    }

    public void setCostDeliverStatic(String costDeliverStatic) {
        this.costDeliverStatic = costDeliverStatic;
    }

    public String getTimeDeliverStatic() {
        return timeDeliverStatic;
    }

    public void setTimeDeliverStatic(String timeDeliverStatic) {
        this.timeDeliverStatic = timeDeliverStatic;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
