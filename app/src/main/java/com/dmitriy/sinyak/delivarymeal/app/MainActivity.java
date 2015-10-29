package com.dmitriy.sinyak.delivarymeal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.*;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.Ifragments.IFragments;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.AsianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.CaucasianButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.EuropeanButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.PizzaButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.menu.category.SushiButtonFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
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

    /*Категории ресторанов*/
    private PizzaButtonFragment pizzaButtonFragment;
    private SushiButtonFragment sushiButtonFragment;
    private AsianButtonFragment asianButtonFragment;
    private EuropeanButtonFragment europeanButtonFragment;
    private CaucasianButtonFragment caucasianButtonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryText = (TextView) findViewById(R.id.countryText);
        cityText = (TextView) findViewById(R.id.cityText);
        kitchenText = (TextView) findViewById(R.id.kitchenText);
        categoryText = (TextView) findViewById(R.id.categoryText);

        initSlidingMenu();
        initFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    private void addFragmentOnPlace(int idFrame, IFragments objKitchen){
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(idFrame, (Fragment) objKitchen);
        ft.commit();
    }

    private void removeFragmentFromPlace(IFragments objKitchen){
        ft = getSupportFragmentManager().beginTransaction();
        ft.remove((Fragment) objKitchen);
        ft.commit();
    }

    private void slidingMenu(int idFrame, IFragments fragment, IFragments nullFragment){
        ft = getSupportFragmentManager().beginTransaction();
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

    private void initSlidingMenu(){
//        String[] countryListStr = {"Эстония"}; //Заменить на данные с сайта Warning
//        countryList = (ListView) findViewById(R.id.countryList);
//        Tools.listViewAddFields(this, countryList, countryListStr);

        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        menu.setMenu(R.layout.menu);
    }
}
