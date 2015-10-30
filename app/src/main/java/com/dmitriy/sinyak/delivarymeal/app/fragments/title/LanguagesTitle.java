package com.dmitriy.sinyak.delivarymeal.app.fragments.title;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.NullFragment;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesTitle extends Fragment {
    private FragmentTransaction ft;
    private AppCompatActivity activity;
    private boolean flag;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;
    private LanguagesImg languagesImg;

    public LanguagesTitle() {
        super();
    }

    public LanguagesTitle(AppCompatActivity activity) {
        this.activity = activity;
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
        languagesImg = new LanguagesImg();

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
                break;
            }

            case R.id.russianText:{

                break;
            }
            case R.id.estoniaText:{
                break;
            }
            case R.id.englishText:{
                break;
            }
            default:break;
        }
    }

    public void init(){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.languagesClick, languagesImg);
        ft.commit();
    }

}
