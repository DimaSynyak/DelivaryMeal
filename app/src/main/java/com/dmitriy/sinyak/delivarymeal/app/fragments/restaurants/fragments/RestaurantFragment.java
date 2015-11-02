package com.dmitriy.sinyak.delivarymeal.app.fragments.restaurants.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.RestaurantActivity;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantFragment extends Fragment {
    private AppCompatActivity activity;

    public RestaurantFragment(){
        super();
    }
    public RestaurantFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_fragment, container, false);
    }

    public void onClickListener(){
        ((LinearLayout) getView().findViewById(R.id.restaurantFragment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestaurantActivity.class);
                intent.putExtra("object", String.valueOf(this));
                startActivity(intent);
            }
        });
    }
}
