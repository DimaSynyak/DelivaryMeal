package ee.menu24.deliverymeal.app.main;

import android.app.ActionBar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.IActivity;
import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.menu.ICustomAboveView;
import ee.menu24.deliverymeal.app.main.menu.SlidingMenuConfig;
import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.thread.ChangeLocale;
import ee.menu24.deliverymeal.app.main.thread.MainAsyncTask;
import ee.menu24.deliverymeal.app.main.title.Language;
import ee.menu24.deliverymeal.app.main.title.Languages;
import ee.menu24.deliverymeal.app.main.title.fragments.LanguagesFragmentOpacityLow;
import ee.menu24.deliverymeal.app.main.title.fragments.LanguagesTitle;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IActivity {

    private Language language;
    private static CustomViewAbove customViewAbove;
    private int languageContainerId;
    private boolean firstFlag;
    private android.support.v7.app.ActionBar actionBar;
    private static MainActivity mainActivity;
    private SlidingMenuConfig slidingMenuConfig;
    private ChangeLocale changeLocale;
    private Thread th;

    private FragmentTransaction ft;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;
    private LanguagesTitle languagesTitle;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String local = Locale.getDefault().getLanguage();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.title);

        /*INIT LANGUAGE*/
        language = Language.getInstance();
        language.setLanguageString(local);
        languagesTitle = language.init(R.id.languageContainer);
        init(language.getLanguages());
        /*END INIT LANGUAGE*/

        actionBar = getSupportActionBar();
        actionBar.hide();
        mainActivity = this;

        Restaurant.setConnection(language.getURL());
        new MainAsyncTask(this).execute(language.getURL());


        th = new Thread(new Runnable() {
            ImageView menuButton = (ImageView) MainActivity.this.findViewById(R.id.menuClick);
            @Override
            public void run() {

                while (true) {
                    if (customViewAbove != null){
                        if (customViewAbove.getCurrentItem() == 1) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    menuButton.setImageResource(R.drawable.lines);
                                }
                            });

                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    menuButton.setImageResource(R.drawable.cross);
                                }
                            });
                        }
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.start();
    }

    /**************************/

    public void init(Languages languages){
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
        if (!(languages == null)){

            switch (languages) {
                case RU: {
                    ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ru);
                    break;
                }
                case EE: {
                    ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ee);
                    break;
                }
                case EN: {
                    ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_en);
                }
            }
        }

        (findViewById(R.id.language_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languagesTitle.dropDownUpLanguageList(MainActivity.this);
            }
        });
    }




    public void setLanguage(Language language) {
        this.language = language;
    }

    public void updateLanguage(){

        ChangeLocale changeLocale = null;
        Locale myLocale = null;

        switch (language.getLanguages()){
            case RU:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ru);
                myLocale = new Locale("ru");

                break;
            }
            case EE:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ee);
                myLocale = new Locale("et");

                break;
            }
            case EN:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_en);
                myLocale = new Locale("en");

                break;
            }
            default: return;
        }
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
    /***************************/





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

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

    public static CustomViewAbove getCustomViewAbove() {
        return customViewAbove;
    }

    public static void setCustomViewAbove(CustomViewAbove customViewAbove) {
        MainActivity.customViewAbove = customViewAbove;
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


        changeLocale = null;
        Locale myLocale = null;


        switch (languages){
            case RU:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
                changeLocale = getChangeLocale();
                changeLocale.setFlagChangeLocale(true);
                changeLocale.execute(Language.RESTAURANTS_URL_RU);
                break;
            }
            case EE:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ee);
                myLocale = new Locale("et");
                changeLocale = getChangeLocale();
                changeLocale.setFlagChangeLocale(true);
                changeLocale.execute(Language.RESTAURANTS_URL_EE);
                break;
            }
            case EN:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_en);
                myLocale = new Locale("en");
                changeLocale = getChangeLocale();
                changeLocale.setFlagChangeLocale(true);
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

        updateUI();
    }


    private void updateUI(){
        EditText search = (EditText) findViewById(R.id.editText);
        search.setHint(R.string.search);

        TextView categoryText = (TextView) findViewById(R.id.categoryText);
        categoryText.setText(R.string.kitchen);

        TextView criteriaText = (TextView) findViewById(R.id.criteriaText);
        criteriaText.setText(R.string.criteria);

        TextView searchButton = (TextView) findViewById(R.id.search_button);
        searchButton.setText(R.string.search_button);
    }

    private void updateImageLanguage(){

        switch (language.getLanguages()){
            case RU:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ru);
                break;
            }
            case EE:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_ee);
                break;
            }
            case EN:{
                ((ImageView) findViewById(R.id.language_image)).setBackgroundResource(R.drawable.language_en);
                break;
            }
            default: return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstFlag) {
            updateImageLanguage();

            Restaurant.setConnection(language.getURL());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        th.interrupt();
        th = null;


        System.exit(0);
    }
}
