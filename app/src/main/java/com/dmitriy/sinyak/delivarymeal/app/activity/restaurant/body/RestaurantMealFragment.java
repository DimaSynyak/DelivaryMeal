package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMealFragment extends Fragment {
    private Meal meal;
    private TextView name;
    private TextView weight;
    private TextView composition;
    private TextView costMeal;
    private ImageView img;
    private LinearLayout linearLayout;
    private Garbage garbage;
    private TextView countMeal;
    private static DisplayMetrics metrics;

    static {
        metrics = new DisplayMetrics();
    }

    public RestaurantMealFragment() {
        super();
    }

    public RestaurantMealFragment(Meal meal) {
        this.meal = meal;
        meal.setFragment(this);
        garbage = Garbage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_fragment, container, false);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        name = (TextView) view.findViewById(R.id.restaurantName);
        name.setTypeface(arimo);
        name.setText(meal.getName());
        if (meal.getName().length() > 13){
            name.setTextSize(metrics.density * 8);
        }

        weight = (TextView) view.findViewById(R.id.costText);
        weight.setTypeface(arimo);
        weight.setText(meal.getWeight());

        composition = (TextView) view.findViewById(R.id.restaurantProfile);
        composition.setTypeface(arimo);
        composition.setText(meal.getComposition());


        costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(meal.getCost());

        img = (ImageView) view.findViewById(R.id.restaurantAvatar);
        img.setImageBitmap(meal.getImg());

        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(geometric);
        countMeal = (TextView) view.findViewById(R.id.textView11);
        countMeal.setTypeface(geometric);
        countMeal.setText(String.valueOf(meal.getCountMeal()));

        ((TextView) view.findViewById(R.id.textView12)).setTypeface(geometric);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal.add();
                countMeal.setText(String.valueOf(meal.getCountMeal()));
                garbage.update();
            }
        });

        return view;
    }
}
