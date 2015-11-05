package com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.Ifragments.IFragments;

/**
 * Created by 1 on 27.10.2015.
 */
public class CategoryButtonFragment extends Fragment implements IFragments {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.category_button, container, false);

        return viewHierarchy;
    }
}
