package com.dmitriy.sinyak.delivarymeal.app.activity.lib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 27.11.2015.
 */
public class NumberProgressBar {
    private Count count;
    private int numCount;
    private AppCompatActivity appCompatActivity;
    private int containerId;
    private LinearLayout linearLayout;
    private TextView dynamicText;
    private TextView staticText;
    private LinearLayout container;
    private Thread thread;
    private int data;
    private float density;
    private LoadFragment loadFragment;
    private FragmentTransaction ft;

    public NumberProgressBar(int numCount, int containerId, AppCompatActivity appCompatActivity) {
        this.numCount = numCount;
        this.containerId = containerId;
        this.appCompatActivity = appCompatActivity;
        density = appCompatActivity.getResources().getDisplayMetrics().density;
    }

    public void init(){
        count = new Count(numCount);
        loadFragment = new LoadFragment();
        loadFragment.setCount(count);

        ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        ft.add(containerId, loadFragment);
        ft.commit();

    }


    public void levelComplete(){
        count.complete();
    }

    public void waitLoadNumber() throws InterruptedException {
        synchronized (count) {
            while (!count.isStateData()) {
                count.wait(100);
            }
        }

        ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        ft.remove(loadFragment);
        ft.commit();
    }


    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
