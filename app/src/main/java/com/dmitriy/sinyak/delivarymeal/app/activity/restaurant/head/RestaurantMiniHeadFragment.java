package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMiniHeadFragment extends Fragment {
    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private RestaurantMiniHeadFragment restaurantMiniHeadFragment;
    private RestaurantHeadFragment restaurantHeadFragment;
    private Restaurant restaurant;

    public RestaurantMiniHeadFragment(Restaurant restaurant) {
        super();
        this.restaurant = restaurant;
    }
    public RestaurantMiniHeadFragment(AppCompatActivity activity,Restaurant restaurant) {
        this.activity = activity;
        restaurantMiniHeadFragment = this;
        this.restaurant = restaurant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.restaurant_mini_head_fragment, container, false);

        TextView restaurantTitle = (TextView) view.findViewById(R.id.restaurantTitle);
        restaurantTitle.setText(restaurant.getName());

        ImageView img = (ImageView) view.findViewById(R.id.restaurantAvatar);
        img.setImageBitmap(restaurant.getImgBitmap());

        TextView menu = (TextView) view.findViewById(R.id.textView7);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");

        restaurantTitle.setTypeface(typeface);
        menu.setTypeface(typeface);
        return view;
    }


    public RestaurantHeadFragment getRestaurantHeadFragment() {
        return restaurantHeadFragment;
    }

    public void setRestaurantHeadFragment(RestaurantHeadFragment restaurantHeadFragment) {
        this.restaurantHeadFragment = restaurantHeadFragment;
    }
}
