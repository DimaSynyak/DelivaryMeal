package com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.FilterData;

/**
 * Created by dmitriy on 12/4/15.
 */
public class FilterItemFragment extends Fragment {

    private TextView textView;
    private LinearLayout buttonLayout;
    private FragmentTransaction ft;
    private FilterFragment filterFragment;
    private HorizontalScrollView horizontalScrollView;

    /***********IMPORTENT*****************/
    private FilterData filterData;
    private int resIdContainer;
    /*************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_item_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        textView = (TextView) view.findViewById(R.id.text);
        textView.setTypeface(geometric);
        textView.setText(filterData.getText());

        horizontalScrollView = (HorizontalScrollView) getActivity().findViewById(R.id.horizontalScrollView);

        buttonLayout = (LinearLayout) view.findViewById(R.id.button_layout);
        buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterData.isStateUse())
                    return;

                filterData.setStateUse(true);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(resIdContainer, filterFragment);
                ft.commit();
            }
        });


        horizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollView.post(new Runnable() {
                    public void run() {
                        horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                    }
                });
            }
        });

        return view;
    }


    public FilterItemFragment() {
        filterFragment = new FilterFragment();
    }

    public void setFilterData(FilterData filterData) {
        this.filterData = filterData;
        filterFragment.setFilterData(filterData);
    }

    public int getResIdContainer() {
        return resIdContainer;
    }

    public void setResIdContainer(int resIdContainer) {
        this.resIdContainer = resIdContainer;
    }
}
