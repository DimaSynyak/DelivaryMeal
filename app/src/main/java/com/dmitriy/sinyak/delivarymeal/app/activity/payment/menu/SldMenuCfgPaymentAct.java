package com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
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
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu.fragments.FormDataFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class SldMenuCfgPaymentAct {

    private FragmentActivity activity;
    private boolean formDataClickFlag;
    private FragmentTransaction ft;


    private FormDataFragment formDataFragment;

    public SldMenuCfgPaymentAct(FragmentActivity activity) {
        this.activity = activity;
    }

    public void initSlidingMenu(){


        SlidingMenu menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_payment);

        initFragment();
    }

    private void initFragment(){
        formDataFragment = new FormDataFragment();
    }

    public void onClickSL(int id){

        switch (id){
            case R.id.formDataClick:{
                if (formDataClickFlag)
                    break;
                addFormDataFragment();

                formDataClickFlag = true;
                break;
            }
            case R.id.orderClick:{
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
}
