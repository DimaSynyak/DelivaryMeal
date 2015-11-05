package com.dmitriy.sinyak.delivarymeal.app.activity.main.title;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesFragmentOpacityLow;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesImg;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesTitle;


/**
 * Created by 1 on 02.11.2015.
 */
public class Language {
    public static Languages languages;
    private LanguagesImg languagesImg;
    private LanguagesTitle languagesTitle;
    private AppCompatActivity activity;

    public Language(AppCompatActivity activity) {
        this.activity = activity;

        languagesImg = new LanguagesImg(activity);
        languagesTitle = new LanguagesTitle();

        languagesImg.setLanguagesTitle(languagesTitle);
        languagesTitle.setLanguagesImg(languagesImg);
        languagesImg.setLanguage(this);
    }

    public LanguagesImg getLanguagesImg() {
        return languagesImg;
    }

    public void setLanguagesImg(LanguagesImg languagesImg) {
        this.languagesImg = languagesImg;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages language) {
        languages = language;
    }

    public void init(){
        languagesImg.init(languages);
    }
}
