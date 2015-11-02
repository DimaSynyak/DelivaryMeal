package com.dmitriy.sinyak.delivarymeal.app.fragments.title;

import android.support.v4.app.Fragment;

import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgEe;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgEn;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgRu;


/**
 * Created by 1 on 02.11.2015.
 */
public class Language {
    private Languages languages;
    private LanguagesImgEe languagesImgEe;
    private LanguagesImgEn languagesImgEn;
    private LanguagesImgRu languagesImgRu;

    public LanguagesImgEe getLanguagesImgEe() {
        return languagesImgEe;
    }

    public void setLanguagesImgEe(LanguagesImgEe languagesImgEe) {
        this.languagesImgEe = languagesImgEe;
    }

    public LanguagesImgEn getLanguagesImgEn() {
        return languagesImgEn;
    }

    public void setLanguagesImgEn(LanguagesImgEn languagesImgEn) {
        this.languagesImgEn = languagesImgEn;
    }

    public LanguagesImgRu getLanguagesImgRu() {
        return languagesImgRu;
    }

    public void setLanguagesImgRu(LanguagesImgRu languagesImgRu) {
        this.languagesImgRu = languagesImgRu;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public Fragment getLanguageFragment(){
        switch (languages){
            case RU:{
                return languagesImgRu;
            }
            case EE:{
                return languagesImgEe;
            }
            case EN:{
                return languagesImgEn;
            }
            default:
                return null;
        }

    }
    public Fragment getLanguageFragment(Languages languages){
        switch (languages){
            case RU:{
                return languagesImgRu;
            }
            case EE:{
                return languagesImgEe;
            }
            case EN:{
                return languagesImgEn;
            }
            default:
                return null;
        }

    }
}
