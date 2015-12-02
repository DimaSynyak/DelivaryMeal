package com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;

/**
 * Created by 1 on 02.12.2015.
 */
public class AddressDataFragment extends Fragment {

    private TextView addressDataText;
    private String addressBranchesOfficesText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_data_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        addressDataText = (TextView) view.findViewById(R.id.address_data_text);
        addressDataText.setTypeface(arimo);
        addressDataText.setText(addressBranchesOfficesText);

        return view;
    }

    public String getAddressBranchesOfficesText() {
        return addressBranchesOfficesText;
    }

    public void setAddressBranchesOfficesText(String addressBranchesOfficesText) {
        this.addressBranchesOfficesText = addressBranchesOfficesText;
    }
}
