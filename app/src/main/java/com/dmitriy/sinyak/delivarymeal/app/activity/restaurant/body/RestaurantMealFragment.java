package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.OrderFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garnir;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 03.11.2015.
 */
public class RestaurantMealFragment extends Fragment {
    private Meal meal;
    private TextView name;
    private TextView weight;
    private TextView composition;
    private TextView costMeal;
    private ImageView img;
    private LinearLayout linearLayout;
    private Garbage garbage;
    private TextView countMeal;
    private static DisplayMetrics metrics;
    private boolean stateBackgroundColor;

    private OrderFragment orderFragment;
    private FragmentTransaction ft;
    private TextView textView;
    private LinearLayout horizontalLayout;
    private RadioButton radioButton;
    List<RadioButton> radioButtonList;

    private Thread thread;

    private int step;

    private AlertDialog alert;

    private Bitmap cutImage;

    private boolean firstFlag;

    static {
        metrics = new DisplayMetrics();
    }

    public RestaurantMealFragment() {
        super();
    }

    public RestaurantMealFragment(Meal meal) {
        this.meal = meal;
        meal.setFragment(this);
        garbage = Garbage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.meal_fragment, container, false);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        name = (TextView) view.findViewById(R.id.restaurantName);
        name.setTypeface(arimo);
        name.setText(meal.getName());
        if (meal.getName().length() > 13){
            name.setTextSize(metrics.density * 8);
        }

        weight = (TextView) view.findViewById(R.id.costText);
        weight.setTypeface(arimo);
        weight.setText(meal.getWeight());

        composition = (TextView) view.findViewById(R.id.restaurantProfile);
        composition.setTypeface(arimo);
//        composition.setText(meal.getComposition());


        costMeal = (TextView) view.findViewById(R.id.costMeal);
        costMeal.setTypeface(geometric);
        costMeal.setText(meal.getCost());

        img = (ImageView) view.findViewById(R.id.restaurantAvatar);
//        img.setImageBitmap(meal.getImg());

        ((TextView) view.findViewById(R.id.costMealText)).setTypeface(geometric);
        countMeal = (TextView) view.findViewById(R.id.textView11);
        countMeal.setTypeface(geometric);
        countMeal.setText(String.valueOf(meal.getCountMeal()));

