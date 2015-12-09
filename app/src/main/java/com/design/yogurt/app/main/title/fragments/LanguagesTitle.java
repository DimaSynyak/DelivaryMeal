package com.design.yogurt.app.main.title.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.IActivity;
import com.design.yogurt.app.main.MainActivity;
import com.design.yogurt.app.main.title.Language;
import com.design.yogurt.app.main.title.Languages;

/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesTitle extends Fragment {

    private Language language;
    private FragmentTransaction ft;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;
    private int idContainer;

    public LanguagesTitle() {
        language = Language.getInstance();
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.languages_fragment, container, false);

        viewHierarchy.findViewById(R.id.russianText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActivity)getActivity()).changeLanguage(Languages.RU);
                dropDownUpLanguageList();
            }
        });
        viewHierarchy.findViewById(R.id.estoniaText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActivity)getActivity()).changeLanguage(Languages.EE);
                dropDownUpLanguageList();
            }
        });
        viewHierarchy.findViewById(R.id.englishText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActivity)getActivity()).changeLanguage(Languages.EN);
                dropDownUpLanguageList();
            }
        });
        return viewHierarchy;
    }


    public void dropDownUpLanguageList(AppCompatActivity activity){
        if (MainActivity.getCustomViewAbove() != null) {
            if (MainActivity.getCustomViewAbove().getCurrentItem() == 0) {
                MainActivity.getCustomViewAbove().setCurrentItem(1);
            }
        }
        ft = activity.getSupportFragmentManager().beginTransaction();
        if (!isAdded()) {
            ft.add(idContainer, languagesFragmentOpacityLow);
            ft.commit();

            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
            ft.add(idContainer, this);
            ft.commit();
        }
        else {
            ft.remove(this);
            ft.commit();
            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.remove(languagesFragmentOpacityLow);
            ft.commit();
        }

    }

    public void dropDownUpLanguageList(){
        if (MainActivity.getCustomViewAbove() != null) {
            if (MainActivity.getCustomViewAbove().getCurrentItem() == 0) {
                MainActivity.getCustomViewAbove().setCurrentItem(1);
            }
        }
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (!isAdded()) {
            ft.add(idContainer, languagesFragmentOpacityLow);
            ft.commit();

            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
            ft.add(idContainer, this);
            ft.commit();
        }
        else {
            ft.remove(this);
            ft.commit();
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(languagesFragmentOpacityLow);
            ft.commit();
        }

    }

    public int getIdContainer() {
        return idContainer;
    }

    public void setIdContainer(int idContainer) {
        this.idContainer = idContainer;
    }
}
