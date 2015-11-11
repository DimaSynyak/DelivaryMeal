package com.dmitriy.sinyak.delivarymeal.app.activity;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

/**
 * Created by 1 on 03.11.2015.
 */
public interface IActivity {
    void setCustomViewAbove(CustomViewAbove customViewAbove);
    CustomViewAbove getCustomViewAbove();

    int getLanguageContainerId();

    void setLanguageContainerId(int languageContainerId);

    void changeLanguage(Languages languages);


}
