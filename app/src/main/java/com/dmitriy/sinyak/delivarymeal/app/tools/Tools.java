package com.dmitriy.sinyak.delivarymeal.app.tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by dmitriy on 9/24/15.
 */
public class Tools {

    public static int resizeComponent(Activity activity, int countObjs){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (metrics.widthPixels / countObjs);
    }

    public static int resizeComponentHeight(int size, double percentHeight){
        percentHeight /= 100;

        return (int) (size * percentHeight/(1 - percentHeight));
    }

    public static void listViewAddFields(Context context, ListView listView, String... list){
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);

        // присваиваем адаптер списку
        listView.setAdapter(adapter);
    }
}
