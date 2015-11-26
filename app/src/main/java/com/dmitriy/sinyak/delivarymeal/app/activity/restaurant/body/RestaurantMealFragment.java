package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.MenuFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.OrderFragment;
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

    private MenuFragment menuFragment;
    private OrderFragment orderFragment;
    private FragmentTransaction ft;

    private boolean firstFlag;

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
//        composition.setText(meal.getComposition());


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

                if (menuFragment == null){
                    menuFragment = (MenuFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.menu_fragment_id);
                }

                if (menuFragment != null){
                    if (menuFragment.isAdded()) {
                        if (menuFragment.isOrderDataClickFlag()) {
                            if (meal.getCountMeal() > 1) {
                                orderFragment = meal.getOrderFragment();
                                TextView countMeal = (TextView) orderFragment.getView().findViewById(R.id.countMeal);
                                countMeal.setText(String.valueOf(meal.getCountMeal()));
                            } else {
                                ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.orderDataContainer, new OrderFragment(meal));
                                ft.commit();
                            }
                        }

                    }
                }
            }
        });


        composition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantMealFragment.this.getActivity());
                builder.setTitle(meal.getName())
                        .setMessage(meal.getComposition())
                        .setCancelable(false)
                        .setNegativeButton(getActivity().getResources().getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!firstFlag){
            firstFlag = true;
            return;
        }
        countMeal.setText(String.valueOf(meal.getCountMeal()));
    }

    public void update(){
        countMeal.setText(String.valueOf(meal.getCountMeal()));
    }
}
