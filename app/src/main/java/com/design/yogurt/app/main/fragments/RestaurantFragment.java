package com.design.yogurt.app.main.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.design.yogurt.app.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.lib.StateFragment;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.service.RestaurantList;
import com.design.yogurt.app.restaurant.RestaurantActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantFragment extends Fragment {
    private AppCompatActivity activity;
    private Restaurant restaurant;
    private RestaurantList restaurantList;

    private boolean firstFlag;

    private TextView name;
    private TextView level;
    private TextView costMeal;
    private TextView costDeliver;
    private TextView timeDeliver;
    private ImageView avatar;
    private RatingBar stars;
    private TextView restaurantProfile;
    private TextView costMealText;
    private TextView costDeliverText;
    private TextView timeDeliverText;
    private Language language;

    private Bitmap cutImage;

    private StateFragment stateFragment;


    public void init(AppCompatActivity activity, Restaurant restaurant){
        this.activity = activity;
        this.restaurant = restaurant;
        restaurantList = RestaurantList.getInstance();
        restaurant.setFragment(this);
        language = Language.getInstance();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_fragment, container, false);
        stateFragment = StateFragment.ON_RESUME;

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        name = (TextView) view.findViewById(R.id.restaurantName);
        name.setTypeface(geometric);
        name.setText(restaurant.getName());

        level = (TextView) view.findViewById(R.id.restaurantLevel);
        level.setTypeface(geometric);
        level.setText(restaurant.getStars().toString());

        costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(restaurant.getCostMeal());

        costDeliver = (TextView) view.findViewById(R.id.costDeliver);
        costDeliver.setTypeface(geometric);
        costDeliver.setText(restaurant.getCostDeliver());

        timeDeliver = (TextView) view.findViewById(R.id.timeDeliver);
        timeDeliver.setTypeface(geometric);
        timeDeliver.setText(restaurant.getTimeDeliver());

        avatar = (ImageView) view.findViewById(R.id.restaurantAvatar);
//        avatar.setImageBitmap(restaurant.getImgBitmap());

        stars = (RatingBar) view.findViewById(R.id.ratingBar);
        stars.setRating(restaurant.getStars());
        stars.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        restaurantProfile = (TextView) view.findViewById(R.id.restaurantProfile);
        restaurantProfile.setTypeface(arimo);
        restaurantProfile.setText(restaurant.getProfile());

        costMealText = (TextView) view.findViewById(R.id.costMealText);
        costMealText.setTypeface(arimo);

        costDeliverText = (TextView) view.findViewById(R.id.costDeliverText);
        costDeliverText.setTypeface(arimo);

        timeDeliverText = (TextView) view.findViewById(R.id.timeDeliverText);
        timeDeliverText.setTypeface(arimo);

        view.findViewById(R.id.restaurantFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantList.setPositionRestaurant(restaurant.getId());
                Intent intent = new Intent(activity, RestaurantActivity.class);

                intent.putExtra("language", language.getLanguages());


                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                restaurantList.setRestaurant(restaurant);
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imgURL = null;
                InputStream is = null;
                try{

                    imgURL = new URL(restaurant.getImgSRC());

                    is = imgURL.openConnection().getInputStream();
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, null, o);
                    is.close();

                    int origWidth = o.outWidth;                     //исходная ширина
                    int origHeight = o.outHeight;                   //исходная высота
                    int bytesPerPixel = 2;                          //соответствует RGB_555 конфигурации
                    int maxSize = 480 * 800 * bytesPerPixel;        //Максимально разрешенный размер Bitmap
                    int desiredWidth = 350;                           //Нужная ширина
                    int desiredHeight = 318;                          //Нужная высота
                    int desiredSize = desiredWidth * desiredHeight * bytesPerPixel; //Максимально разрешенный размер Bitmap для заданных width х height
                    if (desiredSize < maxSize) maxSize = desiredSize;
                    int scale = 1; //кратность уменьшения
                    //высчитываем кратность уменьшения
                    if (origWidth > origHeight) {
                        scale = Math.round((float) origHeight / (float) desiredHeight);
                    } else {
                        scale = Math.round((float) origWidth / (float) desiredWidth);
                    }

                    o = new BitmapFactory.Options();
                    o.inSampleSize = scale;
                    o.inPreferredConfig = Bitmap.Config.RGB_565;

                    is = imgURL.openConnection().getInputStream(); //Ваш InputStream. Важно - открыть его нужно еще раз, т.к второй раз читать из одного и того же InputStream не разрешается (Проверено на ByteArrayInputStream и FileInputStream).
                    cutImage = BitmapFactory.decodeStream(is, null, o);
                    is.close();
                    /**/

                    if (cutImage != null) {
                        synchronized (restaurant) {
                            restaurant.setImgBitmap(cutImage);
                        }

                        RestaurantFragment.this.getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                avatar.setImageBitmap(RestaurantFragment.this.cutImage);
                                cutImage = null;
                            }
                        });
                    }
                }
                catch (Exception e){
                    RestaurantFragment.this.getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            avatar.setImageBitmap(((BitmapDrawable) RestaurantFragment.this.getActivity().getResources().getDrawable(R.drawable.no_image)).getBitmap());
                            cutImage = null;
                        }
                    });

                }
                finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstFlag){
            name.setText(restaurant.getName());
            level.setText(restaurant.getStars().toString());
            costMeal.setText(restaurant.getCostMeal());
            costDeliver.setText(restaurant.getCostDeliver());
            timeDeliver.setText(restaurant.getTimeDeliver());
            restaurantProfile.setText(restaurant.getProfile());
            stars.setRating(restaurant.getStars());


            costMealText.setText(R.string.min_cost_order);
            costDeliverText.setText(R.string.cost_deliver);
            timeDeliverText.setText(R.string.time_deliver);

            if (cutImage != null) {
                avatar.setImageBitmap(cutImage);
                cutImage = null;
            }
        }

        firstFlag = true;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
