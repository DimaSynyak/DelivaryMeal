package com.dmitriy.sinyak.delivarymeal.app.activity.payment;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 1 on 17.11.2015.
 */
public class Garbage {
    private static Garbage garbage;
    private static AppCompatActivity activity;
    private static int total;
    private static TextView garbageNum;
    private static Set<Integer> listID;

    public Garbage() {
        listID = new HashSet<>();
    }

    public static Garbage getInstance(){
        if (garbage == null){
            garbage = new Garbage();
        }
        return garbage;
    }

    public static void update(){
        garbageNum = (TextView) activity.findViewById(R.id.garbageNum);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                garbageNum.setText(String.valueOf(total));
            }
        });
    }



    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setActivity(AppCompatActivity _activity) {
        activity = _activity;
    }

    public static int getTotal() {
        return total;
    }

    public static void add(Meal meal) {
        listID.add(meal.getId());
        total++;
    }

    public static void remove(Meal meal){
        if (total > 0) {
            total--;
        }

        if (meal.getCountMeal() == 0){
            listID.remove(meal.getId());
        }
    }

    public static void removeAll(){
        total = 0;
        listID.clear();
    }
}
