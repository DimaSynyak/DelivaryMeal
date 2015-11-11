package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMealFragment extends Fragment {
    private Meal meal;

    public RestaurantMealFragment() {
        super();
    }

    public RestaurantMealFragment(Meal meal) {
        this.meal = meal;
        meal.setFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        TextView name = (TextView) view.findViewById(R.id.restaurantName);
        name.setTypeface(arimo);
        name.setText(meal.getName());

        TextView weight = (TextView) view.findViewById(R.id.costText);
        weight.setTypeface(arimo);
        weight.setText(meal.getWeight());

        TextView composition = (TextView) view.findViewById(R.id.restaurantProfile);
        composition.setTypeface(arimo);
        composition.setText(meal.getComposition());


        TextView costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(meal.getCost());

        ImageView img = (ImageView) view.findViewById(R.id.restaurantAvatar);
        img.setImageBitmap(meal.getImg());

        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.textView11)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.textView12)).setTypeface(geometric);

        return view;
    }
}
