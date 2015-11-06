package com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantFragment extends Fragment {
    private AppCompatActivity activity;
    private Restaurant restaurant;

    public RestaurantFragment(){
        super();
    }
    public RestaurantFragment(AppCompatActivity activity, Restaurant restaurant) {
        this.activity = activity;
        this.restaurant = restaurant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        TextView name = (TextView) view.findViewById(R.id.restaurantName);
        name.setTypeface(geometric);
        name.setText(restaurant.getName());

        TextView level = (TextView) view.findViewById(R.id.restaurantLevel);
        level.setTypeface(geometric);
        level.setText(restaurant.getStars().toString());

        TextView costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(restaurant.getCostMeal());

        TextView costDeliver = (TextView) view.findViewById(R.id.costDeliver);
        costDeliver.setTypeface(geometric);
        costDeliver.setText(restaurant.getCostDeliver());

        TextView timeDeliver = (TextView) view.findViewById(R.id.timeDeliver);
        timeDeliver.setTypeface(geometric);
        timeDeliver.setText(restaurant.getTimeDeliver());

        ImageView avatar = (ImageView) view.findViewById(R.id.restaurantAvatar);
        avatar.setImageBitmap(restaurant.getImgBitmap());

        RatingBar stars = (RatingBar) view.findViewById(R.id.ratingBar);
        stars.setRating(restaurant.getStars());
        stars.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ((TextView) view.findViewById(R.id.restaurantProfile)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.costDeliverText)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.timeDeliverText)).setTypeface(arimo);

        view.findViewById(R.id.restaurantFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestaurantActivity.class);
                intent.putExtra("language", String.valueOf(this));
                intent.putExtra("restaurant", RestaurantList.getRestaurants().indexOf(restaurant));
                startActivity(intent);
            }
        });
        return view;
    }
}
