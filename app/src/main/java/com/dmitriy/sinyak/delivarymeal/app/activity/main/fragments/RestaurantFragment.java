package com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;

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
        View view = inflater.inflate(R.layout.restaurant_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        ((TextView) view.findViewById(R.id.restaurantName)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.restaurantLevel)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.costMeal)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.costDeliver)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.timeDeliver)).setTypeface(geometric);

        ((TextView) view.findViewById(R.id.restaurantProfile)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.costDeliverText)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.timeDeliverText)).setTypeface(arimo);

        return view;
    }

    public void onClickListener(){
        ((LinearLayout) getView().findViewById(R.id.restaurantFragment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestaurantActivity.class);
                intent.putExtra("language", String.valueOf(this));
                startActivity(intent);
            }
        });
    }
}
