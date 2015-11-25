package com.dmitriy.sinyak.delivarymeal.app.activity.payment;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.FormDataFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.DelivaryData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.MainAsyncTask;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, IActivity {
    private Language language;
    private int paymentLanguageContainer;
    public static final int TWENTY_PERCENT = 20;
    private DelivaryData delivaryData;
    private Restaurant restaurant;
    private FormDataFragment formDataFragment;

    private CustomViewAbove customViewAbove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setCustomView(R.layout.title);

         /*INIT LANGUAGE*/
        paymentLanguageContainer = R.id.paymentLanguageContainer;
        language = new Language(this);
        language.init(TWENTY_PERCENT);
        /*END INIT LANGUAGE*/

//        sldMenuCfgPaymentAct = new SldMenuCfgPaymentAct(this);
//        sldMenuCfgPaymentAct.initSlidingMenu();

        delivaryData = DelivaryData.getInstance();
        restaurant = RestaurantList.getRestaurant();
    }

    @Override
    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.customViewAbove = customViewAbove;
    }

    @Override
    public CustomViewAbove getCustomViewAbove() {
        return customViewAbove;
    }

    @Override
    public int getLanguageContainerId() {
        return paymentLanguageContainer;
    }

    @Override
    public void setLanguageContainerId(int languageContainerId) {

    }

    @Override
    public void changeLanguage(Languages languages) {

    }

    @Override
    public void onClick(View v) {
//        if (sldMenuCfgPaymentAct != null)
//            sldMenuCfgPaymentAct.onClickSL(v.getId());

        switch (v.getId()){
            case R.id.imageView3:{
                finish();
                break;
            }
            case R.id.seb_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("seb");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
            case R.id.danske_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("danske");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
            case R.id.kredit_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("krediidipank");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
            case R.id.lhv_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("lhv");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
            case R.id.nordea_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("nordea");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
            case R.id.swed_btn:{
                updateDelivaryData();
                if (delivaryData.checkData()){
                    delivaryData.setNameBank("swedbank");
                    new MainAsyncTask(this).execute(restaurant.getMenuLink());
                }
                else {
                    customViewAbove.setCurrentItem(0);
                }
                break;
            }
        }
    }

    private void updateDelivaryData(){
        formDataFragment = FormDataFragment.getInstance();

        if (!formDataFragment.isAdded())
            return;

        delivaryData.setYourName(String.valueOf(formDataFragment.getYourName().getText()));
        delivaryData.setDelivaryCity(String.valueOf(formDataFragment.getYourCity().getText()));
        delivaryData.setNumStreet(String.valueOf(formDataFragment.getYourStreet().getText()));
        delivaryData.setNumHouse(String.valueOf(formDataFragment.getYourHouse().getText()));
        delivaryData.setNumFlat(String.valueOf(formDataFragment.getYourFlat().getText()));
        delivaryData.setEmail(String.valueOf(formDataFragment.getYourEmail().getText()));
        delivaryData.setNumPhone(String.valueOf(formDataFragment.getYourPhone().getText()));

        delivaryData.setDelivaryData(formDataFragment.updateDate());
    }
}
