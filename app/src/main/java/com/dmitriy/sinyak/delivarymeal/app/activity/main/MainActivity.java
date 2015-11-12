package com.dmitriy.sinyak.delivarymeal.app.activity.main;

import android.app.ActionBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.ChangeLocale;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.MainAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IActivity {

    private Language language;
    private CustomViewAbove customViewAbove;
    private int languageContainerId;
    private boolean firstFlag;
    private android.support.v7.app.ActionBar actionBar;
    private MainActivity mainActivity;
    private SlidingMenuConfig slidingMenuConfig;
    private ChangeLocale changeLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String local = Locale.getDefault().getLanguage();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);

        /*INIT LANGUAGE*/
        languageContainerId = R.id.languageContainer;
        language = new Language(this);
        language.setLanguageString(local);
        language.init();
        /*END INIT LANGUAGE*/

        actionBar = getSupportActionBar();
        actionBar.hide();
        mainActivity = this;

        new MainAsyncTask(this).execute(language.getURL());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

        if (slidingMenuConfig != null)
            slidingMenuConfig.onClickDp(v.getId());

        switch (v.getId()){
            case R.id.menuClick:{
                if (customViewAbove.getCurrentItem() == 1){
                    customViewAbove.setCurrentItem(0);
                }
                else {
                    customViewAbove.setCurrentItem(1);
                }
                break;
            }
            case R.id.imageView3:{
                if (customViewAbove.getCurrentItem() == 0){
                    customViewAbove.setCurrentItem(1);
                }
                break;
            }
        }
    }

    public CustomViewAbove getCustomViewAbove() {
        return customViewAbove;
    }

    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.customViewAbove = customViewAbove;
    }

    public int getLanguageContainerId() {
        return languageContainerId;
    }

    public void setLanguageContainerId(int languageContainerId) {
        this.languageContainerId = languageContainerId;
    }

    @Override
    public void changeLanguage(Languages languages) {
        if (language.getLanguages() == languages) {
            return;
        }

        if (changeLocale != null){
            if (!changeLocale.isCancled())
                return;
        }

        Fragment iconFragment = getSupportFragmentManager().findFragmentById(R.id.languagesFrame);

        changeLocale = null;
        Locale myLocale = null;

        switch (languages){
            case RU:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_RU);
                break;
            }
            case EE:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_ee);
                myLocale = new Locale("et");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_EE);
                break;
            }
            case EN:{
                ((ImageView) iconFragment.getView().findViewById(R.id.languagesClick)).setImageResource(R.drawable.language_en);
                myLocale = new Locale("en");
                changeLocale = getChangeLocale();
                changeLocale.execute(Language.RESTAURANTS_URL_EN);
                break;
            }
            default: return;
        }

        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        language.setLanguages(languages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstFlag) {
            language.getLanguagesImg().updateLanguage();
        }
        firstFlag = true;
    }

    public ChangeLocale getChangeLocale(){
        changeLocale = new ChangeLocale(this);
        return changeLocale;
    }

    public android.support.v7.app.ActionBar getActionBarActivity() {
        return actionBar;
    }

    public void setSlidingMenuConfig(SlidingMenuConfig slidingMenuConfig) {
        this.slidingMenuConfig = slidingMenuConfig;
    }
}
