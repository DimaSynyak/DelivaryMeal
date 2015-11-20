package com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 20.11.2015.
 */
public class FormDataFragment extends Fragment {

    private TimePicker timePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_data_fragment, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        return view;
    }
}