        ((TextView) view.findViewById(R.id.textView12)).setTypeface(geometric);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        if (meal.getGarnirs() != null && meal.getGarnirs().size() != 0) {

            LinearLayout baseLayout = new LinearLayout(getActivity());
            baseLayout.setOrientation(LinearLayout.VERTICAL);
            baseLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    250
            ));
            baseLayout.setPadding(10, 0, 10, 0);

            LinearLayout garnirContainer = new LinearLayout(getActivity());
            garnirContainer.setOrientation(LinearLayout.VERTICAL);
            garnirContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ScrollView scrollView = new ScrollView(getActivity());
            scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    250));
            scrollView.setBackgroundColor(Color.rgb(0xf1, 0xf1, 0xf1));
            scrollView.getLayoutParams().height = 250;


            radioButtonList = new ArrayList<>();
            for (final Garnir garnir : meal.getGarnirs()){


                horizontalLayout = new LinearLayout(getActivity());
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
                horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                radioButton = new RadioButton(getActivity());
                radioButton.setText("");
                radioButton.setPadding(11, 11, 11, 11);
                radioButton.setClickable(false);


                garnir.setHorizontalLayout(horizontalLayout);
                garnir.setRadioButton(radioButton);

                if (radioButtonList.size() == 0){
                    radioButton.setChecked(true);
                }

                radioButtonList.add(radioButton);

                textView = new TextView(getActivity());

                if (stateBackgroundColor){
                    horizontalLayout.setBackgroundColor(Color.WHITE);
                    stateBackgroundColor = false;
                } else {
                    horizontalLayout.setBackgroundColor(Color.GRAY);
                    stateBackgroundColor = true;
                }
                textView.setTypeface(arimo);
                textView.setTextSize(metrics.density * 10);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextColor(Color.BLACK);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                textView.setText(garnir.getGarnirName());

                horizontalLayout.setClickable(true);
                horizontalLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (Garnir garnir1 :meal.getGarnirs()){
                            if (garnir1.getHorizontalLayout().equals(v)){
                                meal.addGarnir(garnir1);

                                for (RadioButton radioButton : radioButtonList){
                                    if (radioButton.isChecked()){
                                        radioButton.setChecked(false);
                                        break;
                                    }
                                }

                                garnir1.getRadioButton().setChecked(true);
                                break;
                            }
                        }

                        step = 1;
                    }
                });

                horizontalLayout.addView(radioButton);
                horizontalLayout.addView(textView);
                garnirContainer.addView(horizontalLayout);
            }

            scrollView.addView(garnirContainer);
            baseLayout.addView(scrollView);

            AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantMealFragment.this.getActivity());
            builder.setTitle(meal.getName())
                    .setMessage(meal.getComposition())
                    .setCancelable(false)
                    .setView(baseLayout)
                    .setNegativeButton(getActivity().getResources().getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton(getActivity().getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    for (Garnir garnir :meal.getGarnirs()){
                                        if (garnir.getRadioButton().isChecked()){
                                            meal.addGarnir(String.valueOf(garnir.getGarnirName()));
                                        }
                                    }

                                    step = 1;
                                    linearLayout.callOnClick();
                                    step = 0;
                                    dialog.cancel();
                                }
                            }
                    );
            alert = builder.create();
            alert.getWindow().setBackgroundDrawableResource(R.drawable.white_gray);
        }
        else {
            step = 1;
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               switch (step){
                   case 0:{
                       alert.show();
                       break;
                   }
                   case 1:{

                       meal.add();
                       countMeal.setText(String.valueOf(meal.getCountMeal()));
                       garbage.update();

//                       if (menuFragment != null) {
//                           if (menuFragment.isAdded()) {
//                               if (menuFragment.isOrderDataClickFlag()) {
//                                   if (meal.getCountMeal() > 1) {
//                                       orderFragment = meal.getOrderFragment();
//                                       TextView countMeal = (TextView) orderFragment.getView().findViewById(R.id.countMeal);
//                                       countMeal.setText(String.valueOf(meal.getCountMeal()));
//                                   } else {
//                                       ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                       ft.add(R.id.orderDataContainer, new OrderFragment(meal));
//                                       ft.commit();
//                                   }
//                               }
//
//                           }
//                       }
                       break;
                   }
               }


            }
        });


        composition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantMealFragment.this.getActivity());
                builder.setTitle(meal.getName())
                        .setMessage(meal.getComposition())
                        .setCancelable(false)
                        .setNegativeButton(getActivity().getResources().getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });



        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                URL imgURL = null;
                InputStream is = null;

                try{
                    String query = URLEncoder.encode(meal.getImgURL(), "cp1251");
//                    query = URLDecoder.decode(query, "UTF-8");
                    String url = meal.getImgURL();
                    imgURL = new URL(url);

                    is = imgURL.openConnection().getInputStream();
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, null, o);
                    is.close();

                    int origWidth = o.outWidth;                     //исходная ширина
                    int origHeight = o.outHeight;                   //исходная высота
                    int bytesPerPixel = 2;                          //соответствует RGB_555 конфигурации
                    int maxSize = 480 * 800 * bytesPerPixel;        //Максимально разрешенный размер Bitmap
                    int desiredWidth = 150;                           //Нужная ширина
                    int desiredHeight = 150;                          //Нужная высота
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
                        if (RestaurantMealFragment.this.isAdded()) {
                            RestaurantMealFragment.this.getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    img.setImageBitmap(RestaurantMealFragment.this.cutImage);
                                    cutImage = null;
                                }
                            });
                        }
                    }
                }
                catch (Exception e){
                    if (RestaurantMealFragment.this.isAdded()) {
                        RestaurantMealFragment.this.getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                img.setImageBitmap(((BitmapDrawable) RestaurantMealFragment.this.getActivity().getResources().getDrawable(R.drawable.no_image)).getBitmap());
                                cutImage = null;
                            }
                        });
                    }
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
        });
        thread.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!firstFlag){
            firstFlag = true;
            return;
        }
        countMeal.setText(String.valueOf(meal.getCountMeal()));
    }

    public void update(){
        countMeal.setText(String.valueOf(meal.getCountMeal()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (thread != null){
            thread.interrupt();
        }
    }
}
