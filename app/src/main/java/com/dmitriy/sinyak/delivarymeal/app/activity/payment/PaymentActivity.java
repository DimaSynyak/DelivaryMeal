package com.dmitriy.sinyak.delivarymeal.app.activity.payment;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, IActivity {
    private Language language;
    private int paymentLanguageContainer;
    public static final int TWENTY_PERCENT = 20;

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
    }

    @Override
    public void setCustomViewAbove(CustomViewAbove customViewAbove) {

    }

    @Override
    public CustomViewAbove getCustomViewAbove() {
        return null;
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
        switch (v.getId()){
            case R.id.imageView3:{
//                if (changeLocale != null){
//                    if (!changeLocale.isCancelled())
//                        return;
//                }
//
//                if (restaurantAsyncTask != null){
//                    restaurantAsyncTask.getLoadPageFragment().getThread().interrupt();
//                    restaurantAsyncTask.cancel(true);
//                    restaurantAsyncTask = null;
//                }

                finish();
                break;
            }
        }
    }
}
