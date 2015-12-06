package com.dmitriy.sinyak.delivarymeal.app.activity.main.menu;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.FilterItemFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.ChangeLocale;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.FilterData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.RestaurantFilter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class SlidingMenuConfig {

    private FragmentActivity activity;
    private FragmentTransaction ft;


    private LinearLayout categoryLayout;
    private LinearLayout criteriaLayout;
    private LinearLayout categoryContainer;
    private LinearLayout criteriaContainer;
    private EditText editText;

    private Language language;

    private RestaurantFilter filter;

    public SlidingMenuConfig(FragmentActivity activity) {
        this.activity = activity;
        language = Language.getInstance();
    }

    public void initSlidingMenu(){
        Typeface geometric = Typeface.createFromAsset(activity.getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(activity.getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        SlidingMenu menu = new SlidingMenu(activity);
        MainActivity.setCustomViewAbove(menu.getmViewAbove());
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_main);

        categoryLayout = (LinearLayout) activity.findViewById(R.id.category_layout);
        criteriaLayout = (LinearLayout) activity.findViewById(R.id.criteria_layout);
        categoryContainer = (LinearLayout) activity.findViewById(R.id.category_container);
        categoryContainer.setVisibility(View.GONE);
        criteriaContainer = (LinearLayout) activity.findViewById(R.id.criteria_container);
        criteriaContainer.setVisibility(View.GONE);
        editText = (EditText) activity.findViewById(R.id.editText);

        TextView categoryText = (TextView) activity.findViewById(R.id.categoryText);
        categoryText.setTypeface(geometric);

        TextView criteriaText = (TextView) activity.findViewById(R.id.criteriaText);
        criteriaText.setTypeface(geometric);

        TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
        searchButton.setTypeface(geometric);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RestaurantFilter.getSearchData().setText(String.valueOf(editText.getText()));
                RestaurantFilter.getSearchData().setStateUse(true);

                new ChangeLocale(((AppCompatActivity)activity)).execute(language.getURL());
            }
        });

        initListeners();
    }

    private void initListeners(){

        filter = RestaurantFilter.getInstance();

        addFilterData();

        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryContainer.getVisibility() == View.VISIBLE) {
                    categoryContainer.setVisibility(View.GONE);
                } else {
                    categoryContainer.setVisibility(View.VISIBLE);
                    criteriaContainer.setVisibility(View.GONE);
                }

            }
        });

        criteriaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (criteriaContainer.getVisibility() == View.VISIBLE) {
                    criteriaContainer.setVisibility(View.GONE);
                } else {
                    criteriaContainer.setVisibility(View.VISIBLE);
                    categoryContainer.setVisibility(View.GONE);
                }
            }
        });


    }

    private void addFilterData(){
        List<FilterData> dataList = filter.getCategoryList();
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = new FilterItemFragment();
            filterItemFragment.setFilterData(filterData);
            filterItemFragment.setResIdContainer(R.id.filterLayout);

            ft.add(R.id.category_container, filterItemFragment);
        }
        ft.commit();

        dataList = filter.getCriteriaList();
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = new FilterItemFragment();
            filterItemFragment.setFilterData(filterData);
            filterItemFragment.setResIdContainer(R.id.filterLayout);

            ft.add(R.id.criteria_container, filterItemFragment);
        }
        ft.commit();
    }
}
