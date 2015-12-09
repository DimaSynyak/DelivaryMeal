package com.design.yogurt.app.lib;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;

/**
 * Created by 1 on 27.11.2015.
 */
public class LoadFragment extends Fragment {
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
    private LinearLayout layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load2_fragment, container, false);

//        Context context = getActivity().getApplicationContext();
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        dynamicText = (TextView) view.findViewById(R.id.dynamicText);
        dynamicText.setTypeface(arimo);

        staticText = (TextView) view.findViewById(R.id.staticText);
        staticText.setTypeface(arimo);

//        linearLayout = new LinearLayout(context);
//        dynamicText = new TextView(context);
//        staticText = new TextView(context);
//        dynamicText.setText("0");
//        dynamicText.setTextSize(density * 8);
//        dynamicText.setTextColor(Color.BLACK);
//        dynamicText.setTypeface(arimo);
//        staticText.setText("%");
//        staticText.setTextSize(density * 8);
//        staticText.setTextColor(Color.BLACK);
//        staticText.setTypeface(arimo);
//
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        linearLayout.setPadding(0, (int) (15 * density), 0, (int) (15 * density));
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        linearLayout.addView(dynamicText);
//        linearLayout.addView(staticText);
//        linearLayout.setVisibility(LinearLayout.VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        data = 0;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {

                        synchronized (count) {

                            LoadFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dynamicText.setText(String.valueOf(data));
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
        });

        thread.setDaemon(true);
        thread.start();
        synchronized (count) {
            count.setStateLoadFragment(true);
            count.notifyAll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        count = null;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }
}
