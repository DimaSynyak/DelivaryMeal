package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;

import org.w3c.dom.Text;

/**
 * Created by 1 on 02.12.2015.
 */
public class GarnirFragment extends Fragment {

    private TextView garnirName;
    private String garnirNameText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        garnirName = (TextView) view.findViewById(R.id.garnir_name);
        garnirName.setTypeface(geometric);
        garnirName.setText(garnirNameText);

        garnirName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 02.12.2015
            }
        });

        return view;
    }

    public String getGarnirNameText() {
        return garnirNameText;
    }

    public void setGarnirNameText(String garnirNameText) {
        this.garnirNameText = garnirNameText;
    }
}
