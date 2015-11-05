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

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMiniHeadFragment extends Fragment {
    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private RestaurantMiniHeadFragment restaurantMiniHeadFragment;
    private RestaurantHeadFragment restaurantHeadFragment;

    public RestaurantMiniHeadFragment() {
        super();
    }
    public RestaurantMiniHeadFragment(AppCompatActivity activity) {
        this.activity = activity;
        restaurantMiniHeadFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.restaurant_mini_head_fragment, container, false);

        TextView restaurantTitle = (TextView) view.findViewById(R.id.restaurantTitle);
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
