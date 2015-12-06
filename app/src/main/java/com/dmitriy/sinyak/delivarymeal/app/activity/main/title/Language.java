package com.dmitriy.sinyak.delivarymeal.app.activity.main.title;


import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LanguagesTitle;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1 on 02.11.2015.
 */
public class Language {

    public static final String RESTAURANTS_URL_RU = "http://www.menu24.ee/ru/restaurant/";
    public static final String RESTAURANTS_URL_EE = "http://www.menu24.ee/restaurant/";
    public static final String RESTAURANTS_URL_EN = "http://www.menu24.ee/en/restaurant/";

    public static Languages languages;
    private static List<ILanguageListener> iLanguageListeners;
    private static Language language;

    public static Language getInstance(){
        if (language == null){
            language = new Language();
        }
        return language;
    }

    public Language() {


    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages language) {
        languages = language;
    }

    public void setLanguageString(String language){
        language = language.toLowerCase();
        switch (language){
            case "ru":{
                languages = Languages.RU;
                break;
            }
            case "en":{
                languages = Languages.EN;
                break;
            }
            case "et":{
                languages = Languages.EE;
                break;
            }
            default: languages = Languages.RU;
        }
    }

    public String getURL(){
        switch (languages){
            case RU:{
                return RESTAURANTS_URL_RU;
            }
            case EN:{
                return RESTAURANTS_URL_EN;
            }
            case EE:{
                return RESTAURANTS_URL_EE;
            }
            default:{
                return RESTAURANTS_URL_RU;
            }
        }

    }

    public LanguagesTitle init(int container){
        LanguagesTitle languagesTitle = new LanguagesTitle();
        languagesTitle.setIdContainer(container);

        return languagesTitle;
    }

    public static void setiLanguage(ILanguageListener iLanguageListener) {
        if (iLanguageListeners == null){
            iLanguageListeners = new ArrayList<>();
        }

        Language.iLanguageListeners.add(iLanguageListener);
    }

    public void change(){
        if (iLanguageListeners == null)
            return;

        for (int i = 0; i < iLanguageListeners.size(); i++) {
            iLanguageListeners.get(i).change();
        }
    }
}
