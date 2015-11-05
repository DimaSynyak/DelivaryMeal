package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantHeadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_head_fragment, container, false);

        TextView name = (TextView) view.findViewById(R.id.restaurantName);
        TextView profile = (TextView) view.findViewById(R.id.restaurantProfile);
        TextView level = (TextView) view.findViewById(R.id.restaurantLevel);
        TextView costMeal = (TextView) view.findViewById(R.id.costMeal);
        TextView costDeliver = (TextView) view.findViewById(R.id.costDeliver);
        TextView timeDeliver = (TextView) view.findViewById(R.id.timeDeliver);
        TextView costMealText = (TextView) view.findViewById(R.id.costMealText);
        TextView costDeliverText = (TextView) view.findViewById(R.id.costDeliverText);
        TextView timeDeliverText = (TextView) view.findViewById(R.id.timeDeliverText);
        TextView menu = (TextView) view.findViewById(R.id.textView7);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");

        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        name.setTypeface(geometric);
        level.setTypeface(geometric);
        costMeal.setTypeface(geometric);
        costDeliver.setTypeface(geometric);
        timeDeliver.setTypeface(geometric);
        menu.setTypeface(geometric);

        profile.setTypeface(arimo);
        costDeliverText.setTypeface(arimo);
        costMealText.setTypeface(arimo);
        timeDeliverText.setTypeface(arimo);
        return view;
    }
}
