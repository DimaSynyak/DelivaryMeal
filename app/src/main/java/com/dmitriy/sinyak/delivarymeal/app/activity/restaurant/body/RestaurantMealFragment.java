package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMealFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        ((TextView) view.findViewById(R.id.restaurantName)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.costText)).setTypeface(arimo);
        ((TextView) view.findViewById(R.id.restaurantProfile)).setTypeface(arimo);

        ((TextView) view.findViewById(R.id.costMeal)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.textView11)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.textView12)).setTypeface(geometric);

        return view;
    }
}
