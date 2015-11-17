package com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.ChangeLocale;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesImg extends Fragment {

    private LanguagesTitle languagesTitle;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;

    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private boolean flag;
    private ImageView languagesImage;
    private Language language;
    private Fragment iconFragment;
    private boolean languageTitleInitFlag;

    public LanguagesImg() {
        super();

    }

    public LanguagesImg(AppCompatActivity activity) {
        this.activity = activity;
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
        iconFragment = (Fragment) activity.getSupportFragmentManager().findFragmentById(R.id.languagesFrame);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.languages_img_ru, container, false);
    }

    public void init(Languages languages){
        if (!(languages == null)){

            switch (languages) {
                case RU: {
                    ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ru);
                    break;
                }
                case EE: {
                    ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ee);
                    break;
                }
                case EN: {
                    ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_en);
                }
            }
        }

        ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownUpLanguageList();

            }
        });
    }

    public void init(Languages languages, int opacity){
        int fullColor = 255 * opacity / 100;
        fullColor *= 16843009;
        ImageView imageView = (ImageView) iconFragment.getView().findViewById(R.id.languagesClick);
        FrameLayout frameLayout = (FrameLayout) iconFragment.getView().findViewById(R.id.languageOpacity);

        if (!(languages == null)){

            switch (languages) {
                case RU: {
                    imageView.setImageResource(R.drawable.language_ru);
                    break;
                }
                case EE: {
                    imageView.setImageResource(R.drawable.language_ee);
                    break;
                }
                case EN: {
                    imageView.setImageResource(R.drawable.language_en);
                }
            }
        }

        frameLayout.setBackgroundColor(fullColor);
        imageView.setClickable(false);
    }

    protected void dropDownUpLanguageList(){
        if (((IActivity)activity).getCustomViewAbove() != null) {
            if (((IActivity) activity).getCustomViewAbove().getCurrentItem() == 0) {
                ((IActivity) activity).getCustomViewAbove().setCurrentItem(1);
            }
        }
        ft = activity.getSupportFragmentManager().beginTransaction();
        if (!flag) {
            ft.add(((IActivity) activity).getLanguageContainerId(), languagesFragmentOpacityLow);
            ft.commit();
            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
            ft.add(((IActivity) activity).getLanguageContainerId(), languagesTitle);
            ft.commit();

            flag = true;
        }
        else {
            ft.remove(languagesTitle);
            ft.commit();
            ft = activity.getSupportFragmentManager().beginTransaction();
            ft.remove(languagesFragmentOpacityLow);
            ft.commit();
            flag = false;
        }

    }

    public void setLanguagesTitle(LanguagesTitle languagesTitle) {
        this.languagesTitle = languagesTitle;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void updateLanguage(){

        ChangeLocale changeLocale = null;
        Locale myLocale = null;

        switch (language.getLanguages()){
            case RU:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
//                changeLocale = ((MainActivity) activity).getChangeLocale();
//                changeLocale.execute(Language.RESTAURANTS_URL_RU);
                break;
            }
            case EE:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ee);
                myLocale = new Locale("et");
//                changeLocale = ((MainActivity) activity).getChangeLocale();
//                changeLocale.execute(Language.RESTAURANTS_URL_EE);
                break;
            }
            case EN:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_en);
                myLocale = new Locale("en");
//                changeLocale = ((MainActivity) activity).getChangeLocale();
//                changeLocale.execute(Language.RESTAURANTS_URL_EN);
                break;
            }
            default: return;
        }
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
    }
}
