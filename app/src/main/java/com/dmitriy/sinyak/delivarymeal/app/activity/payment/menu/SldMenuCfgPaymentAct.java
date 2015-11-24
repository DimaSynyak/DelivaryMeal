package com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.Country;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.CategoryFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.CityFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.CountryFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.Ifragments.IFragments;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.KitchenFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category.AsianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category.CaucasianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category.EuropeanButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category.PizzaButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category.SushiButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.PaymentActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu.fragments.FormDataFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu.fragments.OrderFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class SldMenuCfgPaymentAct {

    private AppCompatActivity activity;
    private boolean formDataClickFlag;
    private boolean orderDataClickFlag;
    private FragmentTransaction ft;
    private Garbage garbage;

    private LinearLayout total;
    private TextView totalText1;
    private TextView totalText2;


    private FormDataFragment formDataFragment;

    public SldMenuCfgPaymentAct(AppCompatActivity activity) {
        this.activity = activity;
        garbage = Garbage.getInstance();
    }

    public void initSlidingMenu(){

        Typeface geometric = Typeface.createFromAsset(activity.getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(activity.getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        SlidingMenu menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_payment);

        total = (LinearLayout) activity.findViewById(R.id.total);
        totalText1 = (TextView) activity.findViewById(R.id.total_text1);
        totalText1.setTypeface(arimo);
        totalText2 = (TextView) activity.findViewById(R.id.total_text2);
        totalText2.setTypeface(geometric);
        total.setVisibility(LinearLayout.GONE);

        ((PaymentActivity)activity).setCustomViewAbove(CustomViewAbove.customViewAbove);
        initFragment();
    }

    private void initFragment(){
        formDataFragment = FormDataFragment.getInstance();
    }

    public void onClickSL(int id){

        switch (id){
            case R.id.formDataClick:{

                if (orderDataClickFlag){
                    removeOrderFragment();
                    total.setVisibility(LinearLayout.GONE);
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

                break;
            }
            case R.id.orderClick:{

                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }

                if (orderDataClickFlag){
                    removeOrderFragment();
                    orderDataClickFlag = false;
                    total.setVisibility(LinearLayout.GONE);
                }
                else {
                    addOrderFragment();
                    orderDataClickFlag = true;
                    total.setVisibility(LinearLayout.VISIBLE);
                }


                break;
            }
        }
    }

    private void addFormDataFragment(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.formDataContainer, formDataFragment);
        ft.commit();
    }

    private void removeFormDataFragment(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.remove(formDataFragment);
        ft.commit();
    }

    private void addOrderFragment(){

        ft = activity.getSupportFragmentManager().beginTransaction();

        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);
            ft.add(R.id.orderDataContainer, new OrderFragment(meal));
        }

        ft.commit();
    }

    private void removeOrderFragment(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);

            if (meal.getOrderFragment() == null)
                continue;

            ft.remove(meal.getOrderFragment());
            meal.setOrderFragment(null);
        }
        ft.commit();
    }
}
