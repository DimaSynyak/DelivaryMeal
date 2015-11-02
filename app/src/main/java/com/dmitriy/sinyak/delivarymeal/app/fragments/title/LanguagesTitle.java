package com.dmitriy.sinyak.delivarymeal.app.fragments.title;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesFragmentOpacityLow;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgEe;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgEn;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.fragments.LanguagesImgRu;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesTitle extends Fragment {
    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private boolean flag;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;
    private LanguagesImgRu languagesImgRu;
    private LanguagesImgEe languagesImgEe;
    private LanguagesImgEn languagesImgEn;
    private ImageView languagesImage;
    private Language language;

    public LanguagesTitle() {
        super();
    }

    public LanguagesTitle(AppCompatActivity activity) {
        this.activity = activity;
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
        languagesImgRu = new LanguagesImgRu();
        languagesImgEe = new LanguagesImgEe();
        languagesImgEn = new LanguagesImgEn();

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.languages_fragment, container, false);

        return viewHierarchy;
    }

    public void onClickDp(int viewId){
        switch (viewId){
            case R.id.languagesClick:{
                dropDownUpLanguageList();
                break;
            }

            case R.id.russianText:{
                changeLanguage(Languages.RU);
                dropDownUpLanguageList();
                break;
            }
            case R.id.estoniaText:{
                changeLanguage(Languages.EE);
                dropDownUpLanguageList();
                break;
            }
            case R.id.englishText:{
                changeLanguage(Languages.EN);
                dropDownUpLanguageList();
                break;
            }
            default:break;
        }
    }

    public void init(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.languagesFrame, languagesImgRu);
        ft.commit();
        /*ImageView*/
        languagesImage = (ImageView) activity.findViewById(R.id.languagesClick);

        /*Конфигурация языка приложения*/
        language = new Language();
        language.setLanguages(Languages.RU); //Warning привязать к локали телефона
        language.setLanguagesImgEe(languagesImgEe);
        language.setLanguagesImgEn(languagesImgEn);
        language.setLanguagesImgRu(languagesImgRu);
    }

    private void changeLanguage(Languages languages){
        if (language.getLanguages() == languages){
            return;
        }

        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.remove(language.getLanguageFragment());
        ft.commit();

        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.languagesFrame, language.getLanguageFragment(languages));
        ft.commit();

        language.setLanguages(languages);
    }

    private void dropDownUpLanguageList(){
        if (CustomViewAbove.customViewAbove.getCurrentItem() == 0){
            CustomViewAbove.customViewAbove.setCurrentItem(1);
        }
        ft = activity.getSupportFragmentManager().beginTransaction();
        if (!flag) {
            ft.add(R.id.languageContainer,languagesFragmentOpacityLow);
            ft.commit();
            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
            ft.add(R.id.languageContainer, this);
            flag = true;
        }
        else {
            ft.remove(this);
            ft.commit();
            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.remove(languagesFragmentOpacityLow);
            flag = false;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

}
