package ee.menu24.deliverymeal.app.restaurant.service;


import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.tools.Tools;

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
    private Set<Meal> listOrderMeal;
    private float totalCost;
    private TextView totalCostStr;

    public Garbage() {
        listOrderMeal = new HashSet<>();
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
        listOrderMeal.add(meal);
        total++;                                    //all count order meal
        totalCost += Tools.getNum(meal.getCost());  //all cost order meal
    }

    public void remove(Meal meal){
        if (total > 0) {
            total--;
            totalCost -= Tools.getNum(meal.getCost());
        }

        if (meal.getCountMeal() == 0){
            listOrderMeal.remove(meal.getId());
        }
    }

    public void removeAll(){
        total = 0;
        if (listOrderMeal != null)
            listOrderMeal.clear();
    }

    public void onDestroy(){
        removeAll();
        listOrderMeal = null;
        garbage = null;
    }

    public Set<Meal> getListOrderMeal() {
        return listOrderMeal;
    }

    public void setListOrderMeal(Set<Meal> listOrderMeal) {
        this.listOrderMeal = listOrderMeal;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public String getTotalCostStr(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("%.02f", totalCost));
        str.append(" €");

        return str.toString();
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public void removeActivity(){
        activity = null;
    }
}
