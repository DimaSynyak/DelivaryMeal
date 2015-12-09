package com.design.yogurt.app.restaurant.head;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.main.service.Restaurant;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMiniHeadFragment extends Fragment {
    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private RestaurantMiniHeadFragment restaurantMiniHeadFragment;
    private RestaurantHeadFragment restaurantHeadFragment;
    private Restaurant restaurant;

    private TextView infoText;
    private TextView reviewsText;

    private FrameLayout menuFrame;
    private LinearLayout menuButton;


    private boolean firstFlag;

    private TextView restaurantTitle;
    private TextView menu;
    private ImageView img;

    private StateMenu sMenu;
    private StateMenu sInfo;
    private StateMenu sReviews;

    private LinearLayout restaurantMenuContainer;
    private LinearLayout infoLayout;
    private FrameLayout garbageLayout;
    private LinearLayout reviewContainer;
    private Bitmap cutImage;

    public void init(Restaurant restaurant) {
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
//    public void init(AppCompatActivity activity,Restaurant restaurant) {
//        this.activity = activity;
//        restaurantMiniHeadFragment = this;
//        this.restaurant = restaurant;
//
//        if (StateMenu.stateMenus == null || StateMenu.stateMenus.size() == 0) {
//            sMenu = new StateMenu(EStateMenu.MENU, R.string.menu, "menu");
//            sInfo = new StateMenu(EStateMenu.INFO, R.string.info, "info");
//            sReviews = new StateMenu(EStateMenu.REVIEWS, R.string.reviews, "reviews");
//        }
//        else {
//            sMenu = StateMenu.getInstanceByStringName("menu");
//            sInfo = StateMenu.getInstanceByStringName("info");
//            sReviews = StateMenu.getInstanceByStringName("reviews");
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_mini_head_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        synchronized (restaurant){
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

            restaurantTitle = (TextView) view.findViewById(R.id.restaurantTitle);
            restaurantTitle.setText(restaurant.getName());

            img = (ImageView) view.findViewById(R.id.restaurantAvatar);

            if (restaurant.getImgBitmap() != null) {
                img.setImageBitmap(restaurant.getImgBitmap());
            }

            menu = (TextView) view.findViewById(R.id.textView7);

            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");

            restaurantTitle.setTypeface(typeface);
            menu.setTypeface(typeface);
            menu.setText(sMenu.getStringID());

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

    public void setRestaurantHeadFragment(RestaurantHeadFragment restaurantHeadFragment) {
        this.restaurantHeadFragment = restaurantHeadFragment;
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
                if (menuFrame.getVisibility() == FrameLayout.GONE) {
                    menuFrame.setVisibility(FrameLayout.VISIBLE);
                } else {
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
}
