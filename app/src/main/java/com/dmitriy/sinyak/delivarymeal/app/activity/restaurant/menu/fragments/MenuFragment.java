package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.DelivaryData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.MainAsyncTask;


/**
 * Created by 1 on 25.11.2015.
 */
public class MenuFragment extends Fragment {

    private boolean onResumeFlag;
    private FragmentActivity activity;
    private RestaurantActivity restaurantActivity;

    private boolean formDataClickFlag;
    private boolean orderDataClickFlag;
    private FragmentTransaction ft;
    private Garbage garbage;
    private LinearLayout total;
    private TextView totalText1;
    private TextView totalText2;
    private TextView personal_cabinet_text;
    private TextView deliver_text;
    private TextView meal_text;
    private FormDataFragment formDataFragment;

    private LinearLayout baseLayout;
    private LinearLayout garbageLayout;

    private LinearLayout formDataClick;
    private LinearLayout orderClick;
    private LinearLayout paymentMethod;

    private TextView pay;
    private TextView back;

    private ImageView seb_btn;
    private ImageView swed_btn;
    private ImageView lhv_btn;
    private ImageView nordea_btn;
    private ImageView danske_btn;
    private ImageView kredit_btn;

    private DelivaryData delivaryData;

    public MenuFragment() {
        activity = getActivity();
        restaurantActivity = ((RestaurantActivity) activity);
        formDataFragment = FormDataFragment.getInstance();
        garbage = Garbage.getInstance();
        delivaryData = DelivaryData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        baseLayout = (LinearLayout) view.findViewById(R.id.baseLayout);
        garbageLayout = (LinearLayout) view.findViewById(R.id.garbageLayout);


        baseLayout.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        personal_cabinet_text = (TextView) view.findViewById(R.id.personal_cabinet_text);
        personal_cabinet_text.setTypeface(geometric);

        deliver_text = (TextView) view.findViewById(R.id.deliverText);
        deliver_text.setTypeface(geometric);

        meal_text = (TextView) view.findViewById(R.id.mealText);
        meal_text.setTypeface(geometric);

        pay = (TextView) view.findViewById(R.id.textView25);
        pay.setTypeface(geometric);
        pay.setVisibility(LinearLayout.GONE);

        back = (TextView) view.findViewById(R.id.textView26);
        back.setTypeface(geometric);

        paymentMethod = (LinearLayout) view.findViewById(R.id.payment_method);
        paymentMethod.setVisibility(LinearLayout.GONE);

        formDataClick = (LinearLayout) view.findViewById(R.id.formDataClick);
        orderClick = (LinearLayout) view.findViewById(R.id.orderClick);

        seb_btn = (ImageView) view.findViewById(R.id.seb_btn);
        swed_btn = (ImageView) view.findViewById(R.id.swed_btn);
        lhv_btn = (ImageView) view.findViewById(R.id.lhv_btn);
        nordea_btn = (ImageView) view.findViewById(R.id.nordea_btn);
        danske_btn = (ImageView) view.findViewById(R.id.danske_btn);
        kredit_btn = (ImageView) view.findViewById(R.id.kredit_btn);

        total = (LinearLayout) view.findViewById(R.id.total);
        totalText1 = (TextView) view.findViewById(R.id.total_text1);
        totalText1.setTypeface(arimo);
        totalText2 = (TextView) view.findViewById(R.id.total_text2);
        totalText2.setTypeface(geometric);
        total.setVisibility(LinearLayout.GONE);

        initListeners();

        return view;
    }


    private void initListeners(){
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (garbage.getTotal() == 0)
                    return;
                updateDelivaryData();
                new MainAsyncTask(restaurantActivity).execute(RestaurantList.getRestaurant().getMenuLink());
                pay.setVisibility(LinearLayout.GONE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterFragment();
            }
        });

        formDataClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDataClickFlag){
                    removeOrderFragment();
                    total.setVisibility(LinearLayout.GONE);
                    paymentMethod.setVisibility(LinearLayout.GONE);
                    pay.setVisibility(LinearLayout.GONE);
                    orderDataClickFlag = false;
                }

                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }
                else {
                    addFormDataFragment();
                    formDataClickFlag = true;
                }
            }
        });

        orderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }

                if (orderDataClickFlag){
                    removeOrderFragment();
                    orderDataClickFlag = false;
                    total.setVisibility(LinearLayout.GONE);
                    paymentMethod.setVisibility(LinearLayout.GONE);
                    pay.setVisibility(LinearLayout.GONE);
                }
                else {
                    addOrderFragment();
                    orderDataClickFlag = true;
                    total.setVisibility(LinearLayout.VISIBLE);
                    paymentMethod.setVisibility(LinearLayout.VISIBLE);
                    pay.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        seb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                swed_btn.setBackground(null);
                seb_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("seb");
            }
        });
        swed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("swedbank");
            }
        });
        lhv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));

                delivaryData.setNameBank("lhv");
            }
        });

        nordea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("nordea");
            }
        });

        danske_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("danske");
            }
        });

        kredit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("krediidipank");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!onResumeFlag){
            onResumeFlag = true;
            return;
        }

    }

    public void setGarbageFragment(){
        baseLayout.setVisibility(LinearLayout.GONE);
        garbageLayout.setVisibility(LinearLayout.VISIBLE);
    }


    public void setFilterFragment(){
        baseLayout.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);
    }


    private void addFormDataFragment(){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.formDataContainer, formDataFragment);
        ft.commit();
    }

    private void removeFormDataFragment(){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(formDataFragment);
        ft.commit();
    }

    private void addOrderFragment(){

        ft = getActivity().getSupportFragmentManager().beginTransaction();

        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);
            ft.add(R.id.orderDataContainer, new OrderFragment(meal));
        }

        ft.commit();
    }

    private void removeOrderFragment(){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);

            if (meal.getOrderFragment() == null)
                continue;

            ft.remove(meal.getOrderFragment());
            meal.setOrderFragment(null);
        }
        ft.commit();
    }

    public boolean isOrderDataClickFlag() {
        return orderDataClickFlag;
    }

    public void setOrderDataClickFlag(boolean orderDataClickFlag) {
        this.orderDataClickFlag = orderDataClickFlag;
    }


    private void updateDelivaryData(){
        formDataFragment = FormDataFragment.getInstance();

        if (!formDataFragment.isAdded())
            return;

        delivaryData.setYourName(String.valueOf(formDataFragment.getYourName().getText()));
        delivaryData.setDelivaryCity(String.valueOf(formDataFragment.getYourCity().getText()));
        delivaryData.setNumStreet(String.valueOf(formDataFragment.getYourStreet().getText()));
        delivaryData.setNumHouse(String.valueOf(formDataFragment.getYourHouse().getText()));
        delivaryData.setNumFlat(String.valueOf(formDataFragment.getYourFlat().getText()));
        delivaryData.setEmail(String.valueOf(formDataFragment.getYourEmail().getText()));
        delivaryData.setNumPhone(String.valueOf(formDataFragment.getYourPhone().getText()));

        delivaryData.setDelivaryData(formDataFragment.updateDate());
    }
}
