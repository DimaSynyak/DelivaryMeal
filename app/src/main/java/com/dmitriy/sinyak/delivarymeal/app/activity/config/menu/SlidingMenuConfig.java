package com.dmitriy.sinyak.delivarymeal.app.activity.config.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.CategoryFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.CityFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.CountryFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.Ifragments.IFragments;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.KitchenFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.NullFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.AsianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.CaucasianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.EuropeanButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.PizzaButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.SushiButtonFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class SlidingMenuConfig {

    private FragmentActivity activity;

    private int idFragmentGlobal;
    private IFragments nullFragmentGlobal;
    private boolean flag = true;
    private CountryFragment countryFragment;
    private CityFragment cityFragment;
    private KitchenFragment kitchenFragment;
    private CategoryFragment categoryFragment;
    private NullFragment null_fragment1;
    private NullFragment null_fragment2;
    private NullFragment null_fragment3;
    private NullFragment null_fragment4;
    private FragmentTransaction ft;
    private TextView countryText;
    private TextView cityText;
    private TextView kitchenText;
    private TextView categoryText;
    private HorizontalScrollView horizontalScrollView;
    private ListView citiesList;
    private List<String> estonia;
    private List<String> finland;
    private List<String> latvia;
    private List<Country> countries;
    private String[] string;

    /*Категории ресторанов*/
    private PizzaButtonFragment pizzaButtonFragment;
    private SushiButtonFragment sushiButtonFragment;
    private AsianButtonFragment asianButtonFragment;
    private EuropeanButtonFragment europeanButtonFragment;
    private CaucasianButtonFragment caucasianButtonFragment;

    public SlidingMenuConfig(FragmentActivity activity) {
        this.activity = activity;
    }

    public void initSlidingMenu(){

        countryText = (TextView) activity.findViewById(R.id.countryText);
        cityText = (TextView) activity.findViewById(R.id.cityText);
        kitchenText = (TextView) activity.findViewById(R.id.kitchenText);
        categoryText = (TextView) activity.findViewById(R.id.categoryText);

        /*Инициализация стран и городов*/
        List<Country> countries = new ArrayList<Country>();
        List<String> estonia = new ArrayList<String>();
        List<String> finland = new ArrayList<String>();
        List<String> latvia = new ArrayList<String>();

        estonia.add("Тарту");
        estonia.add("Рапла");
        estonia.add("Вилджанди");

        finland.add("Тампере");
        finland.add("Лахти");
        finland.add("Хельсинки");

        latvia.add("Рига");
        latvia.add("Джелгава");
        latvia.add("Валмиера");

        countries.add(new Country("Эстония"));
        countries.add(new Country("Финляндия"));
        countries.add(new Country("Латвия"));

        countries.get(0).setCities(estonia);
        countries.get(1).setCities(finland);
        countries.get(2).setCities(latvia);
        /*Конец инициализации стран и городов*/


        SlidingMenu menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);

        initFragment();
    }

    private void initFragment(){
        countryFragment = new CountryFragment();
        cityFragment = new CityFragment();
        kitchenFragment = new KitchenFragment();
        categoryFragment = new CategoryFragment();
        null_fragment1 = new NullFragment();
        null_fragment2 = new NullFragment();
        null_fragment3 = new NullFragment();
        null_fragment4 = new NullFragment();

        idFragmentGlobal = -1;
    }

    private void addFragmentOnPlace(int idFrame, IFragments objKitchen){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(idFrame, (Fragment) objKitchen);
        ft.commit();

        horizontalScrollView = (HorizontalScrollView) activity.findViewById(R.id.horizontalScrollView);
        horizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollView.post(new Runnable() {
                    public void run() {
                        horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                    }
                });
            }
        });
    }

    private void removeFragmentFromPlace(IFragments objKitchen){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.remove((Fragment) objKitchen);
        ft.commit();
    }

    private void slidingMenu(int idFrame, IFragments fragment, IFragments nullFragment){
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);

        if (flag && (idFragmentGlobal == idFrame || idFragmentGlobal == -1)){
            ft.replace(idFrame, (Fragment) fragment);
            idFragmentGlobal = idFrame;
            nullFragmentGlobal = nullFragment;
            flag = false;
        }
        else if (!flag && idFrame == idFragmentGlobal){
            ft.replace(idFrame, (Fragment) nullFragment);
            idFragmentGlobal = -1;
            flag = true;
        }
        else if (!flag && idFrame != idFragmentGlobal){
            ft.replace(idFragmentGlobal, (Fragment) nullFragmentGlobal);
            ft.addToBackStack(null);
            ft.commit();
            idFragmentGlobal = idFrame;
            flag = true;
            slidingMenu(idFrame, fragment, nullFragment);
            return;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickDp(int viewId){
        switch (viewId){
            case R.id.countryLayout:{
                slidingMenu(R.id.countryFrame, countryFragment, null_fragment1);
                break;
            }
            case R.id.cityLayout:{
                slidingMenu(R.id.cityFrame, cityFragment, null_fragment2);
                break;
            }
            case R.id.kitchenLayout:{
                slidingMenu(R.id.kitchenFrame, kitchenFragment, null_fragment3);
                break;
            }
            case R.id.categoryLayout:{
                slidingMenu(R.id.categoryFrame, categoryFragment, null_fragment4);
                break;
            }
            case R.id.pizzaClick:{
                if (pizzaButtonFragment != null){
                    return;
                }

                pizzaButtonFragment = new PizzaButtonFragment();
                addFragmentOnPlace(R.id.filterLayout, pizzaButtonFragment);
                break;
            }
            case R.id.sushiClick:{
                if (sushiButtonFragment != null){
                    return;
                }

                sushiButtonFragment = new SushiButtonFragment();
                addFragmentOnPlace(R.id.filterLayout, sushiButtonFragment);
                break;
            }
            case R.id.caucasianClick:{
                if (caucasianButtonFragment != null){
                    return;
                }

                caucasianButtonFragment = new CaucasianButtonFragment();
                addFragmentOnPlace(R.id.filterLayout, caucasianButtonFragment);
                break;
            }
            case R.id.asianClick:{
                if (asianButtonFragment != null){
                    return;
                }

                asianButtonFragment = new AsianButtonFragment();
                addFragmentOnPlace(R.id.filterLayout, asianButtonFragment);
                break;
            }
            case R.id.europeanClick:{
                if (europeanButtonFragment != null){
                    return;
                }

                europeanButtonFragment = new EuropeanButtonFragment();
                addFragmentOnPlace(R.id.filterLayout, europeanButtonFragment);
                break;
            }
            case R.id.pizzaClickRemove:{
                removeFragmentFromPlace(pizzaButtonFragment);
                pizzaButtonFragment = null;
                break;
            }
            case R.id.sushiClickRemove:{
                removeFragmentFromPlace(sushiButtonFragment);
                sushiButtonFragment = null;
                break;
            }
            case R.id.caucasianClickRemove:{
                removeFragmentFromPlace(caucasianButtonFragment);
                caucasianButtonFragment = null;
                break;
            }
            case R.id.asianClickRemove:{
                removeFragmentFromPlace(asianButtonFragment);
                asianButtonFragment = null;
                break;
            }
            case R.id.europeanClickRemove:{
                removeFragmentFromPlace(europeanButtonFragment);
                europeanButtonFragment = null;
                break;
            }
            default: break;
        }
    }
}
