package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.head;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantHeadFragment extends Fragment {
    private Restaurant restaurant;
    private boolean firstFlag;

    private TextView name;
    private TextView level;
    private TextView costMeal;
    private TextView costDeliver;
    private TextView timeDeliver;
    private TextView menu;
    private TextView infoText;
    private TextView reviewsText;

    private FrameLayout menuFrame;
    private LinearLayout menuButton;

    private TextView profile;
    private TextView costDeliverText;
    private TextView costMealText;
    private TextView timeDeliverText;
    private RatingBar stars;
    private ImageView img;

    private EStateMenu info;
    private EStateMenu reviews;

    private StateMenu sMenu;
    private StateMenu sInfo;
    private StateMenu sReviews;

    private LinearLayout restaurantMenuContainer;
    private LinearLayout infoLayout;
    private FrameLayout garbageLayout;
    private LinearLayout reviewContainer;
    private Bitmap cutImage;


    public RestaurantHeadFragment(Restaurant restaurant) {
        this.restaurant = restaurant;

        if (StateMenu.stateMenus == null || StateMenu.stateMenus.size() == 0) {
            sMenu = new StateMenu(EStateMenu.MENU, R.string.menu, "menu");
            sInfo = new StateMenu(EStateMenu.INFO, R.string.info, "info");
            sReviews = new StateMenu(EStateMenu.REVIEWS, R.string.reviews, "reviews");
        }
        else {
            sMenu = StateMenu.getInstanceByStringName("menu");
            sInfo = StateMenu.getInstanceByStringName("info");
            sReviews = StateMenu.getInstanceByStringName("reviews");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_head_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        synchronized (restaurant) {
        /*Layout Fields*/
            menuFrame = (FrameLayout) view.findViewById(R.id.menu);
            menuFrame.setVisibility(FrameLayout.GONE);

            menuButton = (LinearLayout) view.findViewById(R.id.menu_button);


        /*Text Fields*/
            reviewsText = (TextView) view.findViewById(R.id.reviews_text);
            reviewsText.setTypeface(geometric);
            reviewsText.setText(sReviews.getStringID());

            infoText = (TextView) view.findViewById(R.id.info_text);
            infoText.setTypeface(geometric);
            infoText.setText(sInfo.getStringID());


            name = (TextView) view.findViewById(R.id.restaurantName);
            name.setText(restaurant.getName());

            profile = (TextView) view.findViewById(R.id.restaurantProfile);
            profile.setText(restaurant.getProfile());

            level = (TextView) view.findViewById(R.id.restaurantLevel);
            level.setText(restaurant.getStars().toString());

            stars = (RatingBar) view.findViewById(R.id.ratingBar);
            stars.setRating(restaurant.getStars());
            stars.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            costMeal = (TextView) view.findViewById(R.id.costMeal);
            costMeal.setText(restaurant.getCostMeal());

            costDeliver = (TextView) view.findViewById(R.id.costDeliver);
            costDeliver.setText(restaurant.getCostDeliver());

            timeDeliver = (TextView) view.findViewById(R.id.timeDeliver);
            timeDeliver.setText(restaurant.getTimeDeliver());

            img = (ImageView) view.findViewById(R.id.restaurantHeadAvatar);


            if (restaurant.getImgBitmap() != null) {
                img.setImageBitmap(restaurant.getImgBitmap());
            }

            costMealText = (TextView) view.findViewById(R.id.costMealText);
            costDeliverText = (TextView) view.findViewById(R.id.costDeliverText);

            timeDeliverText = (TextView) view.findViewById(R.id.timeDeliverText);

            menu = (TextView) view.findViewById(R.id.textView7);
            menu.setText(sMenu.getStringID());


            name.setTypeface(geometric);
            level.setTypeface(geometric);
            costMeal.setTypeface(geometric);
            costDeliver.setTypeface(geometric);
            timeDeliver.setTypeface(geometric);
            menu.setTypeface(geometric);

            profile.setTypeface(arimo);
            costDeliverText.setTypeface(arimo);
            costMealText.setTypeface(arimo);
            timeDeliverText.setTypeface(arimo);

        /*container*/
            restaurantMenuContainer = (LinearLayout) getActivity().findViewById(R.id.restaurantMenuContainer);
            infoLayout = (LinearLayout) getActivity().findViewById(R.id.info_layout);
            garbageLayout = (FrameLayout) getActivity().findViewById(R.id.garbage_layout);
            reviewContainer = (LinearLayout) getActivity().findViewById(R.id.reviews_container);

            initListeners();
            setContentOnDisplay();
        }

        return view;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListeners(){

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuFrame.getVisibility() == FrameLayout.GONE){
                    menuFrame.setVisibility(FrameLayout.VISIBLE);
                }
                else {
                    menuFrame.setVisibility(FrameLayout.GONE);
                }
            }
        });

        infoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFrame.setVisibility(FrameLayout.GONE);

                int tmp;

                tmp = sMenu.getStringID();
                sMenu.setStringID(sInfo.getStringID());
                sInfo.setStringID(tmp);

                menu.setText(sMenu.getStringID());
                infoText.setText(sInfo.getStringID());
                reviewsText.setText(sReviews.getStringID());

                setContentOnDisplay();
            }
        });

        reviewsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFrame.setVisibility(FrameLayout.GONE);

                int tmp;

                tmp = sMenu.getStringID();
                sMenu.setStringID(sReviews.getStringID());
                sReviews.setStringID(tmp);

                menu.setText(sMenu.getStringID());
                infoText.setText(sInfo.getStringID());
                reviewsText.setText(sReviews.getStringID());

                setContentOnDisplay();
            }
        });
    }


    private void setContentOnDisplay(){

        switch (sMenu.getStringID()){
            case R.string.menu:{
                restaurantMenuContainer.setVisibility(LinearLayout.VISIBLE);
                garbageLayout.setVisibility(FrameLayout.VISIBLE);
                infoLayout.setVisibility(LinearLayout.GONE);
                reviewContainer.setVisibility(FrameLayout.GONE);
                break;
            }
            case R.string.info:{
                restaurantMenuContainer.setVisibility(LinearLayout.GONE);
                infoLayout.setVisibility(LinearLayout.VISIBLE);
                garbageLayout.setVisibility(FrameLayout.GONE);
                reviewContainer.setVisibility(FrameLayout.GONE);
                break;
            }
            case R.string.reviews:{
                restaurantMenuContainer.setVisibility(LinearLayout.GONE);
                infoLayout.setVisibility(LinearLayout.GONE);
                garbageLayout.setVisibility(FrameLayout.GONE);

                reviewContainer.setVisibility(FrameLayout.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
