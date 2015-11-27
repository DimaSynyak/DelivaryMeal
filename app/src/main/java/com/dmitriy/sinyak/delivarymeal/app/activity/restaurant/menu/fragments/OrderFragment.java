package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;

/**
 * Created by 1 on 20.11.2015.
 */
public class OrderFragment extends Fragment {

    private TextView costMeal;
    private TextView nameMeal;
    private TextView countMeal;
    private TextView count;
    private LinearLayout miniMealBTN;
    private TextView total;

    private Garbage garbage;

    private FragmentTransaction ft;

    private Meal meal;

    public OrderFragment() {
        super();
    }

    public OrderFragment(Meal meal) {
        this.meal = meal;
        meal.setOrderFragment(this);
        garbage = Garbage.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mini_meal_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        total = (TextView) getActivity().findViewById(R.id.total_text2);

        total.setText(garbage.getTotalCostStr());

        nameMeal = (TextView) view.findViewById(R.id.nameMeal);
        nameMeal.setTypeface(arimo);
        nameMeal.setText(meal.getName());

        costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(meal.getCost());

        countMeal = (TextView) view.findViewById(R.id.countMeal);
        countMeal.setTypeface(arimo);
        countMeal.setText(String.valueOf(meal.getCountMeal()));

        count = (TextView) view.findViewById(R.id.textView24);
        count.setTypeface(arimo);

        miniMealBTN = (LinearLayout) view.findViewById(R.id.miniMealBTN);
        miniMealBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meal.remove();
                if (meal.getCountMeal() == 0) {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.remove(OrderFragment.this);
                    ft.commit();
                }
                else {
                    countMeal.setText(String.valueOf(meal.getCountMeal()));
                }

                total.setText(garbage.getTotalCostStr());
                meal.getFragment().update();//Update count meal on meal_fragment
                garbage.update();
            }
        });

        return view;
    }


}
