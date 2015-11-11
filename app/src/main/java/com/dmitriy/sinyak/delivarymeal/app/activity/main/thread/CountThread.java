package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 10.11.2015.
 */
public class CountThread implements Runnable{

    private AppCompatActivity mainActivity;
    private Count count;
    private TextView dynamicTextView;
    private Object object;
    private int data;
    private int etalon;

    public CountThread(AppCompatActivity mainActivity, Count count, TextView textView) {
        this.mainActivity = mainActivity;
        this.count = count;
        dynamicTextView = textView;
    }

    public int getData() {
        return data;
    }

    @Override
    public void run() {
        try {
            while(true) {

                synchronized (count) {


                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicTextView.setText(String.valueOf(data));
                        }
                    });

                    data++;

                    if (data >= count.getData()) {
                        data = count.getData();
                    }

                    if (count.getData() < 100) {
                        count.wait(100);
                    }
                    else {
                        count.wait(10);
                    }

                    if (data >= 100){
                        count.setStateData(true);
                        break;
                    }
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
