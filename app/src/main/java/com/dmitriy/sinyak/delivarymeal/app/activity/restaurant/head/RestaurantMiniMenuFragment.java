package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMiniMenuFragment extends Fragment {
    private Restaurant restaurant;

    public RestaurantMiniMenuFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_menu_mini, container, false);
//        TextView restaurantTitle = (TextView) view.findViewById(R.id.restaurantTitle);
//        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric_706_black.ttf");
//        restaurantTitle.setTypeface(typeface);
        return view;
    }
}
