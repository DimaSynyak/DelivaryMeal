package com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;

import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesTitle extends Fragment {

    private LanguagesImg languagesImg;
    private IActivity activity;

    public LanguagesTitle() {
        super();
    }

    public LanguagesTitle(IActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.languages_fragment, container, false);

        viewHierarchy.findViewById(R.id.russianText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeLanguage(Languages.RU);
                languagesImg.dropDownUpLanguageList();
            }
        });
        viewHierarchy.findViewById(R.id.estoniaText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeLanguage(Languages.EE);
                languagesImg.dropDownUpLanguageList();
            }
        });
        viewHierarchy.findViewById(R.id.englishText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeLanguage(Languages.EN);
                languagesImg.dropDownUpLanguageList();
            }
        });
        return viewHierarchy;
    }


    public void setLanguagesImg(LanguagesImg languagesImg) {
        this.languagesImg = languagesImg;
    }
}
