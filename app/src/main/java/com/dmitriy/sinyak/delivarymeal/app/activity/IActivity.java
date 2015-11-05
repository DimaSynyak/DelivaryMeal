package com.dmitriy.sinyak.delivarymeal.app.activity;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

/**
 * Created by 1 on 03.11.2015.
 */
public interface IActivity {
    void setCustomViewAbove(CustomViewAbove customViewAbove);
    CustomViewAbove getCustomViewAbove();

    public int getLanguageContainerId();

    public void setLanguageContainerId(int languageContainerId);
}
