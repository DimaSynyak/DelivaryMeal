package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.tools.Tools;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 1 on 17.11.2015.
 */
public class Garbage {
    private static Garbage garbage;
    private AppCompatActivity activity;
    private int total;
    private TextView garbageNum;
    private Set<String> listID;
    private int totalCost;
    private TextView totalCostStr;

    public Garbage() {
        listID = new HashSet<>();
    }

    public static Garbage getInstance(){
        if (garbage == null){
            garbage = new Garbage();
        }
        return garbage;
    }

    public void update(){
        garbageNum = (TextView) activity.findViewById(R.id.garbageNum);
        totalCostStr = (TextView) activity.findViewById(R.id.total_text2);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                garbageNum.setText(String.valueOf(total));
                totalCostStr.setText(getTotalCostStr());
            }
        });
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void setActivity(AppCompatActivity _activity) {
        activity = _activity;
    }

    public int getTotal() {
        return total;
    }

    public void add(Meal meal) {
        listID.add(meal.getId());
        total++;
        totalCost += Tools.getNum(meal.getCost());
    }

    public void remove(Meal meal){
        if (total > 0) {
            total--;
            totalCost -= Tools.getNum(meal.getCost());
        }

        if (meal.getCountMeal() == 0){
            listID.remove(meal.getId());
        }
    }

    public void removeAll(){
        total = 0;
        listID.clear();
    }

    public static void clear(){
        garbage = null;
    }

    public Set<String> getListID() {
        return listID;
    }

    public void setListID(Set<String> listID) {
        this.listID = listID;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getTotalCostStr(){
        StringBuilder str = new StringBuilder();
        str.append(totalCost);
        str.append(" ˆ");

        return str.toString();
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
