package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.support.v4.app.FragmentTransaction;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class LoadPercent {
    private Thread th;
    private Count count;
    private FragmentTransaction ft;
    private IActivity activity;
    private LoadPageFragment loadPageFragment;


    public LoadPercent(Count count, IActivity activity) {
        this.count = count;
        this.activity = activity;
        loadPageFragment = ((RestaurantActivity)activity).getLoadPageFragment();
        init();
    }

    private void init(){
        this.th = new Thread(new Runnable() {
            @Override
            public void run() {             /*load first ui to activity*/
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    ft = ((RestaurantActivity)activity).getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.restaurantLanguageContainer, loadPageFragment);
                    ft.commit();

                    while (!loadPageFragment.isAdded()){
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    synchronized (count) {
                        count.setStateLoadFragment(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start(){
        th.start();
    }
}