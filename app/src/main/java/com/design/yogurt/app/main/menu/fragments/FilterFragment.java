package com.design.yogurt.app.main.menu.fragments;

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
import com.design.yogurt.app.restaurant.service.filter.FilterData;

/**
 * Created by dmitriy on 12/4/15.
 */
public class FilterFragment extends Fragment {

    private TextView textView;
    private LinearLayout filterRemove;
    private FragmentTransaction ft;
    private FilterData filterData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        textView = (TextView) view.findViewById(R.id.text);
        textView.setTypeface(geometric);
        textView.setText(filterData.getText());

        filterRemove = (LinearLayout) view.findViewById(R.id.filter_remove);
        filterRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData.setStateUse(false);
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(FilterFragment.this);
                ft.commit();
            }
        });


        return view;
    }

    public void setFilterData(FilterData filterData) {
        this.filterData = filterData;
    }
}
