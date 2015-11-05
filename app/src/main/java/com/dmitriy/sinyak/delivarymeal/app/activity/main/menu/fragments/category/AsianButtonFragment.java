package com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.category;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.Ifragments.IFragments;

/**
 * Created by 1 on 27.10.2015.
 */
public class AsianButtonFragment extends Fragment implements IFragments {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asian, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");

        ((TextView) view.findViewById(R.id.pizzaText)).setTypeface(geometric);

        return view;
    }
}
