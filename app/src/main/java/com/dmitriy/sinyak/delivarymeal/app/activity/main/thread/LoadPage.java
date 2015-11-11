package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadBarFragment;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class LoadPage {
    private Thread th;
    private Count count;
    private FragmentTransaction ft;
    private IActivity activity;
    private LoadBarFragment loadBarFragment;

    public LoadPage(final Count count, IActivity activity) {
        this.count = count;
        this.activity = activity;
        loadBarFragment = ((MainActivity )activity).getLoadBarFragment();
        init();
    }

    private void init(){
        this.th = new Thread(new Runnable() {
            @Override
            public void run() {             /*load first ui to activity*/
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    ft = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.languageContainer, loadBarFragment);
                    ft.commit();

                    while (!loadBarFragment.isAdded()){
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
