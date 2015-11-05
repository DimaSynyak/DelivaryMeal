package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantHeadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_head_fragment, container, false);
    }
}
